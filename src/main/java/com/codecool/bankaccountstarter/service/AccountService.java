package com.codecool.bankaccountstarter.service;

import com.codecool.bankaccountstarter.model.Account;
import com.codecool.bankaccountstarter.model.AccountOperations;
import com.codecool.bankaccountstarter.model.Currency;
import com.codecool.bankaccountstarter.model.OwnerInfo;
import com.codecool.bankaccountstarter.model.dto.AccountDto;
import com.codecool.bankaccountstarter.model.dto.AccountMapper;
import com.codecool.bankaccountstarter.model.dto.BalanceDto;
import com.codecool.bankaccountstarter.model.dto.BalanceMapper;
import com.codecool.bankaccountstarter.model.exception.DuplicateAccountCodeException;
import com.codecool.bankaccountstarter.model.exception.InsufficientFundsException;
import com.codecool.bankaccountstarter.model.exception.UnauthorizedOperationException;
import com.codecool.bankaccountstarter.model.security.Credentials;
import com.codecool.bankaccountstarter.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.transaction.TransactionRolledbackException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AccountService implements AccountOperations {

    private AccountRepository accountRepo;

    private NumericService numericService;
    private OwnerService ownerService;
    private SecurityManagementService securityService;
    private CashpointVerificationService verificationService;

    private AccountMapper accountMapper;
    private BalanceMapper balanceMapper;

    @Autowired
    public AccountService(
            AccountRepository accountRepo,
            NumericService numericService,
            OwnerService ownerService,
            SecurityManagementService securityService,
            CashpointVerificationService verificationService,
            AccountMapper accountMapper,
            BalanceMapper balanceMapper
    ) {
        this.accountRepo = accountRepo;
        this.numericService = numericService;
        this.ownerService = ownerService;
        this.securityService = securityService;
        this.verificationService = verificationService;
        this.accountMapper = accountMapper;
        this.balanceMapper = balanceMapper;

    }


    @Override
    public AccountDto createLocalAccount(String accountOwner, String accountPassword) {
        Account accountToWrite = new Account.Builder()
                .withOwnerName(accountOwner)
                .withPassword(accountPassword)
                .build();
        return getAccountDtoAfterPersisting(accountToWrite);
    }


    @Override
    public AccountDto createSpecificAccount(String accountCode, String accountOwner, String accountPassword) throws DuplicateAccountCodeException {
        Account accountToWrite = new Account.Builder()
                .withOwnerName(accountOwner)
                .withPassword(accountPassword)
                .withAccountCode(accountCode)
                .build();
        return getAccountDtoAfterPersisting(accountToWrite);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Account> getAccountById(Long id) {
        return accountRepo.findById(id);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Account> getAccountByCode(String accountCode) {
        return accountRepo.findByCode(accountCode);
    }

    @Override
    public void depositAmount(
            double amount,
            Currency amountCurrency,
            Account account,
            String confirmationPayload
    ) throws UnauthorizedOperationException {
        if (!verificationService.verifyIsDepositPoint(confirmationPayload)) {
            throw new UnauthorizedOperationException("Can not deposit money via this method!");
        }
        long amountAsFixed = numericService.mapFloatToFixed(amount);
        Currency properCurrency = account.getCurrency();
        long amountDepositedInProperCurrency = 0L;
        amountDepositedInProperCurrency = numericService.mapFromAmountInForeignCurrency(
                amountAsFixed,
                amountCurrency,
                properCurrency
        );
        long balanceInProperCurrency = account.getBalanceInCents();
        overwriteBalance(account.getId(), balanceInProperCurrency + amountDepositedInProperCurrency);
    }

    @Override
    public void withdrawAmount(
            double amount,
            Currency amountCurrency,
            Account account,
            String verificationPayload
    ) throws InsufficientFundsException, UnauthorizedOperationException {
        if (!verificationService.verifyIsWithdrawPoint(verificationPayload)) {
            throw new UnauthorizedOperationException("Can not withdraw money via this method!");
        }
        long amountAsFixed = numericService.mapFloatToFixed(amount);
        Currency properCurrency = account.getCurrency();
        long amountToWithdrawInProperCurrency = 0L;
        amountToWithdrawInProperCurrency = numericService.mapFromAmountInForeignCurrency(
                amountAsFixed,
                amountCurrency,
                properCurrency
        );
        long balanceInProperCurrency = account.getBalanceInCents();
        if (balanceInProperCurrency < amountToWithdrawInProperCurrency) {
            throw new InsufficientFundsException("Can not withdraw due to insufficient funds!");
        } else {
            long newBalance = balanceInProperCurrency - amountToWithdrawInProperCurrency;
            overwriteBalance(account.getId(), newBalance);
        }
    }

    @Override
    public void transferAmount(
            double amount,
            Currency amountCurrency,
            Account fromAccount,
            Account toAccount
    ) throws InsufficientFundsException {
        long amountAsFixed = numericService.mapFloatToFixed(amount);
        Currency sourceCurrency = fromAccount.getCurrency();
        long amountToWithdrawInSourceCurrency = 0L;
        amountToWithdrawInSourceCurrency = numericService.mapFromAmountInForeignCurrency(
                amountAsFixed,
                amountCurrency,
                sourceCurrency
        );
        long balanceInSourceCurrency = fromAccount.getBalanceInCents();
        if (balanceInSourceCurrency < amountToWithdrawInSourceCurrency) {
            throw new InsufficientFundsException("Can not transfer due to insufficient funds!");
        } else {
            long amountDepositedInDestinationCurrency = 0L;
            amountDepositedInDestinationCurrency = numericService.mapFromAmountInForeignCurrency(
                    amountToWithdrawInSourceCurrency,
                    sourceCurrency,
                    toAccount.getCurrency()
            );
            long newDestinationBalance =  toAccount.getBalanceInCents() + amountDepositedInDestinationCurrency;
            long newSourceBalance = balanceInSourceCurrency - amountToWithdrawInSourceCurrency;

            overwriteBalanceIn2Accounts(
                    newSourceBalance,
                    fromAccount.getId(),
                    newDestinationBalance,
                    toAccount.getId()
            );
        }
    }

    @Override
    public BalanceDto displayBalance(Account account) {
        return balanceMapper.balanceDtoFromAccountEntity(account);
    }

    @Transactional(
            propagation = Propagation.REQUIRED,
            rollbackFor = {IllegalArgumentException.class, NullPointerException.class, IOException.class}
    )
    public void overwriteBalanceIn2Accounts(long balance1, Long account1Id, long balance2, Long account2Id)
            throws NoSuchElementException {
        Account maybeAccount1 = accountRepo.findById(account1Id).orElseThrow();
        Account maybeAccount2 = accountRepo.findById(account2Id).orElseThrow();
        maybeAccount1.setBalanceInCents(balance1);
        maybeAccount2.setBalanceInCents(balance2);
        accountRepo.save(maybeAccount1);
        accountRepo.save(maybeAccount2);
    }



    @Transactional(
            propagation = Propagation.REQUIRED,
            rollbackFor = {IllegalArgumentException.class, NullPointerException.class, IOException.class}
    )
    public void overwriteBalance(Long accountId, long newBalance) throws NoSuchElementException {
        Account maybeAccount = accountRepo.findById(accountId).orElseThrow();
        maybeAccount.setBalanceInCents(newBalance);
        accountRepo.save(maybeAccount);
    }

    @Transactional(
            propagation = Propagation.REQUIRED,
            rollbackFor = {IllegalArgumentException.class, NullPointerException.class, IOException.class}
    )
    public AccountDto getAccountDtoAfterPersisting(Account accountToWrite) {
        OwnerInfo ownerInfoToWrite = accountToWrite.getOwnerInfo();
        Credentials credentialsToWrite = accountToWrite.getCredentials();
        ownerService.saveOwnerInfo(ownerInfoToWrite);
        securityService.saveCredentials(credentialsToWrite);
        Account createdAccount = accountRepo.save(accountToWrite);
        return accountMapper.accountEntityToDto(createdAccount);
    }

}

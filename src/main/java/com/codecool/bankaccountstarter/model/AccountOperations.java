package com.codecool.bankaccountstarter.model;

import com.codecool.bankaccountstarter.model.dto.AccountDto;
import com.codecool.bankaccountstarter.model.dto.BalanceDto;
import com.codecool.bankaccountstarter.model.exception.DuplicateAccountCodeException;
import com.codecool.bankaccountstarter.model.exception.InsufficientFundsException;
import com.codecool.bankaccountstarter.model.exception.UnauthorizedOperationException;
import org.springframework.transaction.TransactionSystemException;

import java.util.Optional;

public interface AccountOperations {

    AccountDto createLocalAccount(String accountOwner, String accountPassword) throws TransactionSystemException;

    AccountDto createSpecificAccount(
            String accountCode,
            String accountOwner,
            String accountPassword
    ) throws DuplicateAccountCodeException, TransactionSystemException;

    Optional<Account> getAccountById(Long id);

    Optional<Account> getAccountByCode(String accountCode);

    void depositAmount(
            double amount,
            Currency amountCurrency,
            Account account,
            String confirmationPayload
    ) throws UnauthorizedOperationException, TransactionSystemException;

    void withdrawAmount(
            double amount,
            Currency amountCurrency,
            Account account,
            String verificationPayload
    ) throws InsufficientFundsException, UnauthorizedOperationException, TransactionSystemException;

    void transferAmount(
            double amount,
            Currency amountCurrency,
            Account fromAccount,
            Account toAccount
    ) throws InsufficientFundsException, TransactionSystemException;

    BalanceDto displayBalance(Account account);
}

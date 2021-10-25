package com.codecool.bankaccountstarter.model;

import com.codecool.bankaccountstarter.model.dto.AccountDto;
import com.codecool.bankaccountstarter.model.exception.DuplicateAccountCodeException;
import com.codecool.bankaccountstarter.model.exception.InsufficientFundsException;
import com.codecool.bankaccountstarter.model.exception.UnauthorizedOperationException;

import java.util.Optional;
import java.util.Set;

public interface AccountOperations {

    AccountDto createLocalAccount(String accountOwner, String accountPassword);

    AccountDto createSpecificAccount(
            String accountCode,
            String accountOwner,
            String accountPassword
    ) throws DuplicateAccountCodeException;

    Optional<Account> getAccountById(Long id);

    Optional<Account> getAccountByCode(String accountCode);

    void depositAmount(
            Double amount,
            Currency amountCurrency,
            Account account,
            String confirmationPayload
    ) throws UnauthorizedOperationException;

    void withdrawAmount(
            Double amount,
            Currency amountCurrency,
            Account account,
            String verificationPayload
    ) throws InsufficientFundsException, UnauthorizedOperationException;

    void transferAmount(
            Double amount,
            Currency amountCurrency,
            Account fromAccount,
            Account toAccount
    ) throws InsufficientFundsException;

    Double displayBalance(Account account);
}

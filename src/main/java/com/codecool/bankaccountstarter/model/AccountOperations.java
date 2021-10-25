package com.codecool.bankaccountstarter.model;

import com.codecool.bankaccountstarter.model.dto.AccountDto;
import com.codecool.bankaccountstarter.model.exception.DuplicateAccountCodeException;
import com.codecool.bankaccountstarter.model.exception.InsufficientFundsException;
public interface AccountOperations {

    AccountDto createLocalAccount(String accountOwner, String accountPassword);

    AccountDto createSpecificAccount(
            String accountCode,
            String accountOwner,
            String accountPassword
    ) throws DuplicateAccountCodeException;

}

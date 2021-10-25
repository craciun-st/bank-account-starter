package com.codecool.bankaccountstarter.model.exception;

public class DuplicateAccountCodeException extends Exception {

    public DuplicateAccountCodeException(String message) {
        super(message);
    }
}

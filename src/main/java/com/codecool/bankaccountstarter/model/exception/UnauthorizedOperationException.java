package com.codecool.bankaccountstarter.model.exception;

public class UnauthorizedOperationException extends Exception {
    public UnauthorizedOperationException(String message) {
        super(message);
    }
}

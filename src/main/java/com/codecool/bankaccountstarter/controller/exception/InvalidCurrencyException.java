package com.codecool.bankaccountstarter.controller.exception;

public class InvalidCurrencyException extends Exception {

    public InvalidCurrencyException() {
        super();
    }

    public InvalidCurrencyException(String message) {
        super(message);
    }
}

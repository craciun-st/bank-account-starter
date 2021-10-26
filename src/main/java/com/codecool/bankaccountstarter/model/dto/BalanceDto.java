package com.codecool.bankaccountstarter.model.dto;

public class BalanceDto {

    protected double balance;
    protected String currency;


    public BalanceDto(double balance, String currency) {
        this.balance = balance;
        this.currency = currency;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "BalanceDto{" +
                "balance=" + balance +
                ", currency='" + currency + '\'' +
                '}';
    }
}

package com.codecool.bankaccountstarter.model.dto.requests;

import com.codecool.bankaccountstarter.model.Account;
import com.codecool.bankaccountstarter.model.Currency;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

public class TransferDto {

    @Positive
    protected double amount;

    @NotBlank
    @Pattern(regexp = "^[A-Z]{3}$")
    protected String amountCurrency;

    @NotNull
    protected Long fromAccountId;

    @NotNull
    protected Long toAccountId;

    public TransferDto(double amount, String amountCurrency, Long fromAccountId, Long toAccountId) {
        this.amount = amount;
        this.amountCurrency = amountCurrency;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAmountCurrency() {
        return amountCurrency;
    }

    public void setAmountCurrency(String amountCurrency) {
        this.amountCurrency = amountCurrency;
    }

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }
}

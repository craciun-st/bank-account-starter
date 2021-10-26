package com.codecool.bankaccountstarter.model.dto.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

public abstract class BalanceModificationDto {

    @Positive
    protected double amount;

    @NotBlank
    @Pattern(regexp = "^[A-Z]{3}$")
    protected String amountCurrency;

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
}

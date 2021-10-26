package com.codecool.bankaccountstarter.model.dto.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DepositDto {

    @Positive
    protected double amount;

    @NotBlank
    @Pattern(regexp = "^[A-Z]{3}$")
    protected String amountCurrency;

    protected String confirmationPayload;

    public DepositDto(double amount, String amountCurrency, String confirmationPayload) {
        this.amount = amount;
        this.amountCurrency = amountCurrency;
        this.confirmationPayload = confirmationPayload;
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

    public String getConfirmationPayload() {
        return confirmationPayload;
    }

    public void setConfirmationPayload(String confirmationPayload) {
        this.confirmationPayload = confirmationPayload;
    }
}

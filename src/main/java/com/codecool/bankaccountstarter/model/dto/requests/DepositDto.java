package com.codecool.bankaccountstarter.model.dto.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DepositDto extends BalanceModificationDto {



    protected String confirmationPayload;

    public DepositDto(double amount, String amountCurrency, String confirmationPayload) {
        this.amount = amount;
        this.amountCurrency = amountCurrency;
        this.confirmationPayload = confirmationPayload;
    }



    public String getConfirmationPayload() {
        return confirmationPayload;
    }

    public void setConfirmationPayload(String confirmationPayload) {
        this.confirmationPayload = confirmationPayload;
    }
}

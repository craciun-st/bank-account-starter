package com.codecool.bankaccountstarter.model.dto.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WithdrawDto extends BalanceModificationDto {

    protected String verificationPayload;


    public WithdrawDto(double amount, String amountCurrency, String verificationPayload) {
        this.amount = amount;
        this.amountCurrency = amountCurrency;
        this.verificationPayload = verificationPayload;
    }

    public String getVerificationPayload() {
        return verificationPayload;
    }

    public void setVerificationPayload(String verificationPayload) {
        this.verificationPayload = verificationPayload;
    }
}

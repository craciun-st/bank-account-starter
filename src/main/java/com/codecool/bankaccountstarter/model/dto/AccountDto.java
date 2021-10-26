package com.codecool.bankaccountstarter.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDto {


    private Long id;
    private String code;
    private String currency;
    private OwnerInfoDto ownerInfo;
    private double balance;

    public AccountDto(Long id, String code, String currency, OwnerInfoDto ownerInfo, double balance) {
        this.id = id;
        this.code = code;
        this.currency = currency;
        this.ownerInfo = ownerInfo;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public OwnerInfoDto getOwnerInfo() {
        return ownerInfo;
    }

    public void setOwnerInfo(OwnerInfoDto ownerInfo) {
        this.ownerInfo = ownerInfo;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "AccountDto{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", currency='" + currency + '\'' +
                ", ownerInfo=" + ownerInfo +
                ", balance=" + String.format("%.2f", balance) +
                '}';
    }




}

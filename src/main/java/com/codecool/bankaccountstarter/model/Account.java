package com.codecool.bankaccountstarter.model;

import com.codecool.bankaccountstarter.model.security.Credentials;
import com.codecool.bankaccountstarter.util.StringUtil;

import javax.persistence.*;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name="iban", nullable = false, unique=true)
    private String code;    // IBAN, BIC, etc.

    @ManyToOne
    @JoinColumn(name="owner_id", nullable = false)
    private OwnerInfo ownerInfo;

    @OneToOne
    @JoinColumn(name="credentials_id", nullable = false, unique = true, updatable = false)
    private Credentials credentials;


    private long balanceInCents = 0;

    @Enumerated(EnumType.STRING)
    private Currency currency = Currency.EUR;

    public Account() {

    }

    public Account(
            Long id,
            String code,
            OwnerInfo ownerInfo,
            Credentials credentials,
            long balanceInCents,
            Currency currency
    ) {
        this.id = id;
        this.code = code;
        this.ownerInfo = ownerInfo;
        this.credentials = credentials;
        if (balanceInCents < 0) {
            throw new IllegalArgumentException("Balance should be non-negative!");
        } else {
            balanceInCents = balanceInCents;
        }
        this.currency = currency;
    }

    public Account(Builder builder) {
        OwnerInfo ownerInfo = new OwnerInfo(null, builder.ownerName);
        Credentials credentials = new Credentials(null, builder.ownerPassword);

        this.code = builder.accountCode;
        this.currency = builder.accountCurrency;
        this.ownerInfo = ownerInfo;
        this.credentials = credentials;
    }






    // BUILDER

    public static class Builder {
        private String ownerName;
        private String ownerPassword;
        private String accountCode;
        private Currency accountCurrency;

        public Builder() {
            this.accountCode = "RO42TEST"+ StringUtil.generateRandomUpcaseAlphaNumericString((byte) 16, null);
            this.accountCurrency = Currency.EUR;
        }

        public Builder reset() {
            return new Builder();
        }

        public Builder withOwnerName(String name) {
            this.ownerName = name;
            return this;
        }

        public Builder withPassword(String password) {
            this.ownerPassword = password;
            return this;
        }

        public Builder withAccountCode(String code) {
            this.accountCode = code;
            return this;
        }

        public Builder withCurrency(Currency currency) {
            this.accountCurrency = currency;
            return this;
        }

        public Account build() {
            return new Account(this);
        }
    }


    // Getters and setters


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

    public OwnerInfo getOwnerInfo() {
        return ownerInfo;
    }

    public void setOwnerInfo(OwnerInfo ownerInfo) {
        this.ownerInfo = ownerInfo;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public long getBalanceInCents() {
        return balanceInCents;
    }

    public void setBalanceInCents(long balanceInCents) {
        this.balanceInCents = balanceInCents;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}

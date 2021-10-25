package com.codecool.bankaccountstarter.model;

import com.codecool.bankaccountstarter.model.security.Credentials;
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

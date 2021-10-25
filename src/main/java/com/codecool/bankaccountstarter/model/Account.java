package com.codecool.bankaccountstarter.model;

import com.codecool.bankaccountstarter.model.security.Credentials;
import javax.persistence.*;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String code;    // IBAN, BIC, etc.

    private OwnerInfo ownerInfo;

    private Credentials credentials;


    private long balanceInCents = 0;

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

}

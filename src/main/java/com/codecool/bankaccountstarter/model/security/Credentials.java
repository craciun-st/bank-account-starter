package com.codecool.bankaccountstarter.model.security;

import com.codecool.bankaccountstarter.model.Account;

import javax.persistence.*;

@Entity
public class Credentials {

    // attributes can be extended/be more detailed later on

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String password;

    @OneToOne(mappedBy = "credentials")
    private Account forAccount;

    public Credentials() {}

    public Credentials(Long id, String password) {
        this.id = id;
        this.password = password;
    }


}

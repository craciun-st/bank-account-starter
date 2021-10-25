package com.codecool.bankaccountstarter.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class OwnerInfo {

    // attributes may be extended/more detailed later on


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "ownerInfo", cascade = CascadeType.ALL)
    private Set<Account> accounts;


    // Constructors

    public OwnerInfo() {}

    public OwnerInfo(Long id, String name) {
        this.id = id;
        this.name = name;
        this.accounts = new HashSet<>();
    }

    public OwnerInfo(Long id, String name, Set<Account> accounts) {
        this.id = id;
        this.name = name;
        this.accounts = accounts;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Account> getAccounts() {
        return new HashSet<>(accounts);
    }

    public void setAccounts(Set<Account> accountList) {
        this.accounts = accountList;
    }


    // Custom methods

    public void addAccount(Account account) {
        account.setOwnerInfo(this);
        this.accounts.add(account);
    }

    public void removeAccount(Account account) {
        boolean success = this.accounts.remove(account);
        if (success) {account.setOwnerInfo(null);}
    }
}

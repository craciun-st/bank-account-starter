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

}

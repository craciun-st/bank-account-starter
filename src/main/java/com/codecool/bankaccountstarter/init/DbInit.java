package com.codecool.bankaccountstarter.init;

import com.codecool.bankaccountstarter.model.Account;
import com.codecool.bankaccountstarter.model.OwnerInfo;
import com.codecool.bankaccountstarter.model.security.Credentials;
import com.codecool.bankaccountstarter.repository.AccountRepository;
import com.codecool.bankaccountstarter.repository.CredentialsRepository;
import com.codecool.bankaccountstarter.repository.OwnerInfoRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DbInit implements CommandLineRunner {

    private OwnerInfoRepository ownerInfoRepo;
    private CredentialsRepository credentialsRepo;
    private AccountRepository accountRepo;

    @Autowired
    public DbInit(OwnerInfoRepository ownerInfoRepo, CredentialsRepository credentialsRepo, AccountRepository accountRepo) {
        this.ownerInfoRepo = ownerInfoRepo;
        this.credentialsRepo = credentialsRepo;
        this.accountRepo = accountRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Account testAccount = new Account.Builder()
                .withOwnerName(faker.name().fullName())
                .withAccountCode("RO42TEST0000111122223333")
                .withPassword("this-is-not-a-password")
                .build();

        OwnerInfo testOwner = testAccount.getOwnerInfo();
        Credentials testCredentials = testAccount.getCredentials();

        ownerInfoRepo.save(testOwner);
        credentialsRepo.save(testCredentials);
        accountRepo.save(testAccount);
    }

}

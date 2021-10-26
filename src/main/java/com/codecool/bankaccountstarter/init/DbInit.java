package com.codecool.bankaccountstarter.init;

import com.codecool.bankaccountstarter.model.Account;
import com.codecool.bankaccountstarter.model.OwnerInfo;
import com.codecool.bankaccountstarter.model.dto.AccountDto;
import com.codecool.bankaccountstarter.model.dto.AccountMapper;
import com.codecool.bankaccountstarter.model.security.Credentials;
import com.codecool.bankaccountstarter.repository.AccountRepository;
import com.codecool.bankaccountstarter.repository.CredentialsRepository;
import com.codecool.bankaccountstarter.repository.OwnerInfoRepository;
import com.codecool.bankaccountstarter.service.AccountService;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionSystemException;

import javax.validation.ValidationException;


@Component
public class DbInit implements CommandLineRunner {

    Logger logger = LoggerFactory.getLogger(DbInit.class);

    private OwnerInfoRepository ownerInfoRepo;
    private CredentialsRepository credentialsRepo;
    private AccountRepository accountRepo;
    private AccountMapper accountMapper;
    private AccountService accountService;

    @Autowired
    public DbInit(
            OwnerInfoRepository ownerInfoRepo,
            CredentialsRepository credentialsRepo,
            AccountRepository accountRepo,
            AccountMapper accountMapper,
            AccountService accountService
    ) {
        this.ownerInfoRepo = ownerInfoRepo;
        this.credentialsRepo = credentialsRepo;
        this.accountRepo = accountRepo;
        this.accountMapper = accountMapper;
        this.accountService = accountService;
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

        testAccount.setBalanceInCents(12345L);
        ownerInfoRepo.save(testOwner);
        credentialsRepo.save(testCredentials);
        accountRepo.save(testAccount);


        AccountDto testAccountDto = accountMapper.accountEntityToDto(testAccount);
        logger.warn(testAccountDto.toString());

        AccountDto testInterfaceDto = new AccountDto(2L, "", "", null, 123.5);
        try {
            testInterfaceDto = accountService.createLocalAccount(
                    "",
                    ""
            );
        } catch (TransactionSystemException|IllegalArgumentException e) {
            logger.error("Could not commit object!");
        } catch (ValidationException e) {
            logger.error("Object is not valid!");
        }
        logger.warn(testInterfaceDto.toString());
    }

}

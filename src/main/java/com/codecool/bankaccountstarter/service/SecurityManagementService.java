package com.codecool.bankaccountstarter.service;

import com.codecool.bankaccountstarter.model.security.Credentials;
import com.codecool.bankaccountstarter.repository.CredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
public class SecurityManagementService {

    private CredentialsRepository credentialsRepo;

    @Autowired
    public SecurityManagementService(CredentialsRepository credentialsRepo) {
        this.credentialsRepo = credentialsRepo;
    }

    @Transactional(
            propagation = Propagation.REQUIRED,
            rollbackFor = {IllegalArgumentException.class, NullPointerException.class, IOException.class}

    )
    public Credentials saveCredentials(Credentials credentials) {
        return credentialsRepo.save(credentials);
    }
}

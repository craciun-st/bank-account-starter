package com.codecool.bankaccountstarter.repository;

import com.codecool.bankaccountstarter.model.security.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialsRepository extends JpaRepository<Credentials, Long> {
}

package com.codecool.bankaccountstarter.repository;

import com.codecool.bankaccountstarter.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByCode(String accountCode);
}

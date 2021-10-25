package com.codecool.bankaccountstarter.repository;

import com.codecool.bankaccountstarter.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}

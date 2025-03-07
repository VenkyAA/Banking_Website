package com.microservices.account_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.microservices.account_service.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}


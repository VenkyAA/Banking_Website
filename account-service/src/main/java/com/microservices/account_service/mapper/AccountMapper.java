package com.microservices.account_service.mapper;

import com.microservices.account_service.dto.AccountDTO;
import com.microservices.account_service.entity.Account;

public class AccountMapper {

    public static Account mapToAccount(AccountDTO acc) {
        return new Account(acc.getId(), acc.getAccountHolder(), acc.getBalance(), acc.getRole(), acc.getUsername());
    }

    public static AccountDTO mapToAccountDTO(Account account) {
        return new AccountDTO(account.getId(), account.getAccountHolder(), account.getBalance(), account.getRole(), account.getUsername());
    }
}





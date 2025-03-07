package com.microservices.account_service.service;

import com.microservices.account_service.dto.AccountDTO;
import com.microservices.account_service.dto.AccountCreationRequest;
import java.util.List;

public interface AccountService {
    AccountDTO createAccount(AccountCreationRequest accountCreationDTO);
    
    AccountDTO getAccountById(long id);
    
    List<AccountDTO> getAllAccounts();
    
    void deleteAccount(long id);
    
    void updateAccountBalance(long id, double amount);
}


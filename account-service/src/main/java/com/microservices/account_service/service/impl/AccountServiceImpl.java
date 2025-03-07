package com.microservices.account_service.service.impl;

import com.microservices.account_service.dto.AccountCreationRequest;
import com.microservices.account_service.dto.AccountDTO;
import com.microservices.account_service.dto.UserDTO;
import com.microservices.account_service.entity.Account;
import com.microservices.account_service.exception.AccountNotFoundException;
import com.microservices.account_service.mapper.AccountMapper;
import com.microservices.account_service.repository.AccountRepository;
import com.microservices.account_service.service.AccountService;
import com.microservices.account_service.feign.LoanClient;
import com.microservices.account_service.feign.ProfileClient;
import com.microservices.account_service.feign.TransactionClient;
import com.microservices.account_service.feign.UserClient;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountRepository accountRepository;
    private final UserClient userClient;
    private final TransactionClient transactionClient;
    private final ProfileClient profileClient;
    private final LoanClient loanClient;

    public AccountServiceImpl(AccountRepository accountRepository, UserClient userClient, TransactionClient transactionClient, ProfileClient profileClient, LoanClient loanClient) {
        this.accountRepository = accountRepository;
        this.userClient = userClient;
        this.transactionClient = transactionClient;
        this.profileClient = profileClient;
        this.loanClient = loanClient;
    }

    @Override
    public AccountDTO createAccount(AccountCreationRequest accountCreationDTO) {
        AccountDTO acc = accountCreationDTO.getAccountDTO();
        UserDTO userDTO = accountCreationDTO.getUserDTO();

        userDTO.setId(acc.getId());
        userClient.registerUser(userDTO);

        // Store the username in the account
        acc.setUsername(userDTO.getUsername());

        Account account = AccountMapper.mapToAccount(acc);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDTO(savedAccount);
    }

    @Override
    public AccountDTO getAccountById(long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account does not exist"));
        return AccountMapper.mapToAccountDTO(account);
    }

    @Override
    public List<AccountDTO> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(AccountMapper::mapToAccountDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteAccount(long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account does not exist"));
        accountRepository.delete(account);

        String username = account.getUsername();
        logger.info("Deleting user with username: {}", username);
        userClient.deleteUserByUsername(username);
        
     // Delete transactions associated with the account
        transactionClient.deleteTransactionsByAccountId(id);
        
     // Delete profile associated with the account
        profileClient.deleteProfile(id);
        
     // Delete loans associated with the account
        loanClient.deleteLoansByAccountId(id);
        
    }

    @Override
    public void updateAccountBalance(long id, double amount) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account does not exist"));
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }
}


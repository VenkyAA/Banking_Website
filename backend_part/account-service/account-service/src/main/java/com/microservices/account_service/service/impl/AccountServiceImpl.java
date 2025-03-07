package com.microservices.account_service.service.impl;

import com.microservices.account_service.dto.AccountCreationRequest;
import com.microservices.account_service.dto.AccountDTO;
import com.microservices.account_service.dto.UserDTO;
import com.microservices.account_service.entity.Account;
import com.microservices.account_service.exception.AccountNotFoundException;
import com.microservices.account_service.exception.InsufficientBalanceException;
import com.microservices.account_service.mapper.AccountMapper;
import com.microservices.account_service.repository.AccountRepository;
import com.microservices.account_service.service.AccountService;
import com.microservices.account_service.feign.UserClient;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AccountServiceImpl implements AccountService {

    
    private AccountRepository accountRepository;
    
    private UserClient userClient;
    
    String msg = "Account does not exist";
    
    public AccountServiceImpl(AccountRepository accountRepository, UserClient userClient) {
		super();
		this.accountRepository = accountRepository;
		this.userClient = userClient;
	}

	@Override
    public AccountDTO createAccount(AccountCreationRequest accountCreationDTO) {
        // Extract AccountDTO and UserDTO
        AccountDTO acc = accountCreationDTO.getAccountDTO();
        UserDTO userDTO = accountCreationDTO.getUserDTO();

        // Register user in USER-SERVICE
        userDTO.setId(acc.getId());
        userClient.registerUser(userDTO);
        
        // Create account
        Account account = AccountMapper.mapToAccount(acc);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDTO(savedAccount);
    }

    @Override
    public AccountDTO getAccountById(long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(msg));
        return AccountMapper.mapToAccountDTO(account);
    }

    @Override
    public AccountDTO deposit(long id, double amount) {
    	Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(msg));
        account.setBalance(account.getBalance() + amount);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDTO(savedAccount);
    }

    @Override
    public AccountDTO withdraw(long id, double amount) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(msg));
        if (account.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
        account.setBalance(account.getBalance() - amount);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDTO(savedAccount);
    }

    @Override
    public List<AccountDTO> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(AccountMapper::mapToAccountDTO).toList();
    }


    @Override
    public void deleteAccount(long id) {
        accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account does not exist"));
        accountRepository.deleteById(id);
    }


    @Override
    public void updateAccountBalance(long id, double amount) {
    	Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(msg));
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }

	
}
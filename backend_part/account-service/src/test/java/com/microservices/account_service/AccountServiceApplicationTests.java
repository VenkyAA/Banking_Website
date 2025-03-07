package com.microservices.account_service;

import com.microservices.account_service.dto.*;
import com.microservices.account_service.entity.Account;
import com.microservices.account_service.exception.AccountNotFoundException;
import com.microservices.account_service.feign.UserClient;
import com.microservices.account_service.repository.AccountRepository;
import com.microservices.account_service.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceApplicationTests {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private AccountServiceImpl accountService;

    private AccountDTO accountDTO;
    private Account account;
    private UserDTO userDTO;
    private AccountCreationRequest accountCreationRequest;

    @BeforeEach
    void setUp() {
        accountDTO = new AccountDTO(1L, "Venkat", 30000.0, "USER", "venky"); // Ensure role is either "USER" or "ADMIN"
        account = new Account(1L, "Venkat", 30000.0, "USER", "venky"); // Ensure role is either "USER" or "ADMIN"
        userDTO = new UserDTO(1L, "venky", "venky", "USER"); // Ensure role is either "USER" or "ADMIN"
        accountCreationRequest = new AccountCreationRequest();
        accountCreationRequest.setAccountDTO(accountDTO);
        accountCreationRequest.setUserDTO(userDTO);
    }

    @Test
    void testCreateAccount() {
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountDTO createdAccount = accountService.createAccount(accountCreationRequest);

        assertNotNull(createdAccount);
        assertEquals(accountDTO.getId(), createdAccount.getId());
        verify(userClient, times(1)).registerUser(any(UserDTO.class));
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testGetAccountById_ThrowsException_WhenAccountNotFound() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountById(1L));
        verify(accountRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetAccountById() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));

        AccountDTO foundAccount = accountService.getAccountById(1L);

        assertNotNull(foundAccount);
        assertEquals(accountDTO.getId(), foundAccount.getId());
        verify(accountRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetAllAccounts() {
        when(accountRepository.findAll()).thenReturn(List.of(account));

        List<AccountDTO> accounts = accountService.getAllAccounts();

        assertNotNull(accounts);
        assertFalse(accounts.isEmpty());
        assertEquals(1, accounts.size());
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    void testDeleteAccount() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));

        accountService.deleteAccount(1L);

        verify(accountRepository, times(1)).findById(anyLong());
        verify(accountRepository, times(1)).delete(any(Account.class));
    }

    @Test
    void testUpdateAccountBalance() {
        account.setBalance(1000.0);
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        accountService.updateAccountBalance(1L, 500.0);

        assertEquals(1500.0, account.getBalance());
        verify(accountRepository, times(1)).findById(anyLong());
        verify(accountRepository, times(1)).save(any(Account.class));
    }
}



package com.microservices.account_service;

import com.microservices.account_service.dto.*;
import com.microservices.account_service.entity.Account;
import com.microservices.account_service.exception.AccountNotFoundException;
import com.microservices.account_service.exception.InsufficientBalanceException;
import com.microservices.account_service.feign.UserClient;
import com.microservices.account_service.mapper.AccountMapper;
import com.microservices.account_service.repository.AccountRepository;
import com.microservices.account_service.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class AccountServiceApplicationTests {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private AccountServiceImpl accountService;

    private AccountDTO accountDTO;
    private Account account;

    @BeforeEach
    void setUp() {
        accountDTO = new AccountDTO(1L, "Venkat", 30000.0);
        account = AccountMapper.mapToAccount(accountDTO);
    }

    @Test
    void testCreateAccount() {
        AccountCreationRequest request = new AccountCreationRequest();
        request.setAccountDTO(accountDTO);
        request.setUserDTO(new UserDTO(1L, "venky", "venky"));

        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountDTO createdAccount = accountService.createAccount(request);

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
    void testDeposit() {
        account.setBalance(1000.0);
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArguments()[0]);

        AccountDTO updatedAccount = accountService.deposit(1L, 500.0);

        assertNotNull(updatedAccount);
        assertEquals(1500.0, updatedAccount.getBalance());
        verify(accountRepository, times(1)).findById(anyLong());
        verify(accountRepository, times(1)).save(any(Account.class));
    }


    @Test
    void testWithdraw_ThrowsException_WhenInsufficientBalance() {
        account.setBalance(100.0);
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));

        assertThrows(InsufficientBalanceException.class, () -> accountService.withdraw(1L, 200.0));
        verify(accountRepository, times(1)).findById(anyLong());
    }

    
    @Test
    void testDeleteAccount() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));

        accountService.deleteAccount(1L);

        verify(accountRepository, times(1)).findById(anyLong());
        verify(accountRepository, times(1)).deleteById(anyLong());
    }
}


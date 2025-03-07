package com.microservices.transaction_service;

import com.microservices.transaction_service.dto.AccountDTO;
import com.microservices.transaction_service.dto.BalanceUpdateDTO;
import com.microservices.transaction_service.dto.TransactionDTO;
import com.microservices.transaction_service.entity.Transaction;
import com.microservices.transaction_service.exception.InsufficientBalanceException;
import com.microservices.transaction_service.exception.TransactionNotFoundException;
import com.microservices.transaction_service.feign.AccountTClient;
import com.microservices.transaction_service.mapper.TransactionMapper;
import com.microservices.transaction_service.repository.TransactionRepository;
import com.microservices.transaction_service.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceApplicationTests {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountTClient accountClient;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private TransactionDTO transactionDTO;
    private Transaction transaction;
    private AccountDTO accountDTO;

    @BeforeEach
    void setUp() {
        transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionId(1L);
        transactionDTO.setId(1L);
        transactionDTO.setType("DEPOSIT");
        transactionDTO.setAmount(100.0);
        transactionDTO.setTargetAccountId(null);

        transaction = new Transaction();
        transaction.setTransactionId(1L);
        transaction.setId(1L);
        transaction.setType("DEPOSIT");
        transaction.setAmount(100.0);
        transaction.setDate(new Date());
        transaction.setTargetAccountId(null);

        accountDTO = new AccountDTO(1L, "Venkat", 500.0);
    }

    @Test
    void testCreateTransaction() {
        when(accountClient.getAccountById(anyLong())).thenReturn(accountDTO);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionDTO createdTransaction = transactionService.createTransaction(transactionDTO);

        assertNotNull(createdTransaction);
        assertEquals(transactionDTO.getTransactionId(), createdTransaction.getTransactionId());
        verify(accountClient, times(1)).getAccountById(anyLong());
        verify(accountClient, times(1)).updateAccountBalance(anyLong(), any(BalanceUpdateDTO.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testCreateTransaction_ThrowsException_WhenInsufficientBalance() {
        accountDTO.setBalance(50.0); // Set balance less than transaction amount

        when(accountClient.getAccountById(anyLong())).thenReturn(accountDTO);

        assertThrows(InsufficientBalanceException.class, () -> transactionService.createTransaction(transactionDTO));
        verify(accountClient, times(1)).getAccountById(anyLong());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testGetTransactionsById() {
        when(transactionRepository.findById(anyLong())).thenReturn(List.of(transaction));

        List<TransactionDTO> transactions = transactionService.getTransactionsById(1L);

        assertNotNull(transactions);
        assertFalse(transactions.isEmpty());
        assertEquals(1, transactions.size());
        verify(transactionRepository, times(1)).findById(anyLong());
    }

    @Test
    void testCreateTransferTransaction() {
        transactionDTO.setType("TRANSFER");
        transactionDTO.setTargetAccountId(2L);

        AccountDTO targetAccountDTO = new AccountDTO(2L, "Target", 200.0);
        when(accountClient.getAccountById(1L)).thenReturn(accountDTO);
        when(accountClient.getAccountById(2L)).thenReturn(targetAccountDTO);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionDTO createdTransaction = transactionService.createTransferTransaction(transactionDTO);

        assertNotNull(createdTransaction);
        assertEquals(transactionDTO.getTransactionId(), createdTransaction.getTransactionId());
        verify(accountClient, times(2)).getAccountById(anyLong());
        verify(accountClient, times(1)).updateAccountBalance(1L, new BalanceUpdateDTO(-transactionDTO.getAmount()));
        verify(accountClient, times(1)).updateAccountBalance(2L, new BalanceUpdateDTO(transactionDTO.getAmount()));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testCreateTransferTransaction_ThrowsException_WhenInsufficientBalance() {
        transactionDTO.setType("TRANSFER");
        transactionDTO.setTargetAccountId(2L);
        accountDTO.setBalance(50.0); // Set balance less than transaction amount

        AccountDTO targetAccountDTO = new AccountDTO(2L, "Target", 200.0);
        when(accountClient.getAccountById(1L)).thenReturn(accountDTO);
        when(accountClient.getAccountById(2L)).thenReturn(targetAccountDTO);

        assertThrows(InsufficientBalanceException.class, () -> transactionService.createTransferTransaction(transactionDTO));
        verify(accountClient, times(1)).getAccountById(1L);
        verify(accountClient, never()).updateAccountBalance(anyLong(), any(BalanceUpdateDTO.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testDeleteTransaction() {
        when(transactionRepository.findByTransactionId(anyLong())).thenReturn(Optional.of(transaction));
        doNothing().when(transactionRepository).delete(any(Transaction.class));

        transactionService.deleteTransaction(1L);

        verify(transactionRepository, times(1)).findByTransactionId(anyLong());
        verify(transactionRepository, times(1)).delete(any(Transaction.class));
    }

    @Test
    void testDeleteTransaction_ThrowsException_WhenTransactionNotFound() {
        when(transactionRepository.findByTransactionId(anyLong())).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.deleteTransaction(1L));
        verify(transactionRepository, times(1)).findByTransactionId(anyLong());
        verify(transactionRepository, never()).delete(any(Transaction.class));
    }

    @Test
    void testGetAccountBalance() {
        when(accountClient.getAccountById(anyLong())).thenReturn(accountDTO);

        double balance = transactionService.getAccountBalance(1L);

        assertEquals(500.0, balance);
        verify(accountClient, times(1)).getAccountById(anyLong());
    }

    @Test
    void testGetTransactionByTransactionId() {
        when(transactionRepository.findByTransactionId(anyLong())).thenReturn(Optional.of(transaction));

        TransactionDTO transactionById = transactionService.getTransactionByTransactionId(1L);

        assertNotNull(transactionById);
        assertEquals(transactionDTO.getTransactionId(), transactionById.getTransactionId());
        verify(transactionRepository, times(1)).findByTransactionId(anyLong());
    }

    @Test
    void testGetTransactionByTransactionId_ThrowsException_WhenTransactionNotFound() {
        when(transactionRepository.findByTransactionId(anyLong())).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionByTransactionId(1L));
        verify(transactionRepository, times(1)).findByTransactionId(anyLong());
    }

    @Test
    void testWithdraw() {
        transactionDTO.setType("WITHDRAW");

        when(accountClient.getAccountById(anyLong())).thenReturn(accountDTO);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionDTO withdrawTransaction = transactionService.withdraw(transactionDTO);

        assertNotNull(withdrawTransaction);
        assertEquals(transactionDTO.getTransactionId(), withdrawTransaction.getTransactionId());
        verify(accountClient, times(1)).getAccountById(anyLong());
        verify(accountClient, times(1)).updateAccountBalance(anyLong(), any(BalanceUpdateDTO.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testWithdraw_ThrowsException_WhenInsufficientBalance() {
        transactionDTO.setType("WITHDRAW");
        accountDTO.setBalance(50.0); // Set balance less than transaction amount

        when(accountClient.getAccountById(anyLong())).thenReturn(accountDTO);

        assertThrows(InsufficientBalanceException.class, () -> transactionService.withdraw(transactionDTO));
        verify(accountClient, times(1)).getAccountById(anyLong());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testDeposit() {
        transactionDTO.setType("DEPOSIT");

        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionDTO depositTransaction = transactionService.deposit(transactionDTO);

        assertNotNull(depositTransaction);
        assertEquals(transactionDTO.getTransactionId(), depositTransaction.getTransactionId());
        verify(accountClient, times(1)).updateAccountBalance(anyLong(), any(BalanceUpdateDTO.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
}


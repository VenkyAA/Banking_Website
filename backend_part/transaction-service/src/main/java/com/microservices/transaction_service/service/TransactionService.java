package com.microservices.transaction_service.service;


import com.microservices.transaction_service.dto.TransactionDTO;
import java.util.List;

public interface TransactionService {
    TransactionDTO createTransaction(TransactionDTO transactionDTO);
    List<TransactionDTO> getTransactionsById(long id);
    
    TransactionDTO getTransactionByTransactionId(long id);
    
    TransactionDTO createTransferTransaction(TransactionDTO transactionDTO);
    
    TransactionDTO withdraw(TransactionDTO transactionDTO);
    
    TransactionDTO deposit(TransactionDTO transactionDTO);
    
    double getAccountBalance(long id);
    
    void deleteTransaction(long transactionId);
    
    void deleteTransactionsByAccountId(long id);
    
    List<TransactionDTO> getAllTransactions();
}

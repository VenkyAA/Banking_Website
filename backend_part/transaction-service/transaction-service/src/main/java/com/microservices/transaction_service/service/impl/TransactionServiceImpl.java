package com.microservices.transaction_service.service.impl;

import com.microservices.transaction_service.dto.TransactionDTO;
import com.microservices.transaction_service.entity.Transaction;
import com.microservices.transaction_service.exception.InsufficientBalanceException;
import com.microservices.transaction_service.exception.TransactionNotFoundException;
import com.microservices.transaction_service.mapper.TransactionMapper;
import com.microservices.transaction_service.repository.TransactionRepository;
import com.microservices.transaction_service.service.TransactionService;
import com.microservices.transaction_service.feign.AccountTClient;
import com.microservices.transaction_service.dto.AccountDTO;
import com.microservices.transaction_service.dto.BalanceUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = Logger.getLogger(TransactionServiceImpl.class.getName());

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountTClient accountClient;

    @Override
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        try {
            AccountDTO accountDTO = accountClient.getAccountById(transactionDTO.getId());
            if (accountDTO.getBalance() < transactionDTO.getAmount()) {
                throw new InsufficientBalanceException("Insufficient balance");
            }

            // Update the balance in the Account microservice
            accountClient.updateAccountBalance(transactionDTO.getId(), new BalanceUpdateDTO(-transactionDTO.getAmount()));

            // Save the transaction
            Transaction transaction = new Transaction();
            transaction.setTransactionId(transactionDTO.getTransactionId());
            transaction.setId(transactionDTO.getId());
            transaction.setTargetAccountId(transactionDTO.getTargetAccountId());
            transaction.setType(transactionDTO.getType());
            transaction.setAmount(transactionDTO.getAmount());
            transaction.setDate(new Date()); // Set current date
            
            // Log transaction details before saving
            logger.info("Saving transaction: " + transaction);
            
            Transaction savedTransaction = transactionRepository.save(transaction);

            // Log transaction details after saving
            logger.info("Transaction saved: " + savedTransaction);

            return TransactionMapper.mapToTransactionDTO(savedTransaction);
        } catch (Exception e) {
            logger.severe("Error creating transaction: " + e.getMessage());
            throw new RuntimeException("Error creating transaction", e);
        
        }
    }
     

    @Override
    public List<TransactionDTO> getTransactionsById(long id) {
        List<Transaction> transactions = transactionRepository.findById(id);
        if (transactions.isEmpty()) {
            throw new TransactionNotFoundException("No transactions found for account ID: " + id);
        }
        return transactions.stream()
                .map(TransactionMapper::mapToTransactionDTO)
                .collect(Collectors.toList());
    }




    @Override
    public TransactionDTO createTransferTransaction(TransactionDTO transactionDTO) {
        try {
            AccountDTO sourceAccountDTO = accountClient.getAccountById(transactionDTO.getId());
            AccountDTO targetAccountDTO = accountClient.getAccountById(transactionDTO.getTargetAccountId());

            if (sourceAccountDTO.getBalance() < transactionDTO.getAmount()) {
                throw new InsufficientBalanceException("Insufficient balance in source account");
            }

            // Update the balances in the Account microservice
            accountClient.updateAccountBalance(transactionDTO.getId(), new BalanceUpdateDTO(-transactionDTO.getAmount()));
            accountClient.updateAccountBalance(transactionDTO.getTargetAccountId(), new BalanceUpdateDTO(transactionDTO.getAmount()));

            // Save the transaction
            Transaction transaction = new Transaction();
            transaction.setTransactionId(transactionDTO.getTransactionId());
            transaction.setId(transactionDTO.getId());
            transaction.setTargetAccountId(transactionDTO.getTargetAccountId());
            transaction.setType(transactionDTO.getType());
            transaction.setAmount(transactionDTO.getAmount());
            transaction.setDate(new Date()); // Set current date

            // Log transaction details before saving
            logger.info("Saving transfer transaction: " + transaction);
            
            Transaction savedTransaction = transactionRepository.save(transaction);

            // Log transaction details after saving
            logger.info("Transfer transaction saved: " + savedTransaction);

            return TransactionMapper.mapToTransactionDTO(savedTransaction);
        } catch (InsufficientBalanceException e) {
            logger.severe("Error processing transfer transaction: " + e.getMessage());
            throw e; // Rethrow the custom exception
        } catch (Exception e) {
            logger.severe("Error processing transfer transaction: " + e.getMessage());
            throw new RuntimeException("Error processing transfer transaction", e);
        }
    }


    @Override
    public double getAccountBalance(long id) {
        AccountDTO accountDTO = accountClient.getAccountById(id);
        return accountDTO.getBalance();
    }

    @Override
    public void deleteTransaction(long transactionId) {
        try {
            Optional<Transaction> transactionOpt = transactionRepository.findByTransactionId(transactionId);
            Transaction transaction = transactionOpt.orElseThrow(() -> new TransactionNotFoundException("Transaction does not exist"));

            // Log transaction details before deletion
            logger.info("Deleting transaction: " + transaction);
            
            transactionRepository.delete(transaction);

            // Log transaction details after deletion
            logger.info("Transaction deleted: " + transactionId);
        } catch (TransactionNotFoundException e) {
            logger.severe("Error deleting transaction: " + e.getMessage());
            throw e; // Rethrow the custom exception
        } catch (Exception e) {
            logger.severe("Error deleting transaction: " + e.getMessage());
            throw new RuntimeException("Error deleting transaction", e);
        }
    }


    @Override
    public TransactionDTO getTransactionByTransactionId(long transactionId) {
        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                    .orElseThrow(() -> new TransactionNotFoundException("Transaction does not exist"));
        return TransactionMapper.mapToTransactionDTO(transaction);
    }

    
}
    



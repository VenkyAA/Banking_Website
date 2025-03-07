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
            // Get account details from the Account microservice
            AccountDTO accountDTO = accountClient.getAccountById(transactionDTO.getId());
            
            // Check if the account has sufficient balance
            if (accountDTO.getBalance() < transactionDTO.getAmount()) {
                throw new InsufficientBalanceException("Insufficient balance");
            }

            // Update the balance in the Account microservice
            accountClient.updateAccountBalance(transactionDTO.getId(), new BalanceUpdateDTO(-transactionDTO.getAmount()));

            // Save the transaction
            Transaction transaction = TransactionMapper.mapToTransaction(transactionDTO);
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
        } catch (InsufficientBalanceException e) {
            logger.severe("Insufficient balance: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.severe("Error creating transaction: " + e.getMessage());
            throw new RuntimeException("Error creating transaction", e);
        }
    }

    @Override
    public List<TransactionDTO> getTransactionsById(long id) {
        // Retrieve transactions by account ID
        List<Transaction> transactions = transactionRepository.findById(id);
        
        // Check if transactions exist
        if (transactions.isEmpty()) {
            throw new TransactionNotFoundException("No transactions found for account ID: " + id);
        }
        
        // Convert to DTOs and return the list
        return transactions.stream()
                .map(TransactionMapper::mapToTransactionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionDTO createTransferTransaction(TransactionDTO transactionDTO) {
        try {
            // Get account details for both source and target accounts
            AccountDTO sourceAccountDTO = accountClient.getAccountById(transactionDTO.getId());
            AccountDTO targetAccountDTO = accountClient.getAccountById(transactionDTO.getTargetAccountId());

            // Check if the source account has sufficient balance
            if (sourceAccountDTO.getBalance() < transactionDTO.getAmount()) {
                throw new InsufficientBalanceException("Insufficient balance in source account");
            }

            // Update the balances in the Account microservice
            accountClient.updateAccountBalance(transactionDTO.getId(), new BalanceUpdateDTO(-transactionDTO.getAmount()));
            accountClient.updateAccountBalance(transactionDTO.getTargetAccountId(), new BalanceUpdateDTO(transactionDTO.getAmount()));

            // Save the transfer transaction
            Transaction transaction = TransactionMapper.mapToTransaction(transactionDTO);
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
        // Get account details from the Account microservice
        AccountDTO accountDTO = accountClient.getAccountById(id);
        return accountDTO.getBalance();
    }

    @Override
    public void deleteTransaction(long transactionId) {
        try {
            // Find the transaction by ID
            Optional<Transaction> transactionOpt = transactionRepository.findByTransactionId(transactionId);
            Transaction transaction = transactionOpt.orElseThrow(() -> new TransactionNotFoundException("Transaction does not exist"));

            // Log transaction details before deletion
            logger.info("Deleting transaction: " + transaction);
            
            // Delete the transaction
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
        // Find the transaction by ID
        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                    .orElseThrow(() -> new TransactionNotFoundException("Transaction does not exist"));
        
        // Convert to DTO and return
        return TransactionMapper.mapToTransactionDTO(transaction);
    }

	@Override
	public List<TransactionDTO> getAllTransactions() {
		List<Transaction> accounts = transactionRepository.findAll();
        return accounts.stream().map(TransactionMapper::mapToTransactionDTO).collect(Collectors.toList());
	}

	@Override
	public TransactionDTO withdraw(TransactionDTO transactionDTO) {
		try {
            // Get account details from the Account microservice
            AccountDTO accountDTO = accountClient.getAccountById(transactionDTO.getId());
            
            // Check if the account has sufficient balance
            if (accountDTO.getBalance() < transactionDTO.getAmount()) {
                throw new InsufficientBalanceException("Insufficient balance");
            }

            // Update the balance in the Account microservice
            accountClient.updateAccountBalance(transactionDTO.getId(), new BalanceUpdateDTO(-transactionDTO.getAmount()));

            // Save the transaction
            Transaction transaction = TransactionMapper.mapToTransaction(transactionDTO);
            transaction.setTransactionId(transactionDTO.getTransactionId());
            transaction.setId(transactionDTO.getId());
            transaction.setTargetAccountId(transactionDTO.getId());
            transaction.setType(transactionDTO.getType());
            transaction.setAmount(transactionDTO.getAmount());
            transaction.setDate(new Date()); // Set current date
            
            // Log transaction details before saving
            logger.info("Saving transaction: " + transaction);
            
            Transaction savedTransaction = transactionRepository.save(transaction);

            // Log transaction details after saving
            logger.info("Transaction saved: " + savedTransaction);

            return TransactionMapper.mapToTransactionDTO(savedTransaction);
        } catch (InsufficientBalanceException e) {
            logger.severe("Insufficient balance: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.severe("Error creating transaction: " + e.getMessage());
            throw new RuntimeException("Error creating transaction", e);
        }
	}

	@Override
	public TransactionDTO deposit(TransactionDTO transactionDTO) {
		try {
   
			// Update the balance in the Account microservice
            accountClient.updateAccountBalance(transactionDTO.getId(), new BalanceUpdateDTO(transactionDTO.getAmount()));

            // Save the transaction
            Transaction transaction = TransactionMapper.mapToTransaction(transactionDTO);
            transaction.setTransactionId(transactionDTO.getTransactionId());
            transaction.setId(transactionDTO.getId());
            transaction.setTargetAccountId(transactionDTO.getId());
            transaction.setType(transactionDTO.getType());
            transaction.setAmount(transactionDTO.getAmount());
            transaction.setDate(new Date()); // Set current date
            
            // Log transaction details before saving
            logger.info("Saving transaction: " + transaction);
            
            Transaction savedTransaction = transactionRepository.save(transaction);

            // Log transaction details after saving
            logger.info("Transaction saved: " + savedTransaction);

            return TransactionMapper.mapToTransactionDTO(savedTransaction);
        } catch (InsufficientBalanceException e) {
            logger.severe("Insufficient balance: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.severe("Error creating transaction: " + e.getMessage());
            throw new RuntimeException("Error creating transaction", e);
        }
	}

	@Override
	public void deleteTransactionsByAccountId(long id) {
	    List<Transaction> transactions = transactionRepository.findById(id);
	    if (transactions.isEmpty()) {
	        throw new TransactionNotFoundException("No transactions found for account ID: " + id);
	    }
	    transactionRepository.deleteAll(transactions);
	    logger.info("Deleted transactions for account ID: " + id);
	}

}

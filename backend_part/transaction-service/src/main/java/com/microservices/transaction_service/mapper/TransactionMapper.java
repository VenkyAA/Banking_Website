package com.microservices.transaction_service.mapper;

import java.util.Date;
import com.microservices.transaction_service.dto.TransactionDTO;
import com.microservices.transaction_service.entity.Transaction;

public class TransactionMapper {

    // Maps TransactionDTO to Transaction entity
    public static Transaction mapToTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionDTO.getTransactionId()); // Generated value
        transaction.setId(transactionDTO.getId());
        transaction.setType(transactionDTO.getType());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setDate(new Date()); // Set current date
        transaction.setTargetAccountId(transactionDTO.getTargetAccountId());
        return transaction;
    }
    
    // Maps Transaction entity to TransactionDTO
    public static TransactionDTO mapToTransactionDTO(Transaction transaction) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionId(transaction.getTransactionId()); // Map generated value
        transactionDTO.setId(transaction.getId());
        transactionDTO.setType(transaction.getType());
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setTargetAccountId(transaction.getTargetAccountId());
        transactionDTO.setDate(transaction.getDate());
        return transactionDTO;
    }
}

package com.microservices.transaction_service.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
public class TransactionDTO {
    private long transactionId; // Generated value

    @NotNull(message = "Account ID cannot be null")
    private long id;

    @NotNull(message = "Transaction type cannot be null")
    private String type;

    @Positive(message = "Amount must be positive")
    private double amount;

    private Long targetAccountId;
}



package com.microservices.loan_service.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class AccountDTO {
    @NotNull(message = "Account ID cannot be null")
    private long id;

    @PositiveOrZero(message = "Balance cannot be negative")
    private double balance;

    @NotEmpty(message = "Account type cannot be empty")
    private String accountType;
}



package com.microservices.loan_service.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class LoanDTO {
    private long loanId;  // New attribute for loanId

    @NotNull(message = "Account ID cannot be null")
    private long id;

    @Positive(message = "Principal amount must be positive")
    private double principalAmount;

    @Min(value = 1, message = "Number of years must be at least 1")
    private int numberOfYears;

    @Positive(message = "Interest rate must be positive")
    private double interestRate;

    @PositiveOrZero(message = "Amount to be repaid cannot be negative")
    private double amountToBeRepaid;

    @PositiveOrZero(message = "Amount paid cannot be negative")
    private double amountPaid;

    @NotEmpty(message = "Loan type cannot be empty")
    private String loanType;
}


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

    @PositiveOrZero(message = "Interest rate cannot be negative")
    private double interestRate;

    @PositiveOrZero(message = "Amount to be repaid cannot be negative")
    private double amountToBeRepaid;

    @PositiveOrZero(message = "Amount paid cannot be negative")
    private double amountPaid;

    @NotEmpty(message = "Loan type cannot be empgty")
    private String loanType;
    
    @Min(value = 300, message = "CIBIL score must be at least 300")
    @Max(value = 900, message = "CIBIL score must be at most 900")
    private int cibilScore; // New attribute for CIBIL score
}


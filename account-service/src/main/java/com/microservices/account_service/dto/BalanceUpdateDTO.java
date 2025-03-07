package com.microservices.account_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BalanceUpdateDTO {
    @NotNull
    @Min(value = 0, message = "Amount must be non-negative")
    private double amount;

    // Default constructor
    public BalanceUpdateDTO() {}

    // Constructor with amount parameter
    public BalanceUpdateDTO(double amount) {
        this.amount = amount;
    }
}


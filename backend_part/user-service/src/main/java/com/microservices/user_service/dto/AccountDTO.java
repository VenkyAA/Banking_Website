package com.microservices.user_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountDTO {
    @Min(1)
    private long id;

    @NotEmpty(message = "Account holder name must not be empty")
    private String accountHolder;

    @NotNull
    @Min(value = 0, message = "Balance must be positive")
    private double balance;

    private String role; // Add role attribute

    public AccountDTO(long id, String accountHolder, double balance, String role) {
        this.id = id;
        this.accountHolder = accountHolder;
        this.balance = balance;
        this.role = role;
    }
}


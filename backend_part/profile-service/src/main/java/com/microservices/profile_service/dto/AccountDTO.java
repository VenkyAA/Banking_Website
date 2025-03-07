package com.microservices.profile_service.dto;

import lombok.Data;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Data
public class AccountDTO {
    @NotNull(message = "Account ID cannot be null")
    private long id;

    @NotEmpty(message = "Account holder cannot be empty")
    private String accountHolder;

    @PositiveOrZero(message = "Balance cannot be negative")
    private double balance;

    public AccountDTO() {}

    public AccountDTO(long id, String accountHolder, double balance) {
        this.id = id;
        this.accountHolder = accountHolder;
        this.balance = balance;
    }
}



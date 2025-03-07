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

    @NotNull(message = "Balance must not be null")
    @Min(value = 0, message = "Balance must be non-negative")
    private double balance;

    public AccountDTO(long id, String accountHolder, double balance) {
        this.id = id;
        this.accountHolder = accountHolder;
        this.balance = balance;
    }
}

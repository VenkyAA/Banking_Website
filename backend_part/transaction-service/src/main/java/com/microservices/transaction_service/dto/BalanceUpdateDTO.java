package com.microservices.transaction_service.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Data
public class BalanceUpdateDTO {
    @NotNull(message = "Amount cannot be null")
    @PositiveOrZero(message = "Amount must be zero or positive")
    private double amount;

    public BalanceUpdateDTO() {}

    public BalanceUpdateDTO(double amount) {
        this.amount = amount;
    }
}


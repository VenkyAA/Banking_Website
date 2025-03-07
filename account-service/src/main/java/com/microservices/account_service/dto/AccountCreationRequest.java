package com.microservices.account_service.dto;

import jakarta.validation.Valid;
import lombok.Data;

@Data
public class AccountCreationRequest {
    @Valid
    private AccountDTO accountDTO;
    
    @Valid
    private UserDTO userDTO;
}



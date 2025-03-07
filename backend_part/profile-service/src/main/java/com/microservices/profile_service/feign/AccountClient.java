package com.microservices.profile_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.microservices.profile_service.dto.AccountDTO;

import jakarta.validation.Valid;

// Define this interface as a Feign client to interact with the ACCOUNT-SERVICE
@FeignClient(name = "ACCOUNT-SERVICE")
public interface AccountClient {

    // Define a method to get account details by account ID
    @GetMapping("/accounts/{id}")
    AccountDTO getAccountById(@Valid @PathVariable("id") long id);
}



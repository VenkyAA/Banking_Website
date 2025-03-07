package com.microservices.transaction_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.microservices.transaction_service.dto.AccountDTO;
import com.microservices.transaction_service.dto.BalanceUpdateDTO;

import jakarta.validation.Valid;

@FeignClient(name = "ACCOUNT-SERVICE")
public interface AccountTClient {

    @GetMapping("/accounts/{id}")
    AccountDTO getAccountById(@PathVariable("id") long id);

    @PutMapping("/accounts/{id}/balance")
    void updateAccountBalance(@PathVariable("id") long id, @RequestBody @Valid BalanceUpdateDTO balanceUpdateDTO);

}


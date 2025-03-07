package com.microservices.account_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "TRANSACTION-SERVICE")
public interface TransactionClient {

    @DeleteMapping("/transactions/account/{id}")
    void deleteTransactionsByAccountId(@PathVariable long id);
}


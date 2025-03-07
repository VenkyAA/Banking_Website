package com.microservices.account_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;

@FeignClient(name = "LOAN-SERVICE")
public interface LoanClient {

    @DeleteMapping("/loans/account/{id}")
    ResponseEntity<Void> deleteLoansByAccountId(@PathVariable long id);
}



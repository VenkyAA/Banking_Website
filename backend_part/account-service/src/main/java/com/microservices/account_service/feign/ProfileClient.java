package com.microservices.account_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;

@FeignClient(name = "PROFILE-SERVICE")
public interface ProfileClient {
    
    @DeleteMapping("/profiles/{id}")
    ResponseEntity<String> deleteProfile(@PathVariable long id);
}


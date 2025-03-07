package com.microservices.account_service.feign;

import com.microservices.account_service.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;

@FeignClient(name = "USER-SERVICE")
public interface UserClient {

    // Feign client method to register a user
    @PostMapping("/users/register")
    ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO);
    
    @DeleteMapping("/users/username/{username}")
    void deleteUserByUsername(@PathVariable String username);

}




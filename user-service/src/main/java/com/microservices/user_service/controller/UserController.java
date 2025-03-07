package com.microservices.user_service.controller;

import com.microservices.user_service.dto.UserDTO;
import com.microservices.user_service.service.UserService;
import com.microservices.user_service.service.impl.JWTService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JWTService jwtService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody @Valid UserDTO userDTO) {
        userService.registerUser(userDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody @Valid UserDTO userDTO) {
        boolean isAuthenticated = userService.authenticate(userDTO);
        if (isAuthenticated) {
            return jwtService.generateToken(userDTO.getUsername());
        } else {
            return "Invalid username or password";
        }
    }
    
   
}

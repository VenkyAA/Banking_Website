package com.microservices.user_service.controller;

import com.microservices.user_service.dto.UserDTO;
import com.microservices.user_service.entity.User;
import com.microservices.user_service.service.UserService;
import com.microservices.user_service.service.impl.JWTService;
import com.microservices.user_service.dto.AuthResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
//@CrossOrigin(origins = "http://localhost:3001")
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
    public ResponseEntity<AuthResponse> loginUser(@RequestBody @Valid UserDTO userDTO) {
        boolean isAuthenticated = userService.authenticate(userDTO);
        if (isAuthenticated) {
            User user = userService.findUserByUsername(userDTO.getUsername());
            String token = jwtService.generateToken(user.getUsername(), user.getRole());
            AuthResponse authResponse = new AuthResponse(token, user.getId(), user.getRole());
            return ResponseEntity.ok(authResponse);
        } else {
            return ResponseEntity.status(401).body(null);
        }
    }   
    
    @DeleteMapping("/username/{username}")
    public ResponseEntity<Void> deleteUserByUsername(@PathVariable String username) {
        userService.deleteUserByUsername(username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

   
}
package com.microservices.user_service.service.impl;

import com.microservices.user_service.dto.UserDTO;
import com.microservices.user_service.entity.User;
import com.microservices.user_service.exception.AuthenticationFailedException;
import com.microservices.user_service.exception.UserNotFoundException;
import com.microservices.user_service.repository.UserRepository;
import com.microservices.user_service.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
   
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public boolean authenticate(UserDTO userDTO) {
        User user = userRepository.findByUsername(userDTO.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        boolean isAuthenticated = passwordEncoder.matches(userDTO.getPassword(), user.getPassword());

        if (isAuthenticated) {
            user.setLoginTime(LocalDateTime.now());
            userRepository.save(user);
        } else {
            throw new AuthenticationFailedException("Invalid username or password");
        }

        return isAuthenticated;
    }


    @Override
    public void registerUser(UserDTO userDTO) {
        // Fetch the account ID from the account service using the Feign client
        //AccountDTO accountDTO = accountClient.getAccountById(userDTO.getId());

        User user = new User();
        user.setId(userDTO.getId()); // Set the ID from account service
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(user);
    }
    
}
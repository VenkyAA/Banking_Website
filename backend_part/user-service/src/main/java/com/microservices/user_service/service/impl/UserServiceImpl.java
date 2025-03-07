package com.microservices.user_service.service.impl;

import com.microservices.user_service.dto.UserDTO;
import com.microservices.user_service.entity.User;
import com.microservices.user_service.exception.AuthenticationFailedException;
import com.microservices.user_service.exception.UserNotFoundException;
import com.microservices.user_service.repository.UserRepository;
import com.microservices.user_service.service.UserService;

import jakarta.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public boolean authenticate(UserDTO userDTO) {
        User user = userRepository.findByUsername(userDTO.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        boolean isAuthenticated = passwordEncoder.matches(userDTO.getPassword(), user.getPassword());

        if (isAuthenticated) {
            if (!user.getRole().equalsIgnoreCase(userDTO.getRole())) {
                throw new AuthenticationFailedException("Invalid role");
            }
            user.setLoginTime(LocalDateTime.now());
            userRepository.save(user);
        } else {
            throw new AuthenticationFailedException("Invalid username or password");
        }

        return isAuthenticated;
    }

    @Override
    public void registerUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(userDTO.getRole()); // Set the role
        userRepository.save(user);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public void deleteUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.delete(user);
    }


    
    
}

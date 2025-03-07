package com.microservices.user_service.service;

import com.microservices.user_service.dto.UserDTO;

public interface UserService {
    boolean authenticate(UserDTO userDTO);
    void registerUser(UserDTO userDTO); // Add new method for registering users
}



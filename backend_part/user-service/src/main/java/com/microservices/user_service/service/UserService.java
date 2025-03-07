package com.microservices.user_service.service;

import com.microservices.user_service.dto.UserDTO;
import com.microservices.user_service.entity.User;

import jakarta.validation.Valid;

public interface UserService {
    boolean authenticate(UserDTO userDTO);
    
    void registerUser(UserDTO userDTO); // Add new method for registering users
    
	User findUserByUsername(String username);

	void deleteUserByUsername(String username);

}



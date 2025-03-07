package com.microservices.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.microservices.user_service.entity.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
	
	@Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsername(String username);

    
    
}

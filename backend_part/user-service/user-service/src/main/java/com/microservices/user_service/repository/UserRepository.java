package com.microservices.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.microservices.user_service.entity.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    
    
}

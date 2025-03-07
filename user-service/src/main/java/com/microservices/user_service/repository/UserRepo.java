package com.microservices.user_service.repository;

//import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.microservices.user_service.entity.User;

public interface UserRepo extends JpaRepository<User, String> {
	
	User findByUsername(String username);

}

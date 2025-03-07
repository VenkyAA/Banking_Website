package com.microservices.profile_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.microservices.profile_service.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}

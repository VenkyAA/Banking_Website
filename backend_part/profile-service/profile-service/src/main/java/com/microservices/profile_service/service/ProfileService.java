package com.microservices.profile_service.service;

import com.microservices.profile_service.dto.ProfileDTO;
import java.util.List;

public interface ProfileService {
    ProfileDTO createProfile(ProfileDTO profileDTO);
    
    ProfileDTO getProfileById(long id);
    
    ProfileDTO updateProfile(ProfileDTO profileDTO);
    
    List<ProfileDTO> getAllProfiles();
    
    void deleteProfile(long id);
}

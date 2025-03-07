package com.microservices.profile_service.mapper;

import com.microservices.profile_service.dto.ProfileDTO;
import com.microservices.profile_service.entity.Profile;

// This class provides methods to map between Profile and ProfileDTO objects
public class ProfileMapper {

    // Map ProfileDTO to Profile entity
    public static Profile mapToProfile(ProfileDTO profileDTO) {
        Profile profile = new Profile();
        profile.setId(profileDTO.getId()); // Set profile ID
        profile.setGovtId(profileDTO.getGovtId()); // Set government ID
        profile.setEmployment(profileDTO.getEmployment()); // Set employment details
        profile.setAddress(profileDTO.getAddress()); // Set address
        profile.setPhoneNumber(profileDTO.getPhoneNumber()); // Set phone number
        profile.setEmailId(profileDTO.getEmailId()); // Set email ID
        return profile;
    }

    // Map Profile entity to ProfileDTO
    public static ProfileDTO mapToProfileDTO(Profile profile) {
        return new ProfileDTO(
            profile.getId(), // Set profile ID
            profile.getGovtId(), // Set government ID
            profile.getEmployment(), // Set employment details
            profile.getAddress(), // Set address
            profile.getPhoneNumber(), // Set phone number
            profile.getEmailId() // Set email ID
        );
    }
}

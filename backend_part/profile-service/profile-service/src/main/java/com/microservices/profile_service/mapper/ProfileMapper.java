package com.microservices.profile_service.mapper;

import com.microservices.profile_service.dto.ProfileDTO;
import com.microservices.profile_service.entity.Profile;

public class ProfileMapper {

    public static Profile mapToProfile(ProfileDTO profileDTO) {
        Profile profile = new Profile();
        profile.setId(profileDTO.getId());
        profile.setAccountHolder(profileDTO.getAccountHolder());
        profile.setGovtId(profileDTO.getGovtId());
        profile.setEmployment(profileDTO.getEmployment());
        profile.setAddress(profileDTO.getAddress());
        profile.setPhoneNumber(profileDTO.getPhoneNumber());
        profile.setEmailId(profileDTO.getEmailId());
        return profile;
    }

    public static ProfileDTO mapToProfileDTO(Profile profile) {
        return new ProfileDTO(profile.getId(), profile.getAccountHolder(), profile.getGovtId(), profile.getEmployment(), profile.getAddress(), profile.getPhoneNumber(), profile.getEmailId());
    }
}

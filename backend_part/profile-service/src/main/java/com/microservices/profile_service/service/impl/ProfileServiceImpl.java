package com.microservices.profile_service.service.impl;

import com.microservices.profile_service.dto.ProfileDTO;
import com.microservices.profile_service.entity.Profile;
import com.microservices.profile_service.exception.ProfileNotFoundException;
import com.microservices.profile_service.mapper.ProfileMapper;
import com.microservices.profile_service.repository.ProfileRepository;
import com.microservices.profile_service.service.ProfileService;
import com.microservices.profile_service.feign.AccountClient;
import com.microservices.profile_service.dto.AccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ProfileServiceImpl implements ProfileService {

    // Logger to log messages
    private static final Logger logger = Logger.getLogger(ProfileServiceImpl.class.getName());

    // Autowire the ProfileRepository to interact with the database
    @Autowired
    private ProfileRepository profileRepository;

    // Autowire the AccountClient to interact with the Account Service
    @Autowired
    private AccountClient accountClient;

    // Create a new profile
    @Override
    public ProfileDTO createProfile(ProfileDTO profileDTO) {
        try {
            // Fetch account details from the Account Service
            AccountDTO accountDTO = accountClient.getAccountById(profileDTO.getId());
            
            // Map ProfileDTO to Profile entity
            Profile profile = ProfileMapper.mapToProfile(profileDTO);
            
            // Save the profile to the database
            Profile savedProfile = profileRepository.save(profile);
            return ProfileMapper.mapToProfileDTO(savedProfile);
        } catch (Exception e) {
            logger.severe("Error creating profile: " + e.getMessage());
            throw new RuntimeException("Error creating profile", e);
        }
    }

    // Get profile by ID
    @Override
    public ProfileDTO getProfileById(long id) {
        // Get the profile details by profile ID
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException("Profile does not exist"));
        return ProfileMapper.mapToProfileDTO(profile);
    }

    // Update a profile
    @Override
    public ProfileDTO updateProfile(ProfileDTO profileDTO) {
        try {
            // Get the existing profile details by profile ID
            Profile existingProfile = profileRepository.findById(profileDTO.getId())
                    .orElseThrow(() -> new ProfileNotFoundException("Profile does not exist"));

            existingProfile.setGovtId(profileDTO.getGovtId());
            existingProfile.setEmployment(profileDTO.getEmployment());
            existingProfile.setAddress(profileDTO.getAddress());
            existingProfile.setPhoneNumber(profileDTO.getPhoneNumber());
            existingProfile.setEmailId(profileDTO.getEmailId());

            // Save the updated profile to the database
            Profile updatedProfile = profileRepository.save(existingProfile);
            return ProfileMapper.mapToProfileDTO(updatedProfile);
        } catch (ProfileNotFoundException e) {
            logger.severe("Profile not found: " + e.getMessage());
            throw e; // Rethrow the custom exception
        } catch (Exception e) {
            logger.severe("Error updating profile: " + e.getMessage());
            throw new RuntimeException("Error updating profile", e);
        }
    }

    // Get all profiles
    @Override
    public List<ProfileDTO> getAllProfiles() {
        try {
            // Get the list of all profiles from the database
            List<Profile> profiles = profileRepository.findAll();
            return profiles.stream()
                    .map(ProfileMapper::mapToProfileDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.severe("Error getting all profiles: " + e.getMessage());
            throw new RuntimeException("Error getting all profiles", e);
        }
    }

    // Delete a profile by ID
    @Override
    public void deleteProfile(long id) {
        try {
            // Get the profile details by profile ID
            Profile profile = profileRepository.findById(id)
                    .orElseThrow(() -> new ProfileNotFoundException("Profile does not exist"));
            // Delete the profile from the database
            profileRepository.deleteById(id);
        } catch (ProfileNotFoundException e) {
            logger.severe("Profile not found: " + e.getMessage());
            throw e; // Rethrow the custom exception
        } catch (Exception e) {
            logger.severe("Error deleting profile: " + e.getMessage());
            throw new RuntimeException("Error deleting profile", e);
        }
    }
    
    
}

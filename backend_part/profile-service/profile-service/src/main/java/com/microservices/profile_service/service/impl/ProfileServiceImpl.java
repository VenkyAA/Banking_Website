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

    private static final Logger logger = Logger.getLogger(ProfileServiceImpl.class.getName());

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private AccountClient accountClient;

    @Override
    public ProfileDTO createProfile(ProfileDTO profileDTO) {
        try {
            // Fetch account details if necessary, remove mapping to account
            AccountDTO accountDTO = accountClient.getAccountById(profileDTO.getId());
            Profile profile = ProfileMapper.mapToProfile(profileDTO);
            Profile savedProfile = profileRepository.save(profile);
            return ProfileMapper.mapToProfileDTO(savedProfile);
        } catch (Exception e) {
            logger.severe("Error creating profile: " + e.getMessage());
            throw new RuntimeException("Error creating profile", e);
        }
    }

    @Override
    public ProfileDTO getProfileById(long id) {
        try {
            Profile profile = profileRepository.findById(id)
                    .orElseThrow(() -> new ProfileNotFoundException("Profile does not exist"));
            return ProfileMapper.mapToProfileDTO(profile);
        } catch (Exception e) {
            logger.severe("Error getting profile by id: " + e.getMessage());
            throw new RuntimeException("Error getting profile by id", e);
        }
    }

    @Override
    public ProfileDTO updateProfile(ProfileDTO profileDTO) {
        try {
            Profile existingProfile = profileRepository.findById(profileDTO.getId())
                    .orElseThrow(() -> new ProfileNotFoundException("Profile does not exist"));

            existingProfile.setAccountHolder(profileDTO.getAccountHolder());
            existingProfile.setGovtId(profileDTO.getGovtId());
            existingProfile.setEmployment(profileDTO.getEmployment());
            existingProfile.setAddress(profileDTO.getAddress());
            existingProfile.setPhoneNumber(profileDTO.getPhoneNumber());
            existingProfile.setEmailId(profileDTO.getEmailId());

            Profile updatedProfile = profileRepository.save(existingProfile);
            return ProfileMapper.mapToProfileDTO(updatedProfile);
        } catch (Exception e) {
            logger.severe("Error updating profile: " + e.getMessage());
            throw new RuntimeException("Error updating profile", e);
        }
    }

    @Override
    public List<ProfileDTO> getAllProfiles() {
        try {
            List<Profile> profiles = profileRepository.findAll();
            return profiles.stream().map(ProfileMapper::mapToProfileDTO).collect(Collectors.toList());
        } catch (Exception e) {
            logger.severe("Error getting all profiles: " + e.getMessage());
            throw new RuntimeException("Error getting all profiles", e);
        }
    }

    @Override
    public void deleteProfile(long id) {
        try {
            Profile profile = profileRepository.findById(id)
                    .orElseThrow(() -> new ProfileNotFoundException("Profile does not exist"));
            profileRepository.deleteById(id);
        } catch (Exception e) {
            logger.severe("Error deleting profile: " + e.getMessage());
            throw new RuntimeException("Error deleting profile", e);
        }
    }
}

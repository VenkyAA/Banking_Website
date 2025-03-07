package com.microservices.profile_service;

import com.microservices.profile_service.dto.ProfileDTO;
import com.microservices.profile_service.entity.Profile;
import com.microservices.profile_service.exception.ProfileNotFoundException;
import com.microservices.profile_service.feign.AccountClient;
import com.microservices.profile_service.mapper.ProfileMapper;
import com.microservices.profile_service.repository.ProfileRepository;
import com.microservices.profile_service.service.impl.ProfileServiceImpl;
import com.microservices.profile_service.dto.AccountDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceApplicationTests {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private AccountClient accountClient;

    @InjectMocks
    private ProfileServiceImpl profileService;

    private ProfileDTO profileDTO;
    private Profile profile;
    private AccountDTO accountDTO;

    @BeforeEach
    void setUp() {
        profileDTO = new ProfileDTO(1L, "A1234567", "Software Engineer", "1234 Street", "1234567890", "venkat@example.com");
        profile = new Profile(1L, "A1234567", "Software Engineer", "1234 Street", "1234567890", "venkat@example.com");
        accountDTO = new AccountDTO(1L, "Venkat", 30000.0);
    }

    @Test
    void testCreateProfile() {
        when(accountClient.getAccountById(anyLong())).thenReturn(accountDTO);
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        ProfileDTO createdProfile = profileService.createProfile(profileDTO);

        assertNotNull(createdProfile);
        assertEquals(profileDTO.getId(), createdProfile.getId());
        verify(accountClient, times(1)).getAccountById(anyLong());
        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    void testGetProfileById() {
        when(profileRepository.findById(anyLong())).thenReturn(Optional.of(profile));

        ProfileDTO foundProfile = profileService.getProfileById(1L);

        assertNotNull(foundProfile);
        assertEquals(profileDTO.getId(), foundProfile.getId());
        verify(profileRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetProfileById_ThrowsException_WhenProfileNotFound() {
        when(profileRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProfileNotFoundException.class, () -> profileService.getProfileById(1L));
        verify(profileRepository, times(1)).findById(anyLong());
    }

    @Test
    void testUpdateProfile() {
        when(profileRepository.findById(anyLong())).thenReturn(Optional.of(profile));
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        ProfileDTO updatedProfile = profileService.updateProfile(profileDTO);

        assertNotNull(updatedProfile);
        assertEquals(profileDTO.getId(), updatedProfile.getId());
        verify(profileRepository, times(1)).findById(anyLong());
        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    void testUpdateProfile_ThrowsException_WhenProfileNotFound() {
        when(profileRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProfileNotFoundException.class, () -> profileService.updateProfile(profileDTO));
        verify(profileRepository, times(1)).findById(anyLong());
        verify(profileRepository, never()).save(any(Profile.class));
    }

    @Test
    void testGetAllProfiles() {
        when(profileRepository.findAll()).thenReturn(Arrays.asList(profile));

        List<ProfileDTO> profiles = profileService.getAllProfiles();

        assertNotNull(profiles);
        assertFalse(profiles.isEmpty());
        assertEquals(1, profiles.size());
        verify(profileRepository, times(1)).findAll();
    }

    @Test
    void testDeleteProfile() {
        when(profileRepository.findById(anyLong())).thenReturn(Optional.of(profile));
        doNothing().when(profileRepository).deleteById(anyLong());

        profileService.deleteProfile(1L);

        verify(profileRepository, times(1)).findById(anyLong());
        verify(profileRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testDeleteProfile_ThrowsException_WhenProfileNotFound() {
        when(profileRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProfileNotFoundException.class, () -> profileService.deleteProfile(1L));
        verify(profileRepository, times(1)).findById(anyLong());
        verify(profileRepository, never()).deleteById(anyLong());
    }
}


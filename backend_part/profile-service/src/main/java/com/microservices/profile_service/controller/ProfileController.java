package com.microservices.profile_service.controller;

import com.microservices.profile_service.dto.ProfileDTO;
import com.microservices.profile_service.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profiles")
//@CrossOrigin(origins = "http://localhost:3001")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping
    public ResponseEntity<ProfileDTO> addProfile(@RequestBody ProfileDTO profileDTO) {
        return new ResponseEntity<>(profileService.createProfile(profileDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileDTO> getProfileById(@PathVariable long id) {
        return ResponseEntity.ok(profileService.getProfileById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileDTO> updateProfile(@PathVariable long id, @RequestBody ProfileDTO profileDTO) {
        profileDTO.setId(id);
        ProfileDTO updatedProfile = profileService.updateProfile(profileDTO);
        return ResponseEntity.ok(updatedProfile);
    }

    @GetMapping
    public ResponseEntity<List<ProfileDTO>> getAllProfiles() {
        return ResponseEntity.ok(profileService.getAllProfiles());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProfile(@PathVariable long id) {
        profileService.deleteProfile(id);
        return ResponseEntity.ok("Profile of ID "+ id + "is deleted successfully!");
    }
}
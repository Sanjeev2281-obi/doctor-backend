package com.example.backend.controller;

import com.example.backend.entity.Profile;
import com.example.backend.repository.ProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "https://doctor-booking-app22.vercel.app") 
public class ProfileController {

    @Autowired
    private ProfileRepo profileRepo;

    // Fetch profile by email (you can also use id)
    @GetMapping("/{email}")
    public ResponseEntity<?> getProfile(@PathVariable String email) {
        Optional<Profile> profileOpt = profileRepo.findByEmail(email);
        if (profileOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Profile not found");
        }
        return ResponseEntity.ok(profileOpt.get());
    }

    // Update profile
    @PutMapping("/{email}")
    public ResponseEntity<?> updateProfile(
        @PathVariable String email,
        @RequestBody Profile updatedProfile) {

    Profile profile = profileRepo.findByEmail(email)
        .orElseGet(() -> {
            Profile p = new Profile();
            p.setEmail(email);   // create new profile
            return p;
        });

    profile.setName(updatedProfile.getName());
    profile.setPhone(updatedProfile.getPhone());
    profile.setGender(updatedProfile.getGender());
    profile.setDob(updatedProfile.getDob());
    profile.setImage(updatedProfile.getImage());
    profile.setAddress(updatedProfile.getAddress());

    profileRepo.save(profile);
    return ResponseEntity.ok(profile);
}

}

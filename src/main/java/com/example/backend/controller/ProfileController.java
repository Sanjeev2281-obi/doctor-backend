package com.example.backend.controller;

import com.example.backend.entity.Profile;
import com.example.backend.repository.ProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
 
public class ProfileController {

    @Autowired
    private ProfileRepo profileRepo;

    @GetMapping("/{email}")
public ResponseEntity<?> getProfile(@PathVariable String email) {

    Profile profile = profileRepo.findByEmail(email)
        .orElseGet(() -> {
            Profile p = new Profile();
            p.setEmail(email);
            p.setName("");     
            p.setPhone("");
            p.setGender("Male");
            profileRepo.save(p);   
            return p;
        }); 

    return ResponseEntity.ok(profile);
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

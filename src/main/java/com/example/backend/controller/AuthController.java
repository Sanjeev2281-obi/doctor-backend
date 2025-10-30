package com.example.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "https://doctor-booking-app-ra1k.vercel.app") // Frontend URL
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        try {
            List<User> existingUsers = userRepository.findByEmail(user.getEmail());
            if (!existingUsers.isEmpty()) {
                return ResponseEntity.status(400).body("Email already exists");
            }

            User saved = userRepository.save(user);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Something went wrong: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        List<User> existingUsers = userRepository.findByEmail(user.getEmail());
        if (existingUsers.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        User existing = existingUsers.get(0);
        if (!existing.getPassword().equals(user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        return ResponseEntity.ok(existing);
    }

}

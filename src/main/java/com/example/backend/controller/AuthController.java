package com.example.backend.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.backend.entity.User;
import com.example.backend.entity.Doctor;
import com.example.backend.entity.Role;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.DoctorRepository;
import com.example.backend.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        try {
            List<User> existingUsers = userRepository.findByEmail(user.getEmail());
            if (!existingUsers.isEmpty()) {
                return ResponseEntity.status(400).body("Email already exists");
            }
            
            // Assign default role of PATIENT on signup
            user.setRole(Role.PATIENT);
            User saved = userRepository.save(user);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Something went wrong: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User credentials) {
        try {
            // 1. Try finding in Users (Patients)
            List<User> existingUsers = userRepository.findByEmail(credentials.getEmail());
            if (!existingUsers.isEmpty()) {
                User existing = existingUsers.get(0);
                if (existing.getPassword() == null || !existing.getPassword().equals(credentials.getPassword())) {
                    return ResponseEntity.status(401).body("Invalid credentials");
                }

                String token = jwtUtil.generateToken(existing.getEmail());
                Role role = existing.getRole() != null ? existing.getRole() : Role.PATIENT;

                UserResponse userResponse = new UserResponse(
                    existing.getId(),
                    existing.getName(),
                    existing.getEmail(),
                    role.name()
                );
                return ResponseEntity.ok(new LoginResponse(token, userResponse));
            }

            // 2. Try finding in Doctors
            Optional<Doctor> existingDoctorOpt = doctorRepository.findByEmail(credentials.getEmail());
            if (existingDoctorOpt.isPresent()) {
                Doctor existingDoctor = existingDoctorOpt.get();
                if (existingDoctor.getPassword() == null || !existingDoctor.getPassword().equals(credentials.getPassword())) {
                    return ResponseEntity.status(401).body("Invalid credentials");
                }

                String token = jwtUtil.generateToken(existingDoctor.getEmail());
                Role role = existingDoctor.getRole() != null ? existingDoctor.getRole() : Role.DOCTOR;

                UserResponse userResponse = new UserResponse(
                    existingDoctor.getId(),
                    existingDoctor.getName(),
                    existingDoctor.getEmail(),
                    role.name()
                );
                return ResponseEntity.ok(new LoginResponse(token, userResponse));
            }

            return ResponseEntity.status(401).body("Invalid credentials");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Login error: " + e.getMessage());
        }
    }

    // Inner classes for returning clean JWT and user payloads
    public static class LoginResponse {
        private String token;
        private UserResponse user;

        public LoginResponse(String token, UserResponse user) {
            this.token = token;
            this.user = user;
        }

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }

        public UserResponse getUser() { return user; }
        public void setUser(UserResponse user) { this.user = user; }
    }

    public static class UserResponse {
        private Long id;
        private String name;
        private String email;
        private String role;

        public UserResponse(Long id, String name, String email, String role) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.role = role;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
}
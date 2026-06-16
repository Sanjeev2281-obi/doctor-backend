package com.example.backend.security;

import com.example.backend.entity.User;
import com.example.backend.entity.Doctor;
import com.example.backend.entity.Role;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        List<User> users = userRepository.findByEmail(email);
        if (!users.isEmpty()) {
            User user = users.get(0);
            Role role = user.getRole() != null ? user.getRole() : Role.PATIENT;
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .authorities("ROLE_" + role.name())
                    .build();
        }

        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            Role role = doctor.getRole() != null ? doctor.getRole() : Role.DOCTOR;
            return org.springframework.security.core.userdetails.User.builder()
                    .username(doctor.getEmail())
                    .password(doctor.getPassword())
                    .authorities("ROLE_" + role.name())
                    .build();
        }

        throw new UsernameNotFoundException("User or Doctor not found with email: " + email);
    }
}

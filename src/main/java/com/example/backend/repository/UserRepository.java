package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
   List<User> findByEmail(String email);


}

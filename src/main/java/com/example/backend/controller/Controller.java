package com.example.backend.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.repository.ProfileRepo;

@RestController
@RequestMapping("/api/profile")

public class Controller {

    @Autowired
    private ProfileRepo profileRepo;
}

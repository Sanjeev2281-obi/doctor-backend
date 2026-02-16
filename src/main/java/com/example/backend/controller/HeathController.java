package com.example.backend.controller;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeathController {
    
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}

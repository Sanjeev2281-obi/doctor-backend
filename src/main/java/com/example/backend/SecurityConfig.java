package com.example.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {})   // enable CORS
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/**",
                    "/health"
                ).permitAll()
                .anyRequest().permitAll()
            )
            .formLogin(form -> form.disable())   // ❌ disable login page
            .httpBasic(basic -> basic.disable()); // ❌ disable basic auth

        return http.build();
    }
}

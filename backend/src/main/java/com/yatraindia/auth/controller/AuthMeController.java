package com.yatraindia.auth.controller;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthMeController {

    @GetMapping("/me")
    public Map<String, Object> getCurrentUser(
            Authentication authentication) {

        return Map.of(
                "message", "Authenticated user",
                "email", authentication.getName(),
                "status", "AUTHENTICATED"
        );
    }
}
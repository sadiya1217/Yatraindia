package com.yatraindia.auth.controller;

import java.util.Map;



import jakarta.validation.Valid;
import com.yatraindia.auth.security.JwtService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yatraindia.user.dto.LoginRequest;

import com.yatraindia.user.dto.RegisterRequest;
import com.yatraindia.user.entity.User;
import com.yatraindia.user.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UserService userService;
	private final JwtService jwtService;

	public AuthController(UserService userService,
            JwtService jwtService) {
this.userService = userService;
this.jwtService = jwtService;
}
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @Valid @RequestBody RegisterRequest request) {

        User user = userService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                        "message", "User registered successfully",
                        "userId", user.getId(),
                        "email", user.getEmail(),
                        "role", user.getRole()
                ));
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @Valid @RequestBody LoginRequest request) {

        User user = userService.login(request);

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Login successful",
                        "token", token,
                        "userId", user.getId(),
                        "email", user.getEmail(),
                        "role", user.getRole()
                )
        );
    }
}
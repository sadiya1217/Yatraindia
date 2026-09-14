package com.yatraindia.user.controller;

import java.util.HashMap;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yatraindia.user.dto.UpdateProfileRequest;
import com.yatraindia.user.entity.User;
import com.yatraindia.user.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getMyProfile(
            Authentication authentication) {

        User user = userService.getUserByEmail(
                authentication.getName());

        Map<String, Object> response = new HashMap<>();

        response.put("id", user.getId());
        response.put("fullName", user.getFullName());
        response.put("email", user.getEmail());
        response.put("phone",
                user.getPhone() == null ? "" : user.getPhone());
        response.put("role", user.getRole());
        response.put("status", user.getStatus());
        response.put("createdAt", user.getCreatedAt());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<Map<String, Object>> updateMyProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request) {

        User user = userService.updateProfile(
                authentication.getName(),
                request);

        Map<String, Object> response = new HashMap<>();

        response.put("message", "Profile updated successfully");
        response.put("id", user.getId());
        response.put("fullName", user.getFullName());
        response.put("email", user.getEmail());
        response.put("phone",
                user.getPhone() == null ? "" : user.getPhone());

        return ResponseEntity.ok(response);
    }
}
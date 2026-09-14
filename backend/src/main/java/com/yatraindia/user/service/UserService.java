package com.yatraindia.user.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.yatraindia.user.dto.LoginRequest;
import com.yatraindia.user.dto.RegisterRequest;
import com.yatraindia.user.dto.UpdateProfileRequest;
import com.yatraindia.user.entity.User;
import com.yatraindia.user.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        if (request.getPhone() != null
                && !request.getPhone().isBlank()
                && userRepository.existsByPhone(request.getPhone())) {

            throw new RuntimeException(
                    "Phone number already registered");
        }

        User user = new User();

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword()));

        user.setRole("CUSTOMER");
        user.setStatus("ACTIVE");

        LocalDateTime now = LocalDateTime.now();

        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        return userRepository.save(user);
    }


    public User login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid email or password"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash())) {

            throw new RuntimeException(
                    "Invalid email or password");
        }

        if (!"ACTIVE".equals(user.getStatus())) {

            throw new RuntimeException(
                    "User account is not active");
        }

        return user;
    }


    public User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"));
    }


    public User updateProfile(
            String email,
            UpdateProfileRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"));

        if (request.getFullName() != null
                && !request.getFullName().isBlank()) {

            user.setFullName(
                    request.getFullName().trim());
        }

        if (request.getPhone() != null) {

            String phone = request.getPhone().trim();

            if (!phone.equals(user.getPhone())
                    && !phone.isBlank()
                    && userRepository.existsByPhone(phone)) {

                throw new RuntimeException(
                        "Phone number already registered");
            }

            user.setPhone(
                    phone.isBlank() ? null : phone);
        }

        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }
}
package com.yatraindia.auth.security;

import java.util.Map;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedUserService {

    private Authentication getAuthenticatedAuthentication() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException(
                    "User is not authenticated");
        }

        return authentication;
    }

    public Long getCurrentUserId() {
        Authentication authentication =
                getAuthenticatedAuthentication();

        Object details = authentication.getDetails();

        if (!(details instanceof Map<?, ?> detailsMap)) {
            throw new IllegalStateException(
                    "Authenticated user ID is missing");
        }

        Object userId = detailsMap.get("userId");

        if (!(userId instanceof Long authenticatedUserId)
                || authenticatedUserId <= 0) {
            throw new IllegalStateException(
                    "Authenticated user ID is invalid");
        }

        return authenticatedUserId;
    }

    public boolean isCurrentUserAdmin() {
        Authentication authentication =
                getAuthenticatedAuthentication();

        return authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        "ROLE_ADMIN".equals(authority.getAuthority()));
    }
}
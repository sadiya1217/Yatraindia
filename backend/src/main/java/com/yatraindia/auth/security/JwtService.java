package com.yatraindia.auth.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.yatraindia.user.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final Key signingKey;
    private final long expirationTime;

    public JwtService(
            @Value("${yatraindia.jwt.secret}") String secret,
            @Value("${yatraindia.jwt.expiration}") long expirationTime) {

        this.signingKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8));

        this.expirationTime = expirationTime;
    }

    public String generateToken(User user) {

        Date now = new Date();
        Date expiration = new Date(
                now.getTime() + expirationTime);

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("role", user.getRole())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(signingKey)
                .compact();
    }

    public String extractEmail(String token) {

    	return getClaimsForAuthentication(token).getSubject();
    }

    public boolean isTokenValid(String token) {

        try {
        	getClaimsForAuthentication(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public Claims getClaimsForAuthentication(String token) { {

        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
}
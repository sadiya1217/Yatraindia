
package com.yatraindia.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.yatraindia.auth.security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .formLogin(form -> form.disable())
            .httpBasic(httpBasic -> httpBasic.disable())
            .logout(logout -> logout.disable())
       .authorizeHttpRequests(auth -> auth

    // Allow browser CORS preflight requests
    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

    // Public authentication and health endpoints
    .requestMatchers(
        "/api/health",
        "/api/auth/register",
        "/api/auth/login",
        "/error"
    ).permitAll()

    // Public destination and service catalog
    .requestMatchers(
        HttpMethod.GET,
        "/api/destinations/**",
        "/api/hotels/**",
        "/api/cabs/**",
        "/api/places/**"
    ).permitAll()

    // Customers can access only their own bookings here
    .requestMatchers(
        HttpMethod.GET,
        "/api/bookings/me"
    ).authenticated()

    // Other booking reads are administrator-only
    .requestMatchers(
        HttpMethod.GET,
        "/api/bookings/**"
    ).hasRole("ADMIN")

    // Booking creation requires authentication
    .requestMatchers(
        HttpMethod.POST,
        "/api/bookings"
    ).authenticated()

    // Booking status changes require an administrator
    .requestMatchers(
        HttpMethod.PATCH,
        "/api/bookings/**"
    ).hasRole("ADMIN")

    // Travel history requires authentication
    .requestMatchers(
        HttpMethod.GET,
        "/api/history/**"
    ).authenticated()

    // Require authentication for all other requests
    .anyRequest().authenticated()
)
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );


        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
            List.of("http://localhost:5173")
        );

        configuration.setAllowedMethods(
            List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        );

        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}

package com.example.Gpseva.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(CorsConfigurationSource corsConfigurationSource) {
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // Disable CSRF for REST APIs
            .csrf(csrf -> csrf.disable())

            // Stateless session (good for JWT / APIs)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Enable CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource))

            // Authorization Rules
            .authorizeHttpRequests(auth -> auth

                // ✅ OTP Authentication APIs
                .requestMatchers("/api/auth/**").permitAll()

                // ✅ Register & Login APIs
                .requestMatchers("/api/register/**").permitAll()
                .requestMatchers("/api/login/**").permitAll()

                // ✅ Payment APIs
                .requestMatchers("/api/payment/**").permitAll()

                // ✅ Document APIs
                .requestMatchers("/api/documents/**").permitAll()

                // ✅ Admin APIs
                .requestMatchers("/api/admin/**").permitAll()

                // ✅ Allow OPTIONS (Preflight)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // 🔐 All other endpoints require authentication
                .anyRequest().authenticated()
            );

        return http.build();
    }
}

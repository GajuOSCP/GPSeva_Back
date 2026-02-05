package com.example.Gpseva.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.security.config.http.SessionCreationPolicy;

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
            // 🔴 Disable CSRF for APIs
            .csrf(csrf -> csrf.disable())

            // 🔴 Stateless REST API
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 🔴 Enable CORS with your config
            .cors(cors -> cors.configurationSource(corsConfigurationSource))

            // 🔴 AUTH RULES
            .authorizeHttpRequests(auth -> auth

                // ✅ AUTH APIs
                .requestMatchers(
                    "/api/register",
                    "/api/register/**",
                    "/api/login",
                    "/api/login/**"
                ).permitAll()

                // ✅ PAYMENT APIs (THIS WAS MISSING)
                .requestMatchers(
                    "/api/payment/create-order",
                    "/api/payment/verify",
                    "/api/payment/**"
                ).permitAll()

                // ✅ Allow preflight
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // 🔐 Everything else secured
                .anyRequest().authenticated()
            );

        return http.build();
    }
}

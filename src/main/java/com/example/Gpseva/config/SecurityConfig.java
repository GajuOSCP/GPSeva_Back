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
            // 🔴 Disable CSRF for REST APIs
            .csrf(csrf -> csrf.disable())

            // 🔴 Stateless API (JWT / REST friendly)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 🔴 Enable CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource))

         // 🔴 Authorization rules
            .authorizeHttpRequests(auth -> auth

                // ✅ AUTH APIs
                .requestMatchers(
                    "/api/register",
                    "/api/register/**",
                    "/api/login",
                    "/api/login/**"
                ).permitAll()

                // ✅ PAYMENT APIs
                .requestMatchers(
                    "/api/payment/create-order",
                    "/api/payment/verify",
                    "/api/payment/**"
                ).permitAll()

                // ✅ DOCUMENT APIs
                .requestMatchers(
                    "/api/documents/upload",
                    "/api/documents/upload/**",
                    "/api/documents/download/**"
                ).permitAll()

                // ✅ 🔥 ADMIN DASHBOARD APIs (THIS WAS MISSING)
                .requestMatchers(
                    "/api/admin/**",
                    "/api/admin/dashboard/**"
                ).permitAll()

                // ✅ Allow preflight
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // 🔐 Everything else
                .anyRequest().authenticated()
            )
;

        return http.build();
    }
}

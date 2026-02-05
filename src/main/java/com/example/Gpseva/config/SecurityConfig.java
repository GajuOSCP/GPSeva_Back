package com.example.Gpseva.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // ✅ THIS IS THE REAL FIX
            .cors(cors -> cors.configurationSource(corsConfigurationSource))

            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/register",
                    "/api/register/**",
                    "/api/login",
                    "/api/login/**"
                ).permitAll()

                // Allow preflight requests
                .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()

                .anyRequest().authenticated()
            );

        return http.build();
    }
}

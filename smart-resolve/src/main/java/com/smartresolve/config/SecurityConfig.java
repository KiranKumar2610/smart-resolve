package com.smartresolve.config;

import com.smartresolve.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth

                        /* ======================
                           PUBLIC (STATIC + AUTH)
                        ======================= */
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/login.html",
                                "/register.html",
                                "/complaints.html",
                                "/createcomplaint.html",
                                "admin.html",
                                "/css/**",
                                "/js/**",
                                "/favicon.ico"
                        ).permitAll()

                        .requestMatchers("/api/auth/**").permitAll()

                        /* ======================
                           USER APIs
                        ======================= */
                        .requestMatchers(HttpMethod.POST, "/api/complaints").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/complaints/my").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/complaints/**").hasRole("USER")

                        /* ======================
                           ADMIN APIs
                        ======================= */
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        /* ======================
                           EVERYTHING ELSE
                        ======================= */
                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }
}

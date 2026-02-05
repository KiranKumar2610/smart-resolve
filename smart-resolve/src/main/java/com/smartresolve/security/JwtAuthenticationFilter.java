package com.smartresolve.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections; // Using Collections.singletonList for single authority
import java.util.List;
import org.slf4j.Logger; // Import SLF4J Logger
import org.slf4j.LoggerFactory; // Import SLF4J LoggerFactory

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class); // Initialize logger

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Log the incoming request path
        logger.debug("Processing request for path: {}", request.getRequestURI());

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                if (jwtUtil.validateToken(token)) {
                    String email = jwtUtil.extractEmail(token);
                    String role = jwtUtil.extractRole(token); // e.g., "USER", "ADMIN", or "user", "admin"

                    // --- CRITICAL CHANGE & LOGGING ---
                    if (role != null && !role.trim().isEmpty()) {
                        String springSecurityRole = "ROLE_" + role.toUpperCase(); // Ensure uppercase for Spring Security

                        logger.debug("JWT Token Validated. Email: {}", email);
                        logger.debug("Extracted Role from JWT (raw): {}", role);
                        logger.debug("Spring Security Authority created: {}", springSecurityRole);

                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        email,
                                        null,
                                        Collections.singletonList(new SimpleGrantedAuthority(springSecurityRole)) // Use Collections.singletonList for efficiency
                                );

                        authentication.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        logger.debug("SecurityContextHolder updated with authentication for email: {}", email);
                    } else {
                        logger.warn("JWT Token provided, but no role or empty role extracted for token: {}", token);
                    }
                } else {
                    logger.warn("JWT Token validation failed for token: {}", token);
                }
            } catch (Exception e) {
                logger.error("Error during JWT authentication process for token: {}. Error: {}", token, e.getMessage(), e);
                // Optionally clear context if an error occurs, to prevent partial authentication
                SecurityContextHolder.clearContext();
            }
        } else {
            logger.debug("No Bearer token found in Authorization header or header is missing for request path: {}", request.getRequestURI());
        }

        filterChain.doFilter(request, response);
    }
}

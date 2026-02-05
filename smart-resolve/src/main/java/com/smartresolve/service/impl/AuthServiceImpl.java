package com.smartresolve.service.impl;

import com.smartresolve.dto.auth.LoginRequest;
import com.smartresolve.dto.auth.LoginResponse;
import com.smartresolve.dto.auth.RegisterRequest;
import com.smartresolve.entity.User;
import com.smartresolve.repository.UserRepository;
import com.smartresolve.security.JwtUtil;
import com.smartresolve.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // ✅ FIXED
        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole()
        );

        return new LoginResponse(token);
    }

    @Override
    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        user.setFullName(request.getFullName()); // ✅ FIX

        userRepository.save(user);
    }

}

package com.smartresolve.service;

import com.smartresolve.dto.auth.LoginRequest;
import com.smartresolve.dto.auth.LoginResponse;
import com.smartresolve.dto.auth.RegisterRequest;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    void register(RegisterRequest request);
}

package com.rmit.auth_service.auth.service;

import com.rmit.auth_service.auth.dto.AuthResponse;
import com.rmit.auth_service.auth.dto.LoginRequest;
import com.rmit.auth_service.auth.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}


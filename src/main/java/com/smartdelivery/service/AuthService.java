package com.smartdelivery.service;

import com.smartdelivery.dto.request.CreateUserRequest;
import com.smartdelivery.dto.request.LoginRequest;
import com.smartdelivery.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(CreateUserRequest request);
    AuthResponse login(LoginRequest request);
}
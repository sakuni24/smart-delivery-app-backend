package com.smartdelivery.service.impl;

import com.smartdelivery.dto.UserDto;
import com.smartdelivery.dto.request.CreateUserRequest;
import com.smartdelivery.dto.request.LoginRequest;
import com.smartdelivery.dto.response.AuthResponse;
import com.smartdelivery.security.JwtTokenProvider;
import com.smartdelivery.service.AuthService;
import com.smartdelivery.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public AuthServiceImpl(UserService userService, AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public AuthResponse register(CreateUserRequest request) {
        System.out.println("Registering new user with email: " + request.getEmail());

        UserDto userDto = userService.createUser(request);
        String token = tokenProvider.generateToken(userDto.getEmail());

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        authResponse.setUser(userDto);
        return authResponse;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        System.out.println("Authenticating user with email: " + request.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String token = tokenProvider.generateToken(authentication.getName());
        UserDto userDto = userService.getUserByEmail(request.getEmail());

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        authResponse.setUser(userDto);
        return authResponse;
    }
}

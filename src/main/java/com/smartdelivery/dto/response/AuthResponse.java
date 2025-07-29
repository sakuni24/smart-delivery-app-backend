package com.smartdelivery.dto.response;

import com.smartdelivery.dto.UserDto;

public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private UserDto user;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public UserDto getUser() { return user; }
    public void setUser(UserDto user) { this.user = user; }

}
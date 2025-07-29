package com.smartdelivery.service;

import com.smartdelivery.dto.UserDto;
import com.smartdelivery.dto.request.CreateUserRequest;
import com.smartdelivery.enums.UserRole;
import com.smartdelivery.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserDto createUser(CreateUserRequest request);

    UserDto getUserById(Long id);

    UserDto getUserByEmail(String email);

    UserDto updateUser(Long id, CreateUserRequest request);

    void deleteUser(Long id);

    List<UserDto> getUsersByRole(UserRole role);

    Page<UserDto> getUsersByRoleAndStatus(UserRole role, UserStatus status, Pageable pageable);

    Page<UserDto> searchUsers(UserRole role, UserStatus status, String searchTerm, Pageable pageable);

    UserDto updateUserStatus(Long id, UserStatus status);

    List<UserDto> getAvailableDrivers();
}
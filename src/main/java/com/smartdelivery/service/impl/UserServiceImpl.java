package com.smartdelivery.service.impl;

import com.smartdelivery.dto.UserDto;
import com.smartdelivery.dto.request.CreateUserRequest;
import com.smartdelivery.entity.User;
import com.smartdelivery.enums.UserRole;
import com.smartdelivery.enums.UserStatus;
import com.smartdelivery.exception.ResourceNotFoundException;
import com.smartdelivery.exception.DuplicateResourceException;
import com.smartdelivery.mapper.UserMapper;
import com.smartdelivery.repository.UserRepository;
import com.smartdelivery.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto createUser(CreateUserRequest request) {
        log.info("Creating user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User with email " + request.getEmail() + " already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(request.getRole());
        user.setStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());

        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        log.info("Getting user by ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByEmail(String email) {
        log.info("Getting user by email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return userMapper.toDto(user);
    }

    @Override
    public UserDto updateUser(Long id, CreateUserRequest request) {
        log.info("Updating user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User with email " + request.getEmail() + " already exists");
        }

        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(request.getRole());

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with ID: {}", updatedUser.getId());

        return userMapper.toDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        user.setStatus(UserStatus.DELETED);
        userRepository.save(user);
        log.info("User marked as deleted with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsersByRole(UserRole role) {
        log.info("Getting users by role: {}", role);
        List<User> users = userRepository.findByRole(role);
        return userMapper.toDtoList(users);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> getUsersByRoleAndStatus(UserRole role, UserStatus status, Pageable pageable) {
        log.info("Getting users by role: {} and status: {}", role, status);
        Page<User> users = userRepository.findByRoleAndStatus(role, status, pageable);
        return users.map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> searchUsers(UserRole role, UserStatus status, String searchTerm, Pageable pageable) {
        log.info("Searching users with term: {} for role: {} and status: {}", searchTerm, role, status);
        Page<User> users = userRepository.findByRoleAndStatusAndSearchTerm(role, status, searchTerm, pageable);
        return users.map(userMapper::toDto);
    }

    @Override
    public UserDto updateUserStatus(Long id, UserStatus status) {
        log.info("Updating status for user ID: {} to: {}", id, status);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        user.setStatus(status);
        User updatedUser = userRepository.save(user);
        log.info("User status updated successfully for ID: {}", id);

        return userMapper.toDto(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAvailableDrivers() {
        log.info("Getting available drivers");
        List<User> drivers = userRepository.findByRoleAndStatus(UserRole.DRIVER, UserStatus.ACTIVE, Pageable.unpaged()).getContent();
        return userMapper.toDtoList(drivers);
    }
}

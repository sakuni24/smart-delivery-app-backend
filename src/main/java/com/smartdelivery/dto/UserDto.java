package com.smartdelivery.dto;

import com.smartdelivery.enums.UserRole;
import com.smartdelivery.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private UserRole role;
    private UserStatus status;
    private AddressDto address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
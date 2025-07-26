package com.smartdelivery.dto;

import com.smartdelivery.enums.RestaurantStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDto {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private String phoneNumber;
    private String email;
    private RestaurantStatus status;
    private BigDecimal deliveryFee;
    private BigDecimal minimumOrder;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private BigDecimal averageRating;
    private Integer totalReviews;
    private AddressDto address;
    private List<MenuItemDto> menuItems;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
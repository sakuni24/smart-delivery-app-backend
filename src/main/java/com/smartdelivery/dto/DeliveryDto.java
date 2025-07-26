package com.smartdelivery.dto;

import com.smartdelivery.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDto {
    private Long id;
    private Long orderId;
    private Long driverId;
    private String driverName;
    private DeliveryStatus status;
    private LocalDateTime pickupTime;
    private LocalDateTime deliveryTime;
    private LocalDateTime estimatedDeliveryTime;
    private String deliveryNotes;
    private BigDecimal driverRating;
    private String customerFeedback;
    private List<DeliveryLocationDto> deliveryLocations;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
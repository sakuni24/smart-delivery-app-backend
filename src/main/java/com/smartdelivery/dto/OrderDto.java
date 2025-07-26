package com.smartdelivery.dto;

import com.smartdelivery.enums.OrderStatus;
import com.smartdelivery.enums.PaymentMethod;
import com.smartdelivery.enums.PaymentStatus;
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
public class OrderDto {
    private Long id;
    private String orderNumber;
    private Long customerId;
    private Long restaurantId;
    private OrderStatus status;
    private BigDecimal subtotal;
    private BigDecimal deliveryFee;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String specialInstructions;
    private LocalDateTime estimatedDeliveryTime;
    private List<OrderItemDto> orderItems;
    private DeliveryDto delivery;
    private AddressDto deliveryAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
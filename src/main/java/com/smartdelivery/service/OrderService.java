package com.smartdelivery.service;

import com.smartdelivery.dto.OrderDto;
import com.smartdelivery.dto.request.CreateOrderRequest;
import com.smartdelivery.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    OrderDto createOrder(CreateOrderRequest request, Long customerId);

    OrderDto getOrderById(Long id);

    OrderDto getOrderByOrderNumber(String orderNumber);

    List<OrderDto> getOrdersByCustomer(Long customerId);

    Page<OrderDto> getOrdersByCustomer(Long customerId, Pageable pageable);

    List<OrderDto> getOrdersByRestaurant(Long restaurantId);

    Page<OrderDto> getOrdersByRestaurant(Long restaurantId, Pageable pageable);

    OrderDto updateOrderStatus(Long id, OrderStatus status);

    List<OrderDto> getOrdersByStatus(OrderStatus status);

    List<OrderDto> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    void cancelOrder(Long id);
}
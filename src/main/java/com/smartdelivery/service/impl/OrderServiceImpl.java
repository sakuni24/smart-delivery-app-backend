package com.smartdelivery.service.impl;

import com.smartdelivery.dto.OrderDto;
import com.smartdelivery.dto.request.CreateOrderRequest;
import com.smartdelivery.entity.*;
import com.smartdelivery.enums.OrderStatus;
import com.smartdelivery.enums.PaymentStatus;
import com.smartdelivery.exception.ResourceNotFoundException;
import com.smartdelivery.exception.InvalidOperationException;
import com.smartdelivery.mapper.OrderMapper;
import com.smartdelivery.repository.*;
import com.smartdelivery.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final AddressRepository addressRepository;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository, AddressRepository addressRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
        this.addressRepository = addressRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public OrderDto createOrder(CreateOrderRequest request, Long customerId) {
        logger.info("Creating order for customer ID: {} at restaurant ID: {}", customerId, request.getRestaurantId());

        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with ID: " + request.getRestaurantId()));

        Address deliveryAddress = new Address();
        deliveryAddress.setStreetAddress(request.getDeliveryAddress().getStreetAddress());
        deliveryAddress.setApartmentNumber(request.getDeliveryAddress().getApartmentNumber());
        deliveryAddress.setCity(request.getDeliveryAddress().getCity());
        deliveryAddress.setState(request.getDeliveryAddress().getState());
        deliveryAddress.setPostalCode(request.getDeliveryAddress().getPostalCode());
        deliveryAddress.setCountry(request.getDeliveryAddress().getCountry());
        deliveryAddress.setLatitude(request.getDeliveryAddress().getLatitude());
        deliveryAddress.setLongitude(request.getDeliveryAddress().getLongitude());

        Address savedAddress = addressRepository.save(deliveryAddress);

        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod(request.getPaymentMethod());
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setSpecialInstructions(request.getSpecialInstructions());
        order.setDeliveryAddress(savedAddress);
        order.setEstimatedDeliveryTime(LocalDateTime.now().plusMinutes(45));

        List<OrderItem> orderItems = request.getOrderItems().stream().map(itemRequest -> {
            MenuItem menuItem = menuItemRepository.findById(itemRequest.getMenuItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with ID: " + itemRequest.getMenuItemId()));

            BigDecimal totalPrice = menuItem.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(menuItem.getPrice());
            orderItem.setTotalPrice(totalPrice);
            orderItem.setSpecialInstructions(itemRequest.getSpecialInstructions());

            return orderItem;
        }).toList();

        BigDecimal subtotal = orderItems.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal deliveryFee = restaurant.getDeliveryFee() != null ? restaurant.getDeliveryFee() : BigDecimal.ZERO;
        BigDecimal taxAmount = subtotal.multiply(BigDecimal.valueOf(0.08));
        BigDecimal totalAmount = subtotal.add(deliveryFee).add(taxAmount);

        order.setSubtotal(subtotal);
        order.setDeliveryFee(deliveryFee);
        order.setTaxAmount(taxAmount);
        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);

        if (restaurant.getMinimumOrder() != null && subtotal.compareTo(restaurant.getMinimumOrder()) < 0) {
            throw new InvalidOperationException("Order amount does not meet minimum order requirement of $" + restaurant.getMinimumOrder());
        }

        Order savedOrder = orderRepository.save(order);
        logger.info("Order created successfully with ID: {} and order number: {}", savedOrder.getId(), savedOrder.getOrderNumber());

        return orderMapper.toDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long id) {
        logger.info("Getting order by ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderByOrderNumber(String orderNumber) {
        logger.info("Getting order by order number: {}", orderNumber);
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with order number: " + orderNumber));
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByCustomer(Long customerId) {
        logger.info("Getting orders by customer ID: {}", customerId);
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        return orderMapper.toDtoList(orders);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> getOrdersByCustomer(Long customerId, Pageable pageable) {
        logger.info("Getting orders by customer ID: {} with pagination", customerId);
        Page<Order> orders = orderRepository.findByCustomerId(customerId, pageable);
        return orders.map(orderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByRestaurant(Long restaurantId) {
        logger.info("Getting orders by restaurant ID: {}", restaurantId);
        List<Order> orders = orderRepository.findByRestaurantId(restaurantId);
        return orderMapper.toDtoList(orders);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> getOrdersByRestaurant(Long restaurantId, Pageable pageable) {
        logger.info("Getting orders by restaurant ID: {} with pagination", restaurantId);
        Page<Order> orders = orderRepository.findByRestaurantId(restaurantId, pageable);
        return orders.map(orderMapper::toDto);
    }

    @Override
    public OrderDto updateOrderStatus(Long id, OrderStatus status) {
        logger.info("Updating order status for ID: {} to: {}", id, status);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));

        validateStatusTransition(order.getStatus(), status);
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        logger.info("Order status updated successfully for ID: {}", id);
        return orderMapper.toDto(updatedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByStatus(OrderStatus status) {
        logger.info("Getting orders by status: {}", status);
        List<Order> orders = orderRepository.findByStatus(status);
        return orderMapper.toDtoList(orders);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Getting orders between {} and {}", startDate, endDate);
        List<Order> orders = orderRepository.findOrdersByDateRange(startDate, endDate);
        return orderMapper.toDtoList(orders);
    }

    @Override
    public void cancelOrder(Long id) {
        logger.info("Cancelling order with ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));

        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new InvalidOperationException("Cannot cancel a delivered order");
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new InvalidOperationException("Order is already cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        logger.info("Order cancelled successfully with ID: {}", id);
    }

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        if (currentStatus == OrderStatus.CANCELLED && newStatus != OrderStatus.CANCELLED) {
            throw new InvalidOperationException("Cannot change status of a cancelled order");
        }
        if (currentStatus == OrderStatus.DELIVERED && newStatus != OrderStatus.DELIVERED) {
            throw new InvalidOperationException("Cannot change status of a delivered order");
        }
    }
}

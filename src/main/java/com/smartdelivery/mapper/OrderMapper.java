package com.smartdelivery.mapper;

import com.smartdelivery.dto.OrderDto;
import com.smartdelivery.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "restaurant.id", target = "restaurantId")
    OrderDto toDto(Order order);

    List<OrderDto> toDtoList(List<Order> orders);
}
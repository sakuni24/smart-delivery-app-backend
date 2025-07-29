package com.smartdelivery.service;

import com.smartdelivery.dto.MenuItemDto;
import com.smartdelivery.dto.RestaurantDto;
import com.smartdelivery.enums.RestaurantStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface RestaurantService {

    RestaurantDto createRestaurant(RestaurantDto restaurantDto);

    RestaurantDto getRestaurantById(Long id);

    RestaurantDto updateRestaurant(Long id, RestaurantDto restaurantDto);

    void deleteRestaurant(Long id);

    List<RestaurantDto> getAllRestaurants();

    Page<RestaurantDto> getRestaurantsByStatus(RestaurantStatus status, Pageable pageable);

    Page<RestaurantDto> searchRestaurants(String searchTerm, Pageable pageable);

    List<RestaurantDto> getTopRatedRestaurants(BigDecimal minRating);

    List<RestaurantDto> getRestaurantsByCity(String city);

    List<MenuItemDto> getRestaurantMenu(Long restaurantId);

    List<String> getRestaurantMenuCategories(Long restaurantId);

    RestaurantDto updateRestaurantStatus(Long id, RestaurantStatus status);
}
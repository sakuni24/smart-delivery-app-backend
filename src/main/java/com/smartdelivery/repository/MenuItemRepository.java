package com.smartdelivery.repository;

import com.smartdelivery.entity.MenuItem;
import com.smartdelivery.enums.MenuItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByRestaurantId(Long restaurantId);

    List<MenuItem> findByRestaurantIdAndStatus(Long restaurantId, MenuItemStatus status);

    Page<MenuItem> findByRestaurantIdAndStatus(Long restaurantId, MenuItemStatus status, Pageable pageable);

    List<MenuItem> findByCategory(String category);

    @Query("SELECT m FROM MenuItem m WHERE m.restaurant.id = :restaurantId AND " +
            "m.status = 'AVAILABLE' AND LOWER(m.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<MenuItem> findAvailableMenuItemsByRestaurantAndName(@Param("restaurantId") Long restaurantId,
                                                             @Param("searchTerm") String searchTerm);

    @Query("SELECT DISTINCT m.category FROM MenuItem m WHERE m.restaurant.id = :restaurantId AND m.status = 'AVAILABLE'")
    List<String> findCategoriesByRestaurant(@Param("restaurantId") Long restaurantId);

    List<MenuItem> findByIsVegetarianTrue();

    List<MenuItem> findByIsVeganTrue();
}
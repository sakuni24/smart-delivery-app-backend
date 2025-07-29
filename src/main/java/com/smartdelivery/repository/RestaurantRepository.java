package com.smartdelivery.repository;

import com.smartdelivery.entity.Restaurant;
import com.smartdelivery.enums.RestaurantStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByStatus(RestaurantStatus status);

    Page<Restaurant> findByStatus(RestaurantStatus status, Pageable pageable);

    @Query("SELECT r FROM Restaurant r WHERE r.status = :status AND " +
            "LOWER(r.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Restaurant> findByStatusAndNameContaining(@Param("status") RestaurantStatus status,
                                                   @Param("searchTerm") String searchTerm,
                                                   Pageable pageable);

    @Query("SELECT r FROM Restaurant r WHERE r.status = 'ACTIVE' AND " +
            "r.averageRating >= :minRating ORDER BY r.averageRating DESC")
    List<Restaurant> findTopRatedRestaurants(@Param("minRating") BigDecimal minRating);

    @Query("SELECT r FROM Restaurant r JOIN r.address a WHERE r.status = 'ACTIVE' AND " +
            "a.city = :city")
    List<Restaurant> findActiveRestaurantsByCity(@Param("city") String city);
}
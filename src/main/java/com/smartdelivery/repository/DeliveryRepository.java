package com.smartdelivery.repository;

import com.smartdelivery.entity.Delivery;
import com.smartdelivery.enums.DeliveryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    List<Delivery> findByDriverId(Long driverId);

    Page<Delivery> findByDriverId(Long driverId, Pageable pageable);

    List<Delivery> findByStatus(DeliveryStatus status);

    Page<Delivery> findByStatus(DeliveryStatus status, Pageable pageable);

    @Query("SELECT d FROM Delivery d WHERE d.driver.id = :driverId AND d.status = :status")
    List<Delivery> findByDriverIdAndStatus(@Param("driverId") Long driverId,
                                           @Param("status") DeliveryStatus status);

    @Query("SELECT d FROM Delivery d WHERE d.status IN :statuses ORDER BY d.createdAt ASC")
    List<Delivery> findAvailableDeliveries(@Param("statuses") List<DeliveryStatus> statuses);
}
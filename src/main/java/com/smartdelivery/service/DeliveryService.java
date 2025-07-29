package com.smartdelivery.service;

import com.smartdelivery.dto.DeliveryDto;
import com.smartdelivery.dto.DeliveryLocationDto;
import com.smartdelivery.enums.DeliveryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface DeliveryService {

    DeliveryDto createDeliveryForOrder(Long orderId);

    DeliveryDto getDeliveryById(Long id);

    List<DeliveryDto> getDeliveriesByDriver(Long driverId);

    Page<DeliveryDto> getDeliveriesByDriver(Long driverId, Pageable pageable);

    DeliveryDto assignDriverToDelivery(Long deliveryId, Long driverId);

    DeliveryDto updateDeliveryStatus(Long id, DeliveryStatus status);

    List<DeliveryDto> getAvailableDeliveries();

    DeliveryDto updateDriverLocation(Long deliveryId, BigDecimal latitude, BigDecimal longitude, BigDecimal accuracy);

    List<DeliveryLocationDto> getDeliveryTrackingHistory(Long deliveryId);

    DeliveryDto rateDelivery(Long deliveryId, BigDecimal rating, String feedback);
}
package com.example.blinkit.repository;

import com.example.blinkit.entity.InventoryReservation;
import com.example.blinkit.entity.ReservationStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryReservationRepository extends JpaRepository<InventoryReservation, Long> {
  List<InventoryReservation> findByStatusAndExpiresAtBefore(ReservationStatus status, LocalDateTime now);
}

package com.example.blinkit.repository;

import com.example.blinkit.entity.ReservationItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationItemRepository extends JpaRepository<ReservationItem, Long> {
  List<ReservationItem> findByReservationId(Long reservationId);
}

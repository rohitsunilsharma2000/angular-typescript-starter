package com.example.blinkit.repository;

import com.example.blinkit.entity.DeliveryEvent;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryEventRepository extends JpaRepository<DeliveryEvent, Long> {
  List<DeliveryEvent> findByOrderIdOrderByCreatedAtAsc(Long orderId);
}

package com.example.blinkit.repository;

import com.example.blinkit.entity.DeliveryBatch;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryBatchRepository extends JpaRepository<DeliveryBatch, Long> {
  List<DeliveryBatch> findByRiderIdAndStatus(Long riderId, String status);
}

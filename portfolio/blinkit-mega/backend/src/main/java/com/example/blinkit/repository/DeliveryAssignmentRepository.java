package com.example.blinkit.repository;

import com.example.blinkit.entity.DeliveryAssignment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryAssignmentRepository extends JpaRepository<DeliveryAssignment, Long> {
  List<DeliveryAssignment> findByBatchId(Long batchId);
}

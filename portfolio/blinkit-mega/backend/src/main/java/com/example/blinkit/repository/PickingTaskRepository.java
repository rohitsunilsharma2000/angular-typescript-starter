package com.example.blinkit.repository;

import com.example.blinkit.entity.PickingTask;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PickingTaskRepository extends JpaRepository<PickingTask, Long> {
  List<PickingTask> findByStatus(String status);
  Optional<PickingTask> findByOrderId(Long orderId);
}

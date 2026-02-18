package com.example.blinkit.repository;

import com.example.blinkit.entity.PickingTaskItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PickingTaskItemRepository extends JpaRepository<PickingTaskItem, Long> {
  List<PickingTaskItem> findByPickingTaskId(Long pickingTaskId);
}

package com.example.blinkit.repository;

import com.example.blinkit.entity.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
  List<Notification> findByUserIdOrderByIdDesc(Long userId);
}

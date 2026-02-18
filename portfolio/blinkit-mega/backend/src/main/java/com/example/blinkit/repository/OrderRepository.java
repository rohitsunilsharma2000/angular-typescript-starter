package com.example.blinkit.repository;

import com.example.blinkit.entity.Order;
import com.example.blinkit.entity.OrderStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findByUserIdOrderByIdDesc(Long userId);
  List<Order> findByStatusAndStoreId(OrderStatus status, Long storeId);
}

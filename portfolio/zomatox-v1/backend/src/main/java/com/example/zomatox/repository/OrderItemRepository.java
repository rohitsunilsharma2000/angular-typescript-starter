package com.example.zomatox.repository;

import com.example.zomatox.entity.Order;
import com.example.zomatox.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
  List<OrderItem> findByOrder(Order order);
}

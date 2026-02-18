package com.example.zomatox.repository;

import com.example.zomatox.entity.Order;
import com.example.zomatox.entity.OrderEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderEventRepository extends JpaRepository<OrderEvent, Long> {
  List<OrderEvent> findByOrderOrderByIdAsc(Order order);
}

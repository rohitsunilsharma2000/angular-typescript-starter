package com.example.zomatox.repository;

import com.example.zomatox.entity.Order;
import com.example.zomatox.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findByUserOrderByIdDesc(User user);
}

package com.example.zomatox.repository;

import com.example.zomatox.entity.Order;
import com.example.zomatox.entity.User;
import com.example.zomatox.entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findByUserOrderByIdDesc(User user);
  List<Order> findByRestaurantOwnerOrderByIdDesc(User owner);
  List<Order> findByRestaurantOwnerAndStatusOrderByIdDesc(User owner, OrderStatus status);
  List<Order> findByStatusAndDeliveryPartnerIsNullOrderByIdDesc(OrderStatus status);
  List<Order> findByDeliveryPartnerOrderByIdDesc(User deliveryPartner);
}

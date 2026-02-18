package com.example.zomatox.service;

import com.example.zomatox.entity.Order;
import com.example.zomatox.entity.User;
import com.example.zomatox.entity.enums.OrderStatus;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryService {
  private final OrderRepository orderRepository;
  private final OrderService orderService;

  public List<Order> availableJobs() {
    return orderRepository.findByStatusAndDeliveryPartnerIsNullOrderByIdDesc(OrderStatus.READY_FOR_PICKUP);
  }

  public List<Order> assignedJobs(User deliveryUser) {
    return orderRepository.findByDeliveryPartnerOrderByIdDesc(deliveryUser).stream()
      .filter(o -> o.getStatus() != OrderStatus.DELIVERED)
      .toList();
  }

  public Order accept(User deliveryUser, Long orderId) {
    Order o = orderService.getOrderOrThrow(orderId);

    if (o.getStatus() != OrderStatus.READY_FOR_PICKUP) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Order must be READY_FOR_PICKUP to accept");
    }
    if (o.getDeliveryPartner() != null) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Order already assigned to a delivery partner");
    }

    o.setDeliveryPartner(deliveryUser);
    orderRepository.save(o);
    return o;
  }

  public Order setStatus(User deliveryUser, Long orderId, OrderStatus next) {
    Order o = orderService.getOrderOrThrow(orderId);

    if (o.getDeliveryPartner() == null || !o.getDeliveryPartner().getId().equals(deliveryUser.getId())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "Order not assigned to this delivery partner");
    }

    if (!(next == OrderStatus.PICKED_UP || next == OrderStatus.OUT_FOR_DELIVERY || next == OrderStatus.DELIVERED)) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Delivery can set only PICKED_UP, OUT_FOR_DELIVERY, DELIVERED");
    }

    return orderService.setStatusWithEvent(o, next, "Delivery updated to " + next, Instant.now());
  }
}

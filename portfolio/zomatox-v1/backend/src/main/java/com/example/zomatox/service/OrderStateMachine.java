package com.example.zomatox.service;

import com.example.zomatox.entity.enums.OrderStatus;
import com.example.zomatox.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class OrderStateMachine {
  public void requireTransitionAllowed(OrderStatus from, OrderStatus to) {
    if (from == to) return;

    if (from == OrderStatus.PAYMENT_PENDING && to == OrderStatus.PAID) return;
    if (from == OrderStatus.PAID && to == OrderStatus.CONFIRMED) return;
    if (from == OrderStatus.CONFIRMED && to == OrderStatus.PREPARING) return;
    if (from == OrderStatus.PREPARING && to == OrderStatus.READY_FOR_PICKUP) return;

    if (from == OrderStatus.READY_FOR_PICKUP && to == OrderStatus.PICKED_UP) return;
    if (from == OrderStatus.PICKED_UP && to == OrderStatus.OUT_FOR_DELIVERY) return;
    if (from == OrderStatus.OUT_FOR_DELIVERY && to == OrderStatus.DELIVERED) return;

    if (Set.of(OrderStatus.PAYMENT_PENDING, OrderStatus.PAID, OrderStatus.CONFIRMED).contains(from)
      && to == OrderStatus.PAYMENT_FAILED) return;

    throw new ApiException(HttpStatus.BAD_REQUEST, "Illegal transition: " + from + " -> " + to);
  }
}

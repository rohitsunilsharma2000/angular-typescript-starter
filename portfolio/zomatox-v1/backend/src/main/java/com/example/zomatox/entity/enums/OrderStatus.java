package com.example.zomatox.entity.enums;

public enum OrderStatus {
  CREATED,
  PAYMENT_PENDING,
  PAID,

  CONFIRMED,
  PREPARING,
  READY_FOR_PICKUP,

  PICKED_UP,
  OUT_FOR_DELIVERY,
  DELIVERED,

  PAYMENT_FAILED
}

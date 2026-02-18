package com.example.zomatox.dto.orders;

import com.example.zomatox.entity.OrderEvent;
import lombok.Value;

import java.time.Instant;

@Value
public class OrderEventResponse {
  Long id;
  Long orderId;
  String status;
  String message;
  Instant createdAt;

  public static OrderEventResponse from(OrderEvent e) {
    return new OrderEventResponse(e.getId(), e.getOrder().getId(), e.getStatus(), e.getMessage(), e.getCreatedAt());
  }
}

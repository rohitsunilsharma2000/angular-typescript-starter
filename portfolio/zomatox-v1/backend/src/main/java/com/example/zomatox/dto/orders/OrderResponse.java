package com.example.zomatox.dto.orders;

import com.example.zomatox.entity.Order;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
public class OrderResponse {
  Long id;
  Long userId;
  Long restaurantId;
  String status;
  long itemTotal;
  long deliveryFee;
  long payableTotal;
  Instant createdAt;
  List<OrderItemResponse> items;

  public static OrderResponse of(Order o, List<OrderItemResponse> items) {
    return new OrderResponse(
      o.getId(), o.getUser().getId(), o.getRestaurant().getId(),
      o.getStatus().name(),
      o.getItemTotal(), o.getDeliveryFee(), o.getPayableTotal(),
      o.getCreatedAt(),
      items
    );
  }
}

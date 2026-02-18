package com.example.zomatox.dto.orders;

import lombok.Value;

@Value
public class OrderItemResponse {
  String name;
  long price;
  int qty;
  long lineTotal;
}

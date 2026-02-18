package com.example.zomatox.dto.cart;

import lombok.Value;

@Value
public class CartLineResponse {
  Long menuItemId;
  String name;
  long price;
  int qty;
  long lineTotal;
}

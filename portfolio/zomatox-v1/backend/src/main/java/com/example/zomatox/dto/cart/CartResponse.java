package com.example.zomatox.dto.cart;

import lombok.Value;

import java.util.List;

@Value
public class CartResponse {
  Long cartId;
  Long userId;
  Long restaurantId; // derived if cart has items from one restaurant; null if empty
  List<CartLineResponse> items;
  long itemTotal;
}

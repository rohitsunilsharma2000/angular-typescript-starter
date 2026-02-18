package com.example.zomatox.dto.restaurants;

import com.example.zomatox.entity.MenuItem;
import lombok.Value;

@Value
public class MenuItemResponse {
  Long id;
  Long restaurantId;
  String name;
  long price;
  boolean isVeg;
  boolean available;
  int stockQty;
  boolean isBlocked;

  public static MenuItemResponse from(MenuItem m) {
    return new MenuItemResponse(
      m.getId(),
      m.getRestaurant().getId(),
      m.getName(),
      m.getPrice(),
      m.isVeg(),
      m.isAvailable(),
      m.getStockQty(),
      m.isBlocked()
    );
  }
}

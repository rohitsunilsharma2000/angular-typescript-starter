package com.example.zomatox.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemUpsertRequest {
  @NotNull
  private Long menuItemId;

  @Min(value = 0, message = "qty must be >= 0")
  private int qty;
}

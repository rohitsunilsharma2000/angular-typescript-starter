package com.example.blinkit.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class CartDtos {
  public record UpsertCartItemRequest(@NotNull Long productId, @NotNull Long storeId, @Min(1) int qty) {}

  public record CartItemView(Long productId, String productName, int qty, BigDecimal price, BigDecimal lineTotal) {}

  public record CartView(Long storeId, List<CartItemView> items, BigDecimal subtotal) {}
}

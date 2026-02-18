package com.example.blinkit.dto;

import java.math.BigDecimal;

public class ProductDtos {
  public record ProductView(Long id, Long categoryId, String name, String description, BigDecimal price) {}
}

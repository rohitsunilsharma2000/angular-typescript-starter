package com.example.zomatox.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminRestaurantCreateRequest {
  @NotBlank private String name;
  @NotBlank private String city;
  @NotBlank private String cuisineType;
  @NotNull private Double ratingAvg;
  @NotNull private Integer deliveryTimeMin;
  private String imageUrl;
}

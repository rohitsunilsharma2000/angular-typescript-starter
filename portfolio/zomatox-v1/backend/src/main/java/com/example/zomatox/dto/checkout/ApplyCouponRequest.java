package com.example.zomatox.dto.checkout;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApplyCouponRequest {
  @NotBlank
  private String couponCode;

  @NotNull
  private Long restaurantId;
}

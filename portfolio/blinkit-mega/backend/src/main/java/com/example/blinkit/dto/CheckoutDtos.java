package com.example.blinkit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CheckoutDtos {
  public record CheckoutPreviewRequest(@NotNull Long storeId) {}

  public record CheckoutPreviewResponse(Long reservationId, LocalDateTime expiresAt, BigDecimal subtotal,
                                        BigDecimal discount, BigDecimal deliveryFee, BigDecimal total) {}

  public record ApplyCouponRequest(@NotBlank String couponCode, @NotNull Long storeId) {}

  public record UseWalletRequest(@NotNull Long storeId, @NotNull BigDecimal amount) {}
}

package com.example.blinkit.dto;

import com.example.blinkit.entity.RefundStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class AdminDtos {
  public record ProductRequest(@NotNull Long categoryId, @NotBlank String name, String description,
                               @NotNull BigDecimal price, boolean active) {}

  public record InventoryUpdateRequest(@Min(0) int stockOnHand) {}

  public record RefundApprovalRequest(String note) {}

  public record MarkMissingRequest(@NotNull Long orderItemId, @Min(1) int missingQty) {}

  public record RiderStatusRequest(@NotBlank String eventType, @NotBlank String message) {}

  public record RefundView(Long id, Long orderId, BigDecimal amount, RefundStatus status, String destination) {}
}

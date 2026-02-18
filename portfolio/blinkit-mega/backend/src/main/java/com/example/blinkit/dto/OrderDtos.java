package com.example.blinkit.dto;

import com.example.blinkit.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDtos {
  public record CreateOrderRequest(@NotNull Long reservationId, String couponCode, BigDecimal walletAmount) {}

  public record StatusUpdateRequest(@NotNull OrderStatus nextStatus) {}

  public record AssignRiderRequest(@NotNull Long riderUserId) {}

  public record OrderItemView(Long productId, String productName, int qty, BigDecimal price, int refundedQty) {}

  public record OrderView(Long id, OrderStatus status, BigDecimal totalAmount, LocalDateTime createdAt,
                          List<OrderItemView> items) {}

  public record ReturnRequest(int hoursFromDelivery) {}
}

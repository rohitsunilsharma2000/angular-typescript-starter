package com.example.blinkit.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "user_id")
  private Long userId;
  @Column(name = "store_id")
  private Long storeId;
  @Enumerated(EnumType.STRING)
  private OrderStatus status;
  @Column(name = "total_amount")
  private BigDecimal totalAmount;
  @Column(name = "delivery_fee")
  private BigDecimal deliveryFee;
  @Column(name = "discount_amount")
  private BigDecimal discountAmount;
  @Column(name = "wallet_used")
  private BigDecimal walletUsed;
  @Column(name = "created_at")
  private LocalDateTime createdAt;
  @Column(name = "delivered_at")
  private LocalDateTime deliveredAt;
  @Column(name = "rider_id")
  private Long riderId;
  @Column(name = "reservation_id")
  private Long reservationId;
}

package com.example.blinkit.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "coupon_redemptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponRedemption {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "coupon_id")
  private Long couponId;
  @Column(name = "user_id")
  private Long userId;
  @Column(name = "order_id")
  private Long orderId;
  @Column(name = "discount_amount")
  private BigDecimal discountAmount;
  @Column(name = "created_at")
  private LocalDateTime createdAt;
}

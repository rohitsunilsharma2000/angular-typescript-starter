package com.example.blinkit.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "refunds")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Refund {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "order_id")
  private Long orderId;
  @Column(name = "order_item_id")
  private Long orderItemId;
  private BigDecimal amount;
  @Enumerated(EnumType.STRING)
  private RefundStatus status;
  private String destination;
  @Column(name = "created_at")
  private LocalDateTime createdAt;
  @Column(name = "approved_at")
  private LocalDateTime approvedAt;
}

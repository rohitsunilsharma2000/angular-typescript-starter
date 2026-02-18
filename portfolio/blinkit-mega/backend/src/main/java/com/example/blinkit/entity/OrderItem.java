package com.example.blinkit.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.*;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "order_id")
  private Long orderId;
  @Column(name = "product_id")
  private Long productId;
  @Column(name = "product_name")
  private String productName;
  private Integer qty;
  private BigDecimal price;
  @Column(name = "refunded_qty")
  private Integer refundedQty;
}

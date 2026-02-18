package com.example.zomatox.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderItem {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private Order order;

  @Column(nullable = false)
  private String menuItemNameSnapshot;

  @Column(nullable = false)
  private long priceSnapshot;

  @Column(nullable = false)
  private int qty;

  @Column(nullable = false)
  private long lineTotal;
}

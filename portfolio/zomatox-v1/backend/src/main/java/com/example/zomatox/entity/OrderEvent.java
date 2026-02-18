package com.example.zomatox.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "order_events")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderEvent {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private Order order;

  @Column(nullable = false)
  private String status;

  @Column
  private String message;

  @Column(nullable = false)
  private Instant createdAt;
}

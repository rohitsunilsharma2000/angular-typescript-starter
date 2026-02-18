package com.example.blinkit.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "order_id", unique = true)
  private Long orderId;
  @Enumerated(EnumType.STRING)
  private PaymentStatus status;
  @Column(name = "provider_ref")
  private String providerRef;
  @Column(name = "created_at")
  private LocalDateTime createdAt;
}

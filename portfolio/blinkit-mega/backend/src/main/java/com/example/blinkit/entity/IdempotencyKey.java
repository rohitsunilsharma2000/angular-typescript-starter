package com.example.blinkit.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "idempotency_keys", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "idem_key"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdempotencyKey {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "user_id")
  private Long userId;
  @Column(name = "idem_key")
  private String idemKey;
  @Column(name = "payload_hash")
  private String payloadHash;
  @Column(name = "order_id")
  private Long orderId;
  @Column(name = "created_at")
  private LocalDateTime createdAt;
}

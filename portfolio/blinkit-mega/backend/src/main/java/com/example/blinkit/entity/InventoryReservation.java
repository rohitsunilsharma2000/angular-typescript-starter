package com.example.blinkit.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "inventory_reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryReservation {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "user_id")
  private Long userId;
  @Column(name = "store_id")
  private Long storeId;
  @Enumerated(EnumType.STRING)
  private ReservationStatus status;
  @Column(name = "expires_at")
  private LocalDateTime expiresAt;
  @Column(name = "created_at")
  private LocalDateTime createdAt;
}

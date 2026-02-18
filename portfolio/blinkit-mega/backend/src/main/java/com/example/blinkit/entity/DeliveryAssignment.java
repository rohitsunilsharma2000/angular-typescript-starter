package com.example.blinkit.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "delivery_assignments", uniqueConstraints = @UniqueConstraint(columnNames = {"batch_id", "order_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryAssignment {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "batch_id")
  private Long batchId;
  @Column(name = "order_id")
  private Long orderId;
  private boolean accepted;
}

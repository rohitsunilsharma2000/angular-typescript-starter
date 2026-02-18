package com.example.blinkit.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "picking_tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PickingTask {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "order_id", unique = true)
  private Long orderId;
  @Column(name = "assigned_to_user_id")
  private Long assignedToUserId;
  private String status;
  @Column(name = "created_at")
  private LocalDateTime createdAt;
}

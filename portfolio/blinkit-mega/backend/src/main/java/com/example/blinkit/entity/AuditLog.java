package com.example.blinkit.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "actor_user_id")
  private Long actorUserId;
  private String action;
  @Column(name = "entity_type")
  private String entityType;
  @Column(name = "entity_id")
  private String entityId;
  private String payload;
  @Column(name = "created_at")
  private LocalDateTime createdAt;
}

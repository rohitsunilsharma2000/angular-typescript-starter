package com.example.blinkit.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "outbox_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEvent {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String topic;
  @Column(name = "aggregate_id")
  private String aggregateId;
  private String payload;
  private boolean published;
  @Column(name = "created_at")
  private LocalDateTime createdAt;
}

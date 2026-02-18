package com.example.zomatox.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "recent_restaurants")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RecentRestaurant {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "restaurant_id")
  private Restaurant restaurant;

  @Column(name = "viewed_at", nullable = false)
  private Instant viewedAt;
}

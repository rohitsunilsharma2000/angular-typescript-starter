package com.example.zomatox.entity;

import com.example.zomatox.entity.enums.RestaurantApprovalStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "restaurants")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Restaurant {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String city;

  @Column(nullable = false)
  private String cuisineType;

  @Column(nullable = false)
  private double ratingAvg;

  @Column(nullable = false)
  private int deliveryTimeMin;

  private String imageUrl;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_user_id")
  private User owner;

  @Enumerated(EnumType.STRING)
  @Column(name = "approval_status", nullable = false)
  @Builder.Default
  private RestaurantApprovalStatus approvalStatus = RestaurantApprovalStatus.PENDING_APPROVAL;

  @Column(name = "is_blocked", nullable = false)
  @Builder.Default
  private boolean isBlocked = false;

  @Column(name = "blocked_reason")
  private String blockedReason;
}

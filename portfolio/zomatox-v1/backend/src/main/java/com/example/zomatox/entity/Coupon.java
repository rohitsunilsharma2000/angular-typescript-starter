package com.example.zomatox.entity;

import com.example.zomatox.entity.enums.CouponType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "coupons")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Coupon {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String code;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CouponType type;

  @Column(nullable = false)
  private long value;

  @Column(name = "min_order", nullable = false)
  private long minOrder;

  @Column(name = "max_cap")
  private Long maxCap;

  @Column(name = "valid_from", nullable = false)
  private Instant validFrom;

  @Column(name = "valid_to", nullable = false)
  private Instant validTo;

  @Column(nullable = false)
  @Builder.Default
  private boolean active = true;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "restaurant_id")
  private Restaurant restaurant;

  @Column(name = "usage_limit_per_user", nullable = false)
  @Builder.Default
  private int usageLimitPerUser = 1;

  @Column(name = "applicable_cuisine_type")
  private String applicableCuisineType;
}

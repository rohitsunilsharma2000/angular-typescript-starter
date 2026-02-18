package com.example.blinkit.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.*;

@Entity
@Table(name = "coupons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(unique = true)
  private String code;
  @Column(name = "discount_type")
  private String discountType;
  @Column(name = "discount_value")
  private BigDecimal discountValue;
  @Column(name = "min_cart_value")
  private BigDecimal minCartValue;
  @Column(name = "max_discount")
  private BigDecimal maxDiscount;
  @Column(name = "category_id")
  private Long categoryId;
  private boolean active;
}

package com.example.blinkit.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.*;

@Entity
@Table(name = "delivery_fee_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryFeeRule {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  @Column(name = "min_km")
  private BigDecimal minKm;
  @Column(name = "max_km")
  private BigDecimal maxKm;
  @Column(name = "peak_hour")
  private boolean peakHour;
  @Column(name = "high_load")
  private boolean highLoad;
  private BigDecimal fee;
  private boolean active;
}

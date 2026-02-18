package com.example.blinkit.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.*;

@Entity
@Table(name = "reservation_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationItem {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "reservation_id")
  private Long reservationId;
  @Column(name = "product_id")
  private Long productId;
  private Integer qty;
  @Column(name = "unit_price")
  private BigDecimal unitPrice;
}

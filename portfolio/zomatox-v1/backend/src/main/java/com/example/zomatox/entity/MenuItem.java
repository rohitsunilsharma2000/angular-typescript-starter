package com.example.zomatox.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "menu_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MenuItem {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "restaurant_id")
  private Restaurant restaurant;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private long price; // paise-like integer (e.g., INR * 100) OR just INR units; keep consistent

  @Column(nullable = false)
  private boolean isVeg;

  @Column(nullable = false)
  private boolean available;

  @Column(nullable = false)
  private int stockQty;
}

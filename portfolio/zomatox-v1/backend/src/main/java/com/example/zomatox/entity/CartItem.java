package com.example.zomatox.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_items",
  uniqueConstraints = @UniqueConstraint(name = "uk_cart_menu", columnNames = {"cart_id", "menu_item_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CartItem {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "cart_id")
  private Cart cart;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "menu_item_id")
  private MenuItem menuItem;

  @Column(nullable = false)
  private int qty;
}

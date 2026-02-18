package com.example.blinkit.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "store_inventory", uniqueConstraints = @UniqueConstraint(columnNames = {"store_id", "product_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreInventory {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "store_id")
  private Long storeId;
  @Column(name = "product_id")
  private Long productId;
  @Column(name = "stock_on_hand")
  private Integer stockOnHand;
  @Column(name = "reserved_qty")
  private Integer reservedQty;
  @Version
  private Long version;
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}

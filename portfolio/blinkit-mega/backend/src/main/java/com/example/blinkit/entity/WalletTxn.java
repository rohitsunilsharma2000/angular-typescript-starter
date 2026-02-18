package com.example.blinkit.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "wallet_txns")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletTxn {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "wallet_id")
  private Long walletId;
  @Column(name = "order_id")
  private Long orderId;
  private String type;
  private BigDecimal amount;
  private String note;
  @Column(name = "created_at")
  private LocalDateTime createdAt;
}

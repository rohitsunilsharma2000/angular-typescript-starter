package com.example.blinkit.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "referrals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Referral {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "referrer_user_id")
  private Long referrerUserId;
  @Column(name = "referred_user_id")
  private Long referredUserId;
  @Column(name = "reward_credited")
  private boolean rewardCredited;
  @Column(name = "created_at")
  private LocalDateTime createdAt;
}

package com.example.blinkit.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  @Column(unique = true)
  private String email;
  @Column(name = "password_hash")
  private String passwordHash;
  @Enumerated(EnumType.STRING)
  private Role role;
  private String pincode;
  @Column(name = "created_at")
  private LocalDateTime createdAt;
}

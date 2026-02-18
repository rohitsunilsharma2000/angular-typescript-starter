package com.example.zomatox.repository;

import com.example.zomatox.entity.RefreshToken;
import com.example.zomatox.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByTokenHash(String tokenHash);
  List<RefreshToken> findByUserAndRevokedAtIsNull(User user);
  long deleteByExpiresAtBefore(Instant t);
}

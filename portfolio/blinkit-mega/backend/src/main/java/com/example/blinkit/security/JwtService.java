package com.example.blinkit.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
  @Value("${app.jwt.secret}")
  private String secret;

  @Value("${app.jwt.access-expiry-minutes}")
  private long accessExpiryMinutes;

  @Value("${app.jwt.refresh-expiry-days}")
  private long refreshExpiryDays;

  public String generateAccessToken(Long userId, String role) {
    return token(userId, role, accessExpiryMinutes * 60);
  }

  public String generateRefreshToken(Long userId, String role) {
    return token(userId, role, refreshExpiryDays * 24 * 3600);
  }

  private String token(Long userId, String role, long seconds) {
    Instant now = Instant.now();
    return Jwts.builder()
        .subject(String.valueOf(userId))
        .claims(Map.of("role", role))
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plusSeconds(seconds)))
        .signWith(key())
        .compact();
  }

  public Claims claims(String token) {
    return Jwts.parser().verifyWith((javax.crypto.SecretKey) key()).build().parseSignedClaims(token).getPayload();
  }

  private Key key() {
    byte[] bytes;
    try {
      bytes = Decoders.BASE64.decode(secret);
    } catch (Exception ex) {
      bytes = secret.getBytes(StandardCharsets.UTF_8);
    }
    return Keys.hmacShaKeyFor(bytes);
  }
}

package com.example.zomatox.security.jwt;

import com.example.zomatox.entity.User;
import com.example.zomatox.entity.enums.UserRole;
import com.example.zomatox.security.AuthenticatedUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
  @Value("${app.security.jwt.secret}")
  private String secret;

  @Value("${app.security.jwt.issuer}")
  private String issuer;

  @Value("${app.security.jwt.access-ttl-minutes}")
  private long accessTtlMinutes;

  private SecretKey key;

  @PostConstruct
  void init() {
    key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public String generateAccessToken(User user) {
    Instant now = Instant.now();
    return Jwts.builder()
      .issuer(issuer)
      .subject(user.getEmail())
      .claim("uid", user.getId())
      .claim("role", user.getRole().name())
      .issuedAt(Date.from(now))
      .expiration(Date.from(now.plusSeconds(accessTtlMinutes * 60)))
      .signWith(key)
      .compact();
  }

  public AuthenticatedUser parseAccessToken(String token) {
    Claims c = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    Long uid = c.get("uid", Number.class).longValue();
    String email = c.getSubject();
    UserRole role = UserRole.valueOf(c.get("role", String.class));
    return new AuthenticatedUser(uid, email, role);
  }
}

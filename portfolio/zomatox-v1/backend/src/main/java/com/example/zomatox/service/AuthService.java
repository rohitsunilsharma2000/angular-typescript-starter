package com.example.zomatox.service;

import com.example.zomatox.dto.auth.*;
import com.example.zomatox.entity.RefreshToken;
import com.example.zomatox.entity.User;
import com.example.zomatox.entity.enums.UserRole;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.repository.RefreshTokenRepository;
import com.example.zomatox.repository.UserRepository;
import com.example.zomatox.security.AuthenticatedUser;
import com.example.zomatox.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;

  @Value("${app.security.jwt.refresh-ttl-days}")
  private long refreshTtlDays;

  public TokenPairResponse signup(SignupRequest req) {
    userRepository.findByEmailIgnoreCase(req.getEmail()).ifPresent(u -> {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Email already exists");
    });

    User user = userRepository.save(User.builder()
      .name(req.getName())
      .email(req.getEmail().toLowerCase())
      .passwordHash(passwordEncoder.encode(req.getPassword()))
      .role(UserRole.CUSTOMER)
      .isActive(true)
      .build());

    return issueTokens(user);
  }

  public TokenPairResponse login(LoginRequest req) {
    User user = userRepository.findByEmailIgnoreCase(req.getEmail())
      .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

    if (!user.isActive()) throw new ApiException(HttpStatus.FORBIDDEN, "User is inactive");
    if (user.getPasswordHash() == null || !passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }

    return issueTokens(user);
  }

  public TokenPairResponse refresh(RefreshRequest req) {
    String tokenHash = sha256(req.getRefreshToken());
    RefreshToken rt = refreshTokenRepository.findByTokenHash(tokenHash)
      .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));

    if (rt.getRevokedAt() != null || rt.getExpiresAt().isBefore(Instant.now())) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "Refresh token expired/revoked");
    }

    User user = rt.getUser();
    rt.setRevokedAt(Instant.now());
    refreshTokenRepository.save(rt);

    return issueTokens(user);
  }

  public void logout(RefreshRequest req) {
    String tokenHash = sha256(req.getRefreshToken());
    refreshTokenRepository.findByTokenHash(tokenHash).ifPresent(rt -> {
      rt.setRevokedAt(Instant.now());
      refreshTokenRepository.save(rt);
    });
  }

  public AuthUserProfile me(AuthenticatedUser authUser) {
    User u = userRepository.findById(authUser.id())
      .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "User not found"));
    return AuthUserProfile.from(u);
  }

  private TokenPairResponse issueTokens(User user) {
    String accessToken = jwtService.generateAccessToken(user);
    String refreshRaw = UUID.randomUUID() + "." + UUID.randomUUID();
    RefreshToken rt = refreshTokenRepository.save(RefreshToken.builder()
      .user(user)
      .tokenHash(sha256(refreshRaw))
      .createdAt(Instant.now())
      .expiresAt(Instant.now().plusSeconds(refreshTtlDays * 24 * 60 * 60))
      .build());

    return new TokenPairResponse(accessToken, refreshRaw, AuthUserProfile.from(user));
  }

  private String sha256(String raw) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      return HexFormat.of().formatHex(md.digest(raw.getBytes(StandardCharsets.UTF_8)));
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}

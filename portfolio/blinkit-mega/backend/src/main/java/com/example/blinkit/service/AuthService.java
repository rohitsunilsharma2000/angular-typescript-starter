package com.example.blinkit.service;

import com.example.blinkit.dto.AuthDtos.*;
import com.example.blinkit.entity.RefreshToken;
import com.example.blinkit.entity.User;
import com.example.blinkit.exception.ApiException;
import com.example.blinkit.repository.RefreshTokenRepository;
import com.example.blinkit.repository.UserRepository;
import com.example.blinkit.security.JwtService;
import com.example.blinkit.util.AuthUtil;
import com.example.blinkit.util.HashUtil;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
  private final UserRepository userRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  @Transactional
  public MeResponse signup(SignupRequest req) {
    userRepository.findByEmail(req.email()).ifPresent(u -> {
      throw new ApiException("Email already exists");
    });
    User user = userRepository.save(User.builder()
        .name(req.name())
        .email(req.email())
        .passwordHash(passwordEncoder.encode(req.password()))
        .role(req.role())
        .pincode(req.pincode())
        .createdAt(LocalDateTime.now())
        .build());
    log.info("signup userId={} role={}", user.getId(), user.getRole());
    return new MeResponse(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getPincode());
  }

  @Transactional
  public TokenResponse login(LoginRequest req) {
    User user = userRepository.findByEmail(req.email()).orElseThrow(() -> new ApiException("Invalid credentials"));
    if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
      throw new ApiException("Invalid credentials");
    }
    String access = jwtService.generateAccessToken(user.getId(), user.getRole().name());
    String refresh = jwtService.generateRefreshToken(user.getId(), user.getRole().name());
    refreshTokenRepository.save(RefreshToken.builder()
        .userId(user.getId())
        .tokenHash(HashUtil.sha256(refresh))
        .expiresAt(LocalDateTime.now().plusDays(7))
        .revoked(false)
        .build());
    return new TokenResponse(access, refresh);
  }

  @Transactional
  public TokenResponse refresh(RefreshRequest req) {
    RefreshToken token = refreshTokenRepository.findByTokenHashAndRevokedFalse(HashUtil.sha256(req.refreshToken()))
        .orElseThrow(() -> new ApiException("Invalid refresh token"));
    if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new ApiException("Refresh token expired");
    }
    User user = userRepository.findById(token.getUserId()).orElseThrow(() -> new ApiException("User not found"));
    String access = jwtService.generateAccessToken(user.getId(), user.getRole().name());
    String refresh = jwtService.generateRefreshToken(user.getId(), user.getRole().name());
    token.setRevoked(true);
    refreshTokenRepository.save(token);
    refreshTokenRepository.save(RefreshToken.builder()
        .userId(user.getId())
        .tokenHash(HashUtil.sha256(refresh))
        .expiresAt(LocalDateTime.now().plusDays(7))
        .revoked(false)
        .build());
    return new TokenResponse(access, refresh);
  }

  @Transactional
  public void logout(String refreshToken) {
    refreshTokenRepository.findByTokenHashAndRevokedFalse(HashUtil.sha256(refreshToken)).ifPresent(token -> {
      token.setRevoked(true);
      refreshTokenRepository.save(token);
    });
  }

  public MeResponse me() {
    Long userId = AuthUtil.userId();
    User user = userRepository.findById(userId).orElseThrow(() -> new ApiException("User not found"));
    return new MeResponse(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getPincode());
  }
}

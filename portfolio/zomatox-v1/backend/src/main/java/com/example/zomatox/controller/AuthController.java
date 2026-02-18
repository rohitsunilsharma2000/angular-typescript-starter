package com.example.zomatox.controller;

import com.example.zomatox.dto.auth.*;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.security.AuthenticatedUser;
import com.example.zomatox.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("/signup")
  public TokenPairResponse signup(@Valid @RequestBody SignupRequest req) {
    return authService.signup(req);
  }

  @PostMapping("/login")
  public TokenPairResponse login(@Valid @RequestBody LoginRequest req) {
    return authService.login(req);
  }

  @PostMapping("/refresh")
  public TokenPairResponse refresh(@Valid @RequestBody RefreshRequest req) {
    return authService.refresh(req);
  }

  @PostMapping("/logout")
  public void logout(@Valid @RequestBody RefreshRequest req) {
    authService.logout(req);
  }

  @GetMapping("/me")
  public AuthUserProfile me(Authentication authentication) {
    if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUser user)) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "Unauthorized");
    }
    return authService.me(user);
  }
}

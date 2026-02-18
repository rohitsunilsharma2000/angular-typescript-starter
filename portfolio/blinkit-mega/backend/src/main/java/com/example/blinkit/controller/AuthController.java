package com.example.blinkit.controller;

import com.example.blinkit.dto.AuthDtos.*;
import com.example.blinkit.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth")
public class AuthController {
  private final AuthService authService;

  @PostMapping("/signup")
  public MeResponse signup(@Valid @RequestBody SignupRequest req) {
    return authService.signup(req);
  }

  @PostMapping("/login")
  public TokenResponse login(@Valid @RequestBody LoginRequest req) {
    return authService.login(req);
  }

  @PostMapping("/refresh")
  public TokenResponse refresh(@Valid @RequestBody RefreshRequest req) {
    return authService.refresh(req);
  }

  @PostMapping("/logout")
  public void logout(@RequestBody RefreshRequest req) {
    authService.logout(req.refreshToken());
  }

  @GetMapping("/me")
  public MeResponse me() {
    return authService.me();
  }
}

package com.example.classroom.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.classroom.dto.LoginRequest;
import com.example.classroom.dto.LoginResponse;
import com.example.classroom.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
    return ResponseEntity.ok(authService.login(req.getEmail(), req.getPassword()));
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(@RequestHeader("X-Auth-Token") String token) {
    authService.logout(token);
    return ResponseEntity.noContent().build();
  }
}

package com.example.blinkit.dto;

import com.example.blinkit.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AuthDtos {
  public record SignupRequest(@NotBlank String name, @NotBlank @Email String email, @NotBlank String password,
                              @NotNull Role role, String pincode) {}

  public record LoginRequest(@NotBlank @Email String email, @NotBlank String password) {}

  public record TokenResponse(String accessToken, String refreshToken) {}

  public record MeResponse(Long id, String name, String email, Role role, String pincode) {}

  public record RefreshRequest(@NotBlank String refreshToken) {}
}

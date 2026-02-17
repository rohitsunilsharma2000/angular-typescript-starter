package com.example.classroom.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserCreateRequest {
  @NotNull
  private String role;

  @NotBlank
  private String name;

  @NotBlank
  private String email;

  @NotBlank
  private String password;
}

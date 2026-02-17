package com.example.classroom.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClassroomCreateRequest {
  @NotBlank
  private String name;

  @NotBlank
  private String dayWindows;
}

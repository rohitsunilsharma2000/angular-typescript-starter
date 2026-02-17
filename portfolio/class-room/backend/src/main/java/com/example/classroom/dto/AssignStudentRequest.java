package com.example.classroom.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AssignStudentRequest {
  @NotBlank
  private String studentId;

  @NotBlank
  private String teacherId;
}

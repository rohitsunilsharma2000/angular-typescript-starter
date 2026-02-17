package com.example.classroom.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AssignTeacherRequest {
  @NotBlank
  private String teacherId;

  @NotBlank
  private String classroomId;
}

package com.example.classroom.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PeriodRequest {
  @NotBlank
  private String classroomId;

  @NotBlank
  private String day;

  @NotBlank
  private String subject;

  @NotBlank
  private String startTime;

  @NotBlank
  private String endTime;
}

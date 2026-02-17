package com.example.classroom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PeriodResponse {
  private String id;
  private String classroomId;
  private String day;
  private String subject;
  private String startTime;
  private String endTime;
}

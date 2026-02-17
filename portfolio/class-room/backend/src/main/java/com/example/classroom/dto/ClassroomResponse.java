package com.example.classroom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClassroomResponse {
  private String id;
  private String name;
  private String dayWindows;
  private String teacherId;
}

package com.example.classroom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
  private String id;
  private String role;
  private String name;
  private String email;
  private String classroomId;
  private String teacherId;
}

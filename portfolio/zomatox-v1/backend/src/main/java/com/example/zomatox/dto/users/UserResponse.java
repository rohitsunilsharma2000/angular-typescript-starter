package com.example.zomatox.dto.users;

import com.example.zomatox.entity.User;
import lombok.Value;

@Value
public class UserResponse {
  Long id;
  String name;
  String email;
  String role;

  public static UserResponse from(User u) {
    return new UserResponse(u.getId(), u.getName(), u.getEmail(), u.getRole().name());
  }
}

package com.example.zomatox.dto.auth;

import com.example.zomatox.entity.User;
import lombok.Value;

@Value
public class AuthUserProfile {
  Long id;
  String name;
  String email;
  String role;

  public static AuthUserProfile from(User u) {
    return new AuthUserProfile(u.getId(), u.getName(), u.getEmail(), u.getRole().name());
  }
}

package com.example.classroom.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.classroom.dto.LoginResponse;
import com.example.classroom.model.User;
import com.example.classroom.repo.UserRepo;

@Service
public class AuthService {

  private final UserRepo userRepo;

  private final Map<String, String> sessions = new HashMap<>();

  public AuthService(UserRepo userRepo) {
    this.userRepo = userRepo;
  }

  public LoginResponse login(String email, String password) {
    User u = userRepo.findByEmail(email).filter(x -> x.getPassword().equals(password))
        .orElseThrow(() -> new IllegalArgumentException("Invalid email/password"));

    String token = UUID.randomUUID().toString();
    sessions.put(token, u.getId());

    return new LoginResponse(token, u.getId(), u.getRole().name(), u.getName());
  }

  public User requireUser(String token) {
    if (token == null || token.isBlank()) {
      throw new IllegalArgumentException("Missing token");
    }
    String userId = sessions.get(token);
    if (userId == null) {
      throw new IllegalArgumentException("Invalid token");
    }
    return userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
  }

  public void logout(String token) {
    if (token != null) {
      sessions.remove(token);
    }
  }
}

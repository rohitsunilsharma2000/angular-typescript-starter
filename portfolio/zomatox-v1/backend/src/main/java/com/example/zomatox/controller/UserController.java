package com.example.zomatox.controller;

import com.example.zomatox.dto.users.UserResponse;
import com.example.zomatox.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping
  public List<UserResponse> list() {
    return userService.listUsers().stream().map(UserResponse::from).toList();
  }

  @GetMapping("/{id}")
  public UserResponse get(@PathVariable("id") Long id) {
    return UserResponse.from(userService.getUserOrThrow(id));
  }
}

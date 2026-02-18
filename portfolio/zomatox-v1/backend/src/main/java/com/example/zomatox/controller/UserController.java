package com.example.zomatox.controller;

import com.example.zomatox.dto.users.AddressResponse;
import com.example.zomatox.dto.users.UserResponse;
import com.example.zomatox.entity.User;
import com.example.zomatox.service.UserService;
import com.example.zomatox.util.RequestContext;
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

  @GetMapping("/me/addresses")
  public List<AddressResponse> myAddresses(@RequestHeader(value = "X-User-Id", required = false) String userId) {
    User user = RequestContext.requireUser(userService, userId);
    return userService.listAddresses(user).stream().map(AddressResponse::from).toList();
  }
}

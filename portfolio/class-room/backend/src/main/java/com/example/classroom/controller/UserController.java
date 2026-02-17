package com.example.classroom.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.classroom.dto.UserCreateRequest;
import com.example.classroom.dto.UserResponse;
import com.example.classroom.model.Role;
import com.example.classroom.model.User;
import com.example.classroom.repo.UserRepo;
import com.example.classroom.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserRepo userRepo;
  private final AuthService auth;

  public UserController(UserRepo userRepo, AuthService auth) {
    this.userRepo = userRepo;
    this.auth = auth;
  }

  @GetMapping
  public List<UserResponse> list(@RequestHeader("X-Auth-Token") String token) {
    User me = auth.requireUser(token);
    if (me.getRole() != Role.PRINCIPAL) {
      throw new IllegalArgumentException("Principal only");
    }

    return userRepo.findAll().stream().map(this::toRes).toList();
  }

  @PostMapping
  public UserResponse create(@RequestHeader("X-Auth-Token") String token, @Valid @RequestBody UserCreateRequest req) {
    User me = auth.requireUser(token);
    if (me.getRole() != Role.PRINCIPAL) {
      throw new IllegalArgumentException("Principal only");
    }

    if (userRepo.findByEmail(req.getEmail()).isPresent()) {
      throw new IllegalArgumentException("Email exists");
    }

    String id = req.getRole().equals("TEACHER") ? "t" + System.nanoTime() : "s" + System.nanoTime();
    User u = User.builder().id(id).role(Role.valueOf(req.getRole())).name(req.getName()).email(req.getEmail())
        .password(req.getPassword()).classroomId(null).teacherId(null).build();

    userRepo.save(u);
    return toRes(u);
  }

  @PutMapping("/{id}")
  public UserResponse update(@RequestHeader("X-Auth-Token") String token, @PathVariable String id,
      @Valid @RequestBody UserCreateRequest req) {
    User me = auth.requireUser(token);
    if (me.getRole() != Role.PRINCIPAL) {
      throw new IllegalArgumentException("Principal only");
    }

    User u = userRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    u.setName(req.getName());
    u.setEmail(req.getEmail());
    u.setPassword(req.getPassword());
    userRepo.save(u);

    return toRes(u);
  }

  @DeleteMapping("/{id}")
  public void delete(@RequestHeader("X-Auth-Token") String token, @PathVariable String id) {
    User me = auth.requireUser(token);
    if (me.getRole() != Role.PRINCIPAL) {
      throw new IllegalArgumentException("Principal only");
    }
    if (id.equals(me.getId())) {
      throw new IllegalArgumentException("Cannot delete self");
    }

    userRepo.deleteById(id);
  }

  private UserResponse toRes(User u) {
    return new UserResponse(u.getId(), u.getRole().name(), u.getName(), u.getEmail(), u.getClassroomId(),
        u.getTeacherId());
  }
}

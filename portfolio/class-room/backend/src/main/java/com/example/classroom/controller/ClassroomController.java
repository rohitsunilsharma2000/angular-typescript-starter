package com.example.classroom.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.classroom.dto.ClassroomCreateRequest;
import com.example.classroom.dto.ClassroomResponse;
import com.example.classroom.model.Classroom;
import com.example.classroom.model.Role;
import com.example.classroom.model.User;
import com.example.classroom.repo.UserRepo;
import com.example.classroom.service.AuthService;
import com.example.classroom.service.ClassroomService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/classrooms")
@CrossOrigin
public class ClassroomController {

  private final AuthService auth;
  private final ClassroomService classroomService;
  private final UserRepo userRepo;

  public ClassroomController(AuthService auth, ClassroomService classroomService, UserRepo userRepo) {
    this.auth = auth;
    this.classroomService = classroomService;
    this.userRepo = userRepo;
  }

  @GetMapping
  public List<ClassroomResponse> list(@RequestHeader("X-Auth-Token") String token) {
    auth.requireUser(token);
    return classroomService.list().stream()
        .map(c -> new ClassroomResponse(c.getId(), c.getName(), c.getDayWindows(), c.getTeacherId())).toList();
  }

  @PostMapping
  public ClassroomResponse create(@RequestHeader("X-Auth-Token") String token,
      @Valid @RequestBody ClassroomCreateRequest req) {
    User me = auth.requireUser(token);
    if (me.getRole() != Role.PRINCIPAL) {
      throw new IllegalArgumentException("Principal only");
    }

    Classroom c = classroomService.create(req.getName(), req.getDayWindows());
    return new ClassroomResponse(c.getId(), c.getName(), c.getDayWindows(), c.getTeacherId());
  }

  @PutMapping("/{id}")
  public ClassroomResponse update(@RequestHeader("X-Auth-Token") String token, @PathVariable String id,
      @Valid @RequestBody ClassroomCreateRequest req) {
    User me = auth.requireUser(token);
    if (me.getRole() != Role.PRINCIPAL) {
      throw new IllegalArgumentException("Principal only");
    }

    Classroom c = classroomService.update(id, req.getName(), req.getDayWindows());
    return new ClassroomResponse(c.getId(), c.getName(), c.getDayWindows(), c.getTeacherId());
  }

  @DeleteMapping("/{id}")
  public void delete(@RequestHeader("X-Auth-Token") String token, @PathVariable String id) {
    User me = auth.requireUser(token);
    if (me.getRole() != Role.PRINCIPAL) {
      throw new IllegalArgumentException("Principal only");
    }

    userRepo.findAll().forEach(u -> {
      if (id.equals(u.getClassroomId())) {
        u.setClassroomId(null);
      }
      userRepo.save(u);
    });

    classroomService.delete(id);
  }
}

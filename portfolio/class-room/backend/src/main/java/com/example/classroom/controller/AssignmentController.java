package com.example.classroom.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.classroom.dto.AssignStudentRequest;
import com.example.classroom.dto.AssignTeacherRequest;
import com.example.classroom.model.Classroom;
import com.example.classroom.model.Role;
import com.example.classroom.model.User;
import com.example.classroom.repo.ClassroomRepo;
import com.example.classroom.repo.UserRepo;
import com.example.classroom.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/assign")
@CrossOrigin
public class AssignmentController {

  private final AuthService auth;
  private final UserRepo userRepo;
  private final ClassroomRepo classroomRepo;

  public AssignmentController(AuthService auth, UserRepo userRepo, ClassroomRepo classroomRepo) {
    this.auth = auth;
    this.userRepo = userRepo;
    this.classroomRepo = classroomRepo;
  }

  @PostMapping("/teacher-classroom")
  public void assignTeacher(@RequestHeader("X-Auth-Token") String token,
      @Valid @RequestBody AssignTeacherRequest req) {
    User me = auth.requireUser(token);
    if (me.getRole() != Role.PRINCIPAL) {
      throw new IllegalArgumentException("Principal only");
    }

    User t = userRepo.findById(req.getTeacherId()).orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
    if (t.getRole() != Role.TEACHER) {
      throw new IllegalArgumentException("Not a teacher");
    }

    Classroom c = classroomRepo.findById(req.getClassroomId())
        .orElseThrow(() -> new IllegalArgumentException("Classroom not found"));

    if (t.getClassroomId() != null && !t.getClassroomId().equals(c.getId())) {
      classroomRepo.findById(t.getClassroomId()).ifPresent(old -> {
        if (req.getTeacherId().equals(old.getTeacherId())) {
          old.setTeacherId(null);
          classroomRepo.save(old);
        }
      });
    }

    if (c.getTeacherId() != null && !c.getTeacherId().equals(t.getId())) {
      userRepo.findById(c.getTeacherId()).ifPresent(oldT -> {
        oldT.setClassroomId(null);
        userRepo.save(oldT);
      });
    }

    t.setClassroomId(c.getId());
    c.setTeacherId(t.getId());
    userRepo.save(t);
    classroomRepo.save(c);

    userRepo.findAll().forEach(s -> {
      if (s.getRole() == Role.STUDENT && t.getId().equals(s.getTeacherId())) {
        s.setClassroomId(c.getId());
        userRepo.save(s);
      }
    });
  }

  @PostMapping("/student-teacher")
  public void assignStudent(@RequestHeader("X-Auth-Token") String token,
      @Valid @RequestBody AssignStudentRequest req) {
    User me = auth.requireUser(token);
    if (me.getRole() != Role.PRINCIPAL) {
      throw new IllegalArgumentException("Principal only");
    }

    User s = userRepo.findById(req.getStudentId()).orElseThrow(() -> new IllegalArgumentException("Student not found"));
    if (s.getRole() != Role.STUDENT) {
      throw new IllegalArgumentException("Not a student");
    }

    User t = userRepo.findById(req.getTeacherId()).orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
    if (t.getRole() != Role.TEACHER) {
      throw new IllegalArgumentException("Not a teacher");
    }

    s.setTeacherId(t.getId());
    s.setClassroomId(t.getClassroomId());
    userRepo.save(s);
  }
}

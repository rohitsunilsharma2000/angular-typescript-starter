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

import com.example.classroom.dto.PeriodRequest;
import com.example.classroom.dto.PeriodResponse;
import com.example.classroom.dto.UserResponse;
import com.example.classroom.model.Period;
import com.example.classroom.model.Role;
import com.example.classroom.model.User;
import com.example.classroom.repo.UserRepo;
import com.example.classroom.service.AuthService;
import com.example.classroom.service.TimetableService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/timetable")
@CrossOrigin
public class TimetableController {

  private final AuthService auth;
  private final TimetableService timetable;
  private final UserRepo userRepo;

  public TimetableController(AuthService auth, TimetableService timetable, UserRepo userRepo) {
    this.auth = auth;
    this.timetable = timetable;
    this.userRepo = userRepo;
  }

  @GetMapping("/{classroomId}")
  public List<PeriodResponse> list(@RequestHeader("X-Auth-Token") String token, @PathVariable String classroomId) {
    User me = auth.requireUser(token);

    if (me.getRole() == Role.TEACHER || me.getRole() == Role.STUDENT) {
      if (me.getClassroomId() == null || !me.getClassroomId().equals(classroomId)) {
        throw new IllegalArgumentException("Not allowed");
      }
    }

    return timetable.listByClassroom(classroomId).stream().map(
        p -> new PeriodResponse(p.getId(), p.getClassroomId(), p.getDay(), p.getSubject(), p.getStartTime(), p.getEndTime()))
        .toList();
  }

  @PostMapping
  public PeriodResponse add(@RequestHeader("X-Auth-Token") String token, @Valid @RequestBody PeriodRequest req) {
    User me = auth.requireUser(token);
    if (me.getRole() == Role.STUDENT) {
      throw new IllegalArgumentException("Student cannot edit timetable");
    }

    if (me.getRole() == Role.TEACHER && (me.getClassroomId() == null || !me.getClassroomId().equals(req.getClassroomId()))) {
      throw new IllegalArgumentException("Teacher can edit only own classroom");
    }

    Period p = Period.builder().classroomId(req.getClassroomId()).day(req.getDay()).subject(req.getSubject())
        .startTime(req.getStartTime()).endTime(req.getEndTime()).build();

    Period saved = timetable.add(p);
    return new PeriodResponse(saved.getId(), saved.getClassroomId(), saved.getDay(), saved.getSubject(), saved.getStartTime(),
        saved.getEndTime());
  }

  @PutMapping("/{periodId}")
  public PeriodResponse update(@RequestHeader("X-Auth-Token") String token, @PathVariable String periodId,
      @Valid @RequestBody PeriodRequest req) {
    User me = auth.requireUser(token);
    if (me.getRole() == Role.STUDENT) {
      throw new IllegalArgumentException("Student cannot edit timetable");
    }

    if (me.getRole() == Role.TEACHER && (me.getClassroomId() == null || !me.getClassroomId().equals(req.getClassroomId()))) {
      throw new IllegalArgumentException("Teacher can edit only own classroom");
    }

    Period p = Period.builder().classroomId(req.getClassroomId()).day(req.getDay()).subject(req.getSubject())
        .startTime(req.getStartTime()).endTime(req.getEndTime()).build();

    Period saved = timetable.update(periodId, p);
    return new PeriodResponse(saved.getId(), saved.getClassroomId(), saved.getDay(), saved.getSubject(), saved.getStartTime(),
        saved.getEndTime());
  }

  @DeleteMapping("/{periodId}")
  public void delete(@RequestHeader("X-Auth-Token") String token, @PathVariable String periodId) {
    User me = auth.requireUser(token);
    if (me.getRole() == Role.STUDENT) {
      throw new IllegalArgumentException("Student cannot edit timetable");
    }
    timetable.delete(periodId);
  }

  @GetMapping("/classmates")
  public List<UserResponse> classmates(@RequestHeader("X-Auth-Token") String token) {
    User me = auth.requireUser(token);
    if (me.getRole() == Role.PRINCIPAL) {
      throw new IllegalArgumentException("Principal does not have classmates view");
    }

    if (me.getClassroomId() == null) {
      throw new IllegalArgumentException("No classroom assigned");
    }

    return userRepo.findAll().stream().filter(u -> u.getRole() == Role.STUDENT && me.getClassroomId().equals(u.getClassroomId()))
        .map(u -> new UserResponse(u.getId(), u.getRole().name(), u.getName(), u.getEmail(), u.getClassroomId(),
            u.getTeacherId()))
        .toList();
  }
}

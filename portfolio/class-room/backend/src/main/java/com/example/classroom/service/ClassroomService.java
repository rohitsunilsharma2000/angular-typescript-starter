package com.example.classroom.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.classroom.model.Classroom;
import com.example.classroom.repo.ClassroomRepo;

@Service
public class ClassroomService {

  private final ClassroomRepo classroomRepo;

  public ClassroomService(ClassroomRepo classroomRepo) {
    this.classroomRepo = classroomRepo;
  }

  public List<Classroom> list() {
    return classroomRepo.findAll();
  }

  public Classroom get(String id) {
    return classroomRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Classroom not found"));
  }

  public Classroom create(String name, String dayWindows) {
    validateDayWindows(dayWindows);
    Classroom c = Classroom.builder().id("c" + System.nanoTime()).name(name).dayWindows(dayWindows)
        .teacherId(null).build();
    return classroomRepo.save(c);
  }

  public Classroom update(String id, String name, String dayWindows) {
    validateDayWindows(dayWindows);
    Classroom c = get(id);
    c.setName(name);
    c.setDayWindows(dayWindows);
    return classroomRepo.save(c);
  }

  public void delete(String id) {
    classroomRepo.deleteById(id);
  }

  private void validateDayWindows(String dayWindows) {
    if (dayWindows == null || dayWindows.isBlank()) {
      throw new IllegalArgumentException("dayWindows required");
    }
    String[] parts = dayWindows.split(";");
    boolean any = false;
    for (String p : parts) {
      p = p.trim();
      if (p.isEmpty()) {
        continue;
      }
      if (!p.contains("=") || !p.contains("-")) {
        throw new IllegalArgumentException("Invalid dayWindows format");
      }
      String[] a = p.split("=");
      String[] b = a[1].split("-");
      if (b.length != 2) {
        throw new IllegalArgumentException("Invalid dayWindows time range");
      }
      if (!b[0].matches("^([01]\\d|2[0-3]):[0-5]\\d$")) {
        throw new IllegalArgumentException("Invalid time: " + b[0]);
      }
      if (!b[1].matches("^([01]\\d|2[0-3]):[0-5]\\d$")) {
        throw new IllegalArgumentException("Invalid time: " + b[1]);
      }
      any = true;
    }
    if (!any) {
      throw new IllegalArgumentException("At least one day window required");
    }
  }
}

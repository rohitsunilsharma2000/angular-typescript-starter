package com.example.classroom.service;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.classroom.model.Classroom;
import com.example.classroom.model.Period;
import com.example.classroom.repo.ClassroomRepo;
import com.example.classroom.repo.PeriodRepo;

@Service
public class TimetableService {

  private final ClassroomRepo classroomRepo;
  private final PeriodRepo periodRepo;

  public TimetableService(ClassroomRepo classroomRepo, PeriodRepo periodRepo) {
    this.classroomRepo = classroomRepo;
    this.periodRepo = periodRepo;
  }

  public List<Period> listByClassroom(String classroomId) {
    return periodRepo.findByClassroomId(classroomId);
  }

  public Period add(Period p) {
    Classroom c = classroomRepo.findById(p.getClassroomId())
        .orElseThrow(() -> new IllegalArgumentException("Classroom not found"));

    validatePeriod(c, null, p.getDay(), p.getStartTime(), p.getEndTime());
    p.setId("tt" + System.nanoTime());
    return periodRepo.save(p);
  }

  public Period update(String periodId, Period newP) {
    Period existing = periodRepo.findById(periodId)
        .orElseThrow(() -> new IllegalArgumentException("Period not found"));

    Classroom c = classroomRepo.findById(existing.getClassroomId())
        .orElseThrow(() -> new IllegalArgumentException("Classroom not found"));

    if (!existing.getClassroomId().equals(newP.getClassroomId())) {
      throw new IllegalArgumentException("Cannot change classroomId");
    }

    validatePeriod(c, periodId, newP.getDay(), newP.getStartTime(), newP.getEndTime());

    existing.setDay(newP.getDay());
    existing.setSubject(newP.getSubject());
    existing.setStartTime(newP.getStartTime());
    existing.setEndTime(newP.getEndTime());

    return periodRepo.save(existing);
  }

  public void delete(String periodId) {
    periodRepo.deleteById(periodId);
  }

  private void validatePeriod(Classroom c, String editingId, String day, String start, String end) {
    LocalTime s = parse(start);
    LocalTime e = parse(end);
    if (!s.isBefore(e)) {
      throw new IllegalArgumentException("Start must be before end");
    }

    Map<String, String[]> windows = parseWindows(c.getDayWindows());
    String[] w = windows.get(day);
    if (w == null) {
      throw new IllegalArgumentException("This day is not enabled in classroom");
    }

    LocalTime ws = parse(w[0]);
    LocalTime we = parse(w[1]);
    if (s.isBefore(ws) || e.isAfter(we)) {
      throw new IllegalArgumentException("Must fit within " + day + " window " + w[0] + "-" + w[1]);
    }

    List<Period> sameDay = periodRepo.findByClassroomIdAndDay(c.getId(), day);
    for (Period x : sameDay) {
      if (editingId != null && x.getId().equals(editingId)) {
        continue;
      }
      LocalTime xs = parse(x.getStartTime());
      LocalTime xe = parse(x.getEndTime());
      if (overlaps(s, e, xs, xe)) {
        throw new IllegalArgumentException(
            "Overlaps with " + x.getSubject() + " (" + x.getStartTime() + "-" + x.getEndTime() + ")");
      }
    }
  }

  private boolean overlaps(LocalTime aS, LocalTime aE, LocalTime bS, LocalTime bE) {
    return aS.isBefore(bE) && bS.isBefore(aE);
  }

  private LocalTime parse(String hhmm) {
    try {
      return LocalTime.parse(hhmm);
    } catch (Exception ex) {
      throw new IllegalArgumentException("Invalid time: " + hhmm);
    }
  }

  private Map<String, String[]> parseWindows(String dw) {
    Map<String, String[]> map = new HashMap<>();
    for (String p : dw.split(";")) {
      p = p.trim();
      if (p.isEmpty()) {
        continue;
      }
      String[] a = p.split("=");
      String day = a[0].trim();
      String[] b = a[1].trim().split("-");
      map.put(day, new String[] { b[0], b[1] });
    }
    return map;
  }
}

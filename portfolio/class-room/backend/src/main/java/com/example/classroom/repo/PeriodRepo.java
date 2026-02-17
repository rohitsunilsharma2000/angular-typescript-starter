package com.example.classroom.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.classroom.model.Period;

public interface PeriodRepo extends JpaRepository<Period, String> {
  List<Period> findByClassroomId(String classroomId);

  List<Period> findByClassroomIdAndDay(String classroomId, String day);
}

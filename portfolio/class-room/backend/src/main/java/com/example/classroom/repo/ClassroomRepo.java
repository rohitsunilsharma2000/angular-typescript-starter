package com.example.classroom.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.classroom.model.Classroom;

public interface ClassroomRepo extends JpaRepository<Classroom, String> {
}

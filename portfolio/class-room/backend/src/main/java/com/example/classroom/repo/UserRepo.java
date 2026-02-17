package com.example.classroom.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.classroom.model.User;

public interface UserRepo extends JpaRepository<User, String> {
  Optional<User> findByEmail(String email);
}

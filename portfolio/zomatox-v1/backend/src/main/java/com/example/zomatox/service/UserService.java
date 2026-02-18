package com.example.zomatox.service;

import com.example.zomatox.entity.User;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public User getUserOrThrow(Long id) {
    return userRepository.findById(id).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "User not found: " + id));
  }

  public List<User> listUsers() {
    return userRepository.findAll();
  }
}

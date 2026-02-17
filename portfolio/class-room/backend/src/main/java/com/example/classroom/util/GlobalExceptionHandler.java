package com.example.classroom.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiError> handleBad(IllegalArgumentException ex) {
    return ResponseEntity.badRequest().body(new ApiError(ex.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleAny(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiError("Server error"));
  }
}

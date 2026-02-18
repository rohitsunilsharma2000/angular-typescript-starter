package com.example.blinkit.exception;

public class ApiException extends RuntimeException {
  public ApiException(String message) {
    super(message);
  }
}

package com.example.zomatox.util;

import com.example.zomatox.exception.ApiException;
import org.springframework.http.HttpStatus;

public final class AdminAuth {
  private AdminAuth() {}

  public static void requireAdminKey(String adminKey) {
    if (adminKey == null || !adminKey.equals("dev")) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid X-Admin-Key");
    }
  }
}

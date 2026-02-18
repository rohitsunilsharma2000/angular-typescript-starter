package com.example.zomatox.util;

import com.example.zomatox.entity.User;
import com.example.zomatox.entity.enums.UserRole;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.service.UserService;
import org.springframework.http.HttpStatus;

public final class RequestContext {
  private RequestContext() {}

  public static User requireUser(UserService userService, String userIdHeader) {
    if (userIdHeader == null || userIdHeader.isBlank()) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "Missing X-User-Id header");
    }
    try {
      return userService.getUserOrThrow(Long.parseLong(userIdHeader));
    } catch (NumberFormatException ex) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid X-User-Id header");
    }
  }

  public static UserRole resolveRole(User user, String roleHeader) {
    // Use DB role as source of truth to avoid stale/client-forged role headers.
    return user.getRole();
  }

  public static void requireRole(UserRole actual, UserRole required) {
    if (actual != required) {
      throw new ApiException(HttpStatus.FORBIDDEN, "Requires role " + required + " but was " + actual);
    }
  }
}

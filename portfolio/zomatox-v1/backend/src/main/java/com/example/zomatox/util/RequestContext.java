package com.example.zomatox.util;

import com.example.zomatox.entity.User;
import com.example.zomatox.entity.enums.UserRole;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.security.AuthenticatedUser;
import com.example.zomatox.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class RequestContext {
  private RequestContext() {}

  public static User requireUser(UserService userService, String userIdHeader) {
    if (isDevProfile() && userIdHeader != null && !userIdHeader.isBlank()) {
      try {
        return userService.getUserOrThrow(Long.parseLong(userIdHeader));
      } catch (NumberFormatException ex) {
        throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid X-User-Id header");
      }
    }

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.getPrincipal() instanceof AuthenticatedUser u) {
      return userService.getUserOrThrow(u.id());
    }

    throw new ApiException(HttpStatus.UNAUTHORIZED, "Missing authentication");
  }

  public static UserRole resolveRole(User user, String roleHeader) {
    return user.getRole();
  }

  public static void requireRole(UserRole actual, UserRole required) {
    if (actual != required) {
      throw new ApiException(HttpStatus.FORBIDDEN, "Requires role " + required + " but was " + actual);
    }
  }

  private static boolean isDevProfile() {
    String active = System.getProperty("spring.profiles.active");
    if (active == null || active.isBlank()) {
      active = System.getenv("SPRING_PROFILES_ACTIVE");
    }
    if (active == null || active.isBlank()) {
      active = "dev";
    }
    return active.contains("dev");
  }
}

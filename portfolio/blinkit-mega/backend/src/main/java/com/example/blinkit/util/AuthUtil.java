package com.example.blinkit.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class AuthUtil {
  private AuthUtil() {}

  public static Long userId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return Long.parseLong(String.valueOf(auth.getPrincipal()));
  }

  public static String role() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return auth.getAuthorities().stream().findFirst().map(a -> a.getAuthority().replace("ROLE_", "")).orElse("CUSTOMER");
  }
}

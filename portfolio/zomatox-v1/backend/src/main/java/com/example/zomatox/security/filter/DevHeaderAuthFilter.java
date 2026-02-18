package com.example.zomatox.security.filter;

import com.example.zomatox.entity.User;
import com.example.zomatox.security.AuthenticatedUser;
import com.example.zomatox.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DevHeaderAuthFilter extends OncePerRequestFilter {
  private final UserService userService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {
    if (SecurityContextHolder.getContext().getAuthentication() == null) {
      String uidHeader = request.getHeader("X-User-Id");
      if (uidHeader != null && !uidHeader.isBlank()) {
        try {
          User user = userService.getUserOrThrow(Long.parseLong(uidHeader));
          var principal = new AuthenticatedUser(user.getId(), user.getEmail(), user.getRole());
          var auth = new UsernamePasswordAuthenticationToken(
            principal,
            null,
            List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
          );
          SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception ignored) {
          SecurityContextHolder.clearContext();
        }
      }
    }
    filterChain.doFilter(request, response);
  }
}

package com.example.blinkit.controller;

import com.example.blinkit.entity.Notification;
import com.example.blinkit.service.OpsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('CUSTOMER','ADMIN','RIDER','WAREHOUSE_STAFF')")
public class NotificationController {
  private final OpsService opsService;

  @GetMapping
  public List<Notification> list() {
    return opsService.notifications();
  }

  @PostMapping("/{id}/read")
  public void markRead(@PathVariable Long id) {
    opsService.readNotification(id);
  }
}

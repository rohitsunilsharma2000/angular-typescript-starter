package com.example.blinkit.controller;

import com.example.blinkit.service.StreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/stream")
@RequiredArgsConstructor
public class RealtimeController {
  private final StreamService streamService;

  @GetMapping("/orders/{orderId}")
  @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN','RIDER','WAREHOUSE_STAFF')")
  public SseEmitter orderStream(@PathVariable Long orderId) {
    return streamService.subscribeOrder(orderId);
  }

  @GetMapping("/admin/orders")
  @PreAuthorize("hasRole('ADMIN')")
  public SseEmitter adminStream() {
    return streamService.subscribeAdmin();
  }
}

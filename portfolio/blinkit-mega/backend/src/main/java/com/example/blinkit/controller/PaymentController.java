package com.example.blinkit.controller;

import com.example.blinkit.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
public class PaymentController {
  private final OrderService orderService;

  @PostMapping("/{orderId}/start")
  public void start(@PathVariable Long orderId) {
    orderService.startPayment(orderId);
  }

  @PostMapping("/{orderId}/confirm")
  public void confirm(@PathVariable Long orderId, @RequestParam String result) {
    orderService.confirmPayment(orderId, result);
  }
}

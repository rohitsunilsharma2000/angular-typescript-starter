package com.example.blinkit.controller;

import com.example.blinkit.dto.OrderDtos.*;
import com.example.blinkit.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
public class OrderController {
  private final OrderService orderService;

  @PostMapping
  @PreAuthorize("hasRole('CUSTOMER')")
  public OrderView create(@RequestHeader("Idempotency-Key") String idem,
                          @Valid @RequestBody CreateOrderRequest req) {
    return orderService.create(idem, req);
  }

  @GetMapping
  public List<OrderView> myOrders() {
    return orderService.myOrders();
  }

  @GetMapping("/{id}")
  public OrderView byId(@PathVariable Long id) {
    return orderService.getById(id);
  }

  @PostMapping("/{id}/cancel")
  public void cancel(@PathVariable Long id) {
    orderService.cancel(id);
  }

  @PostMapping("/{id}/return-request")
  @PreAuthorize("hasRole('CUSTOMER')")
  public void returnReq(@PathVariable Long id) {
    orderService.returnRequest(id);
  }
}

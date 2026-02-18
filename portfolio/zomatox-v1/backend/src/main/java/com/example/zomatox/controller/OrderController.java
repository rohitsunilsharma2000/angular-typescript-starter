package com.example.zomatox.controller;

import com.example.zomatox.dto.orders.CreateOrderRequest;
import com.example.zomatox.dto.orders.OrderResponse;
import com.example.zomatox.entity.User;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.service.OrderService;
import com.example.zomatox.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
  private final UserService userService;
  private final OrderService orderService;

  private User userFromHeader(String userIdHeader) {
    if (userIdHeader == null || userIdHeader.isBlank()) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "Missing X-User-Id header");
    }
    return userService.getUserOrThrow(Long.parseLong(userIdHeader));
  }

  @PostMapping
  public OrderResponse create(@RequestHeader(value = "X-User-Id", required = false) String userId,
                              @Valid @RequestBody CreateOrderRequest req) {
    User u = userFromHeader(userId);
    return orderService.createOrder(u, req.getAddressId());
  }

  @GetMapping
  public List<OrderResponse> list(@RequestHeader(value = "X-User-Id", required = false) String userId) {
    User u = userFromHeader(userId);
    return orderService.listOrders(u);
  }

  @GetMapping("/{id}")
  public OrderResponse get(@RequestHeader(value = "X-User-Id", required = false) String userId,
                           @PathVariable("id") Long id) {
    User u = userFromHeader(userId);
    return orderService.getOrder(u, id);
  }
}

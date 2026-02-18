package com.example.zomatox.controller;

import com.example.zomatox.dto.orders.OrderResponse;
import com.example.zomatox.entity.Order;
import com.example.zomatox.entity.User;
import com.example.zomatox.entity.enums.OrderStatus;
import com.example.zomatox.entity.enums.UserRole;
import com.example.zomatox.service.DeliveryService;
import com.example.zomatox.service.OrderService;
import com.example.zomatox.service.UserService;
import com.example.zomatox.util.RequestContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {
  private final UserService userService;
  private final DeliveryService deliveryService;
  private final OrderService orderService;

  private User deliveryFromHeaders(String userId, String roleHeader) {
    User u = RequestContext.requireUser(userService, userId);
    RequestContext.requireRole(RequestContext.resolveRole(u, roleHeader), UserRole.DELIVERY_PARTNER);
    return u;
  }

  @GetMapping("/jobs")
  public List<OrderResponse> jobs(@RequestHeader(value = "X-User-Id", required = false) String userId,
                                  @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
                                  @RequestParam("status") String status) {
    User delivery = deliveryFromHeaders(userId, roleHeader);
    List<Order> orders = "AVAILABLE".equalsIgnoreCase(status)
      ? deliveryService.availableJobs()
      : deliveryService.assignedJobs(delivery);
    return orders.stream().map(orderService::toResponse).toList();
  }

  @PostMapping("/jobs/{id}/accept")
  public OrderResponse accept(@RequestHeader(value = "X-User-Id", required = false) String userId,
                              @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
                              @PathVariable("id") Long id) {
    User delivery = deliveryFromHeaders(userId, roleHeader);
    return orderService.toResponse(deliveryService.accept(delivery, id));
  }

  @PostMapping("/orders/{id}/status")
  public OrderResponse setStatus(@RequestHeader(value = "X-User-Id", required = false) String userId,
                                 @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
                                 @PathVariable("id") Long id,
                                 @RequestParam("next") String next) {
    User delivery = deliveryFromHeaders(userId, roleHeader);
    OrderStatus status = OrderStatus.valueOf(next);
    return orderService.toResponse(deliveryService.setStatus(delivery, id, status));
  }
}

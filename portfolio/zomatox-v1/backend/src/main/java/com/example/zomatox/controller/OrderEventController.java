package com.example.zomatox.controller;

import com.example.zomatox.dto.orders.OrderEventResponse;
import com.example.zomatox.entity.Order;
import com.example.zomatox.entity.User;
import com.example.zomatox.repository.OrderEventRepository;
import com.example.zomatox.service.OrderService;
import com.example.zomatox.service.UserService;
import com.example.zomatox.util.RequestContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders/{orderId}/events")
@RequiredArgsConstructor
public class OrderEventController {
  private final UserService userService;
  private final OrderService orderService;
  private final OrderEventRepository orderEventRepository;

  @GetMapping
  public List<OrderEventResponse> list(@PathVariable("orderId") Long orderId,
                                       @RequestHeader(value = "X-User-Id", required = false) String userId) {
    User u = RequestContext.requireUser(userService, userId);
    Order o = orderService.getOrderOrThrow(orderId);
    orderService.requireCanAccess(u, o);
    return orderEventRepository.findByOrderOrderByIdAsc(o).stream().map(OrderEventResponse::from).toList();
  }
}

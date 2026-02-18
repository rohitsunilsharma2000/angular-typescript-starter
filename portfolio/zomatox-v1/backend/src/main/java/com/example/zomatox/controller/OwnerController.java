package com.example.zomatox.controller;

import com.example.zomatox.dto.admin.AdminMenuItemCreateRequest;
import com.example.zomatox.dto.admin.AdminMenuItemUpdateRequest;
import com.example.zomatox.dto.admin.AdminRestaurantCreateRequest;
import com.example.zomatox.dto.orders.OrderResponse;
import com.example.zomatox.dto.restaurants.MenuItemResponse;
import com.example.zomatox.dto.restaurants.RestaurantResponse;
import com.example.zomatox.entity.Order;
import com.example.zomatox.entity.User;
import com.example.zomatox.entity.enums.OrderStatus;
import com.example.zomatox.entity.enums.UserRole;
import com.example.zomatox.service.OrderService;
import com.example.zomatox.service.OwnerService;
import com.example.zomatox.service.UserService;
import com.example.zomatox.util.RequestContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner")
@RequiredArgsConstructor
public class OwnerController {
  private final UserService userService;
  private final OwnerService ownerService;
  private final OrderService orderService;

  private User ownerFromHeaders(String userId, String roleHeader) {
    User u = RequestContext.requireUser(userService, userId);
    RequestContext.requireRole(RequestContext.resolveRole(u, roleHeader), UserRole.OWNER);
    return u;
  }

  @GetMapping("/restaurants")
  public List<RestaurantResponse> myRestaurants(@RequestHeader(value = "X-User-Id", required = false) String userId,
                                                @RequestHeader(value = "X-User-Role", required = false) String roleHeader) {
    User owner = ownerFromHeaders(userId, roleHeader);
    return ownerService.myRestaurants(owner).stream().map(RestaurantResponse::from).toList();
  }

  @PostMapping("/restaurants")
  public RestaurantResponse createRestaurant(@RequestHeader(value = "X-User-Id", required = false) String userId,
                                             @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
                                             @Valid @RequestBody AdminRestaurantCreateRequest req) {
    User owner = ownerFromHeaders(userId, roleHeader);
    return RestaurantResponse.from(ownerService.createRestaurant(owner, req));
  }

  @PostMapping("/restaurants/{id}/menu-items")
  public MenuItemResponse addMenuItem(@RequestHeader(value = "X-User-Id", required = false) String userId,
                                      @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
                                      @PathVariable("id") Long id,
                                      @Valid @RequestBody AdminMenuItemCreateRequest req) {
    User owner = ownerFromHeaders(userId, roleHeader);
    return MenuItemResponse.from(ownerService.addMenuItem(owner, id, req));
  }

  @PutMapping("/menu-items/{id}")
  public MenuItemResponse updateMenuItem(@RequestHeader(value = "X-User-Id", required = false) String userId,
                                         @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
                                         @PathVariable("id") Long id,
                                         @Valid @RequestBody AdminMenuItemUpdateRequest req) {
    User owner = ownerFromHeaders(userId, roleHeader);
    return MenuItemResponse.from(ownerService.updateMenuItem(owner, id, req));
  }

  @GetMapping("/orders")
  public List<OrderResponse> orders(@RequestHeader(value = "X-User-Id", required = false) String userId,
                                    @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
                                    @RequestParam(value = "status", required = false) String status) {
    User owner = ownerFromHeaders(userId, roleHeader);
    OrderStatus parsed = status == null || status.isBlank() ? null : OrderStatus.valueOf(status);
    return ownerService.ownerOrders(owner, parsed).stream().map(orderService::toResponse).toList();
  }

  @PostMapping("/orders/{id}/status")
  public OrderResponse setStatus(@RequestHeader(value = "X-User-Id", required = false) String userId,
                                 @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
                                 @PathVariable("id") Long id,
                                 @RequestParam("next") String next) {
    User owner = ownerFromHeaders(userId, roleHeader);
    Order updated = ownerService.changeOrderStatus(owner, id, OrderStatus.valueOf(next));
    return orderService.toResponse(updated);
  }
}

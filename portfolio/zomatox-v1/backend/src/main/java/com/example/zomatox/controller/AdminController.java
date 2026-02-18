package com.example.zomatox.controller;

import com.example.zomatox.dto.admin.*;
import com.example.zomatox.dto.reviews.ReviewResponse;
import com.example.zomatox.dto.restaurants.MenuItemResponse;
import com.example.zomatox.dto.restaurants.RestaurantResponse;
import com.example.zomatox.entity.MenuItem;
import com.example.zomatox.entity.Restaurant;
import com.example.zomatox.entity.Review;
import com.example.zomatox.entity.User;
import com.example.zomatox.entity.enums.RestaurantApprovalStatus;
import com.example.zomatox.entity.enums.ReviewStatus;
import com.example.zomatox.entity.enums.UserRole;
import com.example.zomatox.service.AdminOpsService;
import com.example.zomatox.service.AdminService;
import com.example.zomatox.service.UserService;
import com.example.zomatox.util.RequestContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
  private final AdminService adminService;
  private final AdminOpsService adminOpsService;
  private final UserService userService;

  private void requireAdmin(String userId, String roleHeader) {
    User u = RequestContext.requireUser(userService, userId);
    RequestContext.requireRole(RequestContext.resolveRole(u, roleHeader), UserRole.ADMIN);
  }

  // backward compatible admin endpoints retained
  @PostMapping("/restaurants")
  public RestaurantResponse createRestaurant(@RequestHeader(value = "X-User-Id", required = false) String userId,
                                             @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
                                             @Valid @RequestBody AdminRestaurantCreateRequest req) {
    requireAdmin(userId, roleHeader);
    Restaurant r = adminService.createRestaurant(req);
    return RestaurantResponse.from(r);
  }

  @PostMapping("/restaurants/{id}/menu-items")
  public MenuItemResponse addMenuItem(@RequestHeader(value = "X-User-Id", required = false) String userId,
                                      @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
                                      @PathVariable("id") Long id,
                                      @Valid @RequestBody AdminMenuItemCreateRequest req) {
    requireAdmin(userId, roleHeader);
    MenuItem mi = adminService.addMenuItem(id, req);
    return MenuItemResponse.from(mi);
  }

  @PutMapping("/menu-items/{id}")
  public MenuItemResponse updateMenuItem(@RequestHeader(value = "X-User-Id", required = false) String userId,
                                         @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
                                         @PathVariable("id") Long id,
                                         @Valid @RequestBody AdminMenuItemUpdateRequest req) {
    requireAdmin(userId, roleHeader);
    MenuItem mi = adminService.updateMenuItem(id, req);
    return MenuItemResponse.from(mi);
  }

  @GetMapping("/restaurants")
  public List<RestaurantResponse> byStatus(@RequestHeader(value = "X-User-Id", required = false) String userId,
                                           @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
                                           @RequestParam("status") RestaurantApprovalStatus status) {
    requireAdmin(userId, roleHeader);
    return adminOpsService.listRestaurantsByStatus(status).stream().map(RestaurantResponse::from).toList();
  }

  @PostMapping("/restaurants/{id}/approve")
  public RestaurantResponse approve(@RequestHeader(value = "X-User-Id", required = false) String userId,
                                    @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
                                    @PathVariable("id") Long id) {
    requireAdmin(userId, roleHeader);
    return RestaurantResponse.from(adminOpsService.approveRestaurant(id));
  }

  @PostMapping("/restaurants/{id}/reject")
  public RestaurantResponse reject(@RequestHeader(value = "X-User-Id", required = false) String userId,
                                   @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
                                   @PathVariable("id") Long id) {
    requireAdmin(userId, roleHeader);
    return RestaurantResponse.from(adminOpsService.rejectRestaurant(id));
  }

  @PostMapping("/restaurants/{id}/block")
  public RestaurantResponse block(@RequestHeader(value = "X-User-Id", required = false) String userId,
                                  @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
                                  @PathVariable("id") Long id,
                                  @RequestParam(value = "reason", required = false) String reason) {
    requireAdmin(userId, roleHeader);
    return RestaurantResponse.from(adminOpsService.blockRestaurant(id, reason));
  }

  @PostMapping("/restaurants/{id}/unblock")
  public RestaurantResponse unblock(@RequestHeader(value = "X-User-Id", required = false) String userId,
                                    @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
                                    @PathVariable("id") Long id) {
    requireAdmin(userId, roleHeader);
    return RestaurantResponse.from(adminOpsService.unblockRestaurant(id));
  }

  @PostMapping("/menu-items/{id}/block")
  public MenuItemResponse blockMenuItem(@RequestHeader(value = "X-User-Id", required = false) String userId,
                                        @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
                                        @PathVariable("id") Long id) {
    requireAdmin(userId, roleHeader);
    return MenuItemResponse.from(adminOpsService.blockMenuItem(id));
  }

  @PostMapping("/menu-items/{id}/unblock")
  public MenuItemResponse unblockMenuItem(@RequestHeader(value = "X-User-Id", required = false) String userId,
                                          @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
                                          @PathVariable("id") Long id) {
    requireAdmin(userId, roleHeader);
    return MenuItemResponse.from(adminOpsService.unblockMenuItem(id));
  }

  @GetMapping("/reviews")
  public List<ReviewResponse> reviews(@RequestHeader(value = "X-User-Id", required = false) String userId,
                                      @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
                                      @RequestParam("status") ReviewStatus status) {
    requireAdmin(userId, roleHeader);
    return adminOpsService.listReviews(status).stream().map(ReviewResponse::from).toList();
  }

  @PostMapping("/reviews/{id}/hide")
  public ReviewResponse hideReview(@RequestHeader(value = "X-User-Id", required = false) String userId,
                                   @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
                                   @PathVariable("id") Long id) {
    requireAdmin(userId, roleHeader);
    Review r = adminOpsService.hideReview(id);
    return ReviewResponse.from(r);
  }

  @PostMapping("/reviews/{id}/unhide")
  public ReviewResponse unhideReview(@RequestHeader(value = "X-User-Id", required = false) String userId,
                                     @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
                                     @PathVariable("id") Long id) {
    requireAdmin(userId, roleHeader);
    Review r = adminOpsService.unhideReview(id);
    return ReviewResponse.from(r);
  }
}

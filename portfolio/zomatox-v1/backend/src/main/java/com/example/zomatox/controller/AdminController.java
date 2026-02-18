package com.example.zomatox.controller;

import com.example.zomatox.dto.admin.*;
import com.example.zomatox.dto.restaurants.MenuItemResponse;
import com.example.zomatox.dto.restaurants.RestaurantResponse;
import com.example.zomatox.entity.MenuItem;
import com.example.zomatox.entity.Restaurant;
import com.example.zomatox.service.AdminService;
import com.example.zomatox.util.AdminAuth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
  private final AdminService adminService;

  @PostMapping("/restaurants")
  public RestaurantResponse createRestaurant(@RequestHeader(value = "X-Admin-Key", required = false) String adminKey,
                                             @Valid @RequestBody AdminRestaurantCreateRequest req) {
    AdminAuth.requireAdminKey(adminKey);
    Restaurant r = adminService.createRestaurant(req);
    return RestaurantResponse.from(r);
  }

  @PostMapping("/restaurants/{id}/menu-items")
  public MenuItemResponse addMenuItem(@RequestHeader(value = "X-Admin-Key", required = false) String adminKey,
                                      @PathVariable("id") Long id,
                                      @Valid @RequestBody AdminMenuItemCreateRequest req) {
    AdminAuth.requireAdminKey(adminKey);
    MenuItem mi = adminService.addMenuItem(id, req);
    return MenuItemResponse.from(mi);
  }

  @PutMapping("/menu-items/{id}")
  public MenuItemResponse updateMenuItem(@RequestHeader(value = "X-Admin-Key", required = false) String adminKey,
                                         @PathVariable("id") Long id,
                                         @Valid @RequestBody AdminMenuItemUpdateRequest req) {
    AdminAuth.requireAdminKey(adminKey);
    MenuItem mi = adminService.updateMenuItem(id, req);
    return MenuItemResponse.from(mi);
  }
}

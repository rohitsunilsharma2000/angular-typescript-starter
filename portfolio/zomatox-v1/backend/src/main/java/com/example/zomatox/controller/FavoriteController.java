package com.example.zomatox.controller;

import com.example.zomatox.dto.restaurants.RestaurantResponse;
import com.example.zomatox.entity.User;
import com.example.zomatox.service.FavoriteRecentService;
import com.example.zomatox.service.UserService;
import com.example.zomatox.util.RequestContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FavoriteController {
  private final UserService userService;
  private final FavoriteRecentService favoriteRecentService;

  @PostMapping("/api/favorites/restaurants/{restaurantId}")
  public void addFavorite(@RequestHeader(value = "X-User-Id", required = false) String userId,
                          @PathVariable("restaurantId") Long restaurantId) {
    User user = RequestContext.requireUser(userService, userId);
    favoriteRecentService.addFavorite(user, restaurantId);
  }

  @DeleteMapping("/api/favorites/restaurants/{restaurantId}")
  public void removeFavorite(@RequestHeader(value = "X-User-Id", required = false) String userId,
                             @PathVariable("restaurantId") Long restaurantId) {
    User user = RequestContext.requireUser(userService, userId);
    favoriteRecentService.removeFavorite(user, restaurantId);
  }

  @GetMapping("/api/favorites/restaurants")
  public List<RestaurantResponse> favorites(@RequestHeader(value = "X-User-Id", required = false) String userId) {
    User user = RequestContext.requireUser(userService, userId);
    return favoriteRecentService.listFavorites(user).stream().map(RestaurantResponse::from).toList();
  }

  @PostMapping("/api/recent/restaurants/{restaurantId}")
  public void addRecent(@RequestHeader(value = "X-User-Id", required = false) String userId,
                        @PathVariable("restaurantId") Long restaurantId) {
    User user = RequestContext.requireUser(userService, userId);
    favoriteRecentService.addRecent(user, restaurantId);
  }

  @GetMapping("/api/recent/restaurants")
  public List<RestaurantResponse> recent(@RequestHeader(value = "X-User-Id", required = false) String userId,
                                         @RequestParam(value = "limit", defaultValue = "10") int limit) {
    User user = RequestContext.requireUser(userService, userId);
    return favoriteRecentService.listRecent(user, limit).stream().map(RestaurantResponse::from).toList();
  }
}

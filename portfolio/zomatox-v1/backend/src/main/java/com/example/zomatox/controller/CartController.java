package com.example.zomatox.controller;

import com.example.zomatox.dto.cart.CartItemUpsertRequest;
import com.example.zomatox.dto.cart.CartResponse;
import com.example.zomatox.entity.User;
import com.example.zomatox.service.CartService;
import com.example.zomatox.service.UserService;
import com.example.zomatox.util.RequestContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
  private final UserService userService;
  private final CartService cartService;

  @GetMapping
  public CartResponse getCart(@RequestHeader(value = "X-User-Id", required = false) String userId) {
    User u = RequestContext.requireUser(userService, userId);
    return cartService.getCart(u);
  }

  @PutMapping("/items")
  public CartResponse upsert(@RequestHeader(value = "X-User-Id", required = false) String userId,
                             @Valid @RequestBody CartItemUpsertRequest req) {
    User u = RequestContext.requireUser(userService, userId);
    return cartService.upsertItem(u, req.getMenuItemId(), req.getQty());
  }

  @DeleteMapping("/items/{menuItemId}")
  public CartResponse remove(@RequestHeader(value = "X-User-Id", required = false) String userId,
                             @PathVariable("menuItemId") Long menuItemId) {
    User u = RequestContext.requireUser(userService, userId);
    return cartService.removeItem(u, menuItemId);
  }
}

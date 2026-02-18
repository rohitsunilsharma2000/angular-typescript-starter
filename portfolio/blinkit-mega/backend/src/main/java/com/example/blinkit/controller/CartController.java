package com.example.blinkit.controller;

import com.example.blinkit.dto.CartDtos.*;
import com.example.blinkit.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class CartController {
  private final CartService cartService;

  @PutMapping("/items")
  public CartView upsert(@Valid @RequestBody UpsertCartItemRequest req) {
    return cartService.upsert(req);
  }

  @GetMapping
  public CartView get() {
    return cartService.get();
  }

  @DeleteMapping("/items/{productId}")
  public void remove(@PathVariable Long productId) {
    cartService.remove(productId);
  }
}

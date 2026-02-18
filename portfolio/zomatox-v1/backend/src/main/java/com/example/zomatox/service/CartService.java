package com.example.zomatox.service;

import com.example.zomatox.dto.cart.CartLineResponse;
import com.example.zomatox.dto.cart.CartResponse;
import com.example.zomatox.entity.*;
import com.example.zomatox.entity.enums.RestaurantApprovalStatus;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CartService {
  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;
  private final MenuItemRepository menuItemRepository;

  public Cart getOrCreateCart(User user) {
    return cartRepository.findByUser(user).orElseGet(() -> {
      Cart c = Cart.builder().user(user).updatedAt(Instant.now()).build();
      return cartRepository.save(c);
    });
  }

  public CartResponse getCart(User user) {
    Cart cart = getOrCreateCart(user);
    List<CartItem> items = cartItemRepository.findByCart(cart);

    Long restaurantId = items.isEmpty() ? null : items.get(0).getMenuItem().getRestaurant().getId();

    long total = 0;
    List<CartLineResponse> lines = items.stream().map(ci -> {
      long lineTotal = ci.getMenuItem().getPrice() * ci.getQty();
      return new CartLineResponse(ci.getMenuItem().getId(), ci.getMenuItem().getName(),
        ci.getMenuItem().getPrice(), ci.getQty(), lineTotal);
    }).toList();

    for (CartLineResponse l : lines) total += l.getLineTotal();

    return new CartResponse(cart.getId(), user.getId(), restaurantId, lines, total);
  }

  public CartResponse upsertItem(User user, Long menuItemId, int qty) {
    Cart cart = getOrCreateCart(user);
    MenuItem menuItem = menuItemRepository.findById(menuItemId).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Menu item not found: " + menuItemId));

    Restaurant restaurant = menuItem.getRestaurant();
    if (restaurant.isBlocked() || restaurant.getApprovalStatus() != RestaurantApprovalStatus.APPROVED) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Restaurant is blocked or not approved");
    }

    if (!menuItem.isAvailable() || menuItem.isBlocked()) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Item not available: " + menuItem.getName());
    }

    List<CartItem> current = cartItemRepository.findByCart(cart);
    if (!current.isEmpty()) {
      Long currentRestId = current.get(0).getMenuItem().getRestaurant().getId();
      if (!currentRestId.equals(menuItem.getRestaurant().getId())) {
        throw new ApiException(HttpStatus.BAD_REQUEST, "Cart supports only one restaurant in v1. Clear cart first.");
      }
    }

    if (qty <= 0) {
      cartItemRepository.deleteByCartAndMenuItem(cart, menuItem);
      cart.setUpdatedAt(Instant.now());
      cartRepository.save(cart);
      return getCart(user);
    }

    CartItem item = cartItemRepository.findByCartAndMenuItem(cart, menuItem)
      .orElse(CartItem.builder().cart(cart).menuItem(menuItem).qty(0).build());

    item.setQty(qty);
    cartItemRepository.save(item);

    cart.setUpdatedAt(Instant.now());
    cartRepository.save(cart);

    return getCart(user);
  }

  public CartResponse removeItem(User user, Long menuItemId) {
    return upsertItem(user, menuItemId, 0);
  }

  public List<CartItem> getRawItems(User user) {
    Cart cart = getOrCreateCart(user);
    return cartItemRepository.findByCart(cart);
  }

  public void clearCart(User user) {
    Cart cart = getOrCreateCart(user);
    List<CartItem> items = cartItemRepository.findByCart(cart);
    cartItemRepository.deleteAll(items);
    cart.setUpdatedAt(Instant.now());
    cartRepository.save(cart);
  }
}

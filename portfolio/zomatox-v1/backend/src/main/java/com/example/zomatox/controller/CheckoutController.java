package com.example.zomatox.controller;

import com.example.zomatox.dto.checkout.ApplyCouponRequest;
import com.example.zomatox.dto.checkout.PricingPreviewResponse;
import com.example.zomatox.entity.CartItem;
import com.example.zomatox.entity.Restaurant;
import com.example.zomatox.entity.User;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.service.CartService;
import com.example.zomatox.service.CouponService;
import com.example.zomatox.service.RestaurantService;
import com.example.zomatox.service.UserService;
import com.example.zomatox.util.RequestContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutController {
  private final UserService userService;
  private final RestaurantService restaurantService;
  private final CartService cartService;
  private final CouponService couponService;

  @PostMapping("/apply-coupon")
  public PricingPreviewResponse applyCoupon(@RequestHeader(value = "X-User-Id", required = false) String userId,
                                            @Valid @RequestBody ApplyCouponRequest req) {
    User user = RequestContext.requireUser(userService, userId);
    Restaurant restaurant = restaurantService.getRestaurantOrThrow(req.getRestaurantId());
    List<CartItem> cartItems = cartService.getRawItems(user);
    if (cartItems.isEmpty()) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Cart is empty");
    }
    Long cartRestaurantId = cartItems.get(0).getMenuItem().getRestaurant().getId();
    if (!cartRestaurantId.equals(restaurant.getId())) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Cart items belong to a different restaurant");
    }
    return couponService.preview(user, req.getCouponCode(), restaurant, cartItems);
  }
}

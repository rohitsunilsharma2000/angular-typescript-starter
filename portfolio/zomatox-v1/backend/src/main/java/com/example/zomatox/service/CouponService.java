package com.example.zomatox.service;

import com.example.zomatox.dto.checkout.PricingPreviewResponse;
import com.example.zomatox.entity.*;
import com.example.zomatox.entity.enums.CouponType;
import com.example.zomatox.entity.enums.RestaurantApprovalStatus;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.repository.CouponRedemptionRepository;
import com.example.zomatox.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponService {
  private final CouponRepository couponRepository;
  private final CouponRedemptionRepository couponRedemptionRepository;

  public PricingPreviewResponse preview(User user, String code, Restaurant restaurant, List<CartItem> cartItems) {
    long itemTotal = cartItems.stream().mapToLong(ci -> ci.getMenuItem().getPrice() * ci.getQty()).sum();
    long packagingFee = 0;
    long platformFee = 5;
    long deliveryFee = itemTotal >= 500 ? 0 : 40;

    Coupon coupon = findAndValidateCoupon(user, code, restaurant, itemTotal);
    long discount = calculateDiscount(coupon, itemTotal);
    long payable = Math.max(0, itemTotal + packagingFee + platformFee + deliveryFee - discount);

    return new PricingPreviewResponse(itemTotal, packagingFee, platformFee, deliveryFee, discount, payable, coupon.getCode());
  }

  public Coupon findAndValidateCoupon(User user, String code, Restaurant restaurant, long itemTotal) {
    if (restaurant.isBlocked() || restaurant.getApprovalStatus() != RestaurantApprovalStatus.APPROVED) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Restaurant not available for coupon application");
    }

    Coupon coupon = couponRepository.findByCodeIgnoreCase(code)
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Coupon not found"));

    Instant now = Instant.now();
    if (!coupon.isActive() || coupon.getValidFrom().isAfter(now) || coupon.getValidTo().isBefore(now)) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Coupon is not valid now");
    }

    if (coupon.getRestaurant() != null && !coupon.getRestaurant().getId().equals(restaurant.getId())) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Coupon not valid for this restaurant");
    }

    if (coupon.getApplicableCuisineType() != null && !coupon.getApplicableCuisineType().isBlank()
      && !coupon.getApplicableCuisineType().equalsIgnoreCase(restaurant.getCuisineType())) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Coupon not valid for this cuisine");
    }

    if (itemTotal < coupon.getMinOrder()) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Minimum order not met for coupon");
    }

    long used = couponRedemptionRepository.countByCouponAndUser(coupon, user);
    if (used >= coupon.getUsageLimitPerUser()) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Coupon usage limit reached for user");
    }

    return coupon;
  }

  public long calculateDiscount(Coupon coupon, long itemTotal) {
    long discount;
    if (coupon.getType() == CouponType.FLAT) {
      discount = coupon.getValue();
    } else {
      discount = Math.round(itemTotal * (coupon.getValue() / 100.0));
    }

    if (coupon.getMaxCap() != null) {
      discount = Math.min(discount, coupon.getMaxCap());
    }

    return Math.max(0, Math.min(discount, itemTotal));
  }

  public void markRedeemed(Coupon coupon, User user, Order order) {
    couponRedemptionRepository.save(CouponRedemption.builder()
      .coupon(coupon)
      .user(user)
      .order(order)
      .redeemedAt(Instant.now())
      .build());
  }
}

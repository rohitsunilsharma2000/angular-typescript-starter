package com.example.blinkit.controller;

import com.example.blinkit.dto.CheckoutDtos.*;
import com.example.blinkit.service.CheckoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class CheckoutController {
  private final CheckoutService checkoutService;

  @PostMapping("/preview")
  public CheckoutPreviewResponse preview(@Valid @RequestBody CheckoutPreviewRequest req) {
    return checkoutService.preview(req);
  }

  @PostMapping("/apply-coupon")
  public CheckoutPreviewResponse applyCoupon(@Valid @RequestBody ApplyCouponRequest req) {
    return checkoutService.applyCoupon(req);
  }

  @PostMapping("/use-wallet")
  public CheckoutPreviewResponse useWallet(@Valid @RequestBody UseWalletRequest req) {
    return checkoutService.useWallet(req);
  }
}

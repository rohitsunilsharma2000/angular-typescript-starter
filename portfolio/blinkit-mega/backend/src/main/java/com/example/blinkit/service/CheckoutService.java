package com.example.blinkit.service;

import com.example.blinkit.dto.CartDtos.CartView;
import com.example.blinkit.dto.CheckoutDtos.*;
import com.example.blinkit.entity.*;
import com.example.blinkit.exception.ApiException;
import com.example.blinkit.repository.*;
import com.example.blinkit.util.AuthUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckoutService {
  private final CartService cartService;
  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;
  private final ProductRepository productRepository;
  private final StoreInventoryRepository storeInventoryRepository;
  private final InventoryReservationRepository reservationRepository;
  private final ReservationItemRepository reservationItemRepository;
  private final CouponRepository couponRepository;
  private final WalletRepository walletRepository;

  @Transactional
  public CheckoutPreviewResponse preview(CheckoutPreviewRequest req) {
    for (int attempt = 0; attempt < 3; attempt++) {
      try {
        return reserveInternal(req);
      } catch (ObjectOptimisticLockingFailureException ex) {
        if (attempt == 2) {
          throw new ApiException("Could not reserve stock due to concurrency");
        }
      }
    }
    throw new ApiException("Reservation retry exhausted");
  }

  private CheckoutPreviewResponse reserveInternal(CheckoutPreviewRequest req) {
    Long userId = AuthUtil.userId();
    Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new ApiException("Cart not found"));
    if (!req.storeId().equals(cart.getStoreId())) {
      throw new ApiException("Cart store mismatch");
    }
    List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
    if (items.isEmpty()) {
      throw new ApiException("Cart is empty");
    }

    BigDecimal subtotal = BigDecimal.ZERO;
    List<ReservationItem> reservationItems = new ArrayList<>();
    for (CartItem item : items) {
      Product p = productRepository.findById(item.getProductId()).orElseThrow(() -> new ApiException("Product not found"));
      StoreInventory inv = storeInventoryRepository.findByStoreIdAndProductId(req.storeId(), item.getProductId())
          .orElseThrow(() -> new ApiException("Inventory not found"));
      int available = inv.getStockOnHand() - inv.getReservedQty();
      if (available < item.getQty()) {
        throw new ApiException("Insufficient stock for product " + item.getProductId());
      }
      inv.setReservedQty(inv.getReservedQty() + item.getQty());
      inv.setUpdatedAt(LocalDateTime.now());
      storeInventoryRepository.save(inv);
      subtotal = subtotal.add(p.getPrice().multiply(BigDecimal.valueOf(item.getQty())));
      reservationItems.add(ReservationItem.builder().productId(item.getProductId()).qty(item.getQty()).unitPrice(p.getPrice()).build());
    }

    InventoryReservation reservation = reservationRepository.save(InventoryReservation.builder()
        .userId(userId)
        .storeId(req.storeId())
        .status(ReservationStatus.ACTIVE)
        .createdAt(LocalDateTime.now())
        .expiresAt(LocalDateTime.now().plusMinutes(10))
        .build());
    reservationItems.forEach(i -> i.setReservationId(reservation.getId()));
    reservationItemRepository.saveAll(reservationItems);

    BigDecimal deliveryFee = BigDecimal.valueOf(25);
    BigDecimal total = subtotal.add(deliveryFee);
    return new CheckoutPreviewResponse(reservation.getId(), reservation.getExpiresAt(), subtotal, BigDecimal.ZERO, deliveryFee, total);
  }

  public CheckoutPreviewResponse applyCoupon(ApplyCouponRequest req) {
    CartView cart = cartService.get();
    Coupon coupon = couponRepository.findByCodeAndActiveTrue(req.couponCode()).orElseThrow(() -> new ApiException("Invalid coupon"));
    if (cart.subtotal().compareTo(coupon.getMinCartValue()) < 0) {
      throw new ApiException("Cart value too low for coupon");
    }
    BigDecimal discount = coupon.getDiscountType().equalsIgnoreCase("PERCENT")
        ? cart.subtotal().multiply(coupon.getDiscountValue()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
        : coupon.getDiscountValue();
    if (discount.compareTo(coupon.getMaxDiscount()) > 0) {
      discount = coupon.getMaxDiscount();
    }
    BigDecimal deliveryFee = BigDecimal.valueOf(25);
    return new CheckoutPreviewResponse(null, null, cart.subtotal(), discount, deliveryFee, cart.subtotal().add(deliveryFee).subtract(discount));
  }

  public CheckoutPreviewResponse useWallet(UseWalletRequest req) {
    CartView cart = cartService.get();
    Wallet wallet = walletRepository.findByUserId(AuthUtil.userId()).orElseThrow(() -> new ApiException("Wallet not found"));
    if (wallet.getBalance().compareTo(req.amount()) < 0) {
      throw new ApiException("Insufficient wallet balance");
    }
    BigDecimal deliveryFee = BigDecimal.valueOf(25);
    BigDecimal total = cart.subtotal().add(deliveryFee).subtract(req.amount());
    if (total.compareTo(BigDecimal.ZERO) < 0) {
      total = BigDecimal.ZERO;
    }
    return new CheckoutPreviewResponse(null, null, cart.subtotal(), BigDecimal.ZERO, deliveryFee, total);
  }

  @Transactional
  @Scheduled(fixedDelay = 60000)
  public void expireReservations() {
    List<InventoryReservation> expired = reservationRepository.findByStatusAndExpiresAtBefore(ReservationStatus.ACTIVE, LocalDateTime.now());
    for (InventoryReservation reservation : expired) {
      List<ReservationItem> items = reservationItemRepository.findByReservationId(reservation.getId());
      for (ReservationItem item : items) {
        storeInventoryRepository.findByStoreIdAndProductId(reservation.getStoreId(), item.getProductId()).ifPresent(inv -> {
          inv.setReservedQty(Math.max(0, inv.getReservedQty() - item.getQty()));
          inv.setUpdatedAt(LocalDateTime.now());
          storeInventoryRepository.save(inv);
        });
      }
      reservation.setStatus(ReservationStatus.EXPIRED);
      reservationRepository.save(reservation);
    }
  }
}

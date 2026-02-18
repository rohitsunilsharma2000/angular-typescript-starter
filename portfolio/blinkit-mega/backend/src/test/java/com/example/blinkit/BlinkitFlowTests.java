package com.example.blinkit;

import static org.junit.jupiter.api.Assertions.*;

import com.example.blinkit.dto.CartDtos.UpsertCartItemRequest;
import com.example.blinkit.dto.CheckoutDtos.ApplyCouponRequest;
import com.example.blinkit.dto.CheckoutDtos.CheckoutPreviewRequest;
import com.example.blinkit.dto.OrderDtos.CreateOrderRequest;
import com.example.blinkit.entity.*;
import com.example.blinkit.exception.ApiException;
import com.example.blinkit.repository.*;
import com.example.blinkit.service.CartService;
import com.example.blinkit.service.CheckoutService;
import com.example.blinkit.service.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootTest
class BlinkitFlowTests {
  @Autowired CartService cartService;
  @Autowired CheckoutService checkoutService;
  @Autowired OrderService orderService;
  @Autowired StoreInventoryRepository inventoryRepository;
  @Autowired RefundRepository refundRepository;
  @Autowired OrderRepository orderRepository;
  @Autowired WalletRepository walletRepository;

  @BeforeEach
  void setup() {
    auth(2L, "CUSTOMER");
  }

  @Test
  void reservationConcurrency() throws Exception {
    StoreInventory inv = inventoryRepository.findByStoreIdAndProductId(1L, 1L).orElseThrow();
    inv.setStockOnHand(1);
    inv.setReservedQty(0);
    inventoryRepository.save(inv);

    cartService.upsert(new UpsertCartItemRequest(1L, 1L, 1));

    ExecutorService es = Executors.newFixedThreadPool(2);
    Callable<Boolean> task = () -> {
      auth(2L, "CUSTOMER");
      try {
        checkoutService.preview(new CheckoutPreviewRequest(1L));
        return true;
      } catch (ApiException ex) {
        return false;
      }
    };

    List<Future<Boolean>> results = es.invokeAll(List.of(task, task));
    long success = results.stream().filter(f -> {
      try { return f.get(); } catch (Exception e) { return false; }
    }).count();
    assertEquals(1, success);
    es.shutdownNow();
  }

  @Test
  void idempotencyReturnsSameOrder() {
    cartService.upsert(new UpsertCartItemRequest(2L, 1L, 1));
    Long reservationId = checkoutService.preview(new CheckoutPreviewRequest(1L)).reservationId();
    CreateOrderRequest req = new CreateOrderRequest(reservationId, null, BigDecimal.ZERO);

    Long first = orderService.create("idem-1", req).id();
    Long second = orderService.create("idem-1", req).id();
    assertEquals(first, second);
  }

  @Test
  void couponValidationRejectsLowCart() {
    cartService.upsert(new UpsertCartItemRequest(3L, 1L, 1));
    assertThrows(ApiException.class, () -> checkoutService.applyCoupon(new ApplyCouponRequest("BIGSAVE200", 1L)));
  }

  @Test
  void partialRefundCreditsWalletOnApproval() {
    Order order = orderRepository.save(Order.builder().userId(2L).storeId(1L).status(OrderStatus.PICKING)
        .totalAmount(BigDecimal.valueOf(200)).deliveryFee(BigDecimal.ZERO).discountAmount(BigDecimal.ZERO)
        .walletUsed(BigDecimal.ZERO).createdAt(LocalDateTime.now()).build());
    Refund refund = refundRepository.save(Refund.builder().orderId(order.getId()).amount(BigDecimal.valueOf(20))
        .destination("WALLET").status(RefundStatus.PENDING).createdAt(LocalDateTime.now()).build());
    BigDecimal before = walletRepository.findByUserId(2L).orElseThrow().getBalance();
    orderService.approveRefund(refund.getId());
    BigDecimal after = walletRepository.findByUserId(2L).orElseThrow().getBalance();
    assertEquals(0, after.compareTo(before.add(BigDecimal.valueOf(20))));
  }

  @Test
  void roleAccessValidationForCancel() {
    auth(2L, "CUSTOMER");
    Order order = orderRepository.save(Order.builder().userId(2L).storeId(1L).status(OrderStatus.PACKED)
        .totalAmount(BigDecimal.valueOf(100)).deliveryFee(BigDecimal.ZERO).discountAmount(BigDecimal.ZERO)
        .walletUsed(BigDecimal.ZERO).createdAt(LocalDateTime.now()).build());
    assertThrows(ApiException.class, () -> orderService.cancel(order.getId()));

    auth(1L, "ADMIN");
    assertDoesNotThrow(() -> orderService.cancel(order.getId()));
  }

  private void auth(Long userId, String role) {
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(String.valueOf(userId), null, List.of(() -> "ROLE_" + role)));
  }
}

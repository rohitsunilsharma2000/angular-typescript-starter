package com.example.blinkit.config;

import com.example.blinkit.entity.*;
import com.example.blinkit.repository.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeedDataConfig implements CommandLineRunner {
  private final UserRepository userRepository;
  private final CategoryRepository categoryRepository;
  private final ProductRepository productRepository;
  private final StoreRepository storeRepository;
  private final StoreInventoryRepository storeInventoryRepository;
  private final CouponRepository couponRepository;
  private final WalletRepository walletRepository;
  private final ReferralRepository referralRepository;
  private final RiderRepository riderRepository;
  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;
  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final PaymentRepository paymentRepository;
  private final RefundRepository refundRepository;
  private final NotificationRepository notificationRepository;
  private final DeliveryEventRepository deliveryEventRepository;
  private final DeliveryBatchRepository deliveryBatchRepository;
  private final DeliveryAssignmentRepository deliveryAssignmentRepository;
  private final PickingTaskRepository pickingTaskRepository;
  private final PickingTaskItemRepository pickingTaskItemRepository;
  private final InventoryReservationRepository inventoryReservationRepository;
  private final ReservationItemRepository reservationItemRepository;
  private final SeedMapStore seedMapStore;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) {
    if (userRepository.count() > 0) {
      return;
    }

    User admin = userRepository.save(User.builder().name("Admin").email("admin@blinkit.com").passwordHash(passwordEncoder.encode("admin123")).role(Role.ADMIN).pincode("700001").createdAt(LocalDateTime.now()).build());
    User customer = userRepository.save(User.builder().name("Customer").email("user@blinkit.com").passwordHash(passwordEncoder.encode("user123")).role(Role.CUSTOMER).pincode("700001").createdAt(LocalDateTime.now()).build());
    User riderUser = userRepository.save(User.builder().name("Rider").email("rider@blinkit.com").passwordHash(passwordEncoder.encode("rider123")).role(Role.RIDER).pincode("700001").createdAt(LocalDateTime.now()).build());
    User warehouse = userRepository.save(User.builder().name("Warehouse").email("warehouse@blinkit.com").passwordHash(passwordEncoder.encode("warehouse123")).role(Role.WAREHOUSE_STAFF).pincode("700001").createdAt(LocalDateTime.now()).build());

    riderRepository.save(Rider.builder().userId(riderUser.getId()).capacity(5).active(true).build());

    String[] cats = {"Fruits", "Vegetables", "Dairy", "Snacks", "Beverages", "Bakery", "Home Care", "Personal Care"};
    List<Category> categoryList = new ArrayList<>();
    for (String c : cats) {
      categoryList.add(categoryRepository.save(Category.builder().name(c).active(true).build()));
    }

    List<Product> products = new ArrayList<>();
    for (int i = 1; i <= 80; i++) {
      Category category = categoryList.get((i - 1) % categoryList.size());
      products.add(productRepository.save(Product.builder()
          .categoryId(category.getId())
          .name("Product " + i)
          .description("BlinkIt item " + i)
          .price(BigDecimal.valueOf(20 + i))
          .active(true)
          .build()));
    }

    Store s1 = storeRepository.save(Store.builder().name("Store Salt Lake").pincode("700091").active(true).build());
    Store s2 = storeRepository.save(Store.builder().name("Store New Town").pincode("700156").active(true).build());
    Store s3 = storeRepository.save(Store.builder().name("Store Park Street").pincode("700016").active(true).build());

    for (Store s : List.of(s1, s2, s3)) {
      int idx = 0;
      for (Product p : products) {
        storeInventoryRepository.save(StoreInventory.builder()
            .storeId(s.getId())
            .productId(p.getId())
            .stockOnHand(idx % 9 == 0 ? 3 : 40)
            .reservedQty(0)
            .updatedAt(LocalDateTime.now())
            .build());
        idx++;
      }
    }

    couponRepository.save(Coupon.builder().code("WELCOME50").discountType("FLAT").discountValue(BigDecimal.valueOf(50)).minCartValue(BigDecimal.valueOf(299)).maxDiscount(BigDecimal.valueOf(50)).active(true).build());
    couponRepository.save(Coupon.builder().code("FRUITS10").discountType("PERCENT").discountValue(BigDecimal.valueOf(10)).minCartValue(BigDecimal.valueOf(199)).maxDiscount(BigDecimal.valueOf(120)).categoryId(categoryList.get(0).getId()).active(true).build());
    couponRepository.save(Coupon.builder().code("BIGSAVE200").discountType("FLAT").discountValue(BigDecimal.valueOf(200)).minCartValue(BigDecimal.valueOf(999)).maxDiscount(BigDecimal.valueOf(200)).active(true).build());

    walletRepository.save(Wallet.builder().userId(customer.getId()).balance(BigDecimal.valueOf(250)).build());
    referralRepository.save(Referral.builder().referrerUserId(customer.getId()).referredUserId(riderUser.getId()).rewardCredited(false).createdAt(LocalDateTime.now()).build());

    seedTestData(customer, admin, riderUser, warehouse, s1, products);
  }

  private void seedTestData(User customer, User admin, User riderUser, User warehouseUser, Store store, List<Product> products) {
    LocalDateTime now = LocalDateTime.now();
    Long riderId = riderRepository.findByUserId(riderUser.getId()).map(Rider::getId).orElse(null);

    Cart cart = cartRepository.save(Cart.builder()
        .userId(customer.getId())
        .storeId(store.getId())
        .updatedAt(now)
        .build());
    cartItemRepository.save(CartItem.builder().cartId(cart.getId()).productId(products.get(0).getId()).qty(2).build());
    cartItemRepository.save(CartItem.builder().cartId(cart.getId()).productId(products.get(1).getId()).qty(1).build());

    InventoryReservation reservation = inventoryReservationRepository.save(InventoryReservation.builder()
        .userId(customer.getId())
        .storeId(store.getId())
        .status(ReservationStatus.ACTIVE)
        .createdAt(now)
        .expiresAt(now.plusMinutes(10))
        .build());
    reservationItemRepository.save(ReservationItem.builder()
        .reservationId(reservation.getId())
        .productId(products.get(0).getId())
        .qty(1)
        .unitPrice(products.get(0).getPrice())
        .build());

    Order paidOrder = orderRepository.save(Order.builder()
        .userId(customer.getId())
        .storeId(store.getId())
        .status(OrderStatus.PAID)
        .totalAmount(BigDecimal.valueOf(182))
        .deliveryFee(BigDecimal.valueOf(25))
        .discountAmount(BigDecimal.ZERO)
        .walletUsed(BigDecimal.ZERO)
        .createdAt(now.minusHours(2))
        .riderId(riderId)
        .build());
    orderItemRepository.save(OrderItem.builder()
        .orderId(paidOrder.getId())
        .productId(products.get(2).getId())
        .productName(products.get(2).getName())
        .qty(2)
        .price(products.get(2).getPrice())
        .refundedQty(0)
        .build());
    paymentRepository.save(Payment.builder()
        .orderId(paidOrder.getId())
        .status(PaymentStatus.SUCCESS)
        .providerRef("MOCK-PAY-SUCCESS-1")
        .createdAt(now.minusHours(2))
        .build());

    Order pickingOrder = orderRepository.save(Order.builder()
        .userId(customer.getId())
        .storeId(store.getId())
        .status(OrderStatus.PICKING)
        .totalAmount(BigDecimal.valueOf(141))
        .deliveryFee(BigDecimal.valueOf(25))
        .discountAmount(BigDecimal.ZERO)
        .walletUsed(BigDecimal.ZERO)
        .createdAt(now.minusMinutes(40))
        .riderId(riderId)
        .build());
    OrderItem pickingItem = orderItemRepository.save(OrderItem.builder()
        .orderId(pickingOrder.getId())
        .productId(products.get(3).getId())
        .productName(products.get(3).getName())
        .qty(2)
        .price(products.get(3).getPrice())
        .refundedQty(1)
        .build());
    paymentRepository.save(Payment.builder()
        .orderId(pickingOrder.getId())
        .status(PaymentStatus.SUCCESS)
        .providerRef("MOCK-PAY-SUCCESS-2")
        .createdAt(now.minusMinutes(39))
        .build());
    Refund pendingRefund = refundRepository.save(Refund.builder()
        .orderId(pickingOrder.getId())
        .orderItemId(pickingItem.getId())
        .amount(pickingItem.getPrice())
        .status(RefundStatus.PENDING)
        .destination("WALLET")
        .createdAt(now.minusMinutes(10))
        .build());

    PickingTask pickingTask = pickingTaskRepository.save(PickingTask.builder()
        .orderId(pickingOrder.getId())
        .assignedToUserId(warehouseUser.getId())
        .status("PICKING")
        .createdAt(now.minusMinutes(35))
        .build());
    pickingTaskItemRepository.save(PickingTaskItem.builder()
        .pickingTaskId(pickingTask.getId())
        .orderItemId(pickingItem.getId())
        .missingQty(1)
        .pickedQty(1)
        .build());

    Order deliveredOrder = orderRepository.save(Order.builder()
        .userId(customer.getId())
        .storeId(store.getId())
        .status(OrderStatus.DELIVERED)
        .totalAmount(BigDecimal.valueOf(225))
        .deliveryFee(BigDecimal.valueOf(25))
        .discountAmount(BigDecimal.valueOf(20))
        .walletUsed(BigDecimal.valueOf(30))
        .createdAt(now.minusHours(1))
        .deliveredAt(now.minusMinutes(30))
        .riderId(riderId)
        .build());
    orderItemRepository.save(OrderItem.builder()
        .orderId(deliveredOrder.getId())
        .productId(products.get(4).getId())
        .productName(products.get(4).getName())
        .qty(3)
        .price(products.get(4).getPrice())
        .refundedQty(0)
        .build());
    paymentRepository.save(Payment.builder()
        .orderId(deliveredOrder.getId())
        .status(PaymentStatus.SUCCESS)
        .providerRef("MOCK-PAY-SUCCESS-3")
        .createdAt(now.minusHours(1))
        .build());

    deliveryEventRepository.save(DeliveryEvent.builder().orderId(deliveredOrder.getId()).eventType("PACKED").message("Packed by warehouse").createdAt(now.minusMinutes(55)).build());
    deliveryEventRepository.save(DeliveryEvent.builder().orderId(deliveredOrder.getId()).eventType("OUT_FOR_DELIVERY").message("Rider started route").createdAt(now.minusMinutes(45)).build());
    deliveryEventRepository.save(DeliveryEvent.builder().orderId(deliveredOrder.getId()).eventType("DELIVERED").message("Delivered successfully").createdAt(now.minusMinutes(30)).build());

    DeliveryBatch batch = deliveryBatchRepository.save(DeliveryBatch.builder()
        .riderId(riderId)
        .status("ACTIVE")
        .createdAt(now.minusMinutes(20))
        .build());
    deliveryAssignmentRepository.save(DeliveryAssignment.builder()
        .batchId(batch.getId())
        .orderId(pickingOrder.getId())
        .accepted(false)
        .build());

    Notification customerNotification = notificationRepository.save(Notification.builder().userId(customer.getId()).title("Welcome to BlinkIt Mega").body("Try checkout preview and place order with seeded cart.").readFlag(false).createdAt(now.minusMinutes(20)).build());
    notificationRepository.save(Notification.builder().userId(admin.getId()).title("Refund Pending").body("A refund is waiting for approval in admin panel.").readFlag(false).createdAt(now.minusMinutes(8)).build());
    notificationRepository.save(Notification.builder().userId(riderUser.getId()).title("Ready for Delivery Updates").body("Use rider status APIs to advance seeded orders.").readFlag(false).createdAt(now.minusMinutes(5)).build());

    Map<String, Object> ids = new LinkedHashMap<>();
    ids.put("customerUserId", customer.getId());
    ids.put("adminUserId", admin.getId());
    ids.put("riderUserId", riderUser.getId());
    ids.put("warehouseUserId", warehouseUser.getId());
    ids.put("storeId", store.getId());
    ids.put("reservationId", reservation.getId());
    ids.put("paidOrderId", paidOrder.getId());
    ids.put("pickingOrderId", pickingOrder.getId());
    ids.put("deliveredOrderId", deliveredOrder.getId());
    ids.put("refundId", pendingRefund.getId());
    ids.put("batchId", batch.getId());
    ids.put("notificationId", customerNotification.getId());
    ids.put("sampleOrderItemIdForMissing", pickingItem.getId());

    Map<String, Object> payloads = new LinkedHashMap<>();
    payloads.put("POST /api/auth/signup", Map.of("name", "Demo User", "email", "demo.user@blinkit.com", "password", "demo123", "role", "CUSTOMER", "pincode", "700091"));
    payloads.put("POST /api/auth/login", Map.of("email", "user@blinkit.com", "password", "user123"));
    payloads.put("POST /api/auth/refresh", Map.of("refreshToken", "<REFRESH_TOKEN_FROM_LOGIN>"));
    payloads.put("POST /api/auth/logout", Map.of("refreshToken", "<REFRESH_TOKEN_FROM_LOGIN>"));
    payloads.put("POST /api/checkout/preview", Map.of("storeId", store.getId()));
    payloads.put("POST /api/checkout/apply-coupon", Map.of("couponCode", "WELCOME50", "storeId", store.getId()));
    payloads.put("POST /api/checkout/use-wallet", Map.of("storeId", store.getId(), "amount", 50));
    payloads.put("POST /api/orders", Map.of("reservationId", reservation.getId(), "couponCode", "WELCOME50", "walletAmount", 0));
    payloads.put("POST /api/orders/{id}/cancel", Map.of("orderId", pickingOrder.getId()));
    payloads.put("POST /api/orders/{id}/return-request", Map.of("orderId", deliveredOrder.getId()));
    payloads.put("POST /api/payments/{orderId}/start", Map.of("orderId", paidOrder.getId()));
    payloads.put("POST /api/payments/{orderId}/confirm?result=SUCCESS", Map.of("orderId", paidOrder.getId()));
    payloads.put("POST /api/admin/products", Map.of("categoryId", 1, "name", "Seeded Demo Product", "description", "For API test", "price", 99, "active", true));
    payloads.put("POST /api/admin/orders/{id}/assign-rider", Map.of("orderId", pickingOrder.getId(), "riderUserId", riderUser.getId()));
    payloads.put("POST /api/admin/orders/{id}/advance-status", Map.of("orderId", paidOrder.getId(), "nextStatus", "CONFIRMED"));
    payloads.put("POST /api/admin/refunds/{refundId}/approve", Map.of("refundId", pendingRefund.getId()));
    payloads.put("POST /api/warehouse/orders/{id}/mark-missing", Map.of("orderId", pickingOrder.getId(), "orderItemId", pickingItem.getId(), "missingQty", 1));
    payloads.put("POST /api/warehouse/orders/{id}/packed", Map.of("orderId", pickingOrder.getId()));
    payloads.put("POST /api/rider/orders/{id}/accept", Map.of("orderId", pickingOrder.getId()));
    payloads.put("POST /api/rider/orders/{id}/status", Map.of("orderId", pickingOrder.getId(), "eventType", "OUT_FOR_DELIVERY", "message", "Rider started trip"));
    payloads.put("POST /api/notifications/{id}/read", Map.of("notificationId", customerNotification.getId()));

    seedMapStore.put("seededIds", ids);
    seedMapStore.put("postApiPayloads", payloads);
  }
}

package com.example.blinkit.service;

import com.example.blinkit.dto.OrderDtos.*;
import com.example.blinkit.entity.*;
import com.example.blinkit.exception.ApiException;
import com.example.blinkit.repository.*;
import com.example.blinkit.util.AuthUtil;
import com.example.blinkit.util.HashUtil;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
  private static final Map<OrderStatus, EnumSet<OrderStatus>> FLOW = Map.of(
      OrderStatus.CREATED, EnumSet.of(OrderStatus.PAYMENT_PENDING),
      OrderStatus.PAYMENT_PENDING, EnumSet.of(OrderStatus.PAID),
      OrderStatus.PAID, EnumSet.of(OrderStatus.CONFIRMED),
      OrderStatus.CONFIRMED, EnumSet.of(OrderStatus.PICKING),
      OrderStatus.PICKING, EnumSet.of(OrderStatus.PACKED),
      OrderStatus.PACKED, EnumSet.of(OrderStatus.PICKED_UP),
      OrderStatus.PICKED_UP, EnumSet.of(OrderStatus.OUT_FOR_DELIVERY),
      OrderStatus.OUT_FOR_DELIVERY, EnumSet.of(OrderStatus.DELIVERED)
  );

  private final InventoryReservationRepository reservationRepository;
  private final ReservationItemRepository reservationItemRepository;
  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final IdempotencyKeyRepository idempotencyKeyRepository;
  private final PaymentRepository paymentRepository;
  private final WalletRepository walletRepository;
  private final WalletTxnRepository walletTxnRepository;
  private final RefundRepository refundRepository;
  private final DeliveryEventRepository deliveryEventRepository;

  @Transactional
  public OrderView create(String idemKeyHeader, CreateOrderRequest req) {
    Long userId = AuthUtil.userId();
    if (idemKeyHeader == null || idemKeyHeader.isBlank()) {
      throw new ApiException("Idempotency-Key header is required");
    }

    String payloadHash = HashUtil.sha256(req.reservationId() + ":" + String.valueOf(req.couponCode()) + ":" + String.valueOf(req.walletAmount()));
    IdempotencyKey idem = idempotencyKeyRepository.findByUserIdAndIdemKey(userId, idemKeyHeader).orElse(null);
    if (idem != null) {
      if (!idem.getPayloadHash().equals(payloadHash)) {
        throw new ApiException("Idempotency key reused with different payload");
      }
      Order existing = orderRepository.findById(idem.getOrderId()).orElseThrow(() -> new ApiException("Order not found"));
      return view(existing);
    }

    InventoryReservation reservation = reservationRepository.findById(req.reservationId())
        .orElseThrow(() -> new ApiException("Reservation not found"));
    if (!reservation.getUserId().equals(userId)) {
      throw new ApiException("Reservation ownership mismatch");
    }
    if (reservation.getStatus() != ReservationStatus.ACTIVE || reservation.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new ApiException("Reservation is expired or used");
    }

    List<ReservationItem> items = reservationItemRepository.findByReservationId(reservation.getId());
    BigDecimal subtotal = items.stream().map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQty()))).reduce(BigDecimal.ZERO, BigDecimal::add);
    BigDecimal deliveryFee = BigDecimal.valueOf(25);
    BigDecimal total = subtotal.add(deliveryFee);

    Order order = orderRepository.save(Order.builder()
        .userId(userId)
        .storeId(reservation.getStoreId())
        .status(OrderStatus.CREATED)
        .totalAmount(total)
        .deliveryFee(deliveryFee)
        .discountAmount(BigDecimal.ZERO)
        .walletUsed(req.walletAmount() == null ? BigDecimal.ZERO : req.walletAmount())
        .createdAt(LocalDateTime.now())
        .reservationId(reservation.getId())
        .build());

    for (ReservationItem item : items) {
      orderItemRepository.save(OrderItem.builder()
          .orderId(order.getId())
          .productId(item.getProductId())
          .productName("Product-" + item.getProductId())
          .qty(item.getQty())
          .price(item.getUnitPrice())
          .refundedQty(0)
          .build());
    }

    reservation.setStatus(ReservationStatus.USED);
    reservationRepository.save(reservation);

    idempotencyKeyRepository.save(IdempotencyKey.builder()
        .userId(userId)
        .idemKey(idemKeyHeader)
        .payloadHash(payloadHash)
        .orderId(order.getId())
        .createdAt(LocalDateTime.now())
        .build());

    paymentRepository.save(Payment.builder().orderId(order.getId()).status(PaymentStatus.PENDING).createdAt(LocalDateTime.now()).build());
    pushEvent(order.getId(), "ORDER_CREATED", "Order created");
    return view(order);
  }

  public List<OrderView> myOrders() {
    return orderRepository.findByUserIdOrderByIdDesc(AuthUtil.userId()).stream().map(this::view).toList();
  }

  public OrderView getById(Long id) {
    Order order = orderRepository.findById(id).orElseThrow(() -> new ApiException("Order not found"));
    if (!AuthUtil.role().equals("ADMIN") && !order.getUserId().equals(AuthUtil.userId())) {
      throw new ApiException("Forbidden");
    }
    return view(order);
  }

  @Transactional
  public void cancel(Long id) {
    Order order = orderRepository.findById(id).orElseThrow(() -> new ApiException("Order not found"));
    boolean admin = AuthUtil.role().equals("ADMIN");
    if (order.getStatus().ordinal() >= OrderStatus.PICKING.ordinal() && !admin) {
      throw new ApiException("Customer cancel allowed till PICKING only");
    }
    if (order.getStatus().ordinal() >= OrderStatus.PACKED.ordinal() && !admin) {
      throw new ApiException("After PACKED admin-only cancel");
    }
    order.setStatus(OrderStatus.CANCELLED);
    orderRepository.save(order);
    pushEvent(order.getId(), "ORDER_CANCELLED", "Order cancelled");
  }

  @Transactional
  public void returnRequest(Long id) {
    Order order = orderRepository.findById(id).orElseThrow(() -> new ApiException("Order not found"));
    if (order.getDeliveredAt() == null || Duration.between(order.getDeliveredAt(), LocalDateTime.now()).toHours() > 2) {
      throw new ApiException("Return window is 2 hours after delivery");
    }
    order.setStatus(OrderStatus.RETURN_REQUESTED);
    orderRepository.save(order);
  }

  @Transactional
  public void startPayment(Long orderId) {
    Order order = orderRepository.findById(orderId).orElseThrow(() -> new ApiException("Order not found"));
    order.setStatus(OrderStatus.PAYMENT_PENDING);
    orderRepository.save(order);
  }

  @Transactional
  public void confirmPayment(Long orderId, String result) {
    Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> new ApiException("Payment not found"));
    Order order = orderRepository.findById(orderId).orElseThrow(() -> new ApiException("Order not found"));
    if ("SUCCESS".equalsIgnoreCase(result)) {
      payment.setStatus(PaymentStatus.SUCCESS);
      order.setStatus(OrderStatus.PAID);
      pushEvent(orderId, "PAYMENT_SUCCESS", "Payment success");
    } else {
      payment.setStatus(PaymentStatus.FAILED);
      order.setStatus(OrderStatus.PAYMENT_PENDING);
      pushEvent(orderId, "PAYMENT_FAILED", "Payment failed");
    }
    paymentRepository.save(payment);
    orderRepository.save(order);
  }

  @Transactional
  public void advanceStatus(Long orderId, OrderStatus next) {
    Order order = orderRepository.findById(orderId).orElseThrow(() -> new ApiException("Order not found"));
    EnumSet<OrderStatus> allowed = FLOW.getOrDefault(order.getStatus(), EnumSet.noneOf(OrderStatus.class));
    if (!allowed.contains(next)) {
      throw new ApiException("Invalid status transition");
    }
    order.setStatus(next);
    if (next == OrderStatus.DELIVERED) {
      order.setDeliveredAt(LocalDateTime.now());
    }
    orderRepository.save(order);
    pushEvent(orderId, "STATUS_UPDATED", "Order moved to " + next);
  }

  @Transactional
  public void approveRefund(Long refundId) {
    Refund refund = refundRepository.findById(refundId).orElseThrow(() -> new ApiException("Refund not found"));
    refund.setStatus(RefundStatus.APPROVED);
    refund.setApprovedAt(LocalDateTime.now());
    refundRepository.save(refund);

    Order order = orderRepository.findById(refund.getOrderId()).orElseThrow(() -> new ApiException("Order not found"));
    Wallet wallet = walletRepository.findByUserId(order.getUserId())
        .orElseGet(() -> walletRepository.save(Wallet.builder().userId(order.getUserId()).balance(BigDecimal.ZERO).build()));
    wallet.setBalance(wallet.getBalance().add(refund.getAmount()));
    walletRepository.save(wallet);
    walletTxnRepository.save(WalletTxn.builder()
        .walletId(wallet.getId())
        .orderId(order.getId())
        .amount(refund.getAmount())
        .type("REFUND")
        .note("Partial refund")
        .createdAt(LocalDateTime.now())
        .build());
  }

  public List<Refund> pendingRefunds() {
    return refundRepository.findByStatus(RefundStatus.PENDING);
  }

  private OrderView view(Order order) {
    List<OrderItemView> items = orderItemRepository.findByOrderId(order.getId()).stream()
        .map(i -> new OrderItemView(i.getProductId(), i.getProductName(), i.getQty(), i.getPrice(), i.getRefundedQty()))
        .toList();
    return new OrderView(order.getId(), order.getStatus(), order.getTotalAmount(), order.getCreatedAt(), items);
  }

  public void pushEvent(Long orderId, String type, String message) {
    deliveryEventRepository.save(DeliveryEvent.builder()
        .orderId(orderId)
        .eventType(type)
        .message(message)
        .createdAt(LocalDateTime.now())
        .build());
  }
}

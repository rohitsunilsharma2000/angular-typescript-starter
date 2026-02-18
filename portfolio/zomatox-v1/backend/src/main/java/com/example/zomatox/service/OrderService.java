package com.example.zomatox.service;

import com.example.zomatox.dto.checkout.PricingPreviewResponse;
import com.example.zomatox.dto.orders.OrderItemResponse;
import com.example.zomatox.dto.orders.OrderResponse;
import com.example.zomatox.entity.*;
import com.example.zomatox.entity.enums.OrderStatus;
import com.example.zomatox.entity.enums.PaymentStatus;
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
public class OrderService {
  private final AddressRepository addressRepository;
  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final PaymentRepository paymentRepository;
  private final OrderEventRepository orderEventRepository;
  private final CartService cartService;
  private final OrderStateMachine orderStateMachine;
  private final CouponService couponService;

  public OrderResponse createOrder(User user, Long addressId) {
    return createOrder(user, addressId, null);
  }

  public OrderResponse createOrder(User user, Long addressId, String couponCode) {
    Address addr = addressRepository.findById(addressId).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Address not found: " + addressId));

    if (!addr.getUser().getId().equals(user.getId())) {
      throw new ApiException(
        HttpStatus.FORBIDDEN,
        "Address " + addressId + " belongs to user " + addr.getUser().getId() + ", not user " + user.getId()
      );
    }

    List<CartItem> cartItems = cartService.getRawItems(user);
    if (cartItems.isEmpty()) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Cart is empty");
    }

    for (CartItem ci : cartItems) {
      MenuItem mi = ci.getMenuItem();
      if (!mi.isAvailable() || mi.isBlocked()) {
        throw new ApiException(HttpStatus.BAD_REQUEST, "Item not available: " + mi.getName());
      }
      if (mi.getStockQty() < ci.getQty()) {
        throw new ApiException(HttpStatus.BAD_REQUEST,
          "Not enough stock for " + mi.getName() + ". Available=" + mi.getStockQty());
      }
    }

    Restaurant restaurant = cartItems.get(0).getMenuItem().getRestaurant();
    if (restaurant.isBlocked() || restaurant.getApprovalStatus() != RestaurantApprovalStatus.APPROVED) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Restaurant is blocked or not approved");
    }

    long itemTotal = cartItems.stream().mapToLong(ci -> ci.getMenuItem().getPrice() * ci.getQty()).sum();
    long deliveryFee = calcDeliveryFee(itemTotal);
    long discount = 0;
    String appliedCoupon = null;
    String pricingSnapshot = null;
    Coupon coupon = null;

    if (couponCode != null && !couponCode.isBlank()) {
      coupon = couponService.findAndValidateCoupon(user, couponCode, restaurant, itemTotal);
      discount = couponService.calculateDiscount(coupon, itemTotal);
      appliedCoupon = coupon.getCode();
      PricingPreviewResponse preview = new PricingPreviewResponse(itemTotal, 0, 5, deliveryFee, discount,
        Math.max(0, itemTotal + 5 + deliveryFee - discount), appliedCoupon);
      pricingSnapshot = "{\"itemTotal\":" + preview.getItemTotal() + ",\"platformFee\":" + preview.getPlatformFee() +
        ",\"deliveryFee\":" + preview.getDeliveryFee() + ",\"discount\":" + preview.getDiscount() +
        ",\"payableTotal\":" + preview.getPayableTotal() + "}";
    }

    long payable = Math.max(0, itemTotal + deliveryFee + 5 - discount);
    Instant now = Instant.now();

    Order order = Order.builder()
      .user(user)
      .restaurant(restaurant)
      .status(OrderStatus.PAYMENT_PENDING)
      .itemTotal(itemTotal)
      .deliveryFee(deliveryFee)
      .discountAmount(discount)
      .payableTotal(payable)
      .appliedCouponCode(appliedCoupon)
      .pricingJsonSnapshot(pricingSnapshot)
      .createdAt(now)
      .updatedAt(now)
      .build();

    order = orderRepository.save(order);

    for (CartItem ci : cartItems) {
      MenuItem mi = ci.getMenuItem();
      mi.setStockQty(mi.getStockQty() - ci.getQty());

      OrderItem oi = OrderItem.builder()
        .order(order)
        .menuItemNameSnapshot(mi.getName())
        .priceSnapshot(mi.getPrice())
        .qty(ci.getQty())
        .lineTotal(mi.getPrice() * ci.getQty())
        .build();
      orderItemRepository.save(oi);
    }

    Payment payment = Payment.builder()
      .order(order)
      .status(PaymentStatus.INITIATED)
      .method("MOCK")
      .createdAt(now)
      .build();
    paymentRepository.save(payment);

    if (coupon != null) {
      couponService.markRedeemed(coupon, user, order);
    }

    appendEvent(order, order.getStatus(), "Order created. Awaiting payment", now);

    cartService.clearCart(user);

    return toResponse(order);
  }

  public Order getOrderOrThrow(Long id) {
    return orderRepository.findById(id).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Order not found: " + id));
  }

  public void requireCanAccess(User user, Order order) {
    boolean isCustomer = order.getUser().getId().equals(user.getId());
    boolean isOwner = order.getRestaurant().getOwner() != null
      && order.getRestaurant().getOwner().getId().equals(user.getId());
    boolean isDelivery = order.getDeliveryPartner() != null
      && order.getDeliveryPartner().getId().equals(user.getId());

    if (!(isCustomer || isOwner || isDelivery || user.getRole().name().equals("ADMIN"))) {
      throw new ApiException(HttpStatus.FORBIDDEN, "Order does not belong to this user context");
    }
  }

  public Order setStatusWithEvent(Order order, OrderStatus next, String message, Instant when) {
    orderStateMachine.requireTransitionAllowed(order.getStatus(), next);
    order.setStatus(next);
    order.setUpdatedAt(when);
    Order saved = orderRepository.save(order);
    appendEvent(saved, next, message, when);
    return saved;
  }

  public List<OrderResponse> listOrders(User user) {
    return orderRepository.findByUserOrderByIdDesc(user).stream().map(this::toResponse).toList();
  }

  public OrderResponse getOrder(User user, Long orderId) {
    Order o = getOrderOrThrow(orderId);
    requireCanAccess(user, o);
    return toResponse(o);
  }

  public OrderResponse toResponse(Order o) {
    List<OrderItemResponse> items = orderItemRepository.findByOrder(o).stream()
      .map(oi -> new OrderItemResponse(oi.getMenuItemNameSnapshot(), oi.getPriceSnapshot(), oi.getQty(), oi.getLineTotal()))
      .toList();
    return OrderResponse.of(o, items);
  }

  private void appendEvent(Order order, OrderStatus status, String message, Instant when) {
    orderEventRepository.save(OrderEvent.builder()
      .order(order)
      .status(status.name())
      .message(message)
      .createdAt(when)
      .build());
  }

  private long calcDeliveryFee(long itemTotal) {
    return itemTotal >= 500 ? 0 : 40;
  }
}

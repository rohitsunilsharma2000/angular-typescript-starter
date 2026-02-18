package com.example.zomatox.service;

import com.example.zomatox.dto.orders.OrderItemResponse;
import com.example.zomatox.dto.orders.OrderResponse;
import com.example.zomatox.entity.*;
import com.example.zomatox.entity.enums.OrderStatus;
import com.example.zomatox.entity.enums.PaymentStatus;
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
  private final CartService cartService;

  public OrderResponse createOrder(User user, Long addressId) {
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

    // Stock check
    for (CartItem ci : cartItems) {
      MenuItem mi = ci.getMenuItem();
      if (!mi.isAvailable()) {
        throw new ApiException(HttpStatus.BAD_REQUEST, "Item not available: " + mi.getName());
      }
      if (mi.getStockQty() < ci.getQty()) {
        throw new ApiException(HttpStatus.BAD_REQUEST,
          "Not enough stock for " + mi.getName() + ". Available=" + mi.getStockQty());
      }
    }

    Restaurant restaurant = cartItems.get(0).getMenuItem().getRestaurant();
    long itemTotal = cartItems.stream().mapToLong(ci -> ci.getMenuItem().getPrice() * ci.getQty()).sum();
    long deliveryFee = calcDeliveryFee(itemTotal, restaurant);
    long payable = itemTotal + deliveryFee;

    Order order = Order.builder()
      .user(user)
      .restaurant(restaurant)
      .status(OrderStatus.PAYMENT_PENDING)
      .itemTotal(itemTotal)
      .deliveryFee(deliveryFee)
      .payableTotal(payable)
      .createdAt(Instant.now())
      .build();

    order = orderRepository.save(order);

    for (CartItem ci : cartItems) {
      MenuItem mi = ci.getMenuItem();

      // Deduct stock on order creation (simple v1 approach)
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
      .createdAt(Instant.now())
      .build();
    paymentRepository.save(payment);

    cartService.clearCart(user);

    return getOrderResponse(order, user);
  }

  private long calcDeliveryFee(long itemTotal, Restaurant r) {
    // super simple v1 rule:
    // free delivery above 500, else 40
    return itemTotal >= 500 ? 0 : 40;
  }

  public List<OrderResponse> listOrders(User user) {
    return orderRepository.findByUserOrderByIdDesc(user).stream()
      .map(o -> getOrderResponse(o, user))
      .toList();
  }

  public OrderResponse getOrder(User user, Long orderId) {
    Order o = orderRepository.findById(orderId).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Order not found: " + orderId));
    if (!o.getUser().getId().equals(user.getId())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "Order does not belong to user");
    }
    return getOrderResponse(o, user);
  }

  private OrderResponse getOrderResponse(Order o, User user) {
    List<OrderItemResponse> items = orderItemRepository.findByOrder(o).stream()
      .map(oi -> new OrderItemResponse(oi.getMenuItemNameSnapshot(), oi.getPriceSnapshot(), oi.getQty(), oi.getLineTotal()))
      .toList();
    return OrderResponse.of(o, items);
  }
}

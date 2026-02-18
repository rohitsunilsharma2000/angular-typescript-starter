package com.example.zomatox.service;

import com.example.zomatox.dto.payments.PaymentResponse;
import com.example.zomatox.entity.Order;
import com.example.zomatox.entity.Payment;
import com.example.zomatox.entity.enums.OrderStatus;
import com.example.zomatox.entity.enums.PaymentStatus;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {
  private final PaymentRepository paymentRepository;
  private final OrderService orderService;

  public PaymentResponse confirm(Long orderId, String result) {
    Order order = orderService.getOrderOrThrow(orderId);

    Payment payment = paymentRepository.findByOrder(order).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Payment not found for order: " + orderId));

    Instant now = Instant.now();

    if ("SUCCESS".equalsIgnoreCase(result)) {
      payment.setStatus(PaymentStatus.SUCCESS);
      paymentRepository.save(payment);
      orderService.setStatusWithEvent(order, OrderStatus.PAID, "Payment success", now);
      orderService.setStatusWithEvent(order, OrderStatus.CONFIRMED, "Order auto-confirmed", now);
    } else if ("FAIL".equalsIgnoreCase(result)) {
      payment.setStatus(PaymentStatus.FAIL);
      paymentRepository.save(payment);
      orderService.setStatusWithEvent(order, OrderStatus.PAYMENT_FAILED, "Payment failed", now);
    } else {
      throw new ApiException(HttpStatus.BAD_REQUEST, "result must be SUCCESS or FAIL");
    }

    return PaymentResponse.from(payment);
  }
}

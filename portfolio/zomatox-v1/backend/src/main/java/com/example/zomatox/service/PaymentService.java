package com.example.zomatox.service;

import com.example.zomatox.dto.payments.PaymentResponse;
import com.example.zomatox.entity.Order;
import com.example.zomatox.entity.Payment;
import com.example.zomatox.entity.enums.OrderStatus;
import com.example.zomatox.entity.enums.PaymentStatus;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.repository.OrderRepository;
import com.example.zomatox.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {
  private final OrderRepository orderRepository;
  private final PaymentRepository paymentRepository;

  public PaymentResponse confirm(Long orderId, String result) {
    Order order = orderRepository.findById(orderId).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Order not found: " + orderId));

    Payment payment = paymentRepository.findByOrder(order).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Payment not found for order: " + orderId));

    if ("SUCCESS".equalsIgnoreCase(result)) {
      payment.setStatus(PaymentStatus.SUCCESS);
      order.setStatus(OrderStatus.PAID);
    } else if ("FAIL".equalsIgnoreCase(result)) {
      payment.setStatus(PaymentStatus.FAIL);
      order.setStatus(OrderStatus.PAYMENT_FAILED);
    } else {
      throw new ApiException(HttpStatus.BAD_REQUEST, "result must be SUCCESS or FAIL");
    }

    paymentRepository.save(payment);
    orderRepository.save(order);

    return PaymentResponse.from(payment);
  }
}

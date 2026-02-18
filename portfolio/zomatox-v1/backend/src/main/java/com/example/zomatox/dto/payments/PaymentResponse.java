package com.example.zomatox.dto.payments;

import com.example.zomatox.entity.Payment;
import lombok.Value;

import java.time.Instant;

@Value
public class PaymentResponse {
  Long id;
  Long orderId;
  String status;
  String method;
  Instant createdAt;

  public static PaymentResponse from(Payment p) {
    return new PaymentResponse(
      p.getId(),
      p.getOrder().getId(),
      p.getStatus().name(),
      p.getMethod(),
      p.getCreatedAt()
    );
  }
}

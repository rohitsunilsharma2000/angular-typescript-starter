package com.example.zomatox.controller;

import com.example.zomatox.dto.payments.PaymentResponse;
import com.example.zomatox.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
  private final PaymentService paymentService;

  @PostMapping("/{orderId}/confirm")
  public PaymentResponse confirm(@PathVariable("orderId") Long orderId,
                                 @RequestParam("result") String result) {
    return paymentService.confirm(orderId, result);
  }
}

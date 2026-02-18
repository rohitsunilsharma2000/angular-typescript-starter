package com.example.zomatox.dto.checkout;

import lombok.Value;

@Value
public class PricingPreviewResponse {
  long itemTotal;
  long packagingFee;
  long platformFee;
  long deliveryFee;
  long discount;
  long payableTotal;
  String couponCode;
}

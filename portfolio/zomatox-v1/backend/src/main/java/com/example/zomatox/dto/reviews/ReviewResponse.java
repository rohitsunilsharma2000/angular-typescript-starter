package com.example.zomatox.dto.reviews;

import com.example.zomatox.entity.Review;
import lombok.Value;

import java.time.Instant;

@Value
public class ReviewResponse {
  Long id;
  Long orderId;
  Long restaurantId;
  Long userId;
  int rating;
  String comment;
  String status;
  Instant createdAt;

  public static ReviewResponse from(Review r) {
    return new ReviewResponse(
      r.getId(),
      r.getOrder().getId(),
      r.getRestaurant().getId(),
      r.getUser().getId(),
      r.getRating(),
      r.getComment(),
      r.getStatus().name(),
      r.getCreatedAt()
    );
  }
}

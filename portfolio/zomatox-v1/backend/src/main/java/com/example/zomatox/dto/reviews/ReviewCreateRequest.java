package com.example.zomatox.dto.reviews;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewCreateRequest {
  @NotNull
  private Long orderId;

  @Min(1)
  @Max(5)
  private int rating;

  private String comment;
}

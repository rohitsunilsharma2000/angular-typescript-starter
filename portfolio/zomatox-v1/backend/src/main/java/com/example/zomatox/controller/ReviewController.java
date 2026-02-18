package com.example.zomatox.controller;

import com.example.zomatox.dto.reviews.ReviewCreateRequest;
import com.example.zomatox.dto.reviews.ReviewResponse;
import com.example.zomatox.entity.User;
import com.example.zomatox.entity.enums.UserRole;
import com.example.zomatox.service.ReviewService;
import com.example.zomatox.service.UserService;
import com.example.zomatox.util.RequestContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/reviews")
@RequiredArgsConstructor
public class ReviewController {
  private final UserService userService;
  private final ReviewService reviewService;

  @GetMapping
  public Page<ReviewResponse> list(@PathVariable("restaurantId") Long restaurantId,
                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "size", defaultValue = "10") int size) {
    return reviewService.reviewsByRestaurant(restaurantId, page, size).map(ReviewResponse::from);
  }

  @PostMapping
  public ReviewResponse create(@PathVariable("restaurantId") Long restaurantId,
                               @RequestHeader(value = "X-User-Id", required = false) String userId,
                               @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
                               @Valid @RequestBody ReviewCreateRequest req) {
    User u = RequestContext.requireUser(userService, userId);
    RequestContext.requireRole(RequestContext.resolveRole(u, roleHeader), UserRole.CUSTOMER);
    return ReviewResponse.from(reviewService.createReview(u, restaurantId, req.getOrderId(), req.getRating(), req.getComment()));
  }
}

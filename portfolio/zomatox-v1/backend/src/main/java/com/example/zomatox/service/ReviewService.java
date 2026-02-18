package com.example.zomatox.service;

import com.example.zomatox.entity.Order;
import com.example.zomatox.entity.Restaurant;
import com.example.zomatox.entity.Review;
import com.example.zomatox.entity.User;
import com.example.zomatox.entity.enums.OrderStatus;
import com.example.zomatox.entity.enums.ReviewStatus;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.repository.RestaurantRepository;
import com.example.zomatox.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
  private final ReviewRepository reviewRepository;
  private final RestaurantRepository restaurantRepository;
  private final OrderService orderService;

  public Page<Review> reviewsByRestaurant(Long restaurantId, int page, int size) {
    Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Restaurant not found: " + restaurantId));
    return reviewRepository.findByRestaurantAndStatusOrderByIdDesc(restaurant, ReviewStatus.VISIBLE, PageRequest.of(page, size));
  }

  public Review createReview(User user, Long restaurantId, Long orderId, int rating, String comment) {
    if (rating < 1 || rating > 5) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "rating must be between 1 and 5");
    }

    Order order = orderService.getOrderOrThrow(orderId);
    if (!order.getUser().getId().equals(user.getId())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "You can review only your own order");
    }
    if (!order.getRestaurant().getId().equals(restaurantId)) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Order does not belong to this restaurant");
    }
    if (order.getStatus() != OrderStatus.DELIVERED) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Review allowed only for DELIVERED orders");
    }
    if (reviewRepository.findByOrderId(orderId).isPresent()) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Only one review allowed per order");
    }

    Review review = reviewRepository.save(Review.builder()
      .order(order)
      .restaurant(order.getRestaurant())
      .user(user)
      .rating(rating)
      .comment(comment)
      .status(ReviewStatus.VISIBLE)
      .createdAt(Instant.now())
      .build());

    updateRestaurantRating(order.getRestaurant());
    return review;
  }

  private void updateRestaurantRating(Restaurant restaurant) {
    Double avgRating = reviewRepository.avgRatingByRestaurantId(restaurant.getId());
    double avg = avgRating == null ? 0 : avgRating;
    restaurant.setRatingAvg(avg);
    restaurantRepository.save(restaurant);
  }
}

package com.example.zomatox.repository;

import com.example.zomatox.entity.Restaurant;
import com.example.zomatox.entity.Review;
import com.example.zomatox.entity.enums.ReviewStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  Page<Review> findByRestaurantAndStatusOrderByIdDesc(Restaurant restaurant, ReviewStatus status, Pageable pageable);
  Page<Review> findByStatusOrderByIdDesc(ReviewStatus status, Pageable pageable);
  Optional<Review> findByOrderId(Long orderId);

  @Query("select avg(r.rating) from Review r where r.restaurant.id = :restaurantId and r.status='VISIBLE'")
  Double avgRatingByRestaurantId(Long restaurantId);
}

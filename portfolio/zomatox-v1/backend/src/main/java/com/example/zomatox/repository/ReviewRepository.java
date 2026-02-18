package com.example.zomatox.repository;

import com.example.zomatox.entity.Restaurant;
import com.example.zomatox.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  Page<Review> findByRestaurantOrderByIdDesc(Restaurant restaurant, Pageable pageable);
  Optional<Review> findByOrderId(Long orderId);

  @Query("select avg(r.rating) from Review r where r.restaurant.id = :restaurantId")
  Double avgRatingByRestaurantId(Long restaurantId);
}

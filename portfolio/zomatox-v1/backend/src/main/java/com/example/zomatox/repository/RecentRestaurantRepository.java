package com.example.zomatox.repository;

import com.example.zomatox.entity.RecentRestaurant;
import com.example.zomatox.entity.Restaurant;
import com.example.zomatox.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecentRestaurantRepository extends JpaRepository<RecentRestaurant, Long> {
  Optional<RecentRestaurant> findByUserAndRestaurant(User user, Restaurant restaurant);
  List<RecentRestaurant> findByUserOrderByViewedAtDesc(User user, Pageable pageable);
  long countByUser(User user);
}

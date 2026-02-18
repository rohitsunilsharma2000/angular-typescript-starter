package com.example.zomatox.repository;

import com.example.zomatox.entity.FavoriteRestaurant;
import com.example.zomatox.entity.Restaurant;
import com.example.zomatox.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRestaurantRepository extends JpaRepository<FavoriteRestaurant, Long> {
  Optional<FavoriteRestaurant> findByUserAndRestaurant(User user, Restaurant restaurant);
  List<FavoriteRestaurant> findByUserOrderByIdDesc(User user);
}

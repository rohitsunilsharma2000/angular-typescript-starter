package com.example.zomatox.repository;

import com.example.zomatox.entity.MenuItem;
import com.example.zomatox.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
  List<MenuItem> findByRestaurantOrderByIdAsc(Restaurant restaurant);
}

package com.example.zomatox.repository;

import com.example.zomatox.entity.Restaurant;
import com.example.zomatox.entity.User;
import com.example.zomatox.entity.enums.RestaurantApprovalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
  Page<Restaurant> findByCityContainingIgnoreCaseAndNameContainingIgnoreCase(String city, String q, Pageable pageable);
  Page<Restaurant> findByCityContainingIgnoreCase(String city, Pageable pageable);
  List<Restaurant> findByOwner(User owner);
  List<Restaurant> findByApprovalStatusOrderByIdDesc(RestaurantApprovalStatus status);
}

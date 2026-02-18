package com.example.zomatox.service;

import com.example.zomatox.entity.MenuItem;
import com.example.zomatox.entity.Restaurant;
import com.example.zomatox.entity.Review;
import com.example.zomatox.entity.enums.RestaurantApprovalStatus;
import com.example.zomatox.entity.enums.ReviewStatus;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.repository.MenuItemRepository;
import com.example.zomatox.repository.RestaurantRepository;
import com.example.zomatox.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminOpsService {
  private final RestaurantRepository restaurantRepository;
  private final MenuItemRepository menuItemRepository;
  private final ReviewRepository reviewRepository;

  public List<Restaurant> listRestaurantsByStatus(RestaurantApprovalStatus status) {
    return restaurantRepository.findByApprovalStatusOrderByIdDesc(status);
  }

  public Restaurant approveRestaurant(Long id) {
    Restaurant r = getRestaurant(id);
    r.setApprovalStatus(RestaurantApprovalStatus.APPROVED);
    return restaurantRepository.save(r);
  }

  public Restaurant rejectRestaurant(Long id) {
    Restaurant r = getRestaurant(id);
    r.setApprovalStatus(RestaurantApprovalStatus.REJECTED);
    return restaurantRepository.save(r);
  }

  public Restaurant blockRestaurant(Long id, String reason) {
    Restaurant r = getRestaurant(id);
    r.setBlocked(true);
    r.setBlockedReason(reason == null || reason.isBlank() ? "Blocked by admin" : reason);
    return restaurantRepository.save(r);
  }

  public Restaurant unblockRestaurant(Long id) {
    Restaurant r = getRestaurant(id);
    r.setBlocked(false);
    r.setBlockedReason(null);
    return restaurantRepository.save(r);
  }

  public MenuItem blockMenuItem(Long id) {
    MenuItem m = menuItemRepository.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Menu item not found"));
    m.setBlocked(true);
    return menuItemRepository.save(m);
  }

  public MenuItem unblockMenuItem(Long id) {
    MenuItem m = menuItemRepository.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Menu item not found"));
    m.setBlocked(false);
    return menuItemRepository.save(m);
  }

  public List<Review> listReviews(ReviewStatus status) {
    return reviewRepository.findByStatusOrderByIdDesc(status, PageRequest.of(0, 100)).getContent();
  }

  public Review hideReview(Long id) {
    Review r = reviewRepository.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Review not found"));
    r.setStatus(ReviewStatus.HIDDEN);
    return reviewRepository.save(r);
  }

  public Review unhideReview(Long id) {
    Review r = reviewRepository.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Review not found"));
    r.setStatus(ReviewStatus.VISIBLE);
    return reviewRepository.save(r);
  }

  private Restaurant getRestaurant(Long id) {
    return restaurantRepository.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Restaurant not found"));
  }
}

package com.example.zomatox.service;

import com.example.zomatox.entity.*;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.repository.FavoriteRestaurantRepository;
import com.example.zomatox.repository.RecentRestaurantRepository;
import com.example.zomatox.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteRecentService {
  private final RestaurantRepository restaurantRepository;
  private final FavoriteRestaurantRepository favoriteRestaurantRepository;
  private final RecentRestaurantRepository recentRestaurantRepository;

  public void addFavorite(User user, Long restaurantId) {
    Restaurant r = restaurantRepository.findById(restaurantId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Restaurant not found"));
    favoriteRestaurantRepository.findByUserAndRestaurant(user, r).ifPresent(existing -> {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Already in favorites");
    });
    favoriteRestaurantRepository.save(FavoriteRestaurant.builder().user(user).restaurant(r).createdAt(Instant.now()).build());
  }

  public void removeFavorite(User user, Long restaurantId) {
    Restaurant r = restaurantRepository.findById(restaurantId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Restaurant not found"));
    favoriteRestaurantRepository.findByUserAndRestaurant(user, r).ifPresent(favoriteRestaurantRepository::delete);
  }

  public List<Restaurant> listFavorites(User user) {
    return favoriteRestaurantRepository.findByUserOrderByIdDesc(user).stream().map(FavoriteRestaurant::getRestaurant).toList();
  }

  public void addRecent(User user, Long restaurantId) {
    Restaurant r = restaurantRepository.findById(restaurantId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Restaurant not found"));
    RecentRestaurant recent = recentRestaurantRepository.findByUserAndRestaurant(user, r)
      .orElse(RecentRestaurant.builder().user(user).restaurant(r).build());
    recent.setViewedAt(Instant.now());
    recentRestaurantRepository.save(recent);
  }

  public List<Restaurant> listRecent(User user, int limit) {
    return recentRestaurantRepository.findByUserOrderByViewedAtDesc(user, PageRequest.of(0, Math.max(1, Math.min(50, limit))))
      .stream().map(RecentRestaurant::getRestaurant).toList();
  }
}

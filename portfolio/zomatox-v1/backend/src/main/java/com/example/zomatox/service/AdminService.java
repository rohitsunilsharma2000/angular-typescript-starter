package com.example.zomatox.service;

import com.example.zomatox.dto.admin.*;
import com.example.zomatox.entity.MenuItem;
import com.example.zomatox.entity.Restaurant;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.repository.MenuItemRepository;
import com.example.zomatox.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
  private final RestaurantRepository restaurantRepository;
  private final MenuItemRepository menuItemRepository;

  public Restaurant createRestaurant(AdminRestaurantCreateRequest req) {
    Restaurant r = Restaurant.builder()
      .name(req.getName())
      .city(req.getCity())
      .cuisineType(req.getCuisineType())
      .ratingAvg(req.getRatingAvg())
      .deliveryTimeMin(req.getDeliveryTimeMin())
      .imageUrl(req.getImageUrl())
      .build();
    return restaurantRepository.save(r);
  }

  public MenuItem addMenuItem(Long restaurantId, AdminMenuItemCreateRequest req) {
    Restaurant r = restaurantRepository.findById(restaurantId).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Restaurant not found: " + restaurantId));

    MenuItem mi = MenuItem.builder()
      .restaurant(r)
      .name(req.getName())
      .price(req.getPrice())
      .isVeg(req.getIsVeg())
      .available(req.getAvailable())
      .stockQty(req.getStockQty())
      .build();

    return menuItemRepository.save(mi);
  }

  public MenuItem updateMenuItem(Long menuItemId, AdminMenuItemUpdateRequest req) {
    MenuItem mi = menuItemRepository.findById(menuItemId).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Menu item not found: " + menuItemId));

    mi.setPrice(req.getPrice());
    mi.setAvailable(req.getAvailable());
    mi.setStockQty(req.getStockQty());
    return menuItemRepository.save(mi);
  }
}

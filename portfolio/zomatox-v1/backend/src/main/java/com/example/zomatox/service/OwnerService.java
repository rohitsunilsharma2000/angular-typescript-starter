package com.example.zomatox.service;

import com.example.zomatox.dto.admin.AdminMenuItemCreateRequest;
import com.example.zomatox.dto.admin.AdminMenuItemUpdateRequest;
import com.example.zomatox.dto.admin.AdminRestaurantCreateRequest;
import com.example.zomatox.entity.MenuItem;
import com.example.zomatox.entity.Order;
import com.example.zomatox.entity.Restaurant;
import com.example.zomatox.entity.User;
import com.example.zomatox.entity.enums.OrderStatus;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.repository.MenuItemRepository;
import com.example.zomatox.repository.OrderRepository;
import com.example.zomatox.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OwnerService {
  private final RestaurantRepository restaurantRepository;
  private final MenuItemRepository menuItemRepository;
  private final OrderRepository orderRepository;
  private final OrderService orderService;

  public List<Restaurant> myRestaurants(User owner) {
    return restaurantRepository.findByOwner(owner);
  }

  public Restaurant createRestaurant(User owner, AdminRestaurantCreateRequest req) {
    Restaurant r = Restaurant.builder()
      .name(req.getName())
      .city(req.getCity())
      .cuisineType(req.getCuisineType())
      .ratingAvg(req.getRatingAvg())
      .deliveryTimeMin(req.getDeliveryTimeMin())
      .imageUrl(req.getImageUrl())
      .owner(owner)
      .build();
    return restaurantRepository.save(r);
  }

  public MenuItem addMenuItem(User owner, Long restaurantId, AdminMenuItemCreateRequest req) {
    Restaurant r = restaurantRepository.findById(restaurantId).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Restaurant not found: " + restaurantId));

    if (r.getOwner() == null || !r.getOwner().getId().equals(owner.getId())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "Not your restaurant");
    }

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

  public MenuItem updateMenuItem(User owner, Long menuItemId, AdminMenuItemUpdateRequest req) {
    MenuItem mi = menuItemRepository.findById(menuItemId).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Menu item not found: " + menuItemId));

    Restaurant r = mi.getRestaurant();
    if (r.getOwner() == null || !r.getOwner().getId().equals(owner.getId())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "Not your restaurant menu item");
    }

    mi.setPrice(req.getPrice());
    mi.setAvailable(req.getAvailable());
    mi.setStockQty(req.getStockQty());
    return menuItemRepository.save(mi);
  }

  public List<Order> ownerOrders(User owner, OrderStatus status) {
    if (status == null) {
      return orderRepository.findByRestaurantOwnerOrderByIdDesc(owner);
    }
    return orderRepository.findByRestaurantOwnerAndStatusOrderByIdDesc(owner, status);
  }

  public Order changeOrderStatus(User owner, Long orderId, OrderStatus next) {
    Order o = orderService.getOrderOrThrow(orderId);
    if (o.getRestaurant().getOwner() == null || !o.getRestaurant().getOwner().getId().equals(owner.getId())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "Not your order");
    }

    if (!(next == OrderStatus.PREPARING || next == OrderStatus.READY_FOR_PICKUP)) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Owner can set only PREPARING or READY_FOR_PICKUP");
    }

    return orderService.setStatusWithEvent(o, next, "Owner updated to " + next, Instant.now());
  }
}

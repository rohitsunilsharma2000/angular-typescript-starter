package com.example.zomatox.dto.restaurants;

import com.example.zomatox.entity.Restaurant;
import lombok.Value;

@Value
public class RestaurantResponse {
  Long id;
  String name;
  String city;
  String cuisineType;
  double ratingAvg;
  int deliveryTimeMin;
  String imageUrl;
  String approvalStatus;
  boolean isBlocked;

  public static RestaurantResponse from(Restaurant r) {
    return new RestaurantResponse(
      r.getId(), r.getName(), r.getCity(), r.getCuisineType(),
      r.getRatingAvg(), r.getDeliveryTimeMin(), r.getImageUrl(),
      r.getApprovalStatus().name(), r.isBlocked()
    );
  }
}

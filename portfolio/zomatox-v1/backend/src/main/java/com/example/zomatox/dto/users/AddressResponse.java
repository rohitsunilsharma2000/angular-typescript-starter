package com.example.zomatox.dto.users;

import com.example.zomatox.entity.Address;
import lombok.Value;

@Value
public class AddressResponse {
  Long id;
  Long userId;
  String line1;
  String city;
  String pincode;
  String phone;

  public static AddressResponse from(Address a) {
    return new AddressResponse(
      a.getId(),
      a.getUser().getId(),
      a.getLine1(),
      a.getCity(),
      a.getPincode(),
      a.getPhone()
    );
  }
}

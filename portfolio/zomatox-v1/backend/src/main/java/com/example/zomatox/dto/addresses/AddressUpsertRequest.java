package com.example.zomatox.dto.addresses;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressUpsertRequest {
  @NotBlank
  private String line1;
  @NotBlank
  private String city;
  @NotBlank
  private String pincode;
  @NotBlank
  private String phone;
}

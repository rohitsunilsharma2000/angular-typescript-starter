package com.example.zomatox.dto.admin;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminMenuItemUpdateRequest {
  @NotNull private Long price;
  @NotNull private Boolean available;

  @Min(value = 0, message = "stockQty must be >= 0")
  private Integer stockQty;
}

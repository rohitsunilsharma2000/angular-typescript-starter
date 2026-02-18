package com.example.blinkit.controller;

import com.example.blinkit.dto.AdminDtos.MarkMissingRequest;
import com.example.blinkit.entity.Order;
import com.example.blinkit.entity.OrderItem;
import com.example.blinkit.service.OpsService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/warehouse")
@RequiredArgsConstructor
@PreAuthorize("hasRole('WAREHOUSE_STAFF')")
public class WarehouseController {
  private final OpsService opsService;

  @GetMapping("/orders")
  public List<Order> queue(@RequestParam(defaultValue = "PICKING") String status) {
    return opsService.pickingQueue();
  }

  @GetMapping("/orders/{id}/picklist")
  public List<OrderItem> picklist(@PathVariable Long id) {
    return opsService.picklist(id);
  }

  @PostMapping("/orders/{id}/mark-missing")
  public void markMissing(@PathVariable Long id, @Valid @RequestBody MarkMissingRequest req) {
    opsService.markMissing(id, req);
  }

  @PostMapping("/orders/{id}/packed")
  public void packed(@PathVariable Long id) {
    opsService.markPacked(id);
  }
}

package com.example.blinkit.controller;

import com.example.blinkit.dto.AdminDtos.RiderStatusRequest;
import com.example.blinkit.entity.DeliveryBatch;
import com.example.blinkit.service.OpsService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rider")
@RequiredArgsConstructor
@PreAuthorize("hasRole('RIDER')")
public class RiderController {
  private final OpsService opsService;

  @GetMapping("/batches")
  public List<DeliveryBatch> batches(@RequestParam(defaultValue = "ACTIVE") String status) {
    return opsService.riderBatches(status);
  }

  @PostMapping("/orders/{id}/accept")
  public void accept(@PathVariable Long id) {
    opsService.riderAcceptOrder(id);
  }

  @PostMapping("/orders/{id}/status")
  public void status(@PathVariable Long id, @Valid @RequestBody RiderStatusRequest req) {
    opsService.riderStatus(id, req);
  }
}

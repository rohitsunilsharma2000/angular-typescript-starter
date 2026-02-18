package com.example.blinkit.controller;

import com.example.blinkit.dto.AdminDtos.*;
import com.example.blinkit.dto.OrderDtos.AssignRiderRequest;
import com.example.blinkit.dto.OrderDtos.StatusUpdateRequest;
import com.example.blinkit.entity.*;
import com.example.blinkit.service.AdminService;
import com.example.blinkit.service.OpsService;
import com.example.blinkit.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
  private final AdminService adminService;
  private final OrderService orderService;
  private final OpsService opsService;

  @PostMapping("/products")
  public Product createProduct(@Valid @RequestBody ProductRequest req) {
    return adminService.upsertProduct(req, null);
  }

  @PutMapping("/products/{id}")
  public Product updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest req) {
    return adminService.upsertProduct(req, id);
  }

  @PutMapping("/inventory/{storeId}/{productId}")
  public void updateInventory(@PathVariable Long storeId, @PathVariable Long productId, @RequestBody InventoryUpdateRequest req) {
    adminService.updateInventory(storeId, productId, req);
  }

  @GetMapping("/orders")
  public List<Order> orders(@RequestParam(required = false) OrderStatus status, @RequestParam(required = false) Long storeId) {
    return adminService.orders(status, storeId);
  }

  @PostMapping("/orders/{id}/assign-rider")
  public void assignRider(@PathVariable Long id, @RequestBody AssignRiderRequest req) {
    opsService.assignRider(id, req.riderUserId());
  }

  @PostMapping("/orders/{id}/advance-status")
  public void advance(@PathVariable Long id, @RequestBody StatusUpdateRequest req) {
    orderService.advanceStatus(id, req.nextStatus());
  }

  @GetMapping("/refunds")
  public List<Refund> refunds(@RequestParam(defaultValue = "PENDING") RefundStatus status) {
    return orderService.pendingRefunds();
  }

  @PostMapping("/refunds/{refundId}/approve")
  public void approveRefund(@PathVariable Long refundId) {
    orderService.approveRefund(refundId);
  }

  @GetMapping("/audit")
  public List<AuditLog> audit() {
    return opsService.audits();
  }
}

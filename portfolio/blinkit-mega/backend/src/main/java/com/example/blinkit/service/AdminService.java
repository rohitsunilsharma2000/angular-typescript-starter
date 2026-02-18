package com.example.blinkit.service;

import com.example.blinkit.dto.AdminDtos.*;
import com.example.blinkit.entity.*;
import com.example.blinkit.exception.ApiException;
import com.example.blinkit.repository.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {
  private final ProductRepository productRepository;
  private final StoreInventoryRepository storeInventoryRepository;
  private final OrderRepository orderRepository;
  private final OpsService opsService;

  @Transactional
  public Product upsertProduct(ProductRequest req, Long id) {
    Product p = id == null ? new Product() : productRepository.findById(id).orElseThrow(() -> new ApiException("Product not found"));
    p.setCategoryId(req.categoryId());
    p.setName(req.name());
    p.setDescription(req.description());
    p.setPrice(req.price());
    p.setActive(req.active());
    Product saved = productRepository.save(p);
    opsService.audit(id == null ? "CREATE_PRODUCT" : "UPDATE_PRODUCT", "products", String.valueOf(saved.getId()));
    return saved;
  }

  @Transactional
  public void updateInventory(Long storeId, Long productId, InventoryUpdateRequest req) {
    StoreInventory inv = storeInventoryRepository.findByStoreIdAndProductId(storeId, productId)
        .orElse(StoreInventory.builder().storeId(storeId).productId(productId).reservedQty(0).build());
    inv.setStockOnHand(req.stockOnHand());
    inv.setUpdatedAt(LocalDateTime.now());
    storeInventoryRepository.save(inv);
    opsService.audit("UPDATE_INVENTORY", "store_inventory", storeId + ":" + productId);
  }

  public List<Order> orders(OrderStatus status, Long storeId) {
    if (status != null && storeId != null) {
      return orderRepository.findByStatusAndStoreId(status, storeId);
    }
    return orderRepository.findAll();
  }
}

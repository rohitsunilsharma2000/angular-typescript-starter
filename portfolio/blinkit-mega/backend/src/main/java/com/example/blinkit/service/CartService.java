package com.example.blinkit.service;

import com.example.blinkit.dto.CartDtos.*;
import com.example.blinkit.entity.*;
import com.example.blinkit.exception.ApiException;
import com.example.blinkit.repository.*;
import com.example.blinkit.util.AuthUtil;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;
  private final ProductRepository productRepository;
  private final StoreInventoryRepository storeInventoryRepository;

  @Transactional
  public CartView upsert(UpsertCartItemRequest req) {
    Long userId = AuthUtil.userId();
    Cart cart = cartRepository.findByUserId(userId)
        .orElseGet(() -> cartRepository.save(Cart.builder().userId(userId).storeId(req.storeId()).updatedAt(LocalDateTime.now()).build()));
    if (cart.getStoreId() != null && !cart.getStoreId().equals(req.storeId())) {
      throw new ApiException("Cross-store cart is not allowed");
    }
    cart.setStoreId(req.storeId());
    cart.setUpdatedAt(LocalDateTime.now());
    cartRepository.save(cart);

    StoreInventory inventory = storeInventoryRepository.findByStoreIdAndProductId(req.storeId(), req.productId())
        .orElseThrow(() -> new ApiException("Inventory not found"));
    int available = inventory.getStockOnHand() - inventory.getReservedQty();
    if (available < req.qty()) {
      throw new ApiException("Insufficient stock");
    }

    CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId(), req.productId())
        .orElseGet(() -> CartItem.builder().cartId(cart.getId()).productId(req.productId()).qty(0).build());
    item.setQty(req.qty());
    cartItemRepository.save(item);
    return get();
  }

  public CartView get() {
    Long userId = AuthUtil.userId();
    Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new ApiException("Cart not found"));
    List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
    List<CartItemView> views = new ArrayList<>();
    BigDecimal subtotal = BigDecimal.ZERO;
    for (CartItem item : items) {
      Product p = productRepository.findById(item.getProductId()).orElseThrow(() -> new ApiException("Product not found"));
      BigDecimal line = p.getPrice().multiply(BigDecimal.valueOf(item.getQty()));
      subtotal = subtotal.add(line);
      views.add(new CartItemView(item.getProductId(), p.getName(), item.getQty(), p.getPrice(), line));
    }
    return new CartView(cart.getStoreId(), views, subtotal);
  }

  @Transactional
  public void remove(Long productId) {
    Long userId = AuthUtil.userId();
    Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new ApiException("Cart not found"));
    cartItemRepository.deleteByCartIdAndProductId(cart.getId(), productId);
    log.info("cart remove userId={} productId={}", userId, productId);
  }
}

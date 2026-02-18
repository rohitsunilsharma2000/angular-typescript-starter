package com.example.blinkit.service;

import com.example.blinkit.dto.ProductDtos.ProductView;
import com.example.blinkit.entity.Category;
import com.example.blinkit.entity.Product;
import com.example.blinkit.entity.Store;
import com.example.blinkit.exception.ApiException;
import com.example.blinkit.mapper.ProductMapper;
import com.example.blinkit.repository.CategoryRepository;
import com.example.blinkit.repository.ProductRepository;
import com.example.blinkit.repository.StoreRepository;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CatalogService {
  private final CategoryRepository categoryRepository;
  private final ProductRepository productRepository;
  private final StoreRepository storeRepository;
  private final ProductMapper productMapper;

  public List<Category> categories() {
    return categoryRepository.findAll();
  }

  public List<ProductView> products(Long storeId, Long categoryId, String q, BigDecimal min, BigDecimal max, int page) {
    List<Product> all = productRepository.findByActiveTrueAndNameContainingIgnoreCase(q == null ? "" : q, PageRequest.of(page, 20)).toList();
    return all.stream()
        .filter(p -> categoryId == null || p.getCategoryId().equals(categoryId))
        .filter(p -> min == null || p.getPrice().compareTo(min) >= 0)
        .filter(p -> max == null || p.getPrice().compareTo(max) <= 0)
        .map(productMapper::toView)
        .toList();
  }

  public ProductView product(Long id, Long storeId) {
    Product product = productRepository.findById(id).orElseThrow(() -> new ApiException("Product not found"));
    return productMapper.toView(product);
  }

  public Store resolveStore(String pincode) {
    return storeRepository.findFirstByPincodeAndActiveTrue(pincode)
        .orElseThrow(() -> new ApiException("No active store for pincode"));
  }
}

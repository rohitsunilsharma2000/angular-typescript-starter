package com.example.blinkit.controller;

import com.example.blinkit.dto.ProductDtos.ProductView;
import com.example.blinkit.entity.Category;
import com.example.blinkit.service.CatalogService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CatalogController {
  private final CatalogService catalogService;

  @GetMapping("/categories")
  public List<Category> categories() {
    return catalogService.categories();
  }

  @GetMapping("/products")
  public List<ProductView> products(@RequestParam(required = false) Long storeId,
                                    @RequestParam(required = false) Long categoryId,
                                    @RequestParam(required = false) String q,
                                    @RequestParam(required = false) BigDecimal min,
                                    @RequestParam(required = false) BigDecimal max,
                                    @RequestParam(defaultValue = "0") int page) {
    return catalogService.products(storeId, categoryId, q, min, max, page);
  }

  @GetMapping("/products/{id}")
  public ProductView product(@PathVariable Long id, @RequestParam(required = false) Long storeId) {
    return catalogService.product(id, storeId);
  }
}

package com.example.blinkit.repository;

import com.example.blinkit.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
  Page<Product> findByActiveTrueAndNameContainingIgnoreCase(String q, Pageable pageable);
}

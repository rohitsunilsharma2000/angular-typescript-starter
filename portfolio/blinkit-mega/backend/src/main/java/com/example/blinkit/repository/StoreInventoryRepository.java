package com.example.blinkit.repository;

import com.example.blinkit.entity.StoreInventory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreInventoryRepository extends JpaRepository<StoreInventory, Long> {
  Optional<StoreInventory> findByStoreIdAndProductId(Long storeId, Long productId);
  List<StoreInventory> findByStoreId(Long storeId);
}

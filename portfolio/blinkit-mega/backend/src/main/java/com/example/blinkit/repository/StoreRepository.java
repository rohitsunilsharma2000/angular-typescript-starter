package com.example.blinkit.repository;

import com.example.blinkit.entity.Store;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
  Optional<Store> findFirstByPincodeAndActiveTrue(String pincode);
}

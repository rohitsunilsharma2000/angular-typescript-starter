package com.example.blinkit.repository;

import com.example.blinkit.entity.Coupon;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
  Optional<Coupon> findByCodeAndActiveTrue(String code);
}

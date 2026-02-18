package com.example.zomatox.repository;

import com.example.zomatox.entity.Coupon;
import com.example.zomatox.entity.CouponRedemption;
import com.example.zomatox.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRedemptionRepository extends JpaRepository<CouponRedemption, Long> {
  long countByCouponAndUser(Coupon coupon, User user);
}

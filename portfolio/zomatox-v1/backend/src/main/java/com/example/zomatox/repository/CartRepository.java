package com.example.zomatox.repository;

import com.example.zomatox.entity.Cart;
import com.example.zomatox.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
  Optional<Cart> findByUser(User user);
}

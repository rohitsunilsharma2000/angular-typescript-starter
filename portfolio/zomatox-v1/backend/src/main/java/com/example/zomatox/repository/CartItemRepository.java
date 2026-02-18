package com.example.zomatox.repository;

import com.example.zomatox.entity.Cart;
import com.example.zomatox.entity.CartItem;
import com.example.zomatox.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
  List<CartItem> findByCart(Cart cart);
  Optional<CartItem> findByCartAndMenuItem(Cart cart, MenuItem menuItem);
  void deleteByCartAndMenuItem(Cart cart, MenuItem menuItem);
}

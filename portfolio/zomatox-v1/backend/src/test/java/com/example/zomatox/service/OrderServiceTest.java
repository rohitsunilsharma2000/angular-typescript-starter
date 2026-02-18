package com.example.zomatox.service;

import com.example.zomatox.entity.Address;
import com.example.zomatox.entity.MenuItem;
import com.example.zomatox.entity.User;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.repository.AddressRepository;
import com.example.zomatox.repository.MenuItemRepository;
import com.example.zomatox.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {
  @Autowired UserRepository userRepo;
  @Autowired AddressRepository addressRepo;
  @Autowired MenuItemRepository menuRepo;
  @Autowired CartService cartService;
  @Autowired OrderService orderService;

  @BeforeEach
  void clearUserCart() {
    User u = userRepo.findAll().stream().filter(x -> x.getEmail().equals("user@zomatox.local")).findFirst().orElseThrow();
    cartService.clearCart(u);
  }

  @Test
  void create_order_success() {
    User u = userRepo.findAll().stream().filter(x -> x.getEmail().equals("user@zomatox.local")).findFirst().orElseThrow();
    Address addr = addressRepo.findByUser(u).get(0);
    MenuItem mi = menuRepo.findAll().stream()
      .filter(m -> m.isAvailable() && m.getStockQty() > 1)
      .findFirst()
      .orElseThrow();

    cartService.upsertItem(u, mi.getId(), 1);

    var order = orderService.createOrder(u, addr.getId());
    assertThat(order.getStatus()).isEqualTo("PAYMENT_PENDING");
    assertThat(order.getItems()).isNotEmpty();
  }

  @Test
  void stock_check_rejects_if_not_enough() {
    User u = userRepo.findAll().stream().filter(x -> x.getEmail().equals("user@zomatox.local")).findFirst().orElseThrow();
    Address addr = addressRepo.findByUser(u).get(0);

    MenuItem lowStock = menuRepo.findAll().stream().filter(m -> m.getStockQty() == 1).findFirst().orElseThrow();
    cartService.upsertItem(u, lowStock.getId(), 5);

    assertThatThrownBy(() -> orderService.createOrder(u, addr.getId()))
      .isInstanceOf(ApiException.class)
      .hasMessageContaining("Not enough stock");
  }
}

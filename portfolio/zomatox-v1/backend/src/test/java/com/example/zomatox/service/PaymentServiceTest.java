package com.example.zomatox.service;

import com.example.zomatox.entity.Address;
import com.example.zomatox.entity.MenuItem;
import com.example.zomatox.entity.User;
import com.example.zomatox.repository.AddressRepository;
import com.example.zomatox.repository.MenuItemRepository;
import com.example.zomatox.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class PaymentServiceTest {
  @Autowired UserRepository userRepo;
  @Autowired AddressRepository addressRepo;
  @Autowired MenuItemRepository menuRepo;
  @Autowired CartService cartService;
  @Autowired OrderService orderService;
  @Autowired PaymentService paymentService;

  @BeforeEach
  void clearUserCart() {
    User u = userRepo.findAll().stream().filter(x -> x.getEmail().equals("user@zomatox.local")).findFirst().orElseThrow();
    cartService.clearCart(u);
  }

  @Test
  void confirm_success_marks_paid() {
    User u = userRepo.findAll().stream().filter(x -> x.getEmail().equals("user@zomatox.local")).findFirst().orElseThrow();
    Address addr = addressRepo.findByUser(u).get(0);
    MenuItem mi = menuRepo.findAll().stream()
      .filter(m -> m.isAvailable() && m.getStockQty() > 1)
      .findFirst()
      .orElseThrow();

    cartService.upsertItem(u, mi.getId(), 1);
    var order = orderService.createOrder(u, addr.getId());

    var payment = paymentService.confirm(order.getId(), "SUCCESS");
    assertThat(payment.getStatus()).isEqualTo("SUCCESS");
  }
}

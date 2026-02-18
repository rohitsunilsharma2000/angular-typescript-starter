package com.example.zomatox.service;

import com.example.zomatox.entity.User;
import com.example.zomatox.repository.MenuItemRepository;
import com.example.zomatox.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class CartServiceTest {
  @Autowired CartService cartService;
  @Autowired UserRepository userRepo;
  @Autowired MenuItemRepository menuRepo;

  @Test
  void add_update_remove_cart_item() {
    User u = userRepo.findAll().stream().filter(x -> x.getEmail().equals("user@zomatox.local")).findFirst().orElseThrow();
    Long menuId = menuRepo.findAll().get(0).getId();

    var c1 = cartService.upsertItem(u, menuId, 2);
    assertThat(c1.getItems()).hasSize(1);
    assertThat(c1.getItemTotal()).isGreaterThan(0);

    var c2 = cartService.upsertItem(u, menuId, 3);
    assertThat(c2.getItems().get(0).getQty()).isEqualTo(3);

    var c3 = cartService.removeItem(u, menuId);
    assertThat(c3.getItems()).isEmpty();
  }
}

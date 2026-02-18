package com.example.blinkit.config;

import com.example.blinkit.entity.*;
import com.example.blinkit.repository.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeedDataConfig implements CommandLineRunner {
  private final UserRepository userRepository;
  private final CategoryRepository categoryRepository;
  private final ProductRepository productRepository;
  private final StoreRepository storeRepository;
  private final StoreInventoryRepository storeInventoryRepository;
  private final CouponRepository couponRepository;
  private final WalletRepository walletRepository;
  private final ReferralRepository referralRepository;
  private final RiderRepository riderRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) {
    if (userRepository.count() > 0) {
      return;
    }

    User admin = userRepository.save(User.builder().name("Admin").email("admin@blinkit.com").passwordHash(passwordEncoder.encode("admin123")).role(Role.ADMIN).pincode("700001").createdAt(LocalDateTime.now()).build());
    User customer = userRepository.save(User.builder().name("Customer").email("user@blinkit.com").passwordHash(passwordEncoder.encode("user123")).role(Role.CUSTOMER).pincode("700001").createdAt(LocalDateTime.now()).build());
    User riderUser = userRepository.save(User.builder().name("Rider").email("rider@blinkit.com").passwordHash(passwordEncoder.encode("rider123")).role(Role.RIDER).pincode("700001").createdAt(LocalDateTime.now()).build());
    userRepository.save(User.builder().name("Warehouse").email("warehouse@blinkit.com").passwordHash(passwordEncoder.encode("warehouse123")).role(Role.WAREHOUSE_STAFF).pincode("700001").createdAt(LocalDateTime.now()).build());

    riderRepository.save(Rider.builder().userId(riderUser.getId()).capacity(5).active(true).build());

    String[] cats = {"Fruits", "Vegetables", "Dairy", "Snacks", "Beverages", "Bakery", "Home Care", "Personal Care"};
    List<Category> categoryList = new ArrayList<>();
    for (String c : cats) {
      categoryList.add(categoryRepository.save(Category.builder().name(c).active(true).build()));
    }

    List<Product> products = new ArrayList<>();
    for (int i = 1; i <= 80; i++) {
      Category category = categoryList.get((i - 1) % categoryList.size());
      products.add(productRepository.save(Product.builder()
          .categoryId(category.getId())
          .name("Product " + i)
          .description("BlinkIt item " + i)
          .price(BigDecimal.valueOf(20 + i))
          .active(true)
          .build()));
    }

    Store s1 = storeRepository.save(Store.builder().name("Store Salt Lake").pincode("700091").active(true).build());
    Store s2 = storeRepository.save(Store.builder().name("Store New Town").pincode("700156").active(true).build());
    Store s3 = storeRepository.save(Store.builder().name("Store Park Street").pincode("700016").active(true).build());

    for (Store s : List.of(s1, s2, s3)) {
      int idx = 0;
      for (Product p : products) {
        storeInventoryRepository.save(StoreInventory.builder()
            .storeId(s.getId())
            .productId(p.getId())
            .stockOnHand(idx % 9 == 0 ? 3 : 40)
            .reservedQty(0)
            .updatedAt(LocalDateTime.now())
            .build());
        idx++;
      }
    }

    couponRepository.save(Coupon.builder().code("WELCOME50").discountType("FLAT").discountValue(BigDecimal.valueOf(50)).minCartValue(BigDecimal.valueOf(299)).maxDiscount(BigDecimal.valueOf(50)).active(true).build());
    couponRepository.save(Coupon.builder().code("FRUITS10").discountType("PERCENT").discountValue(BigDecimal.valueOf(10)).minCartValue(BigDecimal.valueOf(199)).maxDiscount(BigDecimal.valueOf(120)).categoryId(categoryList.get(0).getId()).active(true).build());
    couponRepository.save(Coupon.builder().code("BIGSAVE200").discountType("FLAT").discountValue(BigDecimal.valueOf(200)).minCartValue(BigDecimal.valueOf(999)).maxDiscount(BigDecimal.valueOf(200)).active(true).build());

    walletRepository.save(Wallet.builder().userId(customer.getId()).balance(BigDecimal.valueOf(250)).build());
    referralRepository.save(Referral.builder().referrerUserId(customer.getId()).referredUserId(riderUser.getId()).rewardCredited(false).createdAt(LocalDateTime.now()).build());
  }
}

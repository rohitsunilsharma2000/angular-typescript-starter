package com.example.zomatox.seed;

import com.example.zomatox.entity.*;
import com.example.zomatox.entity.enums.*;
import com.example.zomatox.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
  private final UserRepository userRepo;
  private final RestaurantRepository restaurantRepo;
  private final MenuItemRepository menuRepo;
  private final AddressRepository addressRepo;
  private final OrderRepository orderRepo;
  private final OrderItemRepository orderItemRepo;
  private final PaymentRepository paymentRepo;
  private final OrderEventRepository orderEventRepo;
  private final CouponRepository couponRepo;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) {
    User customer = ensureUser("Customer One", "customer@zomatox.local", "customer123", UserRole.CUSTOMER);
    User owner = ensureUser("Owner One", "owner@zomatox.local", "owner123", UserRole.OWNER);
    User delivery = ensureUser("Delivery One", "delivery@zomatox.local", "delivery123", UserRole.DELIVERY_PARTNER);
    User admin = ensureUser("Admin One", "admin@zomatox.local", "admin123", UserRole.ADMIN);

    ensureAddress(customer, "Salt Lake, Sector V", "Kolkata", "700091", "9999999999");
    ensureAddress(customer, "Indiranagar", "Bengaluru", "560038", "8888888888");
    ensureAddress(owner, "Park Street", "Kolkata", "700016", "9090909090");
    ensureAddress(delivery, "BTM Layout", "Bengaluru", "560076", "9393939393");
    ensureAddress(admin, "New Town Action Area I", "Kolkata", "700156", "9494949494");

    if (restaurantRepo.count() > 0) {
      Restaurant approved = restaurantRepo.findAll().stream()
        .filter(r -> r.getApprovalStatus() == RestaurantApprovalStatus.APPROVED)
        .min(Comparator.comparing(Restaurant::getId))
        .orElse(null);
      ensureCoupons(approved);
      return;
    }

    List<String> cities = List.of("Kolkata", "Bengaluru");
    List<String> cuisines = List.of("Bengali", "North Indian", "Chinese", "Biryani", "South Indian");

    Random rnd = new Random(7);
    Restaurant firstApproved = null;

    for (int i = 1; i <= 10; i++) {
      String city = cities.get(i <= 5 ? 0 : 1);
      RestaurantApprovalStatus approval = i <= 6 ? RestaurantApprovalStatus.APPROVED : RestaurantApprovalStatus.PENDING_APPROVAL;
      Restaurant r = restaurantRepo.save(Restaurant.builder()
        .name("Restaurant " + i)
        .city(city)
        .cuisineType(cuisines.get(rnd.nextInt(cuisines.size())))
        .ratingAvg(3.5 + rnd.nextDouble() * 1.5)
        .deliveryTimeMin(25 + rnd.nextInt(25))
        .imageUrl("https://picsum.photos/seed/rest" + i + "/640/360")
        .owner(i <= 8 ? owner : null)
        .approvalStatus(approval)
        .isBlocked(false)
        .build());

      if (firstApproved == null && r.getApprovalStatus() == RestaurantApprovalStatus.APPROVED) firstApproved = r;

      for (int j = 1; j <= 10; j++) {
        boolean veg = rnd.nextBoolean();
        int stock = (j % 7 == 0) ? 1 : (5 + rnd.nextInt(20));
        menuRepo.save(MenuItem.builder()
          .restaurant(r)
          .name((veg ? "Veg" : "Non-Veg") + " Item " + j)
          .price(80 + rnd.nextInt(220))
          .isVeg(veg)
          .available(true)
          .isBlocked(false)
          .stockQty(stock)
          .build());
      }
    }

    if (firstApproved != null) {
      MenuItem sample = menuRepo.findByRestaurantOrderByIdAsc(firstApproved).stream().findFirst().orElse(null);
      if (sample != null) {
        seedOrder(customer, firstApproved, sample, OrderStatus.CONFIRMED, null, "Order auto-confirmed after payment");
        seedOrder(customer, firstApproved, sample, OrderStatus.READY_FOR_PICKUP, null, "Order ready for pickup");
        seedOrder(customer, firstApproved, sample, OrderStatus.OUT_FOR_DELIVERY, delivery, "Out for delivery");
      }
    }

    ensureCoupons(firstApproved);
  }

  private User ensureUser(String name, String email, String rawPassword, UserRole role) {
    User user = userRepo.findByEmailIgnoreCase(email).orElse(
      User.builder().name(name).email(email).build()
    );
    user.setName(name);
    user.setRole(role);
    user.setActive(true);
    user.setPasswordHash(passwordEncoder.encode(rawPassword));
    return userRepo.save(user);
  }

  private void ensureAddress(User user, String line1, String city, String pincode, String phone) {
    boolean exists = addressRepo.findByUser(user).stream().anyMatch(a -> a.getLine1().equalsIgnoreCase(line1));
    if (!exists) {
      addressRepo.save(Address.builder().user(user).line1(line1).city(city).pincode(pincode).phone(phone).build());
    }
  }

  private void ensureCoupons(Restaurant approvedRestaurant) {
    Coupon welcome = couponRepo.findByCodeIgnoreCase("WELCOME20").orElseGet(Coupon::new);
    welcome.setCode("WELCOME20");
    welcome.setType(CouponType.PERCENT);
    welcome.setValue(20);
    welcome.setMinOrder(200);
    welcome.setMaxCap(100L);
    welcome.setValidFrom(Instant.now().minus(1, ChronoUnit.DAYS));
    welcome.setValidTo(Instant.now().plus(30, ChronoUnit.DAYS));
    welcome.setActive(true);
    welcome.setRestaurant(null);
    welcome.setUsageLimitPerUser(3);
    couponRepo.save(welcome);

    if (approvedRestaurant != null) {
      Coupon resto50 = couponRepo.findByCodeIgnoreCase("RESTO50").orElseGet(Coupon::new);
      resto50.setCode("RESTO50");
      resto50.setType(CouponType.FLAT);
      resto50.setValue(50);
      resto50.setMinOrder(300);
      resto50.setMaxCap(50L);
      resto50.setValidFrom(Instant.now().minus(1, ChronoUnit.DAYS));
      resto50.setValidTo(Instant.now().plus(30, ChronoUnit.DAYS));
      resto50.setActive(true);
      resto50.setRestaurant(approvedRestaurant);
      resto50.setUsageLimitPerUser(2);
      couponRepo.save(resto50);
    }
  }

  private void seedOrder(User customer, Restaurant restaurant, MenuItem sampleItem,
                         OrderStatus status, User deliveryPartner, String message) {
    Instant now = Instant.now();
    Order order = orderRepo.save(Order.builder()
      .user(customer)
      .restaurant(restaurant)
      .status(status)
      .itemTotal(sampleItem.getPrice())
      .deliveryFee(40)
      .discountAmount(0)
      .payableTotal(sampleItem.getPrice() + 40)
      .createdAt(now)
      .updatedAt(now)
      .deliveryPartner(deliveryPartner)
      .build());

    orderItemRepo.save(OrderItem.builder()
      .order(order)
      .menuItemNameSnapshot(sampleItem.getName())
      .priceSnapshot(sampleItem.getPrice())
      .qty(1)
      .lineTotal(sampleItem.getPrice())
      .build());

    paymentRepo.save(Payment.builder()
      .order(order)
      .status(status == OrderStatus.PAYMENT_FAILED ? PaymentStatus.FAIL : PaymentStatus.SUCCESS)
      .method("MOCK")
      .createdAt(now)
      .build());

    orderEventRepo.save(OrderEvent.builder()
      .order(order)
      .status(status.name())
      .message(message)
      .createdAt(now)
      .build());
  }
}

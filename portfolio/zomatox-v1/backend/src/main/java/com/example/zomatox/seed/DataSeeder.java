package com.example.zomatox.seed;

import com.example.zomatox.entity.*;
import com.example.zomatox.entity.enums.OrderStatus;
import com.example.zomatox.entity.enums.PaymentStatus;
import com.example.zomatox.entity.enums.UserRole;
import com.example.zomatox.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
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

  @Override
  public void run(String... args) {
    if (userRepo.count() > 0) return;

    User customer = userRepo.save(User.builder().name("Customer One").email("customer@zomatox.local").role(UserRole.CUSTOMER).build());
    User owner = userRepo.save(User.builder().name("Owner One").email("owner@zomatox.local").role(UserRole.OWNER).build());
    User delivery = userRepo.save(User.builder().name("Delivery One").email("delivery@zomatox.local").role(UserRole.DELIVERY_PARTNER).build());
    User admin = userRepo.save(User.builder().name("Admin One").email("admin@zomatox.local").role(UserRole.ADMIN).build());

    addressRepo.save(Address.builder().user(customer).line1("Salt Lake, Sector V").city("Kolkata").pincode("700091").phone("9999999999").build());
    addressRepo.save(Address.builder().user(customer).line1("Indiranagar").city("Bengaluru").pincode("560038").phone("8888888888").build());
    addressRepo.save(Address.builder().user(customer).line1("DLF Cyber City").city("Gurugram").pincode("122002").phone("9898989898").build());
    addressRepo.save(Address.builder().user(owner).line1("Park Street").city("Kolkata").pincode("700016").phone("9090909090").build());
    addressRepo.save(Address.builder().user(owner).line1("HSR Layout").city("Bengaluru").pincode("560102").phone("9191919191").build());
    addressRepo.save(Address.builder().user(delivery).line1("Lake Town").city("Kolkata").pincode("700089").phone("9292929292").build());
    addressRepo.save(Address.builder().user(delivery).line1("BTM Layout").city("Bengaluru").pincode("560076").phone("9393939393").build());
    addressRepo.save(Address.builder().user(admin).line1("New Town Action Area I").city("Kolkata").pincode("700156").phone("9494949494").build());

    List<String> cities = List.of("Kolkata", "Bengaluru");
    List<String> cuisines = List.of("Bengali", "North Indian", "Chinese", "Biryani", "South Indian");

    Random rnd = new Random(7);
    Restaurant first = null;

    for (int i = 1; i <= 10; i++) {
      String city = cities.get(i <= 5 ? 0 : 1);
      Restaurant r = restaurantRepo.save(Restaurant.builder()
        .name("Restaurant " + i)
        .city(city)
        .cuisineType(cuisines.get(rnd.nextInt(cuisines.size())))
        .ratingAvg(3.5 + rnd.nextDouble() * 1.5)
        .deliveryTimeMin(25 + rnd.nextInt(25))
        .imageUrl("https://picsum.photos/seed/rest" + i + "/640/360")
        .owner(i <= 3 ? owner : null)
        .build());

      if (first == null) first = r;

      for (int j = 1; j <= 10; j++) {
        boolean veg = rnd.nextBoolean();
        int stock = (j % 7 == 0) ? 1 : (5 + rnd.nextInt(20));
        menuRepo.save(MenuItem.builder()
          .restaurant(r)
          .name((veg ? "Veg" : "Non-Veg") + " Item " + j)
          .price(80 + rnd.nextInt(220))
          .isVeg(veg)
          .available(true)
          .stockQty(stock)
          .build());
      }
    }

    if (first != null) {
      MenuItem sample = menuRepo.findByRestaurantOrderByIdAsc(first).stream().findFirst().orElse(null);
      if (sample != null) {
        seedOrder(customer, first, sample, OrderStatus.CONFIRMED, null, "Order auto-confirmed after payment");
        seedOrder(customer, first, sample, OrderStatus.READY_FOR_PICKUP, null, "Order ready for pickup");
        seedOrder(customer, first, sample, OrderStatus.OUT_FOR_DELIVERY, delivery, "Out for delivery");
      }
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

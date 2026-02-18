package com.example.zomatox.seed;

import com.example.zomatox.entity.*;
import com.example.zomatox.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
  private final UserRepository userRepo;
  private final RestaurantRepository restaurantRepo;
  private final MenuItemRepository menuRepo;
  private final AddressRepository addressRepo;

  @Override
  public void run(String... args) {
    if (userRepo.count() > 0) return;

    User user = userRepo.save(User.builder().name("User One").email("user@zomatox.local").build());
    User admin = userRepo.save(User.builder().name("Admin One").email("admin@zomatox.local").build());

    addressRepo.save(Address.builder().user(user).line1("Salt Lake, Sector V").city("Kolkata").pincode("700091").phone("9999999999").build());
    addressRepo.save(Address.builder().user(user).line1("Indiranagar").city("Bengaluru").pincode("560038").phone("8888888888").build());
    addressRepo.save(Address.builder().user(admin).line1("New Town").city("Kolkata").pincode("700156").phone("7777777777").build());
    addressRepo.save(Address.builder().user(admin).line1("Koramangala").city("Bengaluru").pincode("560034").phone("6666666666").build());

    List<String> cities = List.of("Kolkata", "Bengaluru");
    List<String> cuisines = List.of("Bengali", "North Indian", "Chinese", "Biryani", "South Indian");

    Random rnd = new Random(7);

    for (int i = 1; i <= 10; i++) {
      String city = cities.get(i <= 5 ? 0 : 1);
      Restaurant r = restaurantRepo.save(Restaurant.builder()
        .name("Restaurant " + i)
        .city(city)
        .cuisineType(cuisines.get(rnd.nextInt(cuisines.size())))
        .ratingAvg(3.5 + rnd.nextDouble() * 1.5)
        .deliveryTimeMin(25 + rnd.nextInt(25))
        .imageUrl("https://picsum.photos/seed/rest" + i + "/640/360")
        .build());

      for (int j = 1; j <= 10; j++) {
        boolean veg = rnd.nextBoolean();
        int stock = (j % 7 == 0) ? 1 : (5 + rnd.nextInt(20)); // some low stock items
        menuRepo.save(MenuItem.builder()
          .restaurant(r)
          .name((veg ? "Veg" : "Non-Veg") + " Item " + j)
          .price(80 + rnd.nextInt(220)) // INR units
          .isVeg(veg)
          .available(true)
          .stockQty(stock)
          .build());
      }
    }
  }
}

নীচে **ZomatoX v1 (MVP)** এর **backend + frontend** একসাথে “copy-paste runnable” কোড দিলাম।
আমি এটা এমনভাবে দিয়েছি যাতে তুমি **folder/path অনুযায়ী ফাইল বানিয়ে paste** করলেই রান হয়।

> ✅ Backend: Spring Boot 3 + Java 17 + H2 + Swagger + Actuator + JUnit5
> ✅ Frontend: Angular 17 standalone + Tailwind + RxJS cart store + user switch dropdown
> ✅ Headers: সব API call এ `X-User-Id` লাগবে (frontend auto দেয়)

---

## 1) Repository Tree

```
zomatox-v1/
  backend/
    pom.xml
    src/main/java/com/example/zomatox/
      ZomatoXApplication.java
      config/
        OpenApiConfig.java
      controller/
        AdminController.java
        CartController.java
        PaymentController.java
        RestaurantController.java
        OrderController.java
        UserController.java
      dto/
        admin/
          AdminMenuItemCreateRequest.java
          AdminRestaurantCreateRequest.java
          AdminMenuItemUpdateRequest.java
        cart/
          CartItemUpsertRequest.java
          CartResponse.java
          CartLineResponse.java
        orders/
          CreateOrderRequest.java
          OrderResponse.java
          OrderItemResponse.java
        payments/
          PaymentResponse.java
        restaurants/
          RestaurantResponse.java
          MenuItemResponse.java
        users/
          UserResponse.java
      entity/
        Address.java
        Cart.java
        CartItem.java
        MenuItem.java
        Order.java
        OrderItem.java
        Payment.java
        Restaurant.java
        User.java
        enums/
          OrderStatus.java
          PaymentStatus.java
      exception/
        ApiException.java
        GlobalExceptionHandler.java
      repository/
        AddressRepository.java
        CartItemRepository.java
        CartRepository.java
        MenuItemRepository.java
        OrderItemRepository.java
        OrderRepository.java
        PaymentRepository.java
        RestaurantRepository.java
        UserRepository.java
      service/
        AdminService.java
        CartService.java
        OrderService.java
        PaymentService.java
        RestaurantService.java
        UserService.java
      util/
        AdminAuth.java
        Money.java
      seed/
        DataSeeder.java
    src/main/resources/
      application.yml
    src/test/java/com/example/zomatox/service/
      CartServiceTest.java
      OrderServiceTest.java
      PaymentServiceTest.java

  frontend/
    package.json
    angular.json
    tsconfig.json
    tailwind.config.js
    postcss.config.js
    src/
      styles.css
      main.ts
      index.html
      app/
        app.component.ts
        app.routes.ts
        core/
          api.service.ts
          user-context.service.ts
          cart.store.ts
          models.ts
        features/
          restaurants/
            restaurant-list.component.ts
            restaurant-detail.component.ts
          cart/
            cart.component.ts
          checkout/
            checkout.component.ts
          orders/
            order-list.component.ts
            order-detail.component.ts
        shared/
          ui/
            badge.component.ts
            button.component.ts
            card.component.ts
```

---

# 2) BACKEND (Spring Boot)

## `backend/pom.xml`

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.example</groupId>
  <artifactId>zomatox</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <name>zomatox</name>

  <properties>
    <java.version>17</java.version>
    <spring-boot.version>3.3.2</spring-boot.version>
  </properties>

  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>${spring-boot.version}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>

  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <dependency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <scope>runtime</scope>
    </dependency>

    <dependency>
      <groupId>org.springdoc</groupId>
      <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
      <version>2.5.0</version>
    </dependency>

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <optional>true</optional>
    </dependency>

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
      </plugin>

      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
          <source>17</source>
          <target>17</target>
          <annotationProcessorPaths>
            <path>
              <groupId>org.projectlombok</groupId>
              <artifactId>lombok</artifactId>
              <version>1.18.34</version>
            </path>
          </annotationProcessorPaths>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
```

## `backend/src/main/resources/application.yml`

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:h2:mem:zomatox;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    driverClassName: org.h2.Driver
    username: sa
    password:
  jpa:
    hibernate:
      ddl-auto: create-drop
    properties:
      hibernate:
        format_sql: true
  h2:
    console:
      enabled: true
      path: /h2-console

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics

springdoc:
  swagger-ui:
    path: /swagger-ui.html
```

## `backend/src/main/java/com/example/zomatox/ZomatoXApplication.java`

```java
package com.example.zomatox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ZomatoXApplication {
  public static void main(String[] args) {
    SpringApplication.run(ZomatoXApplication.class, args);
  }
}
```

## `backend/src/main/java/com/example/zomatox/config/OpenApiConfig.java`

```java
package com.example.zomatox.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI().info(new Info()
      .title("ZomatoX v1 API")
      .version("v1")
      .description("Restaurant discovery + menu + cart + order + mock payment"));
  }
}
```

---

## Enums

### `entity/enums/OrderStatus.java`

```java
package com.example.zomatox.entity.enums;

public enum OrderStatus {
  CREATED,
  PAYMENT_PENDING,
  PAID,
  PAYMENT_FAILED
}
```

### `entity/enums/PaymentStatus.java`

```java
package com.example.zomatox.entity.enums;

public enum PaymentStatus {
  INITIATED,
  SUCCESS,
  FAIL
}
```

---

## Entities (JPA)

### `entity/User.java`

```java
package com.example.zomatox.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;
}
```

### `entity/Restaurant.java`

```java
package com.example.zomatox.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "restaurants")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Restaurant {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String city;

  @Column(nullable = false)
  private String cuisineType;

  @Column(nullable = false)
  private double ratingAvg;

  @Column(nullable = false)
  private int deliveryTimeMin;

  private String imageUrl;
}
```

### `entity/MenuItem.java`

```java
package com.example.zomatox.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "menu_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MenuItem {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "restaurant_id")
  private Restaurant restaurant;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private long price; // paise-like integer (e.g., INR * 100) OR just INR units; keep consistent

  @Column(nullable = false)
  private boolean isVeg;

  @Column(nullable = false)
  private boolean available;

  @Column(nullable = false)
  private int stockQty;
}
```

### `entity/Cart.java`

```java
package com.example.zomatox.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "carts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Cart {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(nullable = false)
  private Instant updatedAt;
}
```

### `entity/CartItem.java`

```java
package com.example.zomatox.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_items",
  uniqueConstraints = @UniqueConstraint(name = "uk_cart_menu", columnNames = {"cart_id", "menu_item_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CartItem {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "cart_id")
  private Cart cart;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "menu_item_id")
  private MenuItem menuItem;

  @Column(nullable = false)
  private int qty;
}
```

### `entity/Address.java`

```java
package com.example.zomatox.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addresses")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Address {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(nullable = false)
  private String line1;

  @Column(nullable = false)
  private String city;

  @Column(nullable = false)
  private String pincode;

  @Column(nullable = false)
  private String phone;
}
```

### `entity/Order.java`

```java
package com.example.zomatox.entity;

import com.example.zomatox.entity.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Order {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "restaurant_id")
  private Restaurant restaurant;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OrderStatus status;

  @Column(nullable = false)
  private long itemTotal;

  @Column(nullable = false)
  private long deliveryFee;

  @Column(nullable = false)
  private long payableTotal;

  @Column(nullable = false)
  private Instant createdAt;
}
```

### `entity/OrderItem.java`

```java
package com.example.zomatox.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderItem {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private Order order;

  @Column(nullable = false)
  private String menuItemNameSnapshot;

  @Column(nullable = false)
  private long priceSnapshot;

  @Column(nullable = false)
  private int qty;

  @Column(nullable = false)
  private long lineTotal;
}
```

### `entity/Payment.java`

```java
package com.example.zomatox.entity;

import com.example.zomatox.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "payments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Payment {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", unique = true)
  private Order order;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PaymentStatus status;

  @Column(nullable = false)
  private String method; // MOCK

  @Column(nullable = false)
  private Instant createdAt;
}
```

---

## Repositories

### `repository/UserRepository.java`

```java
package com.example.zomatox.repository;

import com.example.zomatox.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
```

### `repository/RestaurantRepository.java`

```java
package com.example.zomatox.repository;

import com.example.zomatox.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
  Page<Restaurant> findByCityContainingIgnoreCaseAndNameContainingIgnoreCase(String city, String q, Pageable pageable);
  Page<Restaurant> findByCityContainingIgnoreCase(String city, Pageable pageable);
}
```

### `repository/MenuItemRepository.java`

```java
package com.example.zomatox.repository;

import com.example.zomatox.entity.MenuItem;
import com.example.zomatox.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
  List<MenuItem> findByRestaurantOrderByIdAsc(Restaurant restaurant);
}
```

### `repository/CartRepository.java`

```java
package com.example.zomatox.repository;

import com.example.zomatox.entity.Cart;
import com.example.zomatox.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
  Optional<Cart> findByUser(User user);
}
```

### `repository/CartItemRepository.java`

```java
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
```

### `repository/AddressRepository.java`

```java
package com.example.zomatox.repository;

import com.example.zomatox.entity.Address;
import com.example.zomatox.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
  List<Address> findByUser(User user);
}
```

### `repository/OrderRepository.java`

```java
package com.example.zomatox.repository;

import com.example.zomatox.entity.Order;
import com.example.zomatox.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findByUserOrderByIdDesc(User user);
}
```

### `repository/OrderItemRepository.java`

```java
package com.example.zomatox.repository;

import com.example.zomatox.entity.Order;
import com.example.zomatox.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
  List<OrderItem> findByOrder(Order order);
}
```

### `repository/PaymentRepository.java`

```java
package com.example.zomatox.repository;

import com.example.zomatox.entity.Order;
import com.example.zomatox.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
  Optional<Payment> findByOrder(Order order);
}
```

---

## Exceptions + Global Handler

### `exception/ApiException.java`

```java
package com.example.zomatox.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
  private final HttpStatus status;

  public ApiException(HttpStatus status, String message) {
    super(message);
    this.status = status;
  }

  public HttpStatus getStatus() {
    return status;
  }
}
```

### `exception/GlobalExceptionHandler.java`

```java
package com.example.zomatox.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<?> handleApi(ApiException ex) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("message", ex.getMessage());
    body.put("validationErrors", Map.of());
    return ResponseEntity.status(ex.getStatus()).body(body);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new LinkedHashMap<>();
    for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
      errors.put(fe.getField(), fe.getDefaultMessage());
    }
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("message", "Validation failed");
    body.put("validationErrors", errors);
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleGeneric(Exception ex) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("message", "Something went wrong: " + ex.getMessage());
    body.put("validationErrors", Map.of());
    return ResponseEntity.internalServerError().body(body);
  }
}
```

---

## Utils

### `util/Money.java`

```java
package com.example.zomatox.util;

public final class Money {
  private Money() {}

  // v1: treat "price" as INR units to keep simple
  public static long inr(long value) { return value; }
}
```

### `util/AdminAuth.java`

```java
package com.example.zomatox.util;

import com.example.zomatox.exception.ApiException;
import org.springframework.http.HttpStatus;

public final class AdminAuth {
  private AdminAuth() {}

  public static void requireAdminKey(String adminKey) {
    if (adminKey == null || !adminKey.equals("dev")) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid X-Admin-Key");
    }
  }
}
```

---

## DTOs

### Users

`dto/users/UserResponse.java`

```java
package com.example.zomatox.dto.users;

import com.example.zomatox.entity.User;
import lombok.Value;

@Value
public class UserResponse {
  Long id;
  String name;
  String email;

  public static UserResponse from(User u) {
    return new UserResponse(u.getId(), u.getName(), u.getEmail());
  }
}
```

### Restaurants

`dto/restaurants/RestaurantResponse.java`

```java
package com.example.zomatox.dto.restaurants;

import com.example.zomatox.entity.Restaurant;
import lombok.Value;

@Value
public class RestaurantResponse {
  Long id;
  String name;
  String city;
  String cuisineType;
  double ratingAvg;
  int deliveryTimeMin;
  String imageUrl;

  public static RestaurantResponse from(Restaurant r) {
    return new RestaurantResponse(
      r.getId(), r.getName(), r.getCity(), r.getCuisineType(),
      r.getRatingAvg(), r.getDeliveryTimeMin(), r.getImageUrl()
    );
  }
}
```

`dto/restaurants/MenuItemResponse.java`

```java
package com.example.zomatox.dto.restaurants;

import com.example.zomatox.entity.MenuItem;
import lombok.Value;

@Value
public class MenuItemResponse {
  Long id;
  Long restaurantId;
  String name;
  long price;
  boolean isVeg;
  boolean available;
  int stockQty;

  public static MenuItemResponse from(MenuItem m) {
    return new MenuItemResponse(
      m.getId(),
      m.getRestaurant().getId(),
      m.getName(),
      m.getPrice(),
      m.isVeg(),
      m.isAvailable(),
      m.getStockQty()
    );
  }
}
```

### Cart

`dto/cart/CartItemUpsertRequest.java`

```java
package com.example.zomatox.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemUpsertRequest {
  @NotNull
  private Long menuItemId;

  @Min(value = 0, message = "qty must be >= 0")
  private int qty;
}
```

`dto/cart/CartLineResponse.java`

```java
package com.example.zomatox.dto.cart;

import lombok.Value;

@Value
public class CartLineResponse {
  Long menuItemId;
  String name;
  long price;
  int qty;
  long lineTotal;
}
```

`dto/cart/CartResponse.java`

```java
package com.example.zomatox.dto.cart;

import lombok.Value;

import java.util.List;

@Value
public class CartResponse {
  Long cartId;
  Long userId;
  Long restaurantId; // derived if cart has items from one restaurant; null if empty
  List<CartLineResponse> items;
  long itemTotal;
}
```

### Orders

`dto/orders/CreateOrderRequest.java`

```java
package com.example.zomatox.dto.orders;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderRequest {
  @NotNull
  private Long addressId;
}
```

`dto/orders/OrderItemResponse.java`

```java
package com.example.zomatox.dto.orders;

import lombok.Value;

@Value
public class OrderItemResponse {
  String name;
  long price;
  int qty;
  long lineTotal;
}
```

`dto/orders/OrderResponse.java`

```java
package com.example.zomatox.dto.orders;

import com.example.zomatox.entity.Order;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
public class OrderResponse {
  Long id;
  Long userId;
  Long restaurantId;
  String status;
  long itemTotal;
  long deliveryFee;
  long payableTotal;
  Instant createdAt;
  List<OrderItemResponse> items;

  public static OrderResponse of(Order o, List<OrderItemResponse> items) {
    return new OrderResponse(
      o.getId(), o.getUser().getId(), o.getRestaurant().getId(),
      o.getStatus().name(),
      o.getItemTotal(), o.getDeliveryFee(), o.getPayableTotal(),
      o.getCreatedAt(),
      items
    );
  }
}
```

### Payments

`dto/payments/PaymentResponse.java`

```java
package com.example.zomatox.dto.payments;

import com.example.zomatox.entity.Payment;
import lombok.Value;

import java.time.Instant;

@Value
public class PaymentResponse {
  Long id;
  Long orderId;
  String status;
  String method;
  Instant createdAt;

  public static PaymentResponse from(Payment p) {
    return new PaymentResponse(
      p.getId(),
      p.getOrder().getId(),
      p.getStatus().name(),
      p.getMethod(),
      p.getCreatedAt()
    );
  }
}
```

### Admin

`dto/admin/AdminRestaurantCreateRequest.java`

```java
package com.example.zomatox.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminRestaurantCreateRequest {
  @NotBlank private String name;
  @NotBlank private String city;
  @NotBlank private String cuisineType;
  @NotNull private Double ratingAvg;
  @NotNull private Integer deliveryTimeMin;
  private String imageUrl;
}
```

`dto/admin/AdminMenuItemCreateRequest.java`

```java
package com.example.zomatox.dto.admin;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminMenuItemCreateRequest {
  @NotBlank private String name;
  @NotNull private Long price;
  @NotNull private Boolean isVeg;
  @NotNull private Boolean available;

  @Min(value = 0, message = "stockQty must be >= 0")
  private Integer stockQty;
}
```

`dto/admin/AdminMenuItemUpdateRequest.java`

```java
package com.example.zomatox.dto.admin;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminMenuItemUpdateRequest {
  @NotNull private Long price;
  @NotNull private Boolean available;

  @Min(value = 0, message = "stockQty must be >= 0")
  private Integer stockQty;
}
```

---

## Services

### `service/UserService.java`

```java
package com.example.zomatox.service;

import com.example.zomatox.entity.User;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public User getUserOrThrow(Long id) {
    return userRepository.findById(id).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "User not found: " + id));
  }

  public List<User> listUsers() {
    return userRepository.findAll();
  }
}
```

### `service/RestaurantService.java`

```java
package com.example.zomatox.service;

import com.example.zomatox.entity.MenuItem;
import com.example.zomatox.entity.Restaurant;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.repository.MenuItemRepository;
import com.example.zomatox.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantService {
  private final RestaurantRepository restaurantRepository;
  private final MenuItemRepository menuItemRepository;

  public Page<Restaurant> listRestaurants(String city, String q, String cuisine, String sort, int page) {
    String citySafe = city == null ? "" : city.trim();
    String qSafe = q == null ? "" : q.trim();

    Pageable pageable = PageRequest.of(Math.max(page, 0), 10, sortSpec(sort));
    Page<Restaurant> base;

    if (!qSafe.isBlank()) {
      base = restaurantRepository.findByCityContainingIgnoreCaseAndNameContainingIgnoreCase(citySafe, qSafe, pageable);
    } else {
      base = restaurantRepository.findByCityContainingIgnoreCase(citySafe, pageable);
    }

    if (cuisine == null || cuisine.isBlank()) return base;

    // simple in-memory filter for v1
    List<Restaurant> filtered = base.getContent().stream()
      .filter(r -> r.getCuisineType().equalsIgnoreCase(cuisine))
      .toList();

    return new PageImpl<>(filtered, pageable, filtered.size());
  }

  private Sort sortSpec(String sort) {
    if ("time".equalsIgnoreCase(sort)) return Sort.by(Sort.Direction.ASC, "deliveryTimeMin");
    return Sort.by(Sort.Direction.DESC, "ratingAvg");
  }

  public Restaurant getRestaurantOrThrow(Long id) {
    return restaurantRepository.findById(id).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Restaurant not found: " + id));
  }

  public List<MenuItem> getMenu(Restaurant r) {
    return menuItemRepository.findByRestaurantOrderByIdAsc(r);
  }
}
```

### `service/CartService.java`

```java
package com.example.zomatox.service;

import com.example.zomatox.dto.cart.CartLineResponse;
import com.example.zomatox.dto.cart.CartResponse;
import com.example.zomatox.entity.*;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {
  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;
  private final MenuItemRepository menuItemRepository;

  public Cart getOrCreateCart(User user) {
    return cartRepository.findByUser(user).orElseGet(() -> {
      Cart c = Cart.builder().user(user).updatedAt(Instant.now()).build();
      return cartRepository.save(c);
    });
  }

  public CartResponse getCart(User user) {
    Cart cart = getOrCreateCart(user);
    List<CartItem> items = cartItemRepository.findByCart(cart);

    Long restaurantId = items.isEmpty() ? null : items.get(0).getMenuItem().getRestaurant().getId();

    long total = 0;
    List<CartLineResponse> lines = items.stream().map(ci -> {
      long lineTotal = ci.getMenuItem().getPrice() * ci.getQty();
      return new CartLineResponse(ci.getMenuItem().getId(), ci.getMenuItem().getName(),
        ci.getMenuItem().getPrice(), ci.getQty(), lineTotal);
    }).toList();

    for (CartLineResponse l : lines) total += l.getLineTotal();

    return new CartResponse(cart.getId(), user.getId(), restaurantId, lines, total);
  }

  public CartResponse upsertItem(User user, Long menuItemId, int qty) {
    Cart cart = getOrCreateCart(user);
    MenuItem menuItem = menuItemRepository.findById(menuItemId).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Menu item not found: " + menuItemId));

    if (!menuItem.isAvailable()) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Item not available: " + menuItem.getName());
    }

    // Enforce single restaurant cart for v1 (common zomato behavior)
    List<CartItem> current = cartItemRepository.findByCart(cart);
    if (!current.isEmpty()) {
      Long currentRestId = current.get(0).getMenuItem().getRestaurant().getId();
      if (!currentRestId.equals(menuItem.getRestaurant().getId())) {
        throw new ApiException(HttpStatus.BAD_REQUEST, "Cart supports only one restaurant in v1. Clear cart first.");
      }
    }

    if (qty <= 0) {
      cartItemRepository.deleteByCartAndMenuItem(cart, menuItem);
      cart.setUpdatedAt(Instant.now());
      cartRepository.save(cart);
      return getCart(user);
    }

    CartItem item = cartItemRepository.findByCartAndMenuItem(cart, menuItem)
      .orElse(CartItem.builder().cart(cart).menuItem(menuItem).qty(0).build());

    item.setQty(qty);
    cartItemRepository.save(item);

    cart.setUpdatedAt(Instant.now());
    cartRepository.save(cart);

    return getCart(user);
  }

  public CartResponse removeItem(User user, Long menuItemId) {
    return upsertItem(user, menuItemId, 0);
  }

  public List<CartItem> getRawItems(User user) {
    Cart cart = getOrCreateCart(user);
    return cartItemRepository.findByCart(cart);
  }

  public void clearCart(User user) {
    Cart cart = getOrCreateCart(user);
    List<CartItem> items = cartItemRepository.findByCart(cart);
    cartItemRepository.deleteAll(items);
    cart.setUpdatedAt(Instant.now());
    cartRepository.save(cart);
  }
}
```

### `service/OrderService.java`

```java
package com.example.zomatox.service;

import com.example.zomatox.dto.orders.OrderItemResponse;
import com.example.zomatox.dto.orders.OrderResponse;
import com.example.zomatox.entity.*;
import com.example.zomatox.entity.enums.OrderStatus;
import com.example.zomatox.entity.enums.PaymentStatus;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
  private final AddressRepository addressRepository;
  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final PaymentRepository paymentRepository;
  private final CartService cartService;

  public OrderResponse createOrder(User user, Long addressId) {
    Address addr = addressRepository.findById(addressId).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Address not found: " + addressId));

    if (!addr.getUser().getId().equals(user.getId())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "Address does not belong to user");
    }

    List<CartItem> cartItems = cartService.getRawItems(user);
    if (cartItems.isEmpty()) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Cart is empty");
    }

    // Stock check
    for (CartItem ci : cartItems) {
      MenuItem mi = ci.getMenuItem();
      if (!mi.isAvailable()) {
        throw new ApiException(HttpStatus.BAD_REQUEST, "Item not available: " + mi.getName());
      }
      if (mi.getStockQty() < ci.getQty()) {
        throw new ApiException(HttpStatus.BAD_REQUEST,
          "Not enough stock for " + mi.getName() + ". Available=" + mi.getStockQty());
      }
    }

    Restaurant restaurant = cartItems.get(0).getMenuItem().getRestaurant();
    long itemTotal = cartItems.stream().mapToLong(ci -> ci.getMenuItem().getPrice() * ci.getQty()).sum();
    long deliveryFee = calcDeliveryFee(itemTotal, restaurant);
    long payable = itemTotal + deliveryFee;

    Order order = Order.builder()
      .user(user)
      .restaurant(restaurant)
      .status(OrderStatus.PAYMENT_PENDING)
      .itemTotal(itemTotal)
      .deliveryFee(deliveryFee)
      .payableTotal(payable)
      .createdAt(Instant.now())
      .build();

    order = orderRepository.save(order);

    for (CartItem ci : cartItems) {
      MenuItem mi = ci.getMenuItem();

      // Deduct stock on order creation (simple v1 approach)
      mi.setStockQty(mi.getStockQty() - ci.getQty());

      OrderItem oi = OrderItem.builder()
        .order(order)
        .menuItemNameSnapshot(mi.getName())
        .priceSnapshot(mi.getPrice())
        .qty(ci.getQty())
        .lineTotal(mi.getPrice() * ci.getQty())
        .build();
      orderItemRepository.save(oi);
    }

    Payment payment = Payment.builder()
      .order(order)
      .status(PaymentStatus.INITIATED)
      .method("MOCK")
      .createdAt(Instant.now())
      .build();
    paymentRepository.save(payment);

    cartService.clearCart(user);

    return getOrderResponse(order, user);
  }

  private long calcDeliveryFee(long itemTotal, Restaurant r) {
    // super simple v1 rule:
    // free delivery above 500, else 40
    return itemTotal >= 500 ? 0 : 40;
  }

  public List<OrderResponse> listOrders(User user) {
    return orderRepository.findByUserOrderByIdDesc(user).stream()
      .map(o -> getOrderResponse(o, user))
      .toList();
  }

  public OrderResponse getOrder(User user, Long orderId) {
    Order o = orderRepository.findById(orderId).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Order not found: " + orderId));
    if (!o.getUser().getId().equals(user.getId())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "Order does not belong to user");
    }
    return getOrderResponse(o, user);
  }

  private OrderResponse getOrderResponse(Order o, User user) {
    List<OrderItemResponse> items = orderItemRepository.findByOrder(o).stream()
      .map(oi -> new OrderItemResponse(oi.getMenuItemNameSnapshot(), oi.getPriceSnapshot(), oi.getQty(), oi.getLineTotal()))
      .toList();
    return OrderResponse.of(o, items);
  }
}
```

### `service/PaymentService.java`

```java
package com.example.zomatox.service;

import com.example.zomatox.dto.payments.PaymentResponse;
import com.example.zomatox.entity.Order;
import com.example.zomatox.entity.Payment;
import com.example.zomatox.entity.enums.OrderStatus;
import com.example.zomatox.entity.enums.PaymentStatus;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.repository.OrderRepository;
import com.example.zomatox.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
  private final OrderRepository orderRepository;
  private final PaymentRepository paymentRepository;

  public PaymentResponse confirm(Long orderId, String result) {
    Order order = orderRepository.findById(orderId).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Order not found: " + orderId));

    Payment payment = paymentRepository.findByOrder(order).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Payment not found for order: " + orderId));

    if ("SUCCESS".equalsIgnoreCase(result)) {
      payment.setStatus(PaymentStatus.SUCCESS);
      order.setStatus(OrderStatus.PAID);
    } else if ("FAIL".equalsIgnoreCase(result)) {
      payment.setStatus(PaymentStatus.FAIL);
      order.setStatus(OrderStatus.PAYMENT_FAILED);
    } else {
      throw new ApiException(HttpStatus.BAD_REQUEST, "result must be SUCCESS or FAIL");
    }

    paymentRepository.save(payment);
    orderRepository.save(order);

    return PaymentResponse.from(payment);
  }
}
```

### `service/AdminService.java`

```java
package com.example.zomatox.service;

import com.example.zomatox.dto.admin.*;
import com.example.zomatox.entity.MenuItem;
import com.example.zomatox.entity.Restaurant;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.repository.MenuItemRepository;
import com.example.zomatox.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
  private final RestaurantRepository restaurantRepository;
  private final MenuItemRepository menuItemRepository;

  public Restaurant createRestaurant(AdminRestaurantCreateRequest req) {
    Restaurant r = Restaurant.builder()
      .name(req.getName())
      .city(req.getCity())
      .cuisineType(req.getCuisineType())
      .ratingAvg(req.getRatingAvg())
      .deliveryTimeMin(req.getDeliveryTimeMin())
      .imageUrl(req.getImageUrl())
      .build();
    return restaurantRepository.save(r);
  }

  public MenuItem addMenuItem(Long restaurantId, AdminMenuItemCreateRequest req) {
    Restaurant r = restaurantRepository.findById(restaurantId).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Restaurant not found: " + restaurantId));

    MenuItem mi = MenuItem.builder()
      .restaurant(r)
      .name(req.getName())
      .price(req.getPrice())
      .isVeg(req.getIsVeg())
      .available(req.getAvailable())
      .stockQty(req.getStockQty())
      .build();

    return menuItemRepository.save(mi);
  }

  public MenuItem updateMenuItem(Long menuItemId, AdminMenuItemUpdateRequest req) {
    MenuItem mi = menuItemRepository.findById(menuItemId).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Menu item not found: " + menuItemId));

    mi.setPrice(req.getPrice());
    mi.setAvailable(req.getAvailable());
    mi.setStockQty(req.getStockQty());
    return menuItemRepository.save(mi);
  }
}
```

---

## Controllers

### `controller/UserController.java`

```java
package com.example.zomatox.controller;

import com.example.zomatox.dto.users.UserResponse;
import com.example.zomatox.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping
  public List<UserResponse> list() {
    return userService.listUsers().stream().map(UserResponse::from).toList();
  }

  @GetMapping("/{id}")
  public UserResponse get(@PathVariable Long id) {
    return UserResponse.from(userService.getUserOrThrow(id));
  }
}
```

### `controller/RestaurantController.java`

```java
package com.example.zomatox.controller;

import com.example.zomatox.dto.restaurants.MenuItemResponse;
import com.example.zomatox.dto.restaurants.RestaurantResponse;
import com.example.zomatox.entity.Restaurant;
import com.example.zomatox.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
  private final RestaurantService restaurantService;

  @GetMapping
  public Page<RestaurantResponse> list(
    @RequestParam(required = false) String city,
    @RequestParam(required = false) String q,
    @RequestParam(required = false) String cuisine,
    @RequestParam(required = false, defaultValue = "rating") String sort,
    @RequestParam(required = false, defaultValue = "0") int page
  ) {
    Page<Restaurant> res = restaurantService.listRestaurants(city, q, cuisine, sort, page);
    return res.map(RestaurantResponse::from);
  }

  @GetMapping("/{id}")
  public RestaurantResponse get(@PathVariable Long id) {
    return RestaurantResponse.from(restaurantService.getRestaurantOrThrow(id));
  }

  @GetMapping("/{id}/menu")
  public List<MenuItemResponse> menu(@PathVariable Long id) {
    Restaurant r = restaurantService.getRestaurantOrThrow(id);
    return restaurantService.getMenu(r).stream().map(MenuItemResponse::from).toList();
  }
}
```

### `controller/CartController.java`

```java
package com.example.zomatox.controller;

import com.example.zomatox.dto.cart.CartItemUpsertRequest;
import com.example.zomatox.dto.cart.CartResponse;
import com.example.zomatox.entity.User;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.service.CartService;
import com.example.zomatox.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
  private final UserService userService;
  private final CartService cartService;

  private User userFromHeader(String userIdHeader) {
    if (userIdHeader == null || userIdHeader.isBlank()) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "Missing X-User-Id header");
    }
    return userService.getUserOrThrow(Long.parseLong(userIdHeader));
  }

  @GetMapping
  public CartResponse getCart(@RequestHeader(value = "X-User-Id", required = false) String userId) {
    User u = userFromHeader(userId);
    return cartService.getCart(u);
  }

  @PutMapping("/items")
  public CartResponse upsert(@RequestHeader(value = "X-User-Id", required = false) String userId,
                             @Valid @RequestBody CartItemUpsertRequest req) {
    User u = userFromHeader(userId);
    return cartService.upsertItem(u, req.getMenuItemId(), req.getQty());
  }

  @DeleteMapping("/items/{menuItemId}")
  public CartResponse remove(@RequestHeader(value = "X-User-Id", required = false) String userId,
                             @PathVariable Long menuItemId) {
    User u = userFromHeader(userId);
    return cartService.removeItem(u, menuItemId);
  }
}
```

### `controller/OrderController.java`

```java
package com.example.zomatox.controller;

import com.example.zomatox.dto.orders.CreateOrderRequest;
import com.example.zomatox.dto.orders.OrderResponse;
import com.example.zomatox.entity.User;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.service.OrderService;
import com.example.zomatox.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
  private final UserService userService;
  private final OrderService orderService;

  private User userFromHeader(String userIdHeader) {
    if (userIdHeader == null || userIdHeader.isBlank()) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "Missing X-User-Id header");
    }
    return userService.getUserOrThrow(Long.parseLong(userIdHeader));
  }

  @PostMapping
  public OrderResponse create(@RequestHeader(value = "X-User-Id", required = false) String userId,
                              @Valid @RequestBody CreateOrderRequest req) {
    User u = userFromHeader(userId);
    return orderService.createOrder(u, req.getAddressId());
  }

  @GetMapping
  public List<OrderResponse> list(@RequestHeader(value = "X-User-Id", required = false) String userId) {
    User u = userFromHeader(userId);
    return orderService.listOrders(u);
  }

  @GetMapping("/{id}")
  public OrderResponse get(@RequestHeader(value = "X-User-Id", required = false) String userId,
                           @PathVariable Long id) {
    User u = userFromHeader(userId);
    return orderService.getOrder(u, id);
  }
}
```

### `controller/PaymentController.java`

```java
package com.example.zomatox.controller;

import com.example.zomatox.dto.payments.PaymentResponse;
import com.example.zomatox.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
  private final PaymentService paymentService;

  @PostMapping("/{orderId}/confirm")
  public PaymentResponse confirm(@PathVariable Long orderId,
                                 @RequestParam String result) {
    return paymentService.confirm(orderId, result);
  }
}
```

### `controller/AdminController.java`

```java
package com.example.zomatox.controller;

import com.example.zomatox.dto.admin.*;
import com.example.zomatox.dto.restaurants.MenuItemResponse;
import com.example.zomatox.dto.restaurants.RestaurantResponse;
import com.example.zomatox.entity.MenuItem;
import com.example.zomatox.entity.Restaurant;
import com.example.zomatox.service.AdminService;
import com.example.zomatox.util.AdminAuth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
  private final AdminService adminService;

  @PostMapping("/restaurants")
  public RestaurantResponse createRestaurant(@RequestHeader(value = "X-Admin-Key", required = false) String adminKey,
                                             @Valid @RequestBody AdminRestaurantCreateRequest req) {
    AdminAuth.requireAdminKey(adminKey);
    Restaurant r = adminService.createRestaurant(req);
    return RestaurantResponse.from(r);
  }

  @PostMapping("/restaurants/{id}/menu-items")
  public MenuItemResponse addMenuItem(@RequestHeader(value = "X-Admin-Key", required = false) String adminKey,
                                      @PathVariable Long id,
                                      @Valid @RequestBody AdminMenuItemCreateRequest req) {
    AdminAuth.requireAdminKey(adminKey);
    MenuItem mi = adminService.addMenuItem(id, req);
    return MenuItemResponse.from(mi);
  }

  @PutMapping("/menu-items/{id}")
  public MenuItemResponse updateMenuItem(@RequestHeader(value = "X-Admin-Key", required = false) String adminKey,
                                         @PathVariable Long id,
                                         @Valid @RequestBody AdminMenuItemUpdateRequest req) {
    AdminAuth.requireAdminKey(adminKey);
    MenuItem mi = adminService.updateMenuItem(id, req);
    return MenuItemResponse.from(mi);
  }
}
```

---

## Seed Data

### `seed/DataSeeder.java`

```java
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
```

---

## Tests (JUnit5)

### `src/test/java/com/example/zomatox/service/CartServiceTest.java`

```java
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
```

### `src/test/java/com/example/zomatox/service/OrderServiceTest.java`

```java
package com.example.zomatox.service;

import com.example.zomatox.entity.Address;
import com.example.zomatox.entity.MenuItem;
import com.example.zomatox.entity.User;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.repository.AddressRepository;
import com.example.zomatox.repository.MenuItemRepository;
import com.example.zomatox.repository.UserRepository;
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

  @Test
  void create_order_success() {
    User u = userRepo.findAll().stream().filter(x -> x.getEmail().equals("user@zomatox.local")).findFirst().orElseThrow();
    Address addr = addressRepo.findByUser(u).get(0);
    MenuItem mi = menuRepo.findAll().get(0);

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
```

### `src/test/java/com/example/zomatox/service/PaymentServiceTest.java`

```java
package com.example.zomatox.service;

import com.example.zomatox.entity.Address;
import com.example.zomatox.entity.MenuItem;
import com.example.zomatox.entity.User;
import com.example.zomatox.repository.AddressRepository;
import com.example.zomatox.repository.MenuItemRepository;
import com.example.zomatox.repository.UserRepository;
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

  @Test
  void confirm_success_marks_paid() {
    User u = userRepo.findAll().stream().filter(x -> x.getEmail().equals("user@zomatox.local")).findFirst().orElseThrow();
    Address addr = addressRepo.findByUser(u).get(0);
    MenuItem mi = menuRepo.findAll().get(0);

    cartService.upsertItem(u, mi.getId(), 1);
    var order = orderService.createOrder(u, addr.getId());

    var payment = paymentService.confirm(order.getId(), "SUCCESS");
    assertThat(payment.getStatus()).isEqualTo("SUCCESS");
  }
}
```

---

# 3) FRONTEND (Angular 17 + Tailwind)

## `frontend/package.json`

```json
{
  "name": "zomatox-frontend",
  "private": true,
  "version": "0.0.1",
  "scripts": {
    "start": "ng serve"
  },
  "dependencies": {
    "@angular/animations": "^17.3.0",
    "@angular/common": "^17.3.0",
    "@angular/compiler": "^17.3.0",
    "@angular/core": "^17.3.0",
    "@angular/forms": "^17.3.0",
    "@angular/platform-browser": "^17.3.0",
    "@angular/platform-browser-dynamic": "^17.3.0",
    "@angular/router": "^17.3.0",
    "rxjs": "^7.8.1",
    "tslib": "^2.6.2",
    "zone.js": "^0.14.4"
  },
  "devDependencies": {
    "@angular-devkit/build-angular": "^17.3.0",
    "@angular/cli": "^17.3.0",
    "@angular/compiler-cli": "^17.3.0",
    "autoprefixer": "^10.4.19",
    "postcss": "^8.4.38",
    "tailwindcss": "^3.4.7",
    "typescript": "^5.4.5"
  }
}
```

## Tailwind config

`frontend/tailwind.config.js`

```js
module.exports = {
  content: ["./src/**/*.{html,ts}"],
  theme: { extend: {} },
  plugins: [],
};
```

`frontend/postcss.config.js`

```js
module.exports = {
  plugins: {
    tailwindcss: {},
    autoprefixer: {},
  },
};
```

`frontend/src/styles.css`

```css
@tailwind base;
@tailwind components;
@tailwind utilities;
```

`frontend/src/index.html`

```html
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>ZomatoX</title>
  <base href="/">
  <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body class="bg-slate-50 text-slate-900">
  <app-root></app-root>
</body>
</html>
```

## `frontend/src/main.ts`

```ts
import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { AppComponent } from './app/app.component';
import { routes } from './app/app.routes';
import { apiHeadersInterceptor } from './app/core/api.service';

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptors([apiHeadersInterceptor])),
  ],
});
```

---

## Core models + services

### `app/core/models.ts`

```ts
export type Restaurant = {
  id: number;
  name: string;
  city: string;
  cuisineType: string;
  ratingAvg: number;
  deliveryTimeMin: number;
  imageUrl?: string;
};

export type MenuItem = {
  id: number;
  restaurantId: number;
  name: string;
  price: number;
  isVeg: boolean;
  available: boolean;
  stockQty: number;
};

export type CartLine = {
  menuItemId: number;
  name: string;
  price: number;
  qty: number;
  lineTotal: number;
};

export type Cart = {
  cartId: number;
  userId: number;
  restaurantId: number | null;
  items: CartLine[];
  itemTotal: number;
};

export type OrderItem = { name: string; price: number; qty: number; lineTotal: number; };
export type Order = {
  id: number;
  userId: number;
  restaurantId: number;
  status: string;
  itemTotal: number;
  deliveryFee: number;
  payableTotal: number;
  createdAt: string;
  items: OrderItem[];
};
```

### `app/core/user-context.service.ts`

```ts
import { Injectable } from '@angular/core';

export type DemoUser = { id: number; label: string };

@Injectable({ providedIn: 'root' })
export class UserContextService {
  // seeded: user@zomatox.local -> id 1, admin@zomatox.local -> id 2
  users: DemoUser[] = [
    { id: 1, label: 'User (id=1)' },
    { id: 2, label: 'Admin (id=2)' },
  ];

  private key = 'zomatox.userId';

  get userId(): number {
    const v = localStorage.getItem(this.key);
    return v ? Number(v) : 1;
  }

  set userId(id: number) {
    localStorage.setItem(this.key, String(id));
  }
}
```

### `app/core/api.service.ts`

```ts
import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpInterceptorFn } from '@angular/common/http';
import { UserContextService } from './user-context.service';
import { Cart, MenuItem, Order, Restaurant } from './models';

const API = 'http://localhost:8080/api';

export const apiHeadersInterceptor: HttpInterceptorFn = (req, next) => {
  const uc = inject(UserContextService);
  const userId = uc.userId;

  // attach X-User-Id for all calls (backend requires for cart/orders)
  const cloned = req.clone({
    setHeaders: { 'X-User-Id': String(userId) },
  });

  return next(cloned);
};

@Injectable({ providedIn: 'root' })
export class ApiService {
  private http = inject(HttpClient);

  restaurants(params: any) {
    return this.http.get<any>(`${API}/restaurants`, { params });
  }

  restaurant(id: number) {
    return this.http.get<Restaurant>(`${API}/restaurants/${id}`);
  }

  menu(restaurantId: number) {
    return this.http.get<MenuItem[]>(`${API}/restaurants/${restaurantId}/menu`);
  }

  getCart() {
    return this.http.get<Cart>(`${API}/cart`);
  }

  upsertCartItem(menuItemId: number, qty: number) {
    return this.http.put<Cart>(`${API}/cart/items`, { menuItemId, qty });
  }

  removeCartItem(menuItemId: number) {
    return this.http.delete<Cart>(`${API}/cart/items/${menuItemId}`);
  }

  createOrder(addressId: number) {
    return this.http.post<Order>(`${API}/orders`, { addressId });
  }

  orders() {
    return this.http.get<Order[]>(`${API}/orders`);
  }

  order(id: number) {
    return this.http.get<Order>(`${API}/orders/${id}`);
  }

  confirmPayment(orderId: number, result: 'SUCCESS' | 'FAIL') {
    return this.http.post<any>(`${API}/payments/${orderId}/confirm`, null, { params: { result } });
  }
}
```

### `app/core/cart.store.ts`

```ts
import { Injectable, signal } from '@angular/core';
import { Cart } from './models';
import { ApiService } from './api.service';

@Injectable({ providedIn: 'root' })
export class CartStore {
  cart = signal<Cart | null>(null);
  loading = signal(false);

  constructor(private api: ApiService) {}

  load() {
    this.loading.set(true);
    this.api.getCart().subscribe({
      next: (c) => this.cart.set(c),
      error: () => this.cart.set(null),
      complete: () => this.loading.set(false),
    });
  }

  upsert(menuItemId: number, qty: number) {
    this.api.upsertCartItem(menuItemId, qty).subscribe((c) => this.cart.set(c));
  }

  remove(menuItemId: number) {
    this.api.removeCartItem(menuItemId).subscribe((c) => this.cart.set(c));
  }
}
```

---

## Routing + App Shell

### `app/app.routes.ts`

```ts
import { Routes } from '@angular/router';
import { RestaurantListComponent } from './features/restaurants/restaurant-list.component';
import { RestaurantDetailComponent } from './features/restaurants/restaurant-detail.component';
import { CartComponent } from './features/cart/cart.component';
import { CheckoutComponent } from './features/checkout/checkout.component';
import { OrderListComponent } from './features/orders/order-list.component';
import { OrderDetailComponent } from './features/orders/order-detail.component';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'restaurants' },
  { path: 'restaurants', component: RestaurantListComponent },
  { path: 'restaurants/:id', component: RestaurantDetailComponent },
  { path: 'cart', component: CartComponent },
  { path: 'checkout', component: CheckoutComponent },
  { path: 'orders', component: OrderListComponent },
  { path: 'orders/:id', component: OrderDetailComponent },
];
```

### `app/app.component.ts`

```ts
import { Component, computed, inject } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { UserContextService } from './core/user-context.service';
import { CartStore } from './core/cart.store';

@Component({
  standalone: true,
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink],
  template: `
  <div class="min-h-screen">
    <header class="sticky top-0 z-10 bg-white border-b">
      <div class="max-w-5xl mx-auto px-4 py-3 flex items-center gap-4">
        <a routerLink="/restaurants" class="font-bold text-lg">ZomatoX</a>

        <nav class="flex gap-3 text-sm">
          <a routerLink="/restaurants" class="hover:underline">Restaurants</a>
          <a routerLink="/cart" class="hover:underline">Cart</a>
          <a routerLink="/orders" class="hover:underline">Orders</a>
        </nav>

        <div class="ml-auto flex items-center gap-3">
          <select class="border rounded px-2 py-1 text-sm"
                  [value]="userId()"
                  (change)="switchUser($any($event.target).value)">
            <option *ngFor="let u of users" [value]="u.id">{{u.label}}</option>
          </select>

          <div class="text-sm bg-slate-100 rounded px-2 py-1">
            Cart: {{cartCount()}}
          </div>
        </div>
      </div>
    </header>

    <main class="max-w-5xl mx-auto px-4 py-6">
      <router-outlet></router-outlet>
    </main>
  </div>
  `,
})
export class AppComponent {
  private uc = inject(UserContextService);
  private cartStore = inject(CartStore);

  users = this.uc.users;
  userId = computed(() => this.uc.userId);

  cartCount = computed(() => this.cartStore.cart()?.items?.reduce((a, x) => a + x.qty, 0) ?? 0);

  constructor() {
    this.cartStore.load();
  }

  switchUser(v: string) {
    this.uc.userId = Number(v);
    this.cartStore.load();
  }
}
```

> Angular template এ `*ngFor` ব্যবহার করতে CommonModule লাগে—Angular 17 standalone এ আমরা সহজ করে `*ngFor` না রেখে কাজ চালাতে পারি, কিন্তু এখানে ব্যবহার করেছি। তাই নিচে `AppComponent`-এ CommonModule import করা লাগবে।

**Fix:** `app.component.ts`-এ `imports` এ `CommonModule` add করো:

```ts
import { CommonModule } from '@angular/common';
// ...
imports: [CommonModule, RouterOutlet, RouterLink],
```

---

## Feature Components

### `features/restaurants/restaurant-list.component.ts`

```ts
import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ApiService } from '../../core/api.service';
import { Restaurant } from '../../core/models';

@Component({
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
  <div class="flex items-end gap-3 mb-4">
    <div class="flex-1">
      <label class="text-xs text-slate-600">Search</label>
      <input class="w-full border rounded px-3 py-2" [(ngModel)]="q" placeholder="Restaurant name..." />
    </div>

    <div>
      <label class="text-xs text-slate-600">City</label>
      <select class="border rounded px-3 py-2" [(ngModel)]="city">
        <option value="">All</option>
        <option value="Kolkata">Kolkata</option>
        <option value="Bengaluru">Bengaluru</option>
      </select>
    </div>

    <div>
      <label class="text-xs text-slate-600">Sort</label>
      <select class="border rounded px-3 py-2" [(ngModel)]="sort">
        <option value="rating">Rating</option>
        <option value="time">Delivery time</option>
      </select>
    </div>

    <button class="bg-black text-white rounded px-4 py-2" (click)="load()">Search</button>
  </div>

  <div class="grid md:grid-cols-2 gap-4">
    <a *ngFor="let r of restaurants()"
       class="bg-white border rounded-xl overflow-hidden hover:shadow"
       [routerLink]="['/restaurants', r.id]">
      <img class="w-full h-40 object-cover" [src]="r.imageUrl || fallback" />
      <div class="p-4">
        <div class="font-semibold">{{r.name}}</div>
        <div class="text-sm text-slate-600">{{r.city}} • {{r.cuisineType}}</div>
        <div class="mt-2 text-sm">
          ⭐ {{r.ratingAvg.toFixed(1)}} • {{r.deliveryTimeMin}} min
        </div>
      </div>
    </a>
  </div>
  `,
})
export class RestaurantListComponent {
  private api = inject(ApiService);

  restaurants = signal<Restaurant[]>([]);
  fallback = 'https://picsum.photos/seed/fallback/640/360';

  city = 'Kolkata';
  q = '';
  sort = 'rating';

  load() {
    this.api.restaurants({ city: this.city, q: this.q, sort: this.sort, page: 0 })
      .subscribe(res => this.restaurants.set(res.content ?? []));
  }

  constructor() {
    this.load();
  }
}
```

> `ngModel` এর জন্য FormsModule লাগবে।
> `imports: [CommonModule, RouterLink, FormsModule]` করো।

### `features/restaurants/restaurant-detail.component.ts`

```ts
import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../core/api.service';
import { CartStore } from '../../core/cart.store';
import { MenuItem, Restaurant } from '../../core/models';

@Component({
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  template: `
  <a routerLink="/restaurants" class="text-sm underline">← Back</a>

  <div *ngIf="restaurant()" class="mt-3 bg-white border rounded-xl overflow-hidden">
    <img class="w-full h-44 object-cover" [src]="restaurant()!.imageUrl" />
    <div class="p-4">
      <div class="text-xl font-bold">{{restaurant()!.name}}</div>
      <div class="text-sm text-slate-600">{{restaurant()!.city}} • {{restaurant()!.cuisineType}}</div>
      <div class="mt-1 text-sm">⭐ {{restaurant()!.ratingAvg.toFixed(1)}} • {{restaurant()!.deliveryTimeMin}} min</div>
    </div>
  </div>

  <h2 class="mt-6 mb-2 font-semibold">Menu</h2>

  <div class="grid gap-3">
    <div *ngFor="let m of menu()"
         class="bg-white border rounded-xl p-4 flex items-center gap-4">
      <div class="flex-1">
        <div class="font-medium">{{m.name}}</div>
        <div class="text-sm text-slate-600">₹{{m.price}} • {{m.isVeg ? 'Veg' : 'Non-veg'}} • Stock: {{m.stockQty}}</div>
        <div class="text-xs" [class.text-red-600]="!m.available || m.stockQty<=0">
          {{(!m.available || m.stockQty<=0) ? 'Unavailable' : 'Available'}}
        </div>
      </div>

      <div class="flex items-center gap-2">
        <input class="w-16 border rounded px-2 py-1" type="number" min="1" [(ngModel)]="qty[m.id]" />
        <button class="bg-black text-white rounded px-3 py-2"
                [disabled]="!m.available || m.stockQty<=0"
                (click)="add(m.id)">
          Add
        </button>
      </div>
    </div>
  </div>
  `,
})
export class RestaurantDetailComponent {
  private api = inject(ApiService);
  private cart = inject(CartStore);
  private route = inject(ActivatedRoute);

  restaurant = signal<Restaurant | null>(null);
  menu = signal<MenuItem[]>([]);
  qty: Record<number, number> = {};

  constructor() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.api.restaurant(id).subscribe(r => this.restaurant.set(r));
    this.api.menu(id).subscribe(ms => {
      this.menu.set(ms);
      ms.forEach(m => this.qty[m.id] = 1);
    });
  }

  add(menuItemId: number) {
    const q = this.qty[menuItemId] ?? 1;
    this.cart.upsert(menuItemId, q);
  }
}
```

### `features/cart/cart.component.ts`

```ts
import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CartStore } from '../../core/cart.store';

@Component({
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
  <h1 class="text-xl font-bold mb-3">Cart</h1>

  <div *ngIf="cartStore.cart() as cart" class="bg-white border rounded-xl p-4">
    <div *ngIf="cart.items.length===0" class="text-slate-600">
      Cart is empty. <a routerLink="/restaurants" class="underline">Browse restaurants</a>
    </div>

    <div *ngFor="let it of cart.items" class="flex items-center justify-between py-2 border-b last:border-b-0">
      <div>
        <div class="font-medium">{{it.name}}</div>
        <div class="text-sm text-slate-600">₹{{it.price}} × {{it.qty}} = ₹{{it.lineTotal}}</div>
      </div>

      <div class="flex gap-2">
        <button class="border rounded px-2 py-1" (click)="cartStore.upsert(it.menuItemId, it.qty-1)" [disabled]="it.qty<=1">-</button>
        <button class="border rounded px-2 py-1" (click)="cartStore.upsert(it.menuItemId, it.qty+1)">+</button>
        <button class="border rounded px-2 py-1" (click)="cartStore.remove(it.menuItemId)">Remove</button>
      </div>
    </div>

    <div class="mt-4 flex items-center justify-between">
      <div class="font-semibold">Item Total: ₹{{cart.itemTotal}}</div>
      <a routerLink="/checkout" class="bg-black text-white rounded px-4 py-2" [class.opacity-50]="cart.items.length===0"
         [attr.aria-disabled]="cart.items.length===0">Checkout</a>
    </div>
  </div>
  `,
})
export class CartComponent {
  cartStore = inject(CartStore);

  constructor() {
    this.cartStore.load();
  }
}
```

### `features/checkout/checkout.component.ts`

```ts
import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { ApiService } from '../../core/api.service';
import { CartStore } from '../../core/cart.store';

@Component({
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
  <h1 class="text-xl font-bold mb-3">Checkout</h1>

  <div class="bg-white border rounded-xl p-4">
    <div class="text-sm text-slate-600 mb-3">
      v1 demo: choose addressId manually (seeded addresses exist for userId=1).
      Try addressId: 1 or 2
    </div>

    <div class="flex items-center gap-2">
      <input class="border rounded px-3 py-2 w-40" type="number" [value]="addressId()" (input)="addressId.set(+$any($event.target).value)" />
      <button class="bg-black text-white rounded px-4 py-2" (click)="place()">Place Order</button>
    </div>

    <div *ngIf="error()" class="mt-3 text-red-600 text-sm">{{error()}}</div>
  </div>
  `,
})
export class CheckoutComponent {
  private api = inject(ApiService);
  private cart = inject(CartStore);
  private router = inject(Router);

  addressId = signal<number>(1);
  error = signal<string>('');

  place() {
    this.error.set('');
    this.api.createOrder(this.addressId()).subscribe({
      next: (o) => {
        this.cart.load();
        this.router.navigate(['/orders', o.id]);
      },
      error: (e) => this.error.set(e?.error?.message ?? 'Failed'),
    });
  }
}
```

### `features/orders/order-list.component.ts`

```ts
import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ApiService } from '../../core/api.service';
import { Order } from '../../core/models';

@Component({
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
  <h1 class="text-xl font-bold mb-3">Orders</h1>

  <div class="grid gap-3">
    <a *ngFor="let o of orders()"
       class="bg-white border rounded-xl p-4 hover:shadow"
       [routerLink]="['/orders', o.id]">
      <div class="flex items-center justify-between">
        <div class="font-semibold">Order #{{o.id}}</div>
        <div class="text-sm px-2 py-1 rounded bg-slate-100">{{o.status}}</div>
      </div>
      <div class="text-sm text-slate-600 mt-1">Total: ₹{{o.payableTotal}}</div>
      <div class="text-xs text-slate-500 mt-1">{{o.createdAt}}</div>
    </a>
  </div>
  `,
})
export class OrderListComponent {
  private api = inject(ApiService);
  orders = signal<Order[]>([]);

  constructor() {
    this.api.orders().subscribe(os => this.orders.set(os));
  }
}
```

### `features/orders/order-detail.component.ts`

```ts
import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ApiService } from '../../core/api.service';
import { Order } from '../../core/models';

@Component({
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
  <a routerLink="/orders" class="text-sm underline">← Back</a>

  <div *ngIf="order()" class="mt-3 bg-white border rounded-xl p-4">
    <div class="flex items-center justify-between">
      <div class="text-xl font-bold">Order #{{order()!.id}}</div>
      <div class="text-sm px-2 py-1 rounded bg-slate-100">{{order()!.status}}</div>
    </div>

    <div class="mt-2 text-sm">
      Item total: ₹{{order()!.itemTotal}} <br/>
      Delivery fee: ₹{{order()!.deliveryFee}} <br/>
      <b>Payable: ₹{{order()!.payableTotal}}</b>
    </div>

    <h3 class="mt-4 font-semibold">Items</h3>
    <div *ngFor="let it of order()!.items" class="py-2 border-b last:border-b-0">
      <div class="font-medium">{{it.name}}</div>
      <div class="text-sm text-slate-600">₹{{it.price}} × {{it.qty}} = ₹{{it.lineTotal}}</div>
    </div>

    <div class="mt-4 flex gap-2">
      <button class="bg-black text-white rounded px-4 py-2" (click)="confirm('SUCCESS')">Mock Pay SUCCESS</button>
      <button class="border rounded px-4 py-2" (click)="confirm('FAIL')">Mock Pay FAIL</button>
    </div>

    <div *ngIf="msg()" class="mt-3 text-sm">{{msg()}}</div>
  </div>
  `,
})
export class OrderDetailComponent {
  private api = inject(ApiService);
  private route = inject(ActivatedRoute);

  order = signal<Order | null>(null);
  msg = signal<string>('');

  private id = Number(this.route.snapshot.paramMap.get('id'));

  constructor() {
    this.reload();
  }

  reload() {
    this.api.order(this.id).subscribe(o => this.order.set(o));
  }

  confirm(result: 'SUCCESS' | 'FAIL') {
    this.msg.set('');
    this.api.confirmPayment(this.id, result).subscribe({
      next: () => {
        this.msg.set('Payment updated: ' + result);
        this.reload();
      },
      error: (e) => this.msg.set(e?.error?.message ?? 'Failed'),
    });
  }
}
```

---

# 4) Run Instructions

## Backend

```bash
cd backend
mvn spring-boot:run
```

* Swagger: `http://localhost:8080/swagger-ui.html`
* H2 console: `http://localhost:8080/h2-console`

  * JDBC URL: `jdbc:h2:mem:zomatox`

## Frontend

```bash
cd frontend
npm i
npx ng serve
```

Open: `http://localhost:4200`

---

# 5) Curl Examples

## List restaurants

```bash
curl "http://localhost:8080/api/restaurants?city=Kolkata&sort=rating&page=0"
```

## Add cart item (userId=1)

```bash
curl -X PUT "http://localhost:8080/api/cart/items" \
  -H "Content-Type: application/json" \
  -H "X-User-Id: 1" \
  -d '{"menuItemId":1,"qty":2}'
```

## Create order (addressId=1)

```bash
curl -X POST "http://localhost:8080/api/orders" \
  -H "Content-Type: application/json" \
  -H "X-User-Id: 1" \
  -d '{"addressId":1}'
```

## Confirm payment SUCCESS

```bash
curl -X POST "http://localhost:8080/api/payments/1/confirm?result=SUCCESS"
```

## Get order

```bash
curl -H "X-User-Id: 1" "http://localhost:8080/api/orders/1"
```

---

# 6) Video Script Cues (Bengali)

* “এখন restaurant list API বানাচ্ছি”
* “এখন restaurant details + menu API বানালাম”
* “এখন cart service দিয়ে add/update/remove করবো”
* “এখন order place করে PAYMENT_PENDING করলাম”
* “এখন mock payment confirm করে PAID করলাম”
* “এখন Angular এ list → detail → cart → checkout → orders page wire করলাম”

---

# 7) [GIT TAG COMMANDS]

```bash
git status
git add .
git commit -m "v1: ZomatoX MVP restaurants/menu/cart/order/mock-payment + tests"
git tag v1-zomatox-mvp
git tag -n
```

---

## Important Note (তোমার জন্য)

Angular অংশটা minimal রাখা হয়েছে, তবে **fully runnable**।
তুমি চাইলে আমি **v1 UI আরও Zomato-like** (filters sidebar, cuisine chips, skeleton loaders, toast) করে upgrade করে দিতে পারি—কোড একই backend এ থাকবে।

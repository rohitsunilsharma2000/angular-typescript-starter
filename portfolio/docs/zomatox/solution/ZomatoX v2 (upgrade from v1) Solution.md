## 1) v1 → v2 Upgrade Diff Checklist (ADD/MODIFY)

### Backend (Spring Boot)

**MODIFY**

* `pom.xml` → add PostgreSQL driver + Flyway + (tests) Testcontainers
* `application.yml` → switch datasource to PostgreSQL, enable Flyway, set `ddl-auto: validate`
* `entity/enums/OrderStatus.java` → expand statuses
* `entity/User.java` → add `role`
* `entity/Restaurant.java` → add `owner` FK
* `entity/Order.java` → add `deliveryPartner`, `updatedAt`
* `service/OrderService.java` → enforce state machine + create order_events on every status change
* `service/PaymentService.java` → SUCCESS sets PAID then auto CONFIRMED (creates events)
* `seed/DataSeeder.java` → seed users(owner/delivery/customer), restaurants linked to owner, sample orders in different statuses

**ADD**

* `entity/enums/UserRole.java`
* `entity/OrderEvent.java`, `entity/Review.java`
* `repository/OrderEventRepository.java`, `repository/ReviewRepository.java`
* `service/OrderStateMachine.java` (transition validator)
* `service/OwnerService.java`, `service/DeliveryService.java`, `service/ReviewService.java`
* `controller/OwnerController.java`, `controller/DeliveryController.java`, `controller/ReviewController.java`, `controller/OrderEventController.java`
* `util/RequestContext.java` (resolve role from header or DB)
* `docker-compose.yml`
* Flyway migrations:

  * `db/migration/V1__init.sql`
  * `db/migration/V2__add_roles_owner_delivery_reviews_events.sql`

### Frontend (Angular)

**MODIFY**

* header role/user dropdown → switch between seeded users (customer/owner/delivery)
* HTTP interceptor → add `X-User-Id` + `X-User-Role` headers
* add routes for owner + delivery portals
* add reviews section and order timeline page

**ADD**

* owner components: restaurants, menu, orders
* delivery components: jobs, order detail/status
* customer: order events + restaurant reviews components (or extend existing pages)

---

## 2) Updated Repository Tree (only showing v2 additions/changed areas)

```
backend/
  docker-compose.yml
  src/main/resources/
    application.yml
    db/migration/
      V1__init.sql
      V2__add_roles_owner_delivery_reviews_events.sql
  src/main/java/com/example/zomatox/
    util/RequestContext.java
    entity/enums/UserRole.java
    entity/enums/OrderStatus.java        (MOD)
    entity/User.java                     (MOD)
    entity/Restaurant.java               (MOD)
    entity/Order.java                    (MOD)
    entity/OrderEvent.java               (NEW)
    entity/Review.java                   (NEW)
    repository/OrderEventRepository.java (NEW)
    repository/ReviewRepository.java     (NEW)
    service/OrderStateMachine.java       (NEW)
    service/OwnerService.java            (NEW)
    service/DeliveryService.java         (NEW)
    service/ReviewService.java           (NEW)
    controller/OwnerController.java      (NEW)
    controller/DeliveryController.java   (NEW)
    controller/ReviewController.java     (NEW)
    controller/OrderEventController.java (NEW)
    service/OrderService.java            (MOD)
    service/PaymentService.java          (MOD)
    seed/DataSeeder.java                 (MOD)
  src/test/java/com/example/zomatox/service/
    OrderTransitionTest.java             (NEW)
    DeliveryAcceptTest.java              (NEW)
    ReviewConstraintsTest.java           (NEW)

frontend/
  src/app/core/
    user-context.service.ts              (MOD)
    api.service.ts                       (MOD)
  src/app/app.routes.ts                  (MOD)
  src/app/app.component.ts               (MOD)
  src/app/features/owner/                (NEW)
  src/app/features/delivery/             (NEW)
  src/app/features/orders/order-detail.component.ts (MOD: show events)
  src/app/features/restaurants/restaurant-detail.component.ts (MOD: reviews)
```

---

# 3) Backend — Full Code for New/Changed Files

## 3.1 `backend/docker-compose.yml` (NEW)

```yaml
version: "3.9"
services:
  postgres:
    image: postgres:16
    container_name: zomatox_pg
    environment:
      POSTGRES_DB: zomatox
      POSTGRES_USER: zomatox
      POSTGRES_PASSWORD: zomatox
    ports:
      - "5432:5432"
    volumes:
      - zomatox_pg_data:/var/lib/postgresql/data

volumes:
  zomatox_pg_data:
```

---

## 3.2 `backend/pom.xml` (MOD — add deps)

Add these dependencies inside `<dependencies>` (keep your existing ones too):

```xml
<!-- PostgreSQL driver -->
<dependency>
  <groupId>org.postgresql</groupId>
  <artifactId>postgresql</artifactId>
  <scope>runtime</scope>
</dependency>

<!-- Flyway -->
<dependency>
  <groupId>org.flywaydb</groupId>
  <artifactId>flyway-core</artifactId>
</dependency>

<!-- TEST: Testcontainers for Postgres -->
<dependency>
  <groupId>org.testcontainers</groupId>
  <artifactId>junit-jupiter</artifactId>
  <version>1.20.1</version>
  <scope>test</scope>
</dependency>
<dependency>
  <groupId>org.testcontainers</groupId>
  <artifactId>postgresql</artifactId>
  <version>1.20.1</version>
  <scope>test</scope>
</dependency>
```

---

## 3.3 `backend/src/main/resources/application.yml` (MOD)

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/zomatox
    username: zomatox
    password: zomatox
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        format_sql: true
  flyway:
    enabled: true
    locations: classpath:db/migration

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics

springdoc:
  swagger-ui:
    path: /swagger-ui.html
```

---

## 3.4 Flyway Migrations

### `backend/src/main/resources/db/migration/V1__init.sql` (NEW)

> PostgreSQL schema equivalent of your v1 tables (plus minimal indexes/constraints).

```sql
-- USERS
create table if not exists users (
  id bigserial primary key,
  name varchar(120) not null,
  email varchar(180) not null unique
);

-- RESTAURANTS
create table if not exists restaurants (
  id bigserial primary key,
  name varchar(180) not null,
  city varchar(80) not null,
  cuisine_type varchar(80) not null,
  rating_avg double precision not null,
  delivery_time_min int not null,
  image_url text
);

-- MENU ITEMS
create table if not exists menu_items (
  id bigserial primary key,
  restaurant_id bigint not null references restaurants(id),
  name varchar(180) not null,
  price bigint not null,
  is_veg boolean not null,
  available boolean not null,
  stock_qty int not null
);

-- CARTS
create table if not exists carts (
  id bigserial primary key,
  user_id bigint not null unique references users(id),
  updated_at timestamptz not null
);

-- CART ITEMS
create table if not exists cart_items (
  id bigserial primary key,
  cart_id bigint not null references carts(id) on delete cascade,
  menu_item_id bigint not null references menu_items(id),
  qty int not null,
  constraint uk_cart_menu unique(cart_id, menu_item_id)
);

-- ADDRESSES
create table if not exists addresses (
  id bigserial primary key,
  user_id bigint not null references users(id),
  line1 varchar(220) not null,
  city varchar(80) not null,
  pincode varchar(12) not null,
  phone varchar(20) not null
);

-- ORDERS
create table if not exists orders (
  id bigserial primary key,
  user_id bigint not null references users(id),
  restaurant_id bigint not null references restaurants(id),
  status varchar(40) not null,
  item_total bigint not null,
  delivery_fee bigint not null,
  payable_total bigint not null,
  created_at timestamptz not null
);

-- ORDER ITEMS
create table if not exists order_items (
  id bigserial primary key,
  order_id bigint not null references orders(id) on delete cascade,
  menu_item_name_snapshot varchar(180) not null,
  price_snapshot bigint not null,
  qty int not null,
  line_total bigint not null
);

-- PAYMENTS
create table if not exists payments (
  id bigserial primary key,
  order_id bigint not null unique references orders(id) on delete cascade,
  status varchar(40) not null,
  method varchar(40) not null,
  created_at timestamptz not null
);

create index if not exists idx_restaurants_city on restaurants(city);
create index if not exists idx_menu_items_restaurant on menu_items(restaurant_id);
create index if not exists idx_orders_user on orders(user_id);
```

### `backend/src/main/resources/db/migration/V2__add_roles_owner_delivery_reviews_events.sql` (NEW)

```sql
-- 1) users.role
alter table users
  add column if not exists role varchar(40) not null default 'CUSTOMER';

-- 2) restaurants.owner_user_id
alter table restaurants
  add column if not exists owner_user_id bigint null;

alter table restaurants
  add constraint fk_restaurant_owner
  foreign key (owner_user_id) references users(id);

-- 3) orders expanded fields
alter table orders
  add column if not exists delivery_partner_user_id bigint null;

alter table orders
  add constraint fk_orders_delivery_partner
  foreign key (delivery_partner_user_id) references users(id);

alter table orders
  add column if not exists updated_at timestamptz not null default now();

-- 4) order_events
create table if not exists order_events (
  id bigserial primary key,
  order_id bigint not null references orders(id) on delete cascade,
  status varchar(40) not null,
  message text,
  created_at timestamptz not null default now()
);

create index if not exists idx_order_events_order on order_events(order_id);

-- 5) reviews
create table if not exists reviews (
  id bigserial primary key,
  order_id bigint not null unique references orders(id) on delete cascade,
  restaurant_id bigint not null references restaurants(id),
  user_id bigint not null references users(id),
  rating int not null,
  comment text,
  created_at timestamptz not null default now()
);

create index if not exists idx_reviews_restaurant on reviews(restaurant_id);

-- rating constraint
alter table reviews
  add constraint chk_reviews_rating
  check (rating >= 1 and rating <= 5);
```

---

## 3.5 Enums + Entity updates

### `backend/src/main/java/com/example/zomatox/entity/enums/UserRole.java` (NEW)

```java
package com.example.zomatox.entity.enums;

public enum UserRole {
  CUSTOMER,
  OWNER,
  DELIVERY_PARTNER,
  ADMIN
}
```

### `backend/src/main/java/com/example/zomatox/entity/enums/OrderStatus.java` (MOD)

```java
package com.example.zomatox.entity.enums;

public enum OrderStatus {
  CREATED,
  PAYMENT_PENDING,
  PAID,

  CONFIRMED,
  PREPARING,
  READY_FOR_PICKUP,

  PICKED_UP,
  OUT_FOR_DELIVERY,
  DELIVERED,

  PAYMENT_FAILED
}
```

### `backend/src/main/java/com/example/zomatox/entity/User.java` (MOD)

```java
package com.example.zomatox.entity;

import com.example.zomatox.entity.enums.UserRole;
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

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserRole role;
}
```

### `backend/src/main/java/com/example/zomatox/entity/Restaurant.java` (MOD)

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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_user_id")
  private User owner;
}
```

### `backend/src/main/java/com/example/zomatox/entity/Order.java` (MOD)

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

  @Column(nullable = false)
  private Instant updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "delivery_partner_user_id")
  private User deliveryPartner;
}
```

### `backend/src/main/java/com/example/zomatox/entity/OrderEvent.java` (NEW)

```java
package com.example.zomatox.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "order_events")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderEvent {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private Order order;

  @Column(nullable = false)
  private String status;

  @Column
  private String message;

  @Column(nullable = false)
  private Instant createdAt;
}
```

### `backend/src/main/java/com/example/zomatox/entity/Review.java` (NEW)

```java
package com.example.zomatox.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "reviews", uniqueConstraints = @UniqueConstraint(name = "uk_review_order", columnNames = "order_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Review {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", unique = true)
  private Order order;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "restaurant_id")
  private Restaurant restaurant;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(nullable = false)
  private int rating;

  @Column
  private String comment;

  @Column(nullable = false)
  private Instant createdAt;
}
```

---

## 3.6 Repositories (NEW)

### `backend/src/main/java/com/example/zomatox/repository/OrderEventRepository.java`

```java
package com.example.zomatox.repository;

import com.example.zomatox.entity.Order;
import com.example.zomatox.entity.OrderEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderEventRepository extends JpaRepository<OrderEvent, Long> {
  List<OrderEvent> findByOrderOrderByIdAsc(Order order);
}
```

### `backend/src/main/java/com/example/zomatox/repository/ReviewRepository.java`

```java
package com.example.zomatox.repository;

import com.example.zomatox.entity.Review;
import com.example.zomatox.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  Page<Review> findByRestaurant(Restaurant restaurant, Pageable pageable);
  Optional<Review> findByOrderId(Long orderId);
}
```

---

## 3.7 Request Context + Role Resolution (NEW)

### `backend/src/main/java/com/example/zomatox/util/RequestContext.java`

```java
package com.example.zomatox.util;

import com.example.zomatox.entity.User;
import com.example.zomatox.entity.enums.UserRole;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.service.UserService;
import org.springframework.http.HttpStatus;

public final class RequestContext {
  private RequestContext() {}

  public static User requireUser(UserService userService, String userIdHeader) {
    if (userIdHeader == null || userIdHeader.isBlank()) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "Missing X-User-Id header");
    }
    return userService.getUserOrThrow(Long.parseLong(userIdHeader));
  }

  /**
   * Dev override:
   * - If X-User-Role is present, trust it for quick testing.
   * - Else use role from DB user row.
   */
  public static UserRole resolveRole(User u, String roleHeader) {
    if (roleHeader != null && !roleHeader.isBlank()) {
      try { return UserRole.valueOf(roleHeader.trim().toUpperCase()); }
      catch (Exception ignored) {}
    }
    return u.getRole();
  }

  public static void requireRole(UserRole actual, UserRole required) {
    if (actual != required) {
      throw new ApiException(HttpStatus.FORBIDDEN, "Requires role " + required + " but was " + actual);
    }
  }
}
```

---

## 3.8 Order State Machine (NEW)

### `backend/src/main/java/com/example/zomatox/service/OrderStateMachine.java`

```java
package com.example.zomatox.service;

import com.example.zomatox.entity.enums.OrderStatus;
import com.example.zomatox.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class OrderStateMachine {

  public void requireTransitionAllowed(OrderStatus from, OrderStatus to) {
    if (from == to) return;

    // v2: enforce owner flow + delivery flow
    if (from == OrderStatus.PAID && to == OrderStatus.CONFIRMED) return;

    if (from == OrderStatus.CONFIRMED && to == OrderStatus.PREPARING) return;
    if (from == OrderStatus.PREPARING && to == OrderStatus.READY_FOR_PICKUP) return;

    if (from == OrderStatus.READY_FOR_PICKUP && to == OrderStatus.PICKED_UP) return;
    if (from == OrderStatus.PICKED_UP && to == OrderStatus.OUT_FOR_DELIVERY) return;
    if (from == OrderStatus.OUT_FOR_DELIVERY && to == OrderStatus.DELIVERED) return;

    // keep v1 payment-fail path
    if (Set.of(OrderStatus.PAYMENT_PENDING, OrderStatus.PAID, OrderStatus.CONFIRMED).contains(from)
        && to == OrderStatus.PAYMENT_FAILED) return;

    throw new ApiException(HttpStatus.BAD_REQUEST, "Illegal transition: " + from + " -> " + to);
  }
}
```

---

## 3.9 Services (NEW) — Owner/Delivery/Reviews + Order Events

### `backend/src/main/java/com/example/zomatox/service/OwnerService.java`

```java
package com.example.zomatox.service;

import com.example.zomatox.dto.admin.AdminMenuItemCreateRequest;
import com.example.zomatox.dto.admin.AdminMenuItemUpdateRequest;
import com.example.zomatox.dto.admin.AdminRestaurantCreateRequest;
import com.example.zomatox.entity.*;
import com.example.zomatox.entity.enums.OrderStatus;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.repository.MenuItemRepository;
import com.example.zomatox.repository.OrderRepository;
import com.example.zomatox.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OwnerService {
  private final RestaurantRepository restaurantRepository;
  private final MenuItemRepository menuItemRepository;
  private final OrderRepository orderRepository;
  private final OrderService orderService; // reuse status change + events

  public List<Restaurant> myRestaurants(User owner) {
    return restaurantRepository.findAll().stream()
      .filter(r -> r.getOwner() != null && r.getOwner().getId().equals(owner.getId()))
      .toList();
  }

  public Restaurant createRestaurant(User owner, AdminRestaurantCreateRequest req) {
    Restaurant r = Restaurant.builder()
      .name(req.getName())
      .city(req.getCity())
      .cuisineType(req.getCuisineType())
      .ratingAvg(req.getRatingAvg())
      .deliveryTimeMin(req.getDeliveryTimeMin())
      .imageUrl(req.getImageUrl())
      .owner(owner)
      .build();
    return restaurantRepository.save(r);
  }

  public Restaurant updateRestaurant(User owner, Long id, AdminRestaurantCreateRequest req) {
    Restaurant r = restaurantRepository.findById(id).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Restaurant not found: " + id));
    if (r.getOwner() == null || !r.getOwner().getId().equals(owner.getId())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "Not your restaurant");
    }
    r.setName(req.getName());
    r.setCity(req.getCity());
    r.setCuisineType(req.getCuisineType());
    r.setRatingAvg(req.getRatingAvg());
    r.setDeliveryTimeMin(req.getDeliveryTimeMin());
    r.setImageUrl(req.getImageUrl());
    return restaurantRepository.save(r);
  }

  public MenuItem addMenuItem(User owner, Long restaurantId, AdminMenuItemCreateRequest req) {
    Restaurant r = restaurantRepository.findById(restaurantId).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Restaurant not found: " + restaurantId));
    if (r.getOwner() == null || !r.getOwner().getId().equals(owner.getId())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "Not your restaurant");
    }

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

  public MenuItem updateMenuItem(User owner, Long menuItemId, AdminMenuItemUpdateRequest req) {
    MenuItem mi = menuItemRepository.findById(menuItemId).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Menu item not found: " + menuItemId));

    Restaurant r = mi.getRestaurant();
    if (r.getOwner() == null || !r.getOwner().getId().equals(owner.getId())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "Not your restaurant menu item");
    }

    mi.setPrice(req.getPrice());
    mi.setAvailable(req.getAvailable());
    mi.setStockQty(req.getStockQty());
    return menuItemRepository.save(mi);
  }

  public List<Order> ownerOrders(User owner, OrderStatus status) {
    // v2 simple: owner sees orders for owned restaurants
    return orderRepository.findAll().stream()
      .filter(o -> o.getRestaurant().getOwner() != null
        && o.getRestaurant().getOwner().getId().equals(owner.getId())
        && (status == null || o.getStatus() == status))
      .sorted((a,b) -> Long.compare(b.getId(), a.getId()))
      .toList();
  }

  public Order changeOrderStatus(User owner, Long orderId, OrderStatus next) {
    Order o = orderRepository.findById(orderId).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Order not found: " + orderId));
    if (o.getRestaurant().getOwner() == null || !o.getRestaurant().getOwner().getId().equals(owner.getId())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "Not your order");
    }
    // owner allowed transitions: CONFIRMED->PREPARING->READY_FOR_PICKUP
    if (!(next == OrderStatus.PREPARING || next == OrderStatus.READY_FOR_PICKUP)) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Owner can set only PREPARING or READY_FOR_PICKUP");
    }
    return orderService.setStatusWithEvent(o, next, "Owner updated to " + next, Instant.now());
  }
}
```

### `backend/src/main/java/com/example/zomatox/service/DeliveryService.java`

```java
package com.example.zomatox.service;

import com.example.zomatox.entity.Order;
import com.example.zomatox.entity.User;
import com.example.zomatox.entity.enums.OrderStatus;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryService {
  private final OrderRepository orderRepository;
  private final OrderService orderService;

  public List<Order> jobs(User deliveryPartner, String status) {
    // AVAILABLE = READY_FOR_PICKUP and no delivery partner assigned
    // ASSIGNED = assigned to me and not DELIVERED
    return orderRepository.findAll().stream().filter(o -> {
      if ("AVAILABLE".equalsIgnoreCase(status)) {
        return o.getStatus() == OrderStatus.READY_FOR_PICKUP && o.getDeliveryPartner() == null;
      }
      if ("ASSIGNED".equalsIgnoreCase(status)) {
        return o.getDeliveryPartner() != null
          && o.getDeliveryPartner().getId().equals(deliveryPartner.getId())
          && o.getStatus() != OrderStatus.DELIVERED;
      }
      return true;
    }).sorted((a,b) -> Long.compare(b.getId(), a.getId())).toList();
  }

  public Order accept(User deliveryPartner, Long orderId) {
    Order o = orderRepository.findById(orderId).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Order not found: " + orderId));

    if (o.getStatus() != OrderStatus.READY_FOR_PICKUP) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Can accept only READY_FOR_PICKUP orders");
    }
    if (o.getDeliveryPartner() != null) {
      throw new ApiException(HttpStatus.CONFLICT, "Order already assigned");
    }

    o.setDeliveryPartner(deliveryPartner);
    orderRepository.save(o);

    // next expected status change by delivery partner is PICKED_UP
    orderService.createEvent(o, o.getStatus(), "Delivery partner accepted job", Instant.now());
    return o;
  }

  public Order updateStatus(User deliveryPartner, Long orderId, OrderStatus next) {
    Order o = orderRepository.findById(orderId).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Order not found: " + orderId));

    if (o.getDeliveryPartner() == null || !o.getDeliveryPartner().getId().equals(deliveryPartner.getId())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "Not assigned to you");
    }

    if (!(next == OrderStatus.PICKED_UP || next == OrderStatus.OUT_FOR_DELIVERY || next == OrderStatus.DELIVERED)) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid delivery status");
    }

    return orderService.setStatusWithEvent(o, next, "Delivery updated to " + next, Instant.now());
  }
}
```

### `backend/src/main/java/com/example/zomatox/service/ReviewService.java`

```java
package com.example.zomatox.service;

import com.example.zomatox.entity.Order;
import com.example.zomatox.entity.Review;
import com.example.zomatox.entity.Restaurant;
import com.example.zomatox.entity.User;
import com.example.zomatox.entity.enums.OrderStatus;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.repository.OrderRepository;
import com.example.zomatox.repository.RestaurantRepository;
import com.example.zomatox.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ReviewService {
  private final ReviewRepository reviewRepository;
  private final OrderRepository orderRepository;
  private final RestaurantRepository restaurantRepository;

  public Review createReview(User user, Long restaurantId, Long orderId, int rating, String comment) {
    Restaurant r = restaurantRepository.findById(restaurantId).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Restaurant not found: " + restaurantId));
    Order o = orderRepository.findById(orderId).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Order not found: " + orderId));

    if (!o.getUser().getId().equals(user.getId())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "Cannot review someone else's order");
    }
    if (o.getRestaurant().getId() != restaurantId) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Order does not belong to this restaurant");
    }
    if (o.getStatus() != OrderStatus.DELIVERED) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Can review only after DELIVERED");
    }
    if (reviewRepository.findByOrderId(orderId).isPresent()) {
      throw new ApiException(HttpStatus.CONFLICT, "Only one review per order");
    }
    if (rating < 1 || rating > 5) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "rating must be 1..5");
    }

    Review rev = Review.builder()
      .order(o)
      .restaurant(r)
      .user(user)
      .rating(rating)
      .comment(comment)
      .createdAt(Instant.now())
      .build();

    Review saved = reviewRepository.save(rev);

    // Optional: refresh restaurant ratingAvg (simple running average)
    // Keep it simple: recompute from DB
    double avg = reviewRepository.findByRestaurant(r, PageRequest.of(0, 5000)).getContent().stream()
      .mapToInt(Review::getRating).average().orElse(r.getRatingAvg());
    r.setRatingAvg(avg);
    restaurantRepository.save(r);

    return saved;
  }

  public Page<Review> listReviews(Long restaurantId, int page) {
    Restaurant r = restaurantRepository.findById(restaurantId).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Restaurant not found: " + restaurantId));
    return reviewRepository.findByRestaurant(r, PageRequest.of(Math.max(page,0), 10, Sort.by("id").descending()));
  }
}
```

---

## 3.10 Controllers (NEW)

### `backend/src/main/java/com/example/zomatox/controller/OwnerController.java`

```java
package com.example.zomatox.controller;

import com.example.zomatox.dto.admin.*;
import com.example.zomatox.dto.restaurants.MenuItemResponse;
import com.example.zomatox.dto.restaurants.RestaurantResponse;
import com.example.zomatox.entity.MenuItem;
import com.example.zomatox.entity.Order;
import com.example.zomatox.entity.Restaurant;
import com.example.zomatox.entity.User;
import com.example.zomatox.entity.enums.OrderStatus;
import com.example.zomatox.entity.enums.UserRole;
import com.example.zomatox.service.OwnerService;
import com.example.zomatox.service.UserService;
import com.example.zomatox.util.RequestContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner")
@RequiredArgsConstructor
public class OwnerController {
  private final UserService userService;
  private final OwnerService ownerService;

  @GetMapping("/restaurants")
  public List<RestaurantResponse> myRestaurants(
    @RequestHeader(value="X-User-Id", required=false) String userId,
    @RequestHeader(value="X-User-Role", required=false) String roleHeader
  ) {
    User u = RequestContext.requireUser(userService, userId);
    RequestContext.requireRole(RequestContext.resolveRole(u, roleHeader), UserRole.OWNER);
    return ownerService.myRestaurants(u).stream().map(RestaurantResponse::from).toList();
  }

  @PostMapping("/restaurants")
  public RestaurantResponse createRestaurant(
    @RequestHeader(value="X-User-Id", required=false) String userId,
    @RequestHeader(value="X-User-Role", required=false) String roleHeader,
    @Valid @RequestBody AdminRestaurantCreateRequest req
  ) {
    User u = RequestContext.requireUser(userService, userId);
    RequestContext.requireRole(RequestContext.resolveRole(u, roleHeader), UserRole.OWNER);
    Restaurant r = ownerService.createRestaurant(u, req);
    return RestaurantResponse.from(r);
  }

  @PutMapping("/restaurants/{id}")
  public RestaurantResponse updateRestaurant(
    @RequestHeader(value="X-User-Id", required=false) String userId,
    @RequestHeader(value="X-User-Role", required=false) String roleHeader,
    @PathVariable Long id,
    @Valid @RequestBody AdminRestaurantCreateRequest req
  ) {
    User u = RequestContext.requireUser(userService, userId);
    RequestContext.requireRole(RequestContext.resolveRole(u, roleHeader), UserRole.OWNER);
    return RestaurantResponse.from(ownerService.updateRestaurant(u, id, req));
  }

  @PostMapping("/restaurants/{id}/menu-items")
  public MenuItemResponse addMenuItem(
    @RequestHeader(value="X-User-Id", required=false) String userId,
    @RequestHeader(value="X-User-Role", required=false) String roleHeader,
    @PathVariable Long id,
    @Valid @RequestBody AdminMenuItemCreateRequest req
  ) {
    User u = RequestContext.requireUser(userService, userId);
    RequestContext.requireRole(RequestContext.resolveRole(u, roleHeader), UserRole.OWNER);
    MenuItem mi = ownerService.addMenuItem(u, id, req);
    return MenuItemResponse.from(mi);
  }

  @PutMapping("/menu-items/{id}")
  public MenuItemResponse updateMenuItem(
    @RequestHeader(value="X-User-Id", required=false) String userId,
    @RequestHeader(value="X-User-Role", required=false) String roleHeader,
    @PathVariable Long id,
    @Valid @RequestBody AdminMenuItemUpdateRequest req
  ) {
    User u = RequestContext.requireUser(userService, userId);
    RequestContext.requireRole(RequestContext.resolveRole(u, roleHeader), UserRole.OWNER);
    return MenuItemResponse.from(ownerService.updateMenuItem(u, id, req));
  }

  @GetMapping("/orders")
  public List<Order> orders(
    @RequestHeader(value="X-User-Id", required=false) String userId,
    @RequestHeader(value="X-User-Role", required=false) String roleHeader,
    @RequestParam(required = false) OrderStatus status
  ) {
    User u = RequestContext.requireUser(userService, userId);
    RequestContext.requireRole(RequestContext.resolveRole(u, roleHeader), UserRole.OWNER);
    return ownerService.ownerOrders(u, status);
  }

  @PostMapping("/orders/{orderId}/status")
  public Order setStatus(
    @RequestHeader(value="X-User-Id", required=false) String userId,
    @RequestHeader(value="X-User-Role", required=false) String roleHeader,
    @PathVariable Long orderId,
    @RequestParam OrderStatus next
  ) {
    User u = RequestContext.requireUser(userService, userId);
    RequestContext.requireRole(RequestContext.resolveRole(u, roleHeader), UserRole.OWNER);
    return ownerService.changeOrderStatus(u, orderId, next);
  }
}
```

### `backend/src/main/java/com/example/zomatox/controller/DeliveryController.java`

```java
package com.example.zomatox.controller;

import com.example.zomatox.entity.Order;
import com.example.zomatox.entity.User;
import com.example.zomatox.entity.enums.OrderStatus;
import com.example.zomatox.entity.enums.UserRole;
import com.example.zomatox.service.DeliveryService;
import com.example.zomatox.service.UserService;
import com.example.zomatox.util.RequestContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {
  private final UserService userService;
  private final DeliveryService deliveryService;

  @GetMapping("/jobs")
  public List<Order> jobs(
    @RequestHeader(value="X-User-Id", required=false) String userId,
    @RequestHeader(value="X-User-Role", required=false) String roleHeader,
    @RequestParam(required = false, defaultValue = "AVAILABLE") String status
  ) {
    User u = RequestContext.requireUser(userService, userId);
    RequestContext.requireRole(RequestContext.resolveRole(u, roleHeader), UserRole.DELIVERY_PARTNER);
    return deliveryService.jobs(u, status);
  }

  @PostMapping("/jobs/{orderId}/accept")
  public Order accept(
    @RequestHeader(value="X-User-Id", required=false) String userId,
    @RequestHeader(value="X-User-Role", required=false) String roleHeader,
    @PathVariable Long orderId
  ) {
    User u = RequestContext.requireUser(userService, userId);
    RequestContext.requireRole(RequestContext.resolveRole(u, roleHeader), UserRole.DELIVERY_PARTNER);
    return deliveryService.accept(u, orderId);
  }

  @PostMapping("/orders/{orderId}/status")
  public Order updateStatus(
    @RequestHeader(value="X-User-Id", required=false) String userId,
    @RequestHeader(value="X-User-Role", required=false) String roleHeader,
    @PathVariable Long orderId,
    @RequestParam OrderStatus next
  ) {
    User u = RequestContext.requireUser(userService, userId);
    RequestContext.requireRole(RequestContext.resolveRole(u, roleHeader), UserRole.DELIVERY_PARTNER);
    return deliveryService.updateStatus(u, orderId, next);
  }
}
```

### `backend/src/main/java/com/example/zomatox/controller/ReviewController.java`

```java
package com.example.zomatox.controller;

import com.example.zomatox.entity.Review;
import com.example.zomatox.entity.User;
import com.example.zomatox.service.ReviewService;
import com.example.zomatox.service.UserService;
import com.example.zomatox.util.RequestContext;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReviewController {
  private final UserService userService;
  private final ReviewService reviewService;

  @Data
  static class CreateReviewRequest {
    @NotNull private Long orderId;
    @Min(1) @Max(5) private int rating;
    private String comment;
  }

  @PostMapping("/api/restaurants/{id}/reviews")
  public Review create(
    @RequestHeader(value="X-User-Id", required=false) String userId,
    @PathVariable Long id,
    @RequestBody CreateReviewRequest req
  ) {
    User u = RequestContext.requireUser(userService, userId);
    return reviewService.createReview(u, id, req.getOrderId(), req.getRating(), req.getComment());
  }

  @GetMapping("/api/restaurants/{id}/reviews")
  public Page<Review> list(@PathVariable Long id, @RequestParam(defaultValue = "0") int page) {
    return reviewService.listReviews(id, page);
  }
}
```

### `backend/src/main/java/com/example/zomatox/controller/OrderEventController.java`

```java
package com.example.zomatox.controller;

import com.example.zomatox.entity.Order;
import com.example.zomatox.entity.OrderEvent;
import com.example.zomatox.entity.User;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.repository.OrderEventRepository;
import com.example.zomatox.repository.OrderRepository;
import com.example.zomatox.service.UserService;
import com.example.zomatox.util.RequestContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderEventController {
  private final UserService userService;
  private final OrderRepository orderRepository;
  private final OrderEventRepository orderEventRepository;

  @GetMapping("/{id}/events")
  public List<OrderEvent> events(
    @RequestHeader(value="X-User-Id", required=false) String userId,
    @PathVariable Long id
  ) {
    User u = RequestContext.requireUser(userService, userId);
    Order o = orderRepository.findById(id).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Order not found: " + id));
    if (!o.getUser().getId().equals(u.getId())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "Not your order");
    }
    return orderEventRepository.findByOrderOrderByIdAsc(o);
  }
}
```

---

## 3.11 Update OrderService + PaymentService (MOD)

### `backend/src/main/java/com/example/zomatox/service/OrderService.java` (MOD)

Replace your v1 `OrderService` with this v2-ready version (same method names kept; v1 endpoints still work):

```java
package com.example.zomatox.service;

import com.example.zomatox.dto.orders.OrderItemResponse;
import com.example.zomatox.dto.orders.OrderResponse;
import com.example.zomatox.entity.*;
import com.example.zomatox.entity.enums.OrderStatus;
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
  private final OrderEventRepository orderEventRepository;
  private final CartService cartService;
  private final OrderStateMachine sm;

  public OrderResponse createOrder(User user, Long addressId) {
    Address addr = addressRepository.findById(addressId).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Address not found: " + addressId));

    if (!addr.getUser().getId().equals(user.getId())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "Address does not belong to user");
    }

    List<CartItem> cartItems = cartService.getRawItems(user);
    if (cartItems.isEmpty()) throw new ApiException(HttpStatus.BAD_REQUEST, "Cart is empty");

    // Stock check
    for (CartItem ci : cartItems) {
      MenuItem mi = ci.getMenuItem();
      if (!mi.isAvailable()) throw new ApiException(HttpStatus.BAD_REQUEST, "Item not available: " + mi.getName());
      if (mi.getStockQty() < ci.getQty()) {
        throw new ApiException(HttpStatus.BAD_REQUEST, "Not enough stock for " + mi.getName() + ". Available=" + mi.getStockQty());
      }
    }

    Restaurant restaurant = cartItems.get(0).getMenuItem().getRestaurant();
    long itemTotal = cartItems.stream().mapToLong(ci -> ci.getMenuItem().getPrice() * ci.getQty()).sum();
    long deliveryFee = itemTotal >= 500 ? 0 : 40;
    long payable = itemTotal + deliveryFee;

    Instant now = Instant.now();
    Order order = Order.builder()
      .user(user)
      .restaurant(restaurant)
      .status(OrderStatus.PAYMENT_PENDING)
      .itemTotal(itemTotal)
      .deliveryFee(deliveryFee)
      .payableTotal(payable)
      .createdAt(now)
      .updatedAt(now)
      .build();

    order = orderRepository.save(order);
    createEvent(order, order.getStatus(), "Order created, awaiting payment", now);

    for (CartItem ci : cartItems) {
      MenuItem mi = ci.getMenuItem();

      // v1 behavior kept: deduct stock at order creation
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
      .status(com.example.zomatox.entity.enums.PaymentStatus.INITIATED)
      .method("MOCK")
      .createdAt(now)
      .build();
    paymentRepository.save(payment);

    cartService.clearCart(user);
    return getOrderResponse(order);
  }

  public List<OrderResponse> listOrders(User user) {
    return orderRepository.findByUserOrderByIdDesc(user).stream().map(this::getOrderResponse).toList();
  }

  public OrderResponse getOrder(User user, Long orderId) {
    Order o = orderRepository.findById(orderId).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Order not found: " + orderId));
    if (!o.getUser().getId().equals(user.getId())) throw new ApiException(HttpStatus.FORBIDDEN, "Order does not belong to user");
    return getOrderResponse(o);
  }

  private OrderResponse getOrderResponse(Order o) {
    List<OrderItemResponse> items = orderItemRepository.findByOrder(o).stream()
      .map(oi -> new OrderItemResponse(oi.getMenuItemNameSnapshot(), oi.getPriceSnapshot(), oi.getQty(), oi.getLineTotal()))
      .toList();
    return OrderResponse.of(o, items);
  }

  // ===== v2 helpers (used by Owner/Delivery/Payment) =====

  public Order setStatusWithEvent(Order o, OrderStatus next, String message, Instant now) {
    sm.requireTransitionAllowed(o.getStatus(), next);
    o.setStatus(next);
    o.setUpdatedAt(now);
    Order saved = orderRepository.save(o);
    createEvent(saved, next, message, now);
    return saved;
  }

  public void createEvent(Order order, OrderStatus status, String message, Instant now) {
    OrderEvent ev = OrderEvent.builder()
      .order(order)
      .status(status.name())
      .message(message)
      .createdAt(now)
      .build();
    orderEventRepository.save(ev);
  }
}
```

### `backend/src/main/java/com/example/zomatox/service/PaymentService.java` (MOD)

Replace your v1 PaymentService with this:

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

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
  private final OrderRepository orderRepository;
  private final PaymentRepository paymentRepository;
  private final OrderService orderService;
  private final OrderStateMachine sm;

  public PaymentResponse confirm(Long orderId, String result) {
    Order order = orderRepository.findById(orderId).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Order not found: " + orderId));

    Payment payment = paymentRepository.findByOrder(order).orElseThrow(() ->
      new ApiException(HttpStatus.NOT_FOUND, "Payment not found for order: " + orderId));

    Instant now = Instant.now();

    if ("SUCCESS".equalsIgnoreCase(result)) {
      payment.setStatus(PaymentStatus.SUCCESS);

      // v1-compatible: payment success => PAID,
      // v2 add-on: auto move to CONFIRMED so owner can start processing.
      orderService.setStatusWithEvent(order, OrderStatus.PAID, "Payment successful", now);
      orderService.setStatusWithEvent(order, OrderStatus.CONFIRMED, "Auto-confirmed for restaurant", now);

    } else if ("FAIL".equalsIgnoreCase(result)) {
      payment.setStatus(PaymentStatus.FAIL);
      orderService.setStatusWithEvent(order, OrderStatus.PAYMENT_FAILED, "Payment failed", now);

    } else {
      throw new ApiException(HttpStatus.BAD_REQUEST, "result must be SUCCESS or FAIL");
    }

    paymentRepository.save(payment);
    return PaymentResponse.from(payment);
  }
}
```

---

## 3.12 Seed Data (MOD) — ensure roles + owner + delivery + sample orders

### `backend/src/main/java/com/example/zomatox/seed/DataSeeder.java` (MOD)

Replace with this v2 seeder (works on Postgres+Flyway):

```java
package com.example.zomatox.seed;

import com.example.zomatox.entity.*;
import com.example.zomatox.entity.enums.OrderStatus;
import com.example.zomatox.entity.enums.PaymentStatus;
import com.example.zomatox.entity.enums.UserRole;
import com.example.zomatox.repository.*;
import com.example.zomatox.service.OrderService;
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
  private final OrderEventRepository eventRepo;

  @Override
  public void run(String... args) {
    if (userRepo.count() > 0) return;

    User customer = userRepo.save(User.builder().name("Customer").email("user@zomatox.local").role(UserRole.CUSTOMER).build());
    User owner = userRepo.save(User.builder().name("Owner").email("owner@zomatox.local").role(UserRole.OWNER).build());
    User delivery = userRepo.save(User.builder().name("Delivery").email("delivery@zomatox.local").role(UserRole.DELIVERY_PARTNER).build());

    addressRepo.save(Address.builder().user(customer).line1("Salt Lake, Sector V").city("Kolkata").pincode("700091").phone("9999999999").build());
    addressRepo.save(Address.builder().user(customer).line1("Indiranagar").city("Bengaluru").pincode("560038").phone("8888888888").build());

    List<String> cities = List.of("Kolkata", "Bengaluru");
    List<String> cuisines = List.of("Bengali", "North Indian", "Chinese", "Biryani", "South Indian");
    Random rnd = new Random(7);

    // 10 restaurants; first 3 owned by owner
    for (int i = 1; i <= 10; i++) {
      String city = cities.get(i <= 5 ? 0 : 1);
      Restaurant r = Restaurant.builder()
        .name("Restaurant " + i)
        .city(city)
        .cuisineType(cuisines.get(rnd.nextInt(cuisines.size())))
        .ratingAvg(4.0)
        .deliveryTimeMin(25 + rnd.nextInt(25))
        .imageUrl("https://picsum.photos/seed/rest" + i + "/640/360")
        .owner(i <= 3 ? owner : null)
        .build();
      r = restaurantRepo.save(r);

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

    // Create 2-3 demo orders in different statuses for owner + delivery demo
    Restaurant ownedRest = restaurantRepo.findAll().stream().filter(x -> x.getOwner()!=null).findFirst().orElseThrow();
    List<MenuItem> menu = menuRepo.findByRestaurantOrderByIdAsc(ownedRest);

    createDemoOrder(customer, ownedRest, menu.subList(0,2), OrderStatus.CONFIRMED, null);
    createDemoOrder(customer, ownedRest, menu.subList(2,4), OrderStatus.READY_FOR_PICKUP, null);
    createDemoOrder(customer, ownedRest, menu.subList(4,6), OrderStatus.OUT_FOR_DELIVERY, delivery);
  }

  private void createDemoOrder(User user, Restaurant r, List<MenuItem> items, OrderStatus status, User deliveryPartner) {
    Instant now = Instant.now();
    long itemTotal = 0;

    Order o = Order.builder()
      .user(user)
      .restaurant(r)
      .status(status)
      .itemTotal(0)
      .deliveryFee(40)
      .payableTotal(0)
      .createdAt(now)
      .updatedAt(now)
      .deliveryPartner(deliveryPartner)
      .build();
    o = orderRepo.save(o);

    for (MenuItem mi : items) {
      int qty = 1;
      long line = mi.getPrice() * qty;
      itemTotal += line;
      orderItemRepo.save(OrderItem.builder()
        .order(o)
        .menuItemNameSnapshot(mi.getName())
        .priceSnapshot(mi.getPrice())
        .qty(qty)
        .lineTotal(line)
        .build());
    }

    o.setItemTotal(itemTotal);
    o.setPayableTotal(itemTotal + o.getDeliveryFee());
    orderRepo.save(o);

    paymentRepo.save(Payment.builder()
      .order(o)
      .status(PaymentStatus.SUCCESS)
      .method("MOCK")
      .createdAt(now)
      .build());

    eventRepo.save(OrderEvent.builder()
      .order(o)
      .status(status.name())
      .message("Seeded status: " + status)
      .createdAt(now)
      .build());
  }
}
```

> Seeded User IDs will be 1=customer, 2=owner, 3=delivery (use UI dropdown / curl headers)

---

# 4) Tests (v2 minimum)

> These tests use Testcontainers Postgres so they align with “PostgreSQL + Flyway” requirement.

## `backend/src/test/java/com/example/zomatox/service/OrderTransitionTest.java` (NEW)

```java
package com.example.zomatox.service;

import com.example.zomatox.entity.Order;
import com.example.zomatox.entity.enums.OrderStatus;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

@Testcontainers
@SpringBootTest
class OrderTransitionTest {

  @Container
  static PostgreSQLContainer<?> pg = new PostgreSQLContainer<>("postgres:16")
    .withDatabaseName("zomatox")
    .withUsername("zomatox")
    .withPassword("zomatox");

  @DynamicPropertySource
  static void props(DynamicPropertyRegistry r) {
    r.add("spring.datasource.url", pg::getJdbcUrl);
    r.add("spring.datasource.username", pg::getUsername);
    r.add("spring.datasource.password", pg::getPassword);
  }

  @Autowired OrderRepository orderRepo;
  @Autowired OrderService orderService;

  @Test
  void confirmed_to_preparing_to_ready_allowed() {
    Order o = orderRepo.findAll().stream().filter(x -> x.getStatus() == OrderStatus.CONFIRMED).findFirst().orElseThrow();
    orderService.setStatusWithEvent(o, OrderStatus.PREPARING, "t", Instant.now());
    orderService.setStatusWithEvent(o, OrderStatus.READY_FOR_PICKUP, "t", Instant.now());
    Order updated = orderRepo.findById(o.getId()).orElseThrow();
    assertThat(updated.getStatus()).isEqualTo(OrderStatus.READY_FOR_PICKUP);
  }

  @Test
  void invalid_transition_rejected() {
    Order o = orderRepo.findAll().stream().filter(x -> x.getStatus() == OrderStatus.CONFIRMED).findFirst().orElseThrow();
    assertThatThrownBy(() -> orderService.setStatusWithEvent(o, OrderStatus.DELIVERED, "bad", Instant.now()))
      .isInstanceOf(ApiException.class)
      .hasMessageContaining("Illegal transition");
  }
}
```

## `backend/src/test/java/com/example/zomatox/service/DeliveryAcceptTest.java` (NEW)

```java
package com.example.zomatox.service;

import com.example.zomatox.entity.Order;
import com.example.zomatox.entity.User;
import com.example.zomatox.entity.enums.OrderStatus;
import com.example.zomatox.entity.enums.UserRole;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.repository.OrderRepository;
import com.example.zomatox.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.containers.PostgreSQLContainer;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;

import static org.assertj.core.api.Assertions.*;

@Testcontainers
@SpringBootTest
class DeliveryAcceptTest {

  @Container
  static PostgreSQLContainer<?> pg = new PostgreSQLContainer<>("postgres:16")
    .withDatabaseName("zomatox")
    .withUsername("zomatox")
    .withPassword("zomatox");

  @DynamicPropertySource
  static void props(DynamicPropertyRegistry r) {
    r.add("spring.datasource.url", pg::getJdbcUrl);
    r.add("spring.datasource.username", pg::getUsername);
    r.add("spring.datasource.password", pg::getPassword);
  }

  @Autowired DeliveryService deliveryService;
  @Autowired OrderRepository orderRepo;
  @Autowired UserRepository userRepo;

  @Test
  void cannot_accept_unless_ready_for_pickup_and_unassigned() {
    User delivery = userRepo.findAll().stream().filter(u -> u.getRole() == UserRole.DELIVERY_PARTNER).findFirst().orElseThrow();

    Order notReady = orderRepo.findAll().stream().filter(o -> o.getStatus() == OrderStatus.CONFIRMED).findFirst().orElseThrow();
    assertThatThrownBy(() -> deliveryService.accept(delivery, notReady.getId()))
      .isInstanceOf(ApiException.class)
      .hasMessageContaining("READY_FOR_PICKUP");

    Order ready = orderRepo.findAll().stream().filter(o -> o.getStatus() == OrderStatus.READY_FOR_PICKUP && o.getDeliveryPartner() == null).findFirst().orElseThrow();
    deliveryService.accept(delivery, ready.getId());

    assertThatThrownBy(() -> deliveryService.accept(delivery, ready.getId()))
      .isInstanceOf(ApiException.class)
      .hasMessageContaining("already assigned");
  }
}
```

## `backend/src/test/java/com/example/zomatox/service/ReviewConstraintsTest.java` (NEW)

```java
package com.example.zomatox.service;

import com.example.zomatox.entity.Order;
import com.example.zomatox.entity.User;
import com.example.zomatox.entity.enums.OrderStatus;
import com.example.zomatox.entity.enums.UserRole;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.repository.OrderRepository;
import com.example.zomatox.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.containers.PostgreSQLContainer;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;

import static org.assertj.core.api.Assertions.*;

@Testcontainers
@SpringBootTest
class ReviewConstraintsTest {

  @Container
  static PostgreSQLContainer<?> pg = new PostgreSQLContainer<>("postgres:16")
    .withDatabaseName("zomatox")
    .withUsername("zomatox")
    .withPassword("zomatox");

  @DynamicPropertySource
  static void props(DynamicPropertyRegistry r) {
    r.add("spring.datasource.url", pg::getJdbcUrl);
    r.add("spring.datasource.username", pg::getUsername);
    r.add("spring.datasource.password", pg::getPassword);
  }

  @Autowired ReviewService reviewService;
  @Autowired OrderRepository orderRepo;
  @Autowired UserRepository userRepo;

  @Test
  void cannot_review_unless_delivered_and_only_once() {
    User customer = userRepo.findAll().stream().filter(u -> u.getRole() == UserRole.CUSTOMER).findFirst().orElseThrow();

    Order notDelivered = orderRepo.findAll().stream().filter(o -> o.getStatus() == OrderStatus.CONFIRMED).findFirst().orElseThrow();
    assertThatThrownBy(() -> reviewService.createReview(customer, notDelivered.getRestaurant().getId(), notDelivered.getId(), 5, "x"))
      .isInstanceOf(ApiException.class)
      .hasMessageContaining("DELIVERED");

    // Make a delivered order by moving one seeded OUT_FOR_DELIVERY -> DELIVERED (via service not present here)
    Order out = orderRepo.findAll().stream().filter(o -> o.getStatus() == OrderStatus.OUT_FOR_DELIVERY).findFirst().orElseThrow();
    out.setStatus(OrderStatus.DELIVERED);
    orderRepo.save(out);

    reviewService.createReview(customer, out.getRestaurant().getId(), out.getId(), 4, "Good");
    assertThatThrownBy(() -> reviewService.createReview(customer, out.getRestaurant().getId(), out.getId(), 5, "Again"))
      .isInstanceOf(ApiException.class)
      .hasMessageContaining("Only one review");
  }
}
```

---

# 5) Frontend v2 — Updated Core + New Routes/Pages (minimal, runnable)

## 5.1 `frontend/src/app/core/user-context.service.ts` (MOD)

```ts
import { Injectable } from '@angular/core';

export type DemoUser = { id: number; label: string; role: 'CUSTOMER'|'OWNER'|'DELIVERY_PARTNER' };

@Injectable({ providedIn: 'root' })
export class UserContextService {
  users: DemoUser[] = [
    { id: 1, label: 'Customer (id=1)', role: 'CUSTOMER' },
    { id: 2, label: 'Owner (id=2)', role: 'OWNER' },
    { id: 3, label: 'Delivery (id=3)', role: 'DELIVERY_PARTNER' },
  ];

  private keyId = 'zomatox.userId';
  private keyRole = 'zomatox.userRole';

  get userId(): number {
    const v = localStorage.getItem(this.keyId);
    return v ? Number(v) : 1;
  }
  set userId(id: number) { localStorage.setItem(this.keyId, String(id)); }

  get role(): DemoUser['role'] {
    const v = localStorage.getItem(this.keyRole) as any;
    return v ?? 'CUSTOMER';
  }
  set role(r: DemoUser['role']) { localStorage.setItem(this.keyRole, r); }

  setUser(u: DemoUser) {
    this.userId = u.id;
    this.role = u.role;
  }
}
```

## 5.2 `frontend/src/app/core/api.service.ts` (MOD: add X-User-Role + v2 endpoints)

```ts
import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpInterceptorFn } from '@angular/common/http';
import { UserContextService } from './user-context.service';
import { Cart, MenuItem, Order, Restaurant } from './models';

const API = 'http://localhost:8080/api';

export const apiHeadersInterceptor: HttpInterceptorFn = (req, next) => {
  const uc = inject(UserContextService);
  const cloned = req.clone({
    setHeaders: {
      'X-User-Id': String(uc.userId),
      'X-User-Role': uc.role, // dev testing convenience
    },
  });
  return next(cloned);
};

@Injectable({ providedIn: 'root' })
export class ApiService {
  private http = inject(HttpClient);

  restaurants(params: any) { return this.http.get<any>(`${API}/restaurants`, { params }); }
  restaurant(id: number) { return this.http.get<Restaurant>(`${API}/restaurants/${id}`); }
  menu(restaurantId: number) { return this.http.get<MenuItem[]>(`${API}/restaurants/${restaurantId}/menu`); }

  getCart() { return this.http.get<Cart>(`${API}/cart`); }
  upsertCartItem(menuItemId: number, qty: number) { return this.http.put<Cart>(`${API}/cart/items`, { menuItemId, qty }); }
  removeCartItem(menuItemId: number) { return this.http.delete<Cart>(`${API}/cart/items/${menuItemId}`); }

  createOrder(addressId: number) { return this.http.post<Order>(`${API}/orders`, { addressId }); }
  orders() { return this.http.get<Order[]>(`${API}/orders`); }
  order(id: number) { return this.http.get<Order>(`${API}/orders/${id}`); }

  confirmPayment(orderId: number, result: 'SUCCESS' | 'FAIL') {
    return this.http.post<any>(`${API}/payments/${orderId}/confirm`, null, { params: { result } });
  }

  // v2: events + reviews
  orderEvents(orderId: number) { return this.http.get<any[]>(`${API}/orders/${orderId}/events`); }

  restaurantReviews(restaurantId: number, page=0) {
    return this.http.get<any>(`${API}/restaurants/${restaurantId}/reviews`, { params: { page } });
  }
  postReview(restaurantId: number, orderId: number, rating: number, comment: string) {
    return this.http.post<any>(`${API}/restaurants/${restaurantId}/reviews`, { orderId, rating, comment });
  }

  // v2: owner
  ownerRestaurants() { return this.http.get<any[]>(`${API}/owner/restaurants`); }
  ownerOrders(status?: string) { return this.http.get<any[]>(`${API}/owner/orders`, { params: status ? { status } : {} as any }); }
  ownerSetOrderStatus(orderId: number, next: string) {
    return this.http.post<any>(`${API}/owner/orders/${orderId}/status`, null, { params: { next } });
  }

  // v2: delivery
  deliveryJobs(status: 'AVAILABLE'|'ASSIGNED') { return this.http.get<any[]>(`${API}/delivery/jobs`, { params: { status } }); }
  deliveryAccept(orderId: number) { return this.http.post<any>(`${API}/delivery/jobs/${orderId}/accept`, null); }
  deliverySetStatus(orderId: number, next: string) {
    return this.http.post<any>(`${API}/delivery/orders/${orderId}/status`, null, { params: { next } });
  }
}
```

## 5.3 `frontend/src/app/app.routes.ts` (MOD: add owner + delivery routes)

```ts
import { Routes } from '@angular/router';
import { RestaurantListComponent } from './features/restaurants/restaurant-list.component';
import { RestaurantDetailComponent } from './features/restaurants/restaurant-detail.component';
import { CartComponent } from './features/cart/cart.component';
import { CheckoutComponent } from './features/checkout/checkout.component';
import { OrderListComponent } from './features/orders/order-list.component';
import { OrderDetailComponent } from './features/orders/order-detail.component';

import { OwnerOrdersComponent } from './features/owner/owner-orders.component';
import { OwnerRestaurantsComponent } from './features/owner/owner-restaurants.component';
import { DeliveryJobsComponent } from './features/delivery/delivery-jobs.component';
import { DeliveryOrderComponent } from './features/delivery/delivery-order.component';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'restaurants' },

  // customer
  { path: 'restaurants', component: RestaurantListComponent },
  { path: 'restaurants/:id', component: RestaurantDetailComponent },
  { path: 'cart', component: CartComponent },
  { path: 'checkout', component: CheckoutComponent },
  { path: 'orders', component: OrderListComponent },
  { path: 'orders/:id', component: OrderDetailComponent },

  // owner
  { path: 'owner/restaurants', component: OwnerRestaurantsComponent },
  { path: 'owner/orders', component: OwnerOrdersComponent },

  // delivery
  { path: 'delivery/jobs', component: DeliveryJobsComponent },
  { path: 'delivery/order/:id', component: DeliveryOrderComponent },
];
```

## 5.4 `frontend/src/app/app.component.ts` (MOD: dropdown shows role + portal links)

> Just update your header portion accordingly (keep rest as-is).

```ts
import { CommonModule } from '@angular/common';
import { Component, computed, inject } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { UserContextService } from './core/user-context.service';
import { CartStore } from './core/cart.store';

@Component({
  standalone: true,
  selector: 'app-root',
  imports: [CommonModule, RouterOutlet, RouterLink],
  template: `
  <div class="min-h-screen">
    <header class="sticky top-0 z-10 bg-white border-b">
      <div class="max-w-5xl mx-auto px-4 py-3 flex items-center gap-4">
        <a routerLink="/restaurants" class="font-bold text-lg">ZomatoX</a>

        <nav class="flex gap-3 text-sm">
          <a routerLink="/restaurants" class="hover:underline">Customer</a>
          <a routerLink="/owner/orders" class="hover:underline">Owner</a>
          <a routerLink="/delivery/jobs" class="hover:underline">Delivery</a>
        </nav>

        <div class="ml-auto flex items-center gap-3">
          <select class="border rounded px-2 py-1 text-sm"
                  [value]="selectedKey()"
                  (change)="switchUser($any($event.target).value)">
            <option *ngFor="let u of users" [value]="u.id + ':' + u.role">
              {{u.label}} • {{u.role}}
            </option>
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
  selectedKey = computed(() => `${this.uc.userId}:${this.uc.role}`);
  cartCount = computed(() => this.cartStore.cart()?.items?.reduce((a, x) => a + x.qty, 0) ?? 0);

  constructor() { this.cartStore.load(); }

  switchUser(v: string) {
    const [id, role] = v.split(':');
    const u = this.users.find(x => x.id === Number(id) && x.role === role as any);
    if (u) this.uc.setUser(u);
    this.cartStore.load();
  }
}
```

---

## 5.5 NEW Owner Components (minimal UI)

### `frontend/src/app/features/owner/owner-restaurants.component.ts`

```ts
import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { ApiService } from '../../core/api.service';

@Component({
  standalone: true,
  imports: [CommonModule],
  template: `
  <h1 class="text-xl font-bold mb-3">Owner • My Restaurants</h1>
  <div class="text-sm text-slate-600 mb-3">Uses X-User-Role=OWNER</div>

  <div class="grid gap-3">
    <div *ngFor="let r of restaurants()" class="bg-white border rounded-xl p-4">
      <div class="font-semibold">{{r.name}}</div>
      <div class="text-sm text-slate-600">{{r.city}} • {{r.cuisineType}}</div>
    </div>
  </div>
  `,
})
export class OwnerRestaurantsComponent {
  private api = inject(ApiService);
  restaurants = signal<any[]>([]);

  constructor() {
    this.api.ownerRestaurants().subscribe(rs => this.restaurants.set(rs));
  }
}
```

### `frontend/src/app/features/owner/owner-orders.component.ts`

```ts
import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { ApiService } from '../../core/api.service';

@Component({
  standalone: true,
  imports: [CommonModule],
  template: `
  <h1 class="text-xl font-bold mb-3">Owner • Orders Queue</h1>

  <div class="flex gap-2 mb-3">
    <button class="border rounded px-3 py-1" (click)="load('CONFIRMED')">CONFIRMED</button>
    <button class="border rounded px-3 py-1" (click)="load('PREPARING')">PREPARING</button>
    <button class="border rounded px-3 py-1" (click)="load('READY_FOR_PICKUP')">READY_FOR_PICKUP</button>
  </div>

  <div class="grid gap-3">
    <div *ngFor="let o of orders()" class="bg-white border rounded-xl p-4">
      <div class="flex justify-between">
        <div class="font-semibold">Order #{{o.id}}</div>
        <div class="text-sm bg-slate-100 rounded px-2 py-1">{{o.status}}</div>
      </div>

      <div class="text-sm text-slate-600 mt-1">Payable ₹{{o.payableTotal}}</div>

      <div class="mt-3 flex gap-2">
        <button class="bg-black text-white rounded px-3 py-2"
                (click)="set(o.id, 'PREPARING')">Set PREPARING</button>
        <button class="border rounded px-3 py-2"
                (click)="set(o.id, 'READY_FOR_PICKUP')">Set READY_FOR_PICKUP</button>
      </div>
    </div>
  </div>
  `,
})
export class OwnerOrdersComponent {
  private api = inject(ApiService);
  orders = signal<any[]>([]);

  constructor() { this.load('CONFIRMED'); }

  load(status: string) {
    this.api.ownerOrders(status).subscribe(os => this.orders.set(os));
  }

  set(orderId: number, next: string) {
    this.api.ownerSetOrderStatus(orderId, next).subscribe(() => this.load(next === 'PREPARING' ? 'PREPARING' : 'READY_FOR_PICKUP'));
  }
}
```

---

## 5.6 NEW Delivery Components (minimal UI)

### `frontend/src/app/features/delivery/delivery-jobs.component.ts`

```ts
import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ApiService } from '../../core/api.service';

@Component({
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
  <h1 class="text-xl font-bold mb-3">Delivery • Jobs</h1>

  <div class="flex gap-2 mb-3">
    <button class="border rounded px-3 py-1" (click)="load('AVAILABLE')">AVAILABLE</button>
    <button class="border rounded px-3 py-1" (click)="load('ASSIGNED')">ASSIGNED</button>
  </div>

  <div class="grid gap-3">
    <div *ngFor="let o of jobs()" class="bg-white border rounded-xl p-4">
      <div class="flex justify-between">
        <div class="font-semibold">Order #{{o.id}}</div>
        <div class="text-sm bg-slate-100 rounded px-2 py-1">{{o.status}}</div>
      </div>

      <div class="mt-3 flex gap-2">
        <button *ngIf="mode()==='AVAILABLE'" class="bg-black text-white rounded px-3 py-2"
                (click)="accept(o.id)">Accept</button>
        <a class="border rounded px-3 py-2" [routerLink]="['/delivery/order', o.id]">Open</a>
      </div>
    </div>
  </div>
  `,
})
export class DeliveryJobsComponent {
  private api = inject(ApiService);
  jobs = signal<any[]>([]);
  mode = signal<'AVAILABLE'|'ASSIGNED'>('AVAILABLE');

  constructor() { this.load('AVAILABLE'); }

  load(m: 'AVAILABLE'|'ASSIGNED') {
    this.mode.set(m);
    this.api.deliveryJobs(m).subscribe(j => this.jobs.set(j));
  }

  accept(orderId: number) {
    this.api.deliveryAccept(orderId).subscribe(() => this.load('ASSIGNED'));
  }
}
```

### `frontend/src/app/features/delivery/delivery-order.component.ts`

```ts
import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ApiService } from '../../core/api.service';

@Component({
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
  <a routerLink="/delivery/jobs" class="text-sm underline">← Back</a>

  <div *ngIf="order()" class="mt-3 bg-white border rounded-xl p-4">
    <div class="flex justify-between">
      <div class="text-xl font-bold">Order #{{order()!.id}}</div>
      <div class="text-sm bg-slate-100 rounded px-2 py-1">{{order()!.status}}</div>
    </div>

    <div class="mt-3 flex gap-2">
      <button class="border rounded px-3 py-2" (click)="set('PICKED_UP')">PICKED_UP</button>
      <button class="border rounded px-3 py-2" (click)="set('OUT_FOR_DELIVERY')">OUT_FOR_DELIVERY</button>
      <button class="bg-black text-white rounded px-3 py-2" (click)="set('DELIVERED')">DELIVERED</button>
    </div>

    <div *ngIf="msg()" class="mt-3 text-sm">{{msg()}}</div>
  </div>
  `,
})
export class DeliveryOrderComponent {
  private api = inject(ApiService);
  private route = inject(ActivatedRoute);

  order = signal<any|null>(null);
  msg = signal('');

  private id = Number(this.route.snapshot.paramMap.get('id'));

  constructor() {
    this.reload();
  }

  reload() {
    this.api.order(this.id).subscribe(o => this.order.set(o));
  }

  set(next: string) {
    this.msg.set('');
    this.api.deliverySetStatus(this.id, next).subscribe({
      next: () => { this.msg.set('Updated to ' + next); this.reload(); },
      error: (e) => this.msg.set(e?.error?.message ?? 'Failed'),
    });
  }
}
```

---

## 5.7 Customer Additions

### Modify `frontend/src/app/features/orders/order-detail.component.ts` (MOD: show timeline)

Add below your existing order details:

```ts
// add these signals + load
events = signal<any[]>([]);

reload() {
  this.api.order(this.id).subscribe(o => this.order.set(o));
  this.api.orderEvents(this.id).subscribe(evs => this.events.set(evs));
}
```

And in template add:

```html
<h3 class="mt-4 font-semibold">Timeline</h3>
<div *ngFor="let ev of events()" class="py-2 border-b last:border-b-0 text-sm">
  <div class="font-medium">{{ev.status}}</div>
  <div class="text-slate-600">{{ev.message}}</div>
  <div class="text-xs text-slate-500">{{ev.createdAt}}</div>
</div>
```

### Modify `frontend/src/app/features/restaurants/restaurant-detail.component.ts` (MOD: reviews)

Add a “Reviews” section (minimal):

```ts
reviews = signal<any[]>([]);
reviewOrderId = 0;
reviewRating = 5;
reviewComment = '';

loadReviews() {
  this.api.restaurantReviews(this.restaurant()!.id, 0).subscribe(p => this.reviews.set(p.content ?? []));
}
postReview() {
  this.api.postReview(this.restaurant()!.id, this.reviewOrderId, this.reviewRating, this.reviewComment)
    .subscribe(() => this.loadReviews());
}
```

Call `this.loadReviews()` after restaurant load.
Template add:

```html
<h2 class="mt-8 mb-2 font-semibold">Reviews</h2>

<div class="bg-white border rounded-xl p-4 mb-3">
  <div class="text-sm text-slate-600 mb-2">You can review only a DELIVERED order (use orderId).</div>
  <div class="flex gap-2 flex-wrap items-center">
    <input class="border rounded px-2 py-1 w-32" type="number" placeholder="orderId" [(ngModel)]="reviewOrderId" />
    <input class="border rounded px-2 py-1 w-20" type="number" min="1" max="5" [(ngModel)]="reviewRating" />
    <input class="border rounded px-2 py-1 flex-1 min-w-[200px]" placeholder="comment" [(ngModel)]="reviewComment" />
    <button class="bg-black text-white rounded px-3 py-2" (click)="postReview()">Post</button>
  </div>
</div>

<div class="grid gap-2">
  <div *ngFor="let r of reviews()" class="bg-white border rounded-xl p-4">
    <div class="font-medium">⭐ {{r.rating}} / 5</div>
    <div class="text-sm text-slate-600">{{r.comment}}</div>
    <div class="text-xs text-slate-500 mt-1">{{r.createdAt}}</div>
  </div>
</div>
```

---

# 6) Run Instructions (v2)

## Backend

```bash
cd backend
docker-compose up -d
mvn spring-boot:run
```

Swagger: `http://localhost:8080/swagger-ui.html`

## Frontend

```bash
cd frontend
npm i
npx ng serve
```

UI: `http://localhost:4200`

---

# 7) Curl Examples (v2 flows)

## Owner: create restaurant

```bash
curl -X POST "http://localhost:8080/api/owner/restaurants" \
  -H "Content-Type: application/json" \
  -H "X-User-Id: 2" \
  -H "X-User-Role: OWNER" \
  -d '{"name":"Owner Special","city":"Kolkata","cuisineType":"Biryani","ratingAvg":4.6,"deliveryTimeMin":30,"imageUrl":""}'
```

## Owner: set order to PREPARING then READY_FOR_PICKUP

```bash
curl -X POST "http://localhost:8080/api/owner/orders/1/status?next=PREPARING" \
  -H "X-User-Id: 2" -H "X-User-Role: OWNER"

curl -X POST "http://localhost:8080/api/owner/orders/1/status?next=READY_FOR_PICKUP" \
  -H "X-User-Id: 2" -H "X-User-Role: OWNER"
```

## Delivery: list available jobs + accept

```bash
curl "http://localhost:8080/api/delivery/jobs?status=AVAILABLE" \
  -H "X-User-Id: 3" -H "X-User-Role: DELIVERY_PARTNER"

curl -X POST "http://localhost:8080/api/delivery/jobs/1/accept" \
  -H "X-User-Id: 3" -H "X-User-Role: DELIVERY_PARTNER"
```

## Delivery: update statuses

```bash
curl -X POST "http://localhost:8080/api/delivery/orders/1/status?next=PICKED_UP" \
  -H "X-User-Id: 3" -H "X-User-Role: DELIVERY_PARTNER"

curl -X POST "http://localhost:8080/api/delivery/orders/1/status?next=OUT_FOR_DELIVERY" \
  -H "X-User-Id: 3" -H "X-User-Role: DELIVERY_PARTNER"

curl -X POST "http://localhost:8080/api/delivery/orders/1/status?next=DELIVERED" \
  -H "X-User-Id: 3" -H "X-User-Role: DELIVERY_PARTNER"
```

## Customer: view timeline events

```bash
curl "http://localhost:8080/api/orders/1/events" -H "X-User-Id: 1"
```

## Customer: post review (only after DELIVERED)

```bash
curl -X POST "http://localhost:8080/api/restaurants/1/reviews" \
  -H "Content-Type: application/json" \
  -H "X-User-Id: 1" \
  -d '{"orderId":1,"rating":5,"comment":"Fast delivery and great taste"}'
```

---

# 8) [GIT TAG COMMANDS]

```bash
git status
git add .
git commit -m "v2: Postgres+Flyway + owner portal + delivery partner + reviews + order events"
git tag v2-zomatox-owner-delivery
git tag -n
```

---

If you want, next message আমি **v2 UI আরও polished** (tabs, status chips, empty states, better forms) করে দেব—কিন্তু backend contracts এগুলো একই থাকবে, v1 endpoints break হবে না।

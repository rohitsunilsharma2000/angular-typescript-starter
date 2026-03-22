 
---

# ZomatoX v2 Upgrade (from v1) — PostgreSQL + Flyway + Owner Portal + Delivery Partner + Ratings/Reviews

## Role

You are a senior full-stack architect and lead engineer. You are upgrading an existing v1 codebase of **ZomatoX** (restaurant discovery + menu + cart + order + mock payment) to v2.

You **must keep v1 endpoints working** (backward compatible) while adding v2 features.

Output must be **copy-paste ready and compile/run**.

---

## Input Context

Assume v1 already exists with:

* Spring Boot 3 + Java 17 + H2
* Entities:

  * `User`
  * `Restaurant`
  * `MenuItem`
  * `Cart` / `CartItem`
  * `Address`
  * `Order` / `OrderItem`
  * `Payment`
* APIs:

  * `/restaurants`
  * `/cart`
  * `/orders`
  * `/payments confirm`
  * plus admin endpoints
* Angular app:

  * restaurant list/detail
  * cart
  * checkout
  * orders
* User context by header `X-User-Id`

No JWT yet in v2 (keep it simple). Security will be v3-pro.

---

## Primary Goal

Upgrade v1 to v2 with:

1. Database migration: switch **H2 → PostgreSQL + Flyway** *(mandatory)*
2. Add Roles and Portals: `RESTAURANT_OWNER` and `DELIVERY_PARTNER`
3. Owner can manage menu and process orders
   (`CONFIRMED → PREPARING → READY_FOR_PICKUP`)
4. Delivery partner can accept and deliver
   (`PICKED_UP → OUT_FOR_DELIVERY → DELIVERED`)
5. Ratings & reviews: customer can rate/review **after delivery only**
6. Order event timeline (store every status change), viewable by customer

---

## Upgrade Order — Must Follow

### A) Postgres + Flyway

First, before everything

### B) Role model + simple role checks

No JWT

### C) Owner portal features

### D) Delivery partner features

### E) Reviews + constraints

### F) Frontend UI upgrades

---

## Stack — v2

### Backend

* Java 17
* Spring Boot 3
* Spring Web
* Validation
* Spring Data JPA
* PostgreSQL
* Flyway only
* OpenAPI/Swagger
* Actuator
* JUnit 5 tests

### Frontend

* Angular 17
* Tailwind
* Role-based routing using simple local role in v2 (no JWT yet)
* State: RxJS store
* Keep customer UI same, add Owner + Delivery sections

---

## Data Model Changes — v2

Modify/add entities/tables while preserving v1:

* `users`

  * add `role` column
  * values: `CUSTOMER`, `OWNER`, `DELIVERY_PARTNER`, `ADMIN` *(optional)*

* `restaurants`

  * add `owner_user_id` (FK → users)

* `menu_items`

  * keep existing
  * ensure `restaurant_id` FK

* `orders`

  * add expanded status:

    * `CREATED`
    * `PAYMENT_PENDING`
    * `PAID`
    * `CONFIRMED`
    * `PREPARING`
    * `READY_FOR_PICKUP`
    * `PICKED_UP`
    * `OUT_FOR_DELIVERY`
    * `DELIVERED`
  * add `delivery_partner_user_id` (nullable)
  * add `updated_at`

* `order_events` *(new)*

  * `id`
  * `order_id`
  * `status`
  * `message`
  * `created_at`

* `reviews` *(new)*

  * `id`
  * `order_id`
  * `restaurant_id`
  * `user_id`
  * `rating` (1–5)
  * `comment`
  * `created_at`

### Constraints

* Review allowed **only if order status is `DELIVERED`** and reviewer is the same user
* Only **one review per order**

---

## API Contracts — v2 Adds

Keep existing v1 endpoints unchanged where possible.

### Owner APIs

Role: `OWNER`
Check via `X-User-Role` header or resolve from user table by `X-User-Id`

* `GET /api/owner/restaurants`
  Restaurants owned by current owner

* `POST /api/owner/restaurants`
  Create restaurant owned by current owner

* `PUT /api/owner/restaurants/{id}`
  Update restaurant

* `POST /api/owner/restaurants/{id}/menu-items`
  Add menu item

* `PUT /api/owner/menu-items/{id}`
  Update price / stock / available

* `GET /api/owner/orders?status=CONFIRMED|PREPARING|READY_FOR_PICKUP`

* `POST /api/owner/orders/{orderId}/status`
  Allowed: `PREPARING` or `READY_FOR_PICKUP`

---

### Delivery Partner APIs

Role: `DELIVERY_PARTNER`

* `GET /api/delivery/jobs?status=AVAILABLE|ASSIGNED`

  * `AVAILABLE` means orders `READY_FOR_PICKUP` with no delivery partner assigned

* `POST /api/delivery/jobs/{orderId}/accept`

* `POST /api/delivery/orders/{orderId}/status`

  * Allowed:

    * `PICKED_UP`
    * `OUT_FOR_DELIVERY`
    * `DELIVERED`

---

### Customer Additions

* `POST /api/restaurants/{id}/reviews`

  * body: `orderId`, `rating`, `comment`

* `GET /api/restaurants/{id}/reviews?page=`

* `GET /api/orders/{id}/events`

  * order status timeline

---

### Admin

Admin endpoints from v1 can remain, but owner endpoints are preferred for v2.

---

## Backward Compatibility Rules

* `/api/restaurants`, `/api/cart`, `/api/orders`, `/api/payments confirm` must still work for `CUSTOMER`
* Header `X-User-Id` remains for v2
* Add optional header `X-User-Role` for dev testing
* If not present, derive role from user table

---

## Backend Architecture

### Base Package

```text
com.example.zomatox
```

### Structure

```text
controller
dto
entity
repository
service
config
exception
util
```

### Rules

* Add `OrderStateMachine` validator in service to prevent illegal transitions
* Every status change must create an `order_events` record
* Global exception handler response format:

```json
{
  "message": "...",
  "validationErrors": {
    "field": "error"
  }
}
```

* Logging: `@Slf4j` in services

---

## Flyway Requirements

Provide:

* `docker-compose.yml` for PostgreSQL
* Flyway migrations:

  * `V1__init.sql` *(from v1 schema)*
  * `V2__add_roles_owner_delivery_reviews_events.sql` *(v2 additions)*

Provide seed data SQL or Java initializer:

* users:

  * customer
  * owner
  * delivery partner
* restaurants mapped to owner
* menu items
* 2–3 orders in different statuses for demo

---

## Frontend Requirements — v2

Keep customer pages from v1.

Add:

### Owner Portal Routes

* `/owner/restaurants`
* `/owner/menu/:restaurantId`
* `/owner/orders`

### Delivery Portal Routes

* `/delivery/jobs`
* `/delivery/order/:id`

### Customer Additions

* restaurant reviews section
* order timeline events view

### Frontend Integration

* Continue using `X-User-Id` header
* Add a role switch dropdown in header to switch between seeded users:

  * customer
  * owner
  * delivery

---

## Tests — v2 Minimum

### `OrderTransitionTest`

* `CONFIRMED → PREPARING → READY_FOR_PICKUP` allowed
* invalid transitions rejected

### `DeliveryAcceptTest`

* cannot accept unless `READY_FOR_PICKUP` and unassigned

### `ReviewConstraintsTest`

* cannot review unless `DELIVERED`
* only one review per order

### `FlywayMigrationTest` *(optional)*

* app starts and migrations apply

---

## Run Instructions — Must Output

### Backend

```bash
docker-compose up -d
mvn spring-boot:run
```

### Frontend

```bash
npm i
ng serve
```

---

## Curl Examples — Must Provide

* create owner restaurant
* owner updates order status to `PREPARING` / `READY_FOR_PICKUP`
* delivery accepts job
* delivery marks `DELIVERED`
* customer posts review

---

## Output Format — Must Follow

1. Show what files to **ADD/MODIFY** from v1 *(upgrade diff checklist)*
2. Provide updated repository tree
3. Provide full code for new/changed files *(copy-paste ready)*
4. Provide Flyway migrations and `docker-compose`
5. Provide updated seed data
6. Provide updated Angular pages/services
7. Provide tests + run steps + curl
8. At the end output:

---

## Git Tag Commands

```bash
git status
git add .
git commit -m "v2: Postgres+Flyway + owner portal + delivery partner + reviews + order events"
git tag v2-zomatox-owner-delivery
git tag -n
```

---

## Quality Bar

* Must compile and run on PostgreSQL with Flyway
* No placeholders that break compilation
* v1 APIs must continue working
* Clean DTO validation + global error handler
* No copyrighted assets; generic icons only

---

I can also give you this in a **`.md` file format block** so you can save it directly as `zomatox-v2-upgrade.md`.

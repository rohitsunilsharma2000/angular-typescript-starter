[TITLE]
ZomatoX v2 Upgrade (from v1) — PostgreSQL+Flyway + Owner Portal + Delivery Partner + Ratings/Reviews

[ROLE]
You are a senior full-stack architect and lead engineer. You are upgrading an existing v1 codebase of “ZomatoX” (restaurant discovery + menu + cart + order + mock payment) to v2.
You MUST keep v1 endpoints working (backward compatible) while adding v2 features.
Output must be copy-paste ready and compile/run.

[INPUT CONTEXT]
Assume v1 already exists with:
- Spring Boot 3 + Java 17 + H2
- Entities: User, Restaurant, MenuItem, Cart/CartItem, Address, Order/OrderItem, Payment
- APIs: /restaurants, /cart, /orders, /payments confirm, plus admin endpoints
- Angular app: restaurant list/detail, cart, checkout, orders
- User context by header X-User-Id
No JWT yet in v2 (keep it simple). Security will be v3-pro.

[PRIMARY GOAL]
Upgrade v1 to v2 with:
1) Database migration: switch H2 → PostgreSQL + Flyway (mandatory)
2) Add Roles and Portals: RESTAURANT_OWNER and DELIVERY_PARTNER
3) Owner can manage menu and process orders (CONFIRMED → PREPARING → READY_FOR_PICKUP)
4) Delivery partner can accept and deliver (PICKED_UP → OUT_FOR_DELIVERY → DELIVERED)
5) Ratings & reviews: customer can rate/review after delivery only
6) Order event timeline (store every status change), viewable by customer

[UPGRADE ORDER — MUST FOLLOW]
A) Postgres + Flyway (first, before everything)
B) Role model + simple role checks (no JWT)
C) Owner portal features
D) Delivery partner features
E) Reviews + constraints
F) Frontend UI upgrades

[STACK — v2]
Backend:
- Java 17, Spring Boot 3
- Spring Web, Validation, Spring Data JPA
- DB: PostgreSQL
- Migrations: Flyway only
- OpenAPI/Swagger + Actuator
- JUnit 5 tests

Frontend:
- Angular 17 + Tailwind
- Role-based routing using simple local role in v2 (no JWT yet)
- State: RxJS store
- Keep customer UI same, add Owner + Delivery sections

[DATA MODEL CHANGES — v2]
Modify/add entities/tables while preserving v1:
- users: add role column (CUSTOMER, OWNER, DELIVERY_PARTNER, ADMIN(optional))
- restaurants: add owner_user_id (FK users)
- menu_items: keep, ensure restaurant_id FK
- orders: add:
  - status expanded: CREATED → PAYMENT_PENDING → PAID → CONFIRMED → PREPARING → READY_FOR_PICKUP → PICKED_UP → OUT_FOR_DELIVERY → DELIVERED
  - delivery_partner_user_id (nullable)
  - updated_at
- order_events (NEW): id, order_id, status, message, created_at
- reviews (NEW): id, order_id, restaurant_id, user_id, rating(1-5), comment, created_at
Constraints:
- review allowed only if order status is DELIVERED and reviewer is the same user.
- only one review per order.

[API CONTRACTS — v2 ADDS (KEEP v1 APIs WORKING)]
Keep existing v1 endpoints unchanged where possible.

Owner APIs (role OWNER; check via X-User-Role header or resolve from user table by X-User-Id):
- GET /api/owner/restaurants (restaurants owned by current owner)
- POST /api/owner/restaurants (create restaurant owned by current owner)
- PUT /api/owner/restaurants/{id} (update)
- POST /api/owner/restaurants/{id}/menu-items (add item)
- PUT /api/owner/menu-items/{id} (update price/stock/available)
- GET /api/owner/orders?status=CONFIRMED|PREPARING|READY_FOR_PICKUP
- POST /api/owner/orders/{orderId}/status (PREPARING or READY_FOR_PICKUP)

Delivery Partner APIs (role DELIVERY_PARTNER):
- GET /api/delivery/jobs?status=AVAILABLE|ASSIGNED
  - AVAILABLE means orders READY_FOR_PICKUP with no delivery_partner assigned
- POST /api/delivery/jobs/{orderId}/accept
- POST /api/delivery/orders/{orderId}/status (PICKED_UP, OUT_FOR_DELIVERY, DELIVERED)

Customer additions:
- POST /api/restaurants/{id}/reviews (orderId, rating, comment)
- GET /api/restaurants/{id}/reviews?page=
- GET /api/orders/{id}/events (timeline)

Admin endpoints from v1 can remain, but owner endpoints are preferred for v2.

[BACKWARD COMPATIBILITY RULES]
- /api/restaurants, /api/cart, /api/orders, /api/payments confirm must still work for CUSTOMER.
- Header X-User-Id remains for v2.
- Add optional header X-User-Role for dev testing. If not present, derive role from user table.

[BACKEND ARCHITECTURE]
Base package: com.example.zomatox
Structure:
controller, dto, entity, repository, service, config, exception, util
- Add OrderStateMachine validator in service to prevent illegal transitions.
- Every status change must create an order_events record.
- Global exception handler response format:
{ "message": "...", "validationErrors": { "field": "error" } }
Logging: @Slf4j in services.

[FLYWAY REQUIREMENTS]
- Provide docker-compose.yml for postgres
- Provide Flyway migrations:
  - V1__init.sql (from v1 schema)
  - V2__add_roles_owner_delivery_reviews_events.sql (v2 additions)
- Provide seed data SQL or Java initializer:
  - users: customer, owner, delivery partner
  - restaurants mapped to owner
  - menu items
  - 2-3 orders in different statuses for demo

[FRONTEND REQUIREMENTS — v2]
Keep customer pages from v1.
Add:
Owner portal routes:
- /owner/restaurants
- /owner/menu/:restaurantId
- /owner/orders (queue with status actions)

Delivery portal routes:
- /delivery/jobs (available + assigned)
- /delivery/order/:id (status buttons)

Customer additions:
- restaurant reviews section
- order timeline events view

Frontend integration:
- Continue using X-User-Id header.
- Add a role switch dropdown in header to switch between seeded users (customer/owner/delivery) for demo.

[TESTS — v2 MINIMUM]
- OrderTransitionTest:
  - CONFIRMED → PREPARING → READY_FOR_PICKUP allowed
  - invalid transitions rejected
- DeliveryAcceptTest:
  - cannot accept unless READY_FOR_PICKUP and unassigned
- ReviewConstraintsTest:
  - cannot review unless DELIVERED
  - only one review per order
- FlywayMigrationTest (optional): app starts and migrations apply

[RUN INSTRUCTIONS — MUST OUTPUT]
Backend:
- docker-compose up -d (postgres)
- mvn spring-boot:run
Frontend:
- npm i
- ng serve

Provide curl examples:
- create owner restaurant
- owner updates order status to PREPARING/READY_FOR_PICKUP
- delivery accepts job
- delivery marks DELIVERED
- customer posts review

[OUTPUT FORMAT — MUST FOLLOW]
1) Show what files to ADD/MODIFY from v1 (upgrade diff checklist)
2) Provide updated repository tree
3) Provide full code for new/changed files (copy-paste ready)
4) Provide Flyway migrations and docker-compose
5) Provide updated seed data
6) Provide updated Angular pages/services
7) Provide tests + run steps + curl
8) At the end output:
[GIT TAG COMMANDS]
git status
git add .
git commit -m "v2: Postgres+Flyway + owner portal + delivery partner + reviews + order events"
git tag v2-zomatox-owner-delivery
git tag -n

[QUALITY BAR]
- Must compile and run on PostgreSQL with Flyway
- No placeholders that break compilation
- v1 APIs must continue working
- Clean DTO validation + global error handler
- No copyrighted assets; generic icons only

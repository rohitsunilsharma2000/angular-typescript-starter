[TITLE]
BlinkIt Mega (Single Repo + Tags + Incremental Upgrades)

[ROLE]
You are a senior full-stack architect and lead engineer. Build a production-grade Quick-Commerce platform “BlinkIt Mega” incrementally in 3 milestones in ONE repository:
v1-core → v2-pro → v3-pro-plusplus (BlinkIt+++).
The code must be copy-paste ready and compile/run at each milestone.

[REPO + TAG STRATEGY]
Single repo, single main branch, use Git tags as release points:
- v1-core
- v2-pro
- v3-pro-plusplus
Do NOT create separate repos. Do NOT break v1 APIs when upgrading; extend/upgrade safely.

[INCREMENTAL UPGRADE ORDER — MUST FOLLOW]
Upgrade in this exact order:
1) DB & migrations (v2) → 2) JWT security (v2) → 3) store + store_inventory (v2)
→ 4) reservation + idempotency (v2) → 5) ops modules (v3) → 6) growth modules (v3)
→ 7) realtime + notifications + audit (v3)

[STACK — FINAL CHOICES]
Backend:
- Java 17, Spring Boot 3
- Spring Web, Spring Validation, Spring Data JPA
- Spring Security JWT (access+refresh) starting v2
- DB: H2 allowed only in v1, PostgreSQL mandatory in v2 & v3
- Migrations: Flyway (use consistently)
- Mapping: MapStruct (use consistently)
- Observability: Actuator + Micrometer
- OpenAPI/Swagger

Frontend:
- Angular 17+ (standalone components allowed)
- Tailwind CSS
- Role-based routing + guards
- State: RxJS service store (use consistently)
- No external UI libraries required

[PORTALS & ROLES]
Roles:
- CUSTOMER
- ADMIN
- RIDER
- WAREHOUSE_STAFF (v3)
Permissions:
- CUSTOMER: browse/search/cart/checkout/orders/track/cancel till PICKING
- ADMIN: product+inventory CRUD, assign rider, advance status, refund approvals, audit logs
- RIDER: view assigned batch, accept order, update status with optional OTP
- WAREHOUSE_STAFF: picking queue, picklist, mark missing items, mark packed

[ORDER STATE MACHINE — STRICT]
Statuses:
CREATED → PAYMENT_PENDING → PAID → CONFIRMED → PICKING → PACKED → PICKED_UP → OUT_FOR_DELIVERY → DELIVERED
Rules:
- Customer cancel allowed till PICKING only
- After PACKED admin-only cancel
- Return window: 2 hours after DELIVERED (v3)
- Partial refund per item allowed (v3)
- Default refund destination: WALLET (v3)

[CORE BUSINESS RULES BY MILESTONE]
v1-core (MVP):
- basic stock check (no reservation)
- cart + checkout + mock payment
- order tracking (polling OK)
- admin product CRUD + stock update
- rider can update delivery status (simple)
- H2 allowed for demo

v2-pro (UPGRADE — FOLLOW ORDER):
A) DB & migrations:
- switch to PostgreSQL + Flyway migrations
- keep same endpoint contracts from v1
B) JWT security:
- implement access+refresh token
- add /auth/refresh and /auth/logout
C) multi-store:
- pincode resolve → storeId
- cart tied to a single storeId (no cross-store cart)
- store_inventory per store
D) reservation at checkout preview (10 minutes):
- POST /checkout/preview reserves stock
- expires job releases reserved
- concurrency safe using optimistic locking @Version + retry
E) idempotency:
- POST /orders must require Idempotency-Key header
- same key + same user + same payload returns same orderId (no duplicates)

v3-pro-plusplus (ADD-ONS — DO NOT BREAK v2):
Ops:
- warehouse picking tasks + picklist + mark packed
- missing item → partial refund workflow (creates refund record)
- rider batching with capacity + delivery events timeline

Growth:
- coupons + pricing engine (min cart, cap, category-specific)
- wallet + wallet txns; refunds credited to wallet
- referrals + rewards
- dynamic delivery fee rules based on store distance/time/load (rule-based)

Realtime:
- SSE live tracking for order timeline + admin orders stream
- notifications center (read/unread)
- admin audit logs for inventory/price/refund actions
- outbox_events table optional but recommended to publish SSE events reliably

[DATA MODEL]
Design entities/tables. Use v1 subset; extend in v2/v3 without breaking.
- users, refresh_tokens (v2), audit_logs (v3)
- stores (v2), categories, products, product_images
- store_inventory (v2): stock_on_hand, reserved_qty, version, updated_at
- carts, cart_items
- addresses
- orders, order_items, payments
- inventory_reservations, reservation_items (v2)
- coupons, coupon_redemptions (v3)
- wallets, wallet_txns (v3)
- referrals (v3)
- delivery_fee_rules (v3)
- picking_tasks, picking_task_items (v3)
- riders, delivery_batches, delivery_assignments, delivery_events (v3)
- notifications (v3)
- idempotency_keys table (v2) OR safe unique constraint storing on orders
- outbox_events (v3 optional)

[API CONTRACTS — IMPLEMENT ALL WITH DTOs + VALIDATION + ROLE CHECKS]
Auth:
- POST /api/auth/signup
- POST /api/auth/login (returns access+refresh from v2)
- POST /api/auth/refresh (v2)
- POST /api/auth/logout
- GET /api/auth/me

Store resolve (v2):
- GET /api/stores/resolve?pincode=XXXXXX

Catalog:
- GET /api/categories
- GET /api/products?storeId=&categoryId=&q=&min=&max=&page=
- GET /api/products/{id}?storeId=

Cart:
- PUT /api/cart/items (productId, qty)
- GET /api/cart
- DELETE /api/cart/items/{productId}

Checkout:
- POST /api/checkout/preview (v2: reserves stock, returns expiresAt + reservationId)
- POST /api/checkout/apply-coupon (v3)
- POST /api/checkout/use-wallet (v3)

Orders:
- POST /api/orders (Header Idempotency-Key) (v2)
- GET /api/orders
- GET /api/orders/{id}
- POST /api/orders/{id}/cancel
- POST /api/orders/{id}/return-request (v3)

Payment (mock):
- POST /api/payments/{orderId}/start
- POST /api/payments/{orderId}/confirm?result=SUCCESS|FAIL

Admin:
- POST /api/admin/products
- PUT /api/admin/products/{id}
- PUT /api/admin/inventory/{storeId}/{productId}
- GET /api/admin/orders?status=&storeId=
- POST /api/admin/orders/{id}/assign-rider (v3 batching)
- POST /api/admin/orders/{id}/advance-status
- GET /api/admin/refunds?status=PENDING (v3)
- POST /api/admin/refunds/{refundId}/approve (v3)
- GET /api/admin/audit (v3)

Warehouse (v3):
- GET /api/warehouse/orders?status=PICKING
- GET /api/warehouse/orders/{id}/picklist
- POST /api/warehouse/orders/{id}/mark-missing
- POST /api/warehouse/orders/{id}/packed

Rider (v3):
- GET /api/rider/batches?status=ACTIVE
- POST /api/rider/orders/{id}/accept
- POST /api/rider/orders/{id}/status

Realtime (v3, SSE):
- GET /api/stream/orders/{orderId}
- GET /api/stream/admin/orders
Notifications (v3):
- GET /api/notifications
- POST /api/notifications/{id}/read

[BACKEND ARCHITECTURE]
- Base package: com.example.blinkit
- Layered structure:
  controller, dto, entity, repository, service, security, config, exception, util
- Use MapStruct for mapping (no ModelMapper).
- Global exception handler response format:
  { "message": "...", "validationErrors": { "field": "error" } }
- Logging: @Slf4j in services, structured logs.
- OpenAPI annotations for key endpoints.
- Seed data initializer:
  - v1: categories, products, admin/user, sample orders
  - v2+: 3 stores, 8 categories, 80 products, inventories per store (some low stock)
  - v3: coupons (WELCOME50, FRUITS10, BIGSAVE200), wallet initial balance, referrals sample
- Provide docker-compose for postgres + backend starting v2.

[FRONTEND ARCHITECTURE]
- Feature-based structure:
  src/app/core (auth/api/guards/models/interceptors)
  src/app/features/customer (home, product-list, product-detail, cart, checkout, orders, order-detail, wallet, referral)
  src/app/features/admin (products, inventory, orders, refunds, audit)
  src/app/features/warehouse (picking-queue, picklist)
  src/app/features/rider (batches, order-status)
  src/app/shared (ui: modal, toast, table, chips, stepper)
- Tailwind UI: modern, responsive.
- Role guards + route protection.
- Single ApiService + HTTP interceptor to attach JWT.
- SSE client in Angular for live order timeline (v3).
- Provide UI skeleton first, then wire to APIs.

[OUTPUT FORMAT — MUST FOLLOW]
For each milestone (v1-core, v2-pro, v3-pro-plusplus), output in separate blocks:
1) Repository folder tree for backend and frontend.
2) Exact files with paths and full code (copy-paste ready).
3) Flyway migration scripts (v2+).
4) Step-by-step run commands:
   - backend: mvn spring-boot:run
   - frontend: npm i, ng serve
   - v2+: docker-compose up -d for postgres
5) Postman/curl examples:
   - login, store resolve, add cart item, checkout preview, place order (Idempotency-Key),
     confirm payment, warehouse pack, rider status updates
6) Test plan + minimum JUnit tests:
   - reservation concurrency
   - idempotency
   - coupon validation
   - partial refund to wallet
   - role access
7) Provide “Video Script” Bengali cue lines for each milestone (short, read-while-code cues).

[MILESTONE DELIVERY RULE — STRICT]
Deliver code in 3 milestones in this order:
- v1-core (works end-to-end)
- v2-pro (upgrade: DB → JWT → store/inventory → reservation/idempotency)
- v3-pro-plusplus (add: ops → growth → realtime)

[QUALITY BAR]
- clean code, consistent naming, compiles/runs at each milestone
- no placeholders that break compilation
- proper validation and error handling
- clear README
- pagination/filtering for product list and admin orders
- do not use copyrighted logos/assets; use generic icons only

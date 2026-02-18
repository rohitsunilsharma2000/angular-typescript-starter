এখানে আমি **BlinkIt → BlinkIt++ → BlinkIt+++** — তিনটা assignment কে একসাথে **একটা Unified “Mega Assignment”** বানিয়ে দিলাম, যাতে তুমি ধাপে ধাপে level-up করতে পারো।
(মানে: **Core → Pro → Pro+++** সব একই প্রজেক্টে incrementally add হবে)

---

# ✅ Mega Assignment: Build “BlinkIt (Core → Pro → Pro+++)” Quick-Commerce Platform

## Project Vision

একটা Full-Stack quick-commerce platform বানাবে (Blinkit style) যেখানে:

* Customer 10–30 মিনিটে grocery order করবে
* Admin inventory/order ops চালাবে
* Warehouse staff packing করবে
* Rider delivery করবে
* System হবে secure, scalable, real-time, growth-ready

---

# 0) Level System (এটাই Combined Structure)

## Level 1 — **BlinkIt (Core MVP)**

Focus: catalog + cart + order + basic delivery flow

## Level 2 — **BlinkIt++ (PRO)**

Focus: JWT security, transactions, inventory reservation, idempotency, multi-store

## Level 3 — **BlinkIt+++ (PRO + Ops + Realtime + Growth)**

Focus: warehouse picking, rider batching, refunds/wallet, coupons, notifications, realtime tracking, referral, dynamic delivery fee

✅ তুমি একটাই repo বানাবে।
প্রতিটা Level complete হলে git tag দিবে:

* `v1-core`
* `v2-pro`
* `v3-pro-plusplus`

---

# 1) Tech Stack (Final Combined)

## Backend (Spring Boot)

* Java 17 + Spring Boot 3
* Spring Web + Validation + Data JPA
* Spring Security JWT (access + refresh) **(Level 2+)**
* PostgreSQL **(Level 2+)**; H2 allowed for Level 1 demo
* Flyway/Liquibase migrations **(Level 2+)**
* Actuator + Micrometer **(Level 2+)**
* OpenAPI/Swagger **(Level 2+)**

## Frontend (Angular + Tailwind)

* Angular 17+
* Tailwind only
* Role-based routing + guards
* State: RxJS store OR signals store

---

# 2) Roles (Final Combined)

## Level 1

* CUSTOMER
* ADMIN
* RIDER (basic)

## Level 3 adds

* WAREHOUSE_STAFF

---

# 3) Core Domain Modules (Combined)

## A) Store & Catalog

* Store resolve by pincode (Level 2+)
* Categories, products, images
* Filters/search/pagination

## B) Cart

* Add/update qty
* Stock checks
* Price summary

## C) Checkout & Orders

* address
* delivery slot/ETA
* payment mock
* place order → status tracking
* cancellation rules

## D) Inventory

* Level 1: basic stock check
* Level 2+: reservation + transactional decrement + optimistic lock

## E) Ops (Level 3)

* warehouse picking list
* missing item → partial refund
* rider batching + capacity

## F) Growth (Level 3)

* coupons + pricing engine
* wallet + refunds to wallet
* referral rewards
* dynamic delivery fee

## G) Realtime (Level 3)

* SSE/WebSocket order tracking
* notifications center

---

# 4) Order Lifecycle (Unified)

`CREATED → PAYMENT_PENDING → PAID → CONFIRMED → PICKING → PACKED → PICKED_UP → OUT_FOR_DELIVERY → DELIVERED`

Cancellation policy:

* Customer: till `PICKING`
* After `PACKED`: Admin only

Refund policy:

* Partial refund allowed (missing/damage) **Level 3**
* Default refund → wallet

---

# 5) Database Tables (Unified Full)

(তুমি Level 1 এ subset নেবে, Level 2/3 এ extend করবে)

## Users & Auth

* users
* refresh_tokens (Level 2+)
* roles (optional)
* audit_logs (Level 3)

## Stores & Catalog

* stores (Level 2+)
* categories
* products
* product_images

## Inventory

* store_inventory (Level 2+)
* inventory_reservations + reservation_items (Level 2+)

## Cart

* carts
* cart_items

## Orders & Payment

* addresses
* orders
* order_items
* payments (mock)

## Ops

* picking_tasks + picking_task_items (Level 3)
* riders + delivery_batches + delivery_assignments (Level 3)
* delivery_events (Level 3 timeline)

## Growth

* coupons + coupon_redemptions (Level 3)
* wallets + wallet_txns (Level 3)
* referrals (Level 3)
* delivery_fee_rules (Level 3)

## Realtime

* notifications (Level 3)
* outbox_events (optional, Level 3)

---

# 6) REST APIs (Unified Full Set)

## Auth

* `POST /api/auth/signup`
* `POST /api/auth/login`
* `POST /api/auth/refresh` (Level 2+)
* `POST /api/auth/logout`
* `GET /api/auth/me`

## Store resolve (Level 2+)

* `GET /api/stores/resolve?pincode=`

## Catalog

* `GET /api/categories`
* `GET /api/products?storeId=&categoryId=&q=&min=&max=&page=`
* `GET /api/products/{id}?storeId=`

## Cart

* `PUT /api/cart/items`
* `GET /api/cart`
* `DELETE /api/cart/items/{productId}`

## Checkout

* `POST /api/checkout/preview` (Level 2+ reserves stock)
* `POST /api/checkout/apply-coupon` (Level 3)
* `POST /api/checkout/use-wallet` (Level 3)

## Orders (Idempotent Level 2+)

* `POST /api/orders` (Header `Idempotency-Key`)
* `GET /api/orders`
* `GET /api/orders/{id}`
* `POST /api/orders/{id}/cancel`
* `POST /api/orders/{id}/return-request` (Level 3)

## Payments (Mock)

* `POST /api/payments/{orderId}/start`
* `POST /api/payments/{orderId}/confirm?result=SUCCESS|FAIL`

## Admin Ops

* `POST /api/admin/products`
* `PUT /api/admin/products/{id}`
* `PUT /api/admin/inventory/{storeId}/{productId}`
* `GET /api/admin/orders?status=&storeId=`
* `POST /api/admin/orders/{id}/assign-rider`
* `POST /api/admin/orders/{id}/advance-status`
* `GET /api/admin/refunds?status=`
* `POST /api/admin/refunds/{refundId}/approve`
* `GET /api/admin/audit`

## Warehouse (Level 3)

* `GET /api/warehouse/orders?status=PICKING`
* `GET /api/warehouse/orders/{id}/picklist`
* `POST /api/warehouse/orders/{id}/mark-missing`
* `POST /api/warehouse/orders/{id}/packed`

## Rider (Level 3 batching)

* `GET /api/rider/batches?status=ACTIVE`
* `POST /api/rider/orders/{id}/accept`
* `POST /api/rider/orders/{id}/status`

## Realtime (Level 3)

* SSE:

  * `GET /api/stream/orders/{orderId}`
  * `GET /api/stream/admin/orders`
* Notifications:

  * `GET /api/notifications`
  * `POST /api/notifications/{id}/read`

---

# 7) Frontend Pages (Unified)

## Customer

* Home, Category, Search
* Product details
* Cart
* Checkout (coupon + wallet in Level 3)
* Orders list
* Order details (live timeline in Level 3)
* Wallet + Referral (Level 3)

## Admin

* Products CRUD
* Inventory (store-wise)
* Orders queue (live in Level 3)
* Assign rider + batch overview
* Refund queue
* Audit logs

## Warehouse (Level 3)

* Picking queue
* Picklist + missing items modal
* Pack confirm

## Rider

* Assigned orders/batch list
* Status update flow

---

# 8) Milestone Plan (Combined Roadmap)

## Milestone 1 (Level 1 Core MVP)

* Catalog + cart + checkout (mock payment) + order tracking
* Basic stock check
* Admin: product CRUD + stock update
* Rider: update order status

## Milestone 2 (Level 2 PRO)

* JWT access+refresh
* PostgreSQL + migrations
* Multi-store + pincode resolve
* Inventory reservation + expiry
* Idempotency key

## Milestone 3 (Level 3 PRO+++)

* Warehouse picklist + missing items → partial refund
* Wallet + wallet refunds
* Coupons + pricing engine
* Rider batching + capacity
* SSE live tracking + notifications
* Referral rewards + dynamic delivery fee
* Audit logs

---

# 9) Seed Data (Unified)

* 3 stores
* 8 categories
* 80 products + images
* inventory per store (some low stock)
* coupons: WELCOME50, FRUITS10, BIGSAVE200
* users:

  * [admin@blinkit.local](mailto:admin@blinkit.local) / Admin
  * [warehouse@blinkit.local](mailto:warehouse@blinkit.local) / wh
  * [rider1@blinkit.local](mailto:rider1@blinkit.local) / rider
  * [user@blinkit.local](mailto:user@blinkit.local) / user
* wallet balance seeded for customer

---

# 🎥 Bengali “Read-While-Code” Lines (Reusable)

* “এখন আমার Core BlinkIt build করবো”
* “এখন Pro level এ JWT + PostgreSQL যোগ করছি”
* “এখন stock reserve করছি 10 মিনিটের জন্য”
* “এখন idempotency key add করলাম”
* “এখন warehouse picking + partial refund যোগ করলাম”
* “এখন wallet refund implement করলাম”
* “এখন coupon + referral + dynamic delivery fee যোগ করলাম”
* “এখন SSE দিয়ে live order tracking দেখাবো”
* “এখন admin audit logs এ সব changes ট্র্যাক হচ্ছে”

---

## তুমি যদি চাও, আমি এটাকে তোমার জন্য **একটা “single master prompt”** বানিয়ে দেবো যেটা তুমি Codex/ChatGPT তে দিয়ে সরাসরি:

* full folder structure
* migrations
* entity + DTO + service + controller
* Angular UI pages
* seed data
  সব generate করাতে পারবে।

বল: তুমি **SSE** নেবে (recommended) নাকি **WebSocket**?

নিচে তোমার জন্য **PRO + One-Level-Up “BlinkIt++” assignment** দিলাম—এটা শুধু e-commerce না, একদম **ops-grade quick-commerce system**: JWT + PostgreSQL + transactional inventory + idempotency + coupons + wallet + partial refund + delivery batching + real-time tracking + admin ops.

---

# ✅ Assignment: Build “BlinkIt++” (PRO + Level-Up)

## Tech Stack (Strict)

### Backend

* Java 17, Spring Boot 3
* Spring Security + JWT (Access + Refresh)
* PostgreSQL
* Flyway/Liquibase (migration mandatory)
* JPA + optimistic locking (`@Version`)
* OpenAPI (Swagger)
* Observability: Actuator + Micrometer (Prometheus-ready)

### Frontend

* Angular 17+ + Tailwind
* Role-based routes + guards
* State: signals/RxJS service store (pick one, be consistent)
* Reusable UI components: modal, toast, table, chips

---

# 1) Roles & Portals

### Roles

1. **CUSTOMER**
2. **ADMIN**
3. **RIDER**
4. **WAREHOUSE_STAFF** *(new)* — packing workflow (picking list, mark packed)

---

# 2) Core Domains (Data Model)

### Entities (Minimum)

* `users`, `roles`, `refresh_tokens`
* `categories`, `products`, `product_images`
* `inventory` (stockOnHand, reserved, updatedAt, version)
* `carts`, `cart_items`
* `addresses`
* `orders`, `order_items`
* `payments` (mock gateway + payment attempts)
* `coupons`, `coupon_redemptions`
* `wallets`, `wallet_transactions` *(new)*
* `deliveries`, `delivery_assignments`, `delivery_events` *(status timeline)*
* `stores` *(dark store)*, `store_products` *(per-store inventory)*
* `audit_logs` *(admin actions)*

✅ **Multi-store** (Level-up): user’s pincode → nearest store → inventory per store.

---

# 3) Order Lifecycle (Strict State Machine)

`CREATED → PAYMENT_PENDING → PAID → CONFIRMED → PICKING → PACKED → PICKED_UP → OUT_FOR_DELIVERY → DELIVERED`

Cancellation rules:

* Customer cancel allowed till `PICKING`
* After `PACKED`, only ADMIN can cancel (partial/ full refund policy)

Return/refund rules:

* `DELIVERED` এর পরে **Return window** 2 hours (config)
* Partial refund allowed per item (damage/missing)

---

# 4) Level-Up Functions (Add these beyond “Pro”)

## A) Inventory Reservation (Hard Requirement)

* Add-to-cart doesn’t reserve stock (soft check only)
* **Checkout preview** reserves stock for `N minutes` (e.g., 10 min)
* If payment fails/timeout → reservation release
* Use transactional logic:

  * `reserved += qty`
  * `stockOnHand` unchanged until final commit or decrement at packing (choose one)
* Concurrency safe using `@Version` + retry

## B) Idempotent Order Placement (Hard Requirement)

* Header: `Idempotency-Key`
* Same key + same payload → return same orderId (no duplicates)
* Store in `payment_attempts` or `idempotency_keys` table

## C) Coupons + Pricing Engine (Level-Up)

* Coupon types:

  * flat discount (₹50 off)
  * percent discount (10% up to cap)
  * category-specific coupon
  * min cart value constraint
* Pricing breakdown must return:

  * itemTotal, discount, deliveryFee, taxes (optional), grandTotal

## D) Wallet (Level-Up++)

* Customer wallet balance
* Refunds go to wallet by default (or original payment)
* Wallet can partially pay order

## E) Warehouse Picking App (New Portal)

* Screen: “Orders to Pack”
* Packing checklist:

  * show item list + quantities
  * mark missing item → triggers partial refund flow
* Mark “PACKED” only after checklist complete

## F) Rider Delivery batching (Level-Up++)

* Rider can carry max `k` orders (capacity)
* Admin assigns route batch (same area)
* Rider status updates produce `delivery_events` timeline

## G) Real-time Tracking (Optional but recommended)

* WebSocket/SSE endpoint:

  * customer order status updates pushed
  * rider status updates broadcast

## H) Admin Ops (Level-Up)

* Low stock alerts
* Price history log
* Cancel/refund queue
* Audit logs for:

  * inventory change
  * price change
  * admin cancellations/refunds

---

# 5) API Contract (Minimum + Level-Up)

## Auth (JWT + Refresh)

* `POST /api/auth/signup`
* `POST /api/auth/login` → access + refresh
* `POST /api/auth/refresh`
* `POST /api/auth/logout`
* `GET /api/auth/me`

## Store selection

* `GET /api/stores/resolve?pincode=700xxx` → storeId + etaRange

## Catalog

* `GET /api/categories`
* `GET /api/products?storeId=&categoryId=&q=&min=&max=&page=`
* `GET /api/products/{id}?storeId=`

## Cart

* `PUT /api/cart/items` (add/update qty)
* `GET /api/cart`
* `DELETE /api/cart/items/{productId}`

## Checkout

* `POST /api/checkout/preview`
  returns pricing + reserves stock (expiresAt)
* `POST /api/checkout/apply-coupon`
* `POST /api/orders` (requires `Idempotency-Key`)

## Orders

* `GET /api/orders`
* `GET /api/orders/{id}`
* `POST /api/orders/{id}/cancel`
* `POST /api/orders/{id}/return-request` *(Level-up)*

## Payments (Mock)

* `POST /api/payments/{orderId}/start`
* `POST /api/payments/{orderId}/confirm` *(simulate success/failure)*

## Admin

* `POST /api/admin/products`
* `PUT /api/admin/products/{id}`
* `PUT /api/admin/inventory/{storeId}/{productId}`
* `GET /api/admin/orders?status=&storeId=`
* `POST /api/admin/orders/{id}/assign-rider`
* `POST /api/admin/orders/{id}/advance-status`
* `POST /api/admin/refunds/{orderId}/approve`
* `GET /api/admin/audit`

## Warehouse

* `GET /api/warehouse/orders?status=PICKING`
* `POST /api/warehouse/orders/{id}/packed`
* `POST /api/warehouse/orders/{id}/item-missing` (partial refund trigger)

## Rider

* `GET /api/rider/assignments`
* `POST /api/rider/orders/{id}/accept`
* `POST /api/rider/orders/{id}/status` (picked_up/out_for_delivery/delivered)

## Realtime (Optional)

* `GET /api/stream/orders/{id}` (SSE)
* `/ws` (WebSocket)

---

# 6) Frontend Pages (Angular)

## Customer

* Home (categories + top picks)
* Product list + search + filters
* Product details
* Cart
* Checkout (address + slot + coupon + wallet)
* Orders list
* Order details (timeline + realtime updates)

## Admin

* Products CRUD
* Store inventory + low stock
* Orders ops + assign rider + refund queue
* Audit logs page

## Warehouse

* Picking queue
* Order pack screen

## Rider

* Assigned batches
* Update status + OTP delivery confirmation *(extra)*

---

# 7) Seed Data (Mandatory)

* 3 stores (pincode mapping)
* 6 categories, 60 products
* inventory per store
* coupons: WELCOME50, FRUITS10, BIGBASKET200 (sample)
* users:

  * [admin@blinkit.local](mailto:admin@blinkit.local) / Admin
  * [warehouse@blinkit.local](mailto:warehouse@blinkit.local) / wh
  * [rider1@blinkit.local](mailto:rider1@blinkit.local) / rider
  * [user@blinkit.local](mailto:user@blinkit.local) / user

---

# 8) Hard Testing Requirements

## Backend

* Inventory reservation concurrency test (2 users same product)
* Idempotency test (double confirm)
* Coupon validation tests
* Refund-to-wallet tests
* Role access tests (security)

## Frontend

* 1 e2e flow:
  customer → add cart → preview → apply coupon → pay → order timeline update

---

# 9) Video Script Style (Bengali “read-while-code” cues)

তুমি ভিডিওতে এই লাইনগুলো পড়বে:

* “এখন আমার backend ta baniye ni—JWT auth setup করছি”
* “এখন idempotency key add করলাম যাতে double order না হয়”
* “এখন inventory reserve করছি 10 মিনিটের জন্য”
* “এখন warehouse app থেকে missing item mark করলে partial refund হবে”
* “এখন refund wallet এ যাবে”
* “এখন rider batch assignment implement করলাম”
* “এখন SSE/WebSocket দিয়ে customer timeline live আপডেট হবে”

---

## তুমি কোন “One-Level-Up” বেছে নেবে?

Pro++ এ আমি 3টা path দিচ্ছি—তুমি যেটা নেবে সেটাই আমি পরে **complete copy-paste code** দিয়ে সাজিয়ে দেবো:

1. **Ops-heavy**: warehouse picking + partial refund + audit + batching
2. **Realtime-heavy**: WebSocket/SSE live tracking + notification center
3. **Growth-heavy**: coupons + wallet + referral + dynamic delivery fee

তুমি শুধু লিখে দাও: **1 / 2 / 3** (একটা সিলেক্ট করলেই চলবে), আমি সেটার জন্য পরের রিপ্লাইতে পুরো project structure + code plan + endpoints mapping একদম প্রস্তুত করে দেবো।

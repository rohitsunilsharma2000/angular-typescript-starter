চল — **BlinkIt+++ (Ops + Realtime + Growth সব 3 একসাথে)**। নিচে তোমার জন্য একদম **end-to-end “level up” assignment spec + build roadmap + DB schema + APIs + frontend pages + video-script cues** দিলাম যাতে তুমি এটাকে full project হিসেবে বানাতে পারো।

> ✅ Stack (PRO): Spring Boot 3 + Java 17 + Spring Security JWT (access+refresh) + PostgreSQL + Flyway/Liquibase + Actuator/Micrometer
> ✅ Frontend: Angular 17 + Tailwind + Guards + store pattern (signals/RxJS)

---

# 1) Final Product: BlinkIt+++ (All-in-one)

### Portals

1. **Customer App**
2. **Admin Ops**
3. **Warehouse Staff**
4. **Rider App**
5. **Realtime + Notification Center** (Customer + Admin)

### “All 3” add-ons included

* **Ops-heavy:** warehouse picking, partial refund, audit logs, rider batching
* **Realtime-heavy:** SSE/WebSocket live order tracking + notifications
* **Growth-heavy:** coupons + wallet + referral + dynamic delivery fee

---

# 2) Core Order Journey (Strict State Machine)

`CREATED → PAYMENT_PENDING → PAID → CONFIRMED → PICKING → PACKED → PICKED_UP → OUT_FOR_DELIVERY → DELIVERED`

### Cancel rules

* Customer cancel: **till PICKING**
* After PACKED: **Admin only** (refund policy)

### Return/Refund rules

* Delivered এর পরে return window: **2 hours**
* **Partial refund per item** (missing/damaged)
* Default refund destination: **Wallet** (optional: original method)

---

# 3) Critical Business Rules (Must-have)

## Inventory Reservation (Hard)

* Add-to-cart: soft check (no reserve)
* Checkout preview: **reserve stock for 10 mins** (per store)
* Payment success: convert reserve → finalize (decrement on-hand or commit reserve → sold)
* Payment fail/expire: release reservation

**Concurrency safety:** `@Version` optimistic locking + retry loop

## Idempotency (Hard)

* Header: `Idempotency-Key`
* Same key + same user + same cart snapshot → same orderId (no duplicates)

## Multi-store

* User pincode → nearest store resolved
* Inventory per store

---

# 4) Database Design (Tables)

## Auth & users

* `users(id, name, email, phone, password_hash, role, created_at, active)`
* `refresh_tokens(id, user_id, token_hash, expires_at, revoked_at)`
* `audit_logs(id, actor_user_id, action, entity_type, entity_id, payload_json, created_at)`

## Catalog

* `stores(id, name, pincode_prefix, lat, lng, active)`
* `categories(id, name, sort_order, active)`
* `products(id, category_id, brand, name, unit, mrp, price, active, created_at)`
* `product_images(id, product_id, url, sort_order)`

## Inventory per store

* `store_inventory(id, store_id, product_id, stock_on_hand, reserved_qty, version, updated_at)`
* `inventory_reservations(id, user_id, store_id, expires_at, status)`  (ACTIVE/EXPIRED/CONSUMED)
* `reservation_items(id, reservation_id, product_id, qty, price_snapshot)`

## Cart

* `carts(id, user_id, store_id, updated_at)`
* `cart_items(id, cart_id, product_id, qty, added_at)`

## Pricing/Growth

* `coupons(id, code, type, value, max_discount, min_cart, category_id_nullable, active, starts_at, ends_at)`
* `coupon_redemptions(id, coupon_id, user_id, order_id, redeemed_at)`
* `wallets(user_id, balance, updated_at)`
* `wallet_txns(id, user_id, type, amount, ref_type, ref_id, created_at)` (CREDIT/DEBIT)
* `referrals(id, referrer_user_id, referee_user_id, status, created_at)` (INVITED/COMPLETED)
* `delivery_fee_rules(id, min_distance_km, max_distance_km, fee, min_order_free_delivery)`

## Orders & payment

* `orders(id, user_id, store_id, address_id, status, item_total, discount_total, delivery_fee, payable_total, payment_status, idempotency_key, created_at)`
* `order_items(id, order_id, product_id, name_snapshot, unit_snapshot, price_snapshot, qty, line_total)`
* `payments(id, order_id, method, status, gateway_ref, created_at)` (MOCK)
* `refunds(id, order_id, amount, reason, destination, status, created_at)` (WALLET/ORIGINAL)

## Warehouse + Rider

* `picking_tasks(id, order_id, status, created_at)` (PICKING/READY/PACKED)
* `picking_task_items(id, picking_task_id, product_id, qty_expected, qty_picked, missing_reason_nullable)`
* `riders(id, user_id, capacity, active)`
* `delivery_batches(id, store_id, rider_id, status, created_at)` (CREATED/ACTIVE/DONE)
* `delivery_assignments(id, batch_id, order_id, status, sequence_no)`
* `delivery_events(id, order_id, status, message, created_at)` (timeline)

## Realtime notifications

* `notifications(id, user_id, type, title, body, read_at, created_at)`
* `outbox_events(id, topic, payload_json, status, created_at)` (for reliable event publishing)

---

# 5) Backend APIs (Complete Set)

## Auth (JWT + Refresh)

* `POST /api/auth/signup`
* `POST /api/auth/login` → access+refresh
* `POST /api/auth/refresh`
* `POST /api/auth/logout`
* `GET /api/auth/me`

## Store resolve

* `GET /api/stores/resolve?pincode=700xxx` → storeId + etaRange + distance

## Catalog

* `GET /api/categories`
* `GET /api/products?storeId=&categoryId=&q=&min=&max=&page=`
* `GET /api/products/{id}?storeId=`

## Cart

* `PUT /api/cart/items` (productId, qty)
* `GET /api/cart`
* `DELETE /api/cart/items/{productId}`

## Checkout + Pricing + Reservation

* `POST /api/checkout/preview`
  → pricing breakdown + reservationId + expiresAt
* `POST /api/checkout/apply-coupon` (reservationId, couponCode)
* `POST /api/checkout/use-wallet` (reservationId, walletAmount)

## Orders (Idempotent)

* `POST /api/orders` (reservationId, addressId, paymentMethod)
  Header: `Idempotency-Key`
* `GET /api/orders`
* `GET /api/orders/{id}`
* `POST /api/orders/{id}/cancel` (customer rules apply)

## Payment (Mock gateway)

* `POST /api/payments/{orderId}/start`
* `POST /api/payments/{orderId}/confirm?result=SUCCESS|FAIL`

## Returns + Refunds

* `POST /api/orders/{id}/return-request` (items + reason)
* `GET /api/wallet`
* `GET /api/wallet/txns`

## Admin Ops

* `POST /api/admin/products`
* `PUT /api/admin/products/{id}`
* `PUT /api/admin/inventory/{storeId}/{productId}` (stock adjust)
* `GET /api/admin/orders?status=&storeId=`
* `POST /api/admin/orders/{id}/assign-rider`
* `POST /api/admin/orders/{id}/advance-status`
* `GET /api/admin/refunds?status=PENDING`
* `POST /api/admin/refunds/{refundId}/approve`
* `GET /api/admin/audit`

## Warehouse

* `GET /api/warehouse/orders?status=PICKING`
* `GET /api/warehouse/orders/{id}/picklist`
* `POST /api/warehouse/orders/{id}/mark-missing` (itemId, missingQty, reason)
* `POST /api/warehouse/orders/{id}/packed`

## Rider

* `GET /api/rider/batches?status=ACTIVE`
* `POST /api/rider/orders/{id}/accept`
* `POST /api/rider/orders/{id}/status` (PICKED_UP / OUT_FOR_DELIVERY / DELIVERED + otp)

## Realtime

### Option 1: SSE (Beginner-friendly)

* `GET /api/stream/orders/{orderId}` (customer)
* `GET /api/stream/admin/orders` (admin live queue)

### Option 2: WebSocket

* `/ws` with topics: `/topic/orders/{id}`, `/topic/admin/orders`

## Notifications

* `GET /api/notifications`
* `POST /api/notifications/{id}/read`

---

# 6) Frontend Pages (Angular + Tailwind)

## Customer App

* Home (categories + top picks)
* Search (debounce + suggestions)
* Category listing (filters)
* Product detail
* Cart (stepper + stock warnings)
* Checkout:

  * address
  * delivery fee
  * coupon apply
  * wallet slider
* Orders list
* Order detail:

  * timeline
  * live updates (SSE)
  * cancel button (policy aware)
* Wallet page + referral page

## Admin

* Products CRUD + images
* Inventory per store + low-stock alerts
* Orders queue (live)
* Assign rider + batch overview
* Refund queue
* Audit logs

## Warehouse

* Picking queue
* Picklist screen + missing item modal
* Pack confirm

## Rider (mobile-friendly)

* Active batch list + map placeholder
* Order details + status buttons + OTP input

---

# 7) Implementation Roadmap (Best build order)

## Phase 1 — Foundation (Day 1–2)

1. Auth (JWT + refresh)
2. Catalog + store resolve
3. Cart (DB-backed)

## Phase 2 — Money & Order (Day 3–5)

4. Checkout preview + inventory reservation
5. Coupons + pricing engine
6. Idempotent order creation
7. Mock payment confirm

## Phase 3 — Ops & Delivery (Day 6–8)

8. Warehouse picklist + missing items → partial refund
9. Rider batching + delivery events timeline
10. Admin ops screens (assign/advance/refund)

## Phase 4 — Realtime + Growth (Day 9–10)

11. SSE/WebSocket order timeline
12. Notification center
13. Wallet + referral rewards + dynamic delivery fee

---

# 8) “One level up” Extra Functions (Add these too)

## A) Smart Substitution (Blinkit-like)

* If item missing:

  * propose substitute products (same category + similar unit)
  * customer approves/declines (within 5 minutes)
* APIs:

  * `POST /api/warehouse/orders/{id}/suggest-substitute`
  * `POST /api/orders/{id}/substitute/decision`

## B) Surge Delivery Fee (dynamic)

* Delivery fee depends on:

  * distance + order load + time (peak hour)
* Store config rule table + runtime calculation

## C) Rate Limiting + Abuse protection

* login attempts limit
* order placement rate limit per user

## D) Admin “Price change schedule”

* price effective date/time (future pricing)

---

# 9) Seed Data (Mandatory)

* 3 stores: 700xxx, 560xxx, 110xxx
* 8 categories, 80 products
* inventory per store (some low stock)
* coupons:

  * `WELCOME50` (flat)
  * `FRUITS10` (% with cap)
  * `BIGSAVE200` (min cart)
* users:

  * [admin@blinkit.local](mailto:admin@blinkit.local) / Admin
  * [warehouse@blinkit.local](mailto:warehouse@blinkit.local) / wh
  * [rider1@blinkit.local](mailto:rider1@blinkit.local) / rider
  * [user@blinkit.local](mailto:user@blinkit.local) / user
* delivery fee rules + referral bonus

---

# 10) Testing (Hard Requirements)

## Backend tests

* Inventory reservation concurrency (2 users same SKU)
* Reservation expire release test
* Idempotency key duplicate prevention test
* Coupon validation tests (minCart/cap/category)
* Missing item → partial refund → wallet credit test
* Role-based access tests (admin/warehouse/rider/customer)
* SSE emits event on status update test (or event outbox)

## Frontend tests (minimum)

* 1 e2e script:

  * customer add items → preview → apply coupon → use wallet → place order → see live timeline update

---

# 🎥 Bengali “Read-while-code” Script Cues (use this style)

* “এখন আমার backend ta baniye ni—JWT + refresh token setup করছি”
* “এখন store resolve করছি pincode দিয়ে”
* “এখন checkout preview তে 10 মিনিটের stock reserve করলাম”
* “এখন idempotency key add করলাম যাতে double click এ double order না হয়”
* “এখন coupon apply করে pricing breakdown দেখাবো”
* “এখন warehouse picking screen বানাচ্ছি”
* “এখন missing item mark করলে partial refund wallet এ যাবে”
* “এখন rider batch assignment implement করলাম”
* “এখন SSE দিয়ে customer কে live timeline update পাঠাবো”
* “এখন notification center বানালাম”
* “এখন referral + wallet bonus add করলাম”
* “এখন dynamic delivery fee peak hour এ বাড়বে”

---

## তোমার জন্য “Next Step” (কোনটা বানাবো আগে?)

তুমি যেহেতু বলেছ **All 3**, আমি সাজেস্ট করবো এই order এ coding শুরু করো:

1. **Auth + Store resolve + Catalog + Cart**
2. **Checkout preview + Reservation + Idempotency + Payment mock**
3. **Warehouse + Missing items + Partial refund + Wallet**
4. **Rider batching + delivery events timeline**
5. **SSE/WebSocket + Notifications**
6. **Coupons + Referral + Dynamic delivery fee**

যদি তুমি বলো:
✅ তুমি **SSE** নেবে নাকি **WebSocket** (আমি recommend করি **SSE first**) —
আমি পরের রিপ্লাইতেই তোমাকে **complete folder structure + exact endpoints DTO list + entity mapping checklist + migration file plan** একদম copy-paste style এ দিয়ে দেবো (কোন ফাইল আগে বানাবে সেটাও line-by-line)।

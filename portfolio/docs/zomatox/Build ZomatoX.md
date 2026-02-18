নিচে তোমার জন্য **Full-Stack “Zomato-style” Mega Assignment** দিলাম—৪টা phase এ (v1 → v2 → v3-pro → v4) incremental upgrade, যাতে তুমি সিরিজ করে ভিডিও বানাতে পারো এবং প্রতিটা phase শেষে tag দিতে পারো।

---

# ✅ Mega Assignment: Build “ZomatoX” (Full-Stack Food Discovery + Ordering)

## Goal

একটা Zomato-like platform বানাবে যেখানে:

* Users রেস্টুরেন্ট খুঁজবে, filter/sort করবে
* Menu দেখবে, cart বানাবে, order place করবে
* Real-time order status ট্র্যাক করবে
* Restaurant owner menu & orders manage করবে
* Delivery partner pickup/deliver করবে
* Admin platform ops চালাবে (approvals, disputes, refunds)

---

# Tech Stack (Recommended)

### Backend

* Java 17, Spring Boot 3
* Spring Web + Validation + JPA
* v2 থেকে: PostgreSQL + Flyway
* v3-pro থেকে: Spring Security JWT (access+refresh)
* v4 থেকে: Realtime SSE/WebSocket + notifications + audit
* Actuator + Micrometer + OpenAPI

### Frontend

* Angular 17+ + Tailwind
* Role-based routes + guards
* State: RxJS store (consistent)

---

# Roles (Incremental)

* CUSTOMER
* RESTAURANT_OWNER (v2+)
* DELIVERY_PARTNER (v2+)
* ADMIN (v3-pro+)

---

# Core Domain Modules

* Restaurant discovery (search, filters, sort)
* Menu & cart
* Checkout + payment (mock)
* Order lifecycle + tracking
* Ratings & reviews (v2+)
* Offers/coupons (v3-pro+)
* Refunds & disputes (v4)
* Realtime updates (v4)
* Notifications + audit (v4)

---

# Order State Machine (ZomatoX)

`CREATED → PAYMENT_PENDING → PAID → CONFIRMED → PREPARING → READY_FOR_PICKUP → PICKED_UP → OUT_FOR_DELIVERY → DELIVERED`
Cancellation:

* Customer cancel allowed till `CONFIRMED`
* After `PREPARING` owner/admin only (policy)

---

# ✅ Phase v1 (MVP) — “Food Discovery + Order (Basic)”

## Features

Customer:

* Signup/login (simple token ok)
* Restaurant list
* Restaurant details (menu)
* Cart (add/remove/qty)
* Checkout (address + mock payment)
* Orders list + order details (polling)

Admin (simple/hardcoded):

* Add restaurants
* Add menu items

## DB (can use H2 in v1)

* users
* restaurants
* menu_items
* carts, cart_items
* orders, order_items
* payments (mock)

## APIs (minimum)

* GET /restaurants?city=&q=
* GET /restaurants/{id}
* GET /restaurants/{id}/menu
* PUT /cart/items
* GET /cart
* POST /orders
* GET /orders
* GET /orders/{id}
* POST /payments/{orderId}/confirm?result=SUCCESS|FAIL

## Tests (minimum)

* Add to cart
* Place order
* Out-of-stock menu item (if you track stock)

✅ Tag: `v1-zomato-mvp`

---

# ✅ Phase v2 — “Owner Portal + Delivery Partner + Ratings”

## Upgrade focus

1. PostgreSQL + Flyway migrations
2. Add roles: RESTAURANT_OWNER, DELIVERY_PARTNER
3. Owner can manage menu & orders
4. Delivery partner can accept deliveries
5. Ratings & reviews module

## New Features

Owner:

* Create/update restaurant profile
* CRUD menu items
* View incoming orders
* Move order status: CONFIRMED → PREPARING → READY_FOR_PICKUP

Delivery partner:

* View available deliveries
* Accept → PICKED_UP → OUT_FOR_DELIVERY → DELIVERED

Customer:

* Rate restaurant + review after delivered

## DB additions

* reviews (rating, comment)
* delivery_assignments
* order_events timeline (optional)

## Tests

* Owner order status transitions
* Delivery accept and deliver flow
* Review constraints (only delivered orders)

✅ Tag: `v2-zomato-owner-delivery`

---

# ✅ Phase v3-pro — “JWT + Coupons + Pricing + Address Book + Favorites”

## Upgrade focus

1. JWT access+refresh + Spring Security
2. Coupons + pricing engine
3. Address book
4. Favorites + recently viewed
5. Search improvements (filters, sort)

## New Features

Auth:

* /auth/login returns access+refresh
* /auth/refresh

Customer:

* Save multiple addresses
* Apply coupons (min order, cap, category/restaurant)
* Favorites restaurants
* Recently viewed list

Owner:

* Special offers/coupons for their restaurant (optional)

Admin:

* Approve restaurant onboarding
* Flag restaurants/items

## DB additions

* refresh_tokens
* coupons, coupon_redemptions
* addresses
* favorites
* admin_approvals

## Tests

* JWT role access tests
* Coupon validation tests
* Address CRUD tests

✅ Tag: `v3-zomato-pro`

---

# ✅ Phase v4 — “Realtime Tracking + Notifications + Refunds/Disputes + Audit”

## Upgrade focus

1. Realtime status via SSE/WebSocket
2. Notifications center (customer/owner/delivery)
3. Refunds + disputes workflow
4. Admin audit logs + outbox pattern (optional)

## New Features

Realtime:

* Customer sees live order timeline
* Owner sees live new orders
* Delivery sees assignment instantly

Notifications:

* Order confirmed
* Preparing
* Rider assigned
* Delivered
* Refund approved/denied

Refund/Dispute:

* Customer raise dispute (late/missing/wrong item)
* Owner responds
* Admin resolves and triggers refund (wallet or original mock)

Audit:

* Track admin actions: approve/ban/refund

## APIs

* GET /stream/orders/{id} (SSE)
* GET /stream/owner/orders (SSE)
* GET /notifications
* POST /disputes
* POST /admin/disputes/{id}/resolve
* POST /admin/refunds/{id}/approve

## DB additions

* notifications
* disputes
* refunds
* audit_logs
* outbox_events (optional)

## Tests

* Dispute create + resolve
* Refund to wallet
* SSE emits status updates (basic)

✅ Tag: `v4-zomato-realtime-refunds`

---

# Suggested UI Pages (Angular)

Customer:

* Home (restaurants)
* Restaurant details + menu
* Cart
* Checkout + address book
* Orders + order detail timeline
* Favorites + profile

Owner:

* Dashboard
* Restaurant edit
* Menu management
* Orders queue (live v4)

Delivery:

* Jobs list
* Job detail + status updates

Admin:

* Approvals
* Disputes/refunds
* Audit logs

---

# Bengali “Read-While-Code” cues (per phase)

v1:

* “এখন restaurant list বানাচ্ছি”
* “এখন menu থেকে cart এ add করবো”
* “এখন mock payment দিয়ে order place করলাম”

v2:

* “এখন owner portal থেকে order status এগোবে”
* “এখন delivery partner accept করে deliver করবে”
* “এখন review module যোগ করলাম”

v3-pro:

* “এখন JWT access+refresh setup করলাম”
* “এখন coupon apply করে pricing breakdown দেখাবো”
* “এখন address book যোগ করলাম”

v4:

* “এখন SSE দিয়ে live order tracking দেখাবো”
* “এখন dispute raise করে admin resolve করবো”
* “এখন audit logs এ সব action ট্র্যাক হচ্ছে”

---

তুমি চাইলে আমি এটাকে তোমার আগের মতো **Single Master Prompt (tag-wise)** বানিয়ে দেবো—যেটা Codex/ChatGPT তে দিলে phase by phase সম্পূর্ণ code generate করবে।
ZomatoX সিরিজে **v5** মানে হবে v4 (realtime + refunds/disputes) এর পরের **next-gen, production-ish** upgrade—যেখানে “scale, reliability, growth, ops automation” যোগ হবে।

নিচে আমি v5 কে **clear scope** করে দিলাম + চাইলে তোমার জন্য v5-এর **master prompt** বানাতে পারি।

---

## ✅ ZomatoX v5 = “Scale + Reliability + Growth + Ops Automation”

### 1) Architecture / Scalability

* **Modularization**: monolith-এর ভিতরে clearly bounded modules (restaurant, order, delivery, payments, trust)
* Optional: **extract 2 services** (order-service, notification-service) *or* keep modular monolith but event-driven internally
* **Event bus**: internal domain events + outbox mandatory

### 2) Performance & Search

* **Elastic / OpenSearch** style search (optional)
* If not: Postgres full-text search + trigram index
* **Caching**: Redis for restaurant list/menu/hot offers
* **Pagination everywhere** + cursor pagination for feeds

### 3) Reliability Hardening

* **Outbox required** + retry + dead-letter table
* **Idempotency** for:

  * order placement
  * delivery status updates
  * refund approvals
* **Optimistic locking** critical tables

### 4) Payment & Settlement (Mock → more realistic)

* Payment intents (PENDING → AUTHORIZED → CAPTURED/FAILED)
* Refund ledger + reconciliation report
* Settlement: restaurant payout records (weekly payouts)

### 5) Delivery Optimization

* **Batching / routing** (simple heuristic)
* Rider capacity + assignment rules
* SLA tracking + late delivery penalties

### 6) Fraud / Abuse / Trust

* Review fraud detection rules (basic heuristics)
* Rate limiting + suspicious activity flags
* Admin “risk dashboard”

### 7) Observability & Ops

* **Tracing** (OpenTelemetry optional)
* Metrics dashboards: order pipeline health, outbox lag, refund rate, SLA breach rate
* Admin “ops console” improvements
* Feature flags for enabling v5 features safely

### 8) Growth

* **Subscriptions** (Zomato Gold-like): free delivery/extra discounts
* Loyalty tiers + points
* Referral improvements (fraud safe)

---

## ✅ Suggested Version Ladder (simple)

* **v1**: discovery + cart + order + mock payment
* **v2**: postgres + owner + delivery + reviews + events
* **v3-pro**: JWT + RBAC + coupons + addresses + favorites + admin approvals/moderation
* **v4**: SSE realtime + notifications + disputes/refunds + audit + outbox
* **v5**: scale + reliability hardening + payments/settlement + delivery optimization + ops/growth

---

## তুমি কোন v5 চাইছো? (আমি best-effort দিয়ে এগিয়ে যাচ্ছি)

আমি ধরে নিচ্ছি তুমি v5 হিসেবে এটা চাইছো:

✅ **v5 = “Gold subscription + Redis cache + idempotency everywhere + settlement + SLA metrics + routing/batching improvements + rate limiting”**
 
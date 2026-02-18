[TITLE]
ZomatoX v5 Upgrade (from v4) — Scale + Reliability Hardening + Payments/Settlement + Delivery Optimization + Growth (Gold) + Ops

[ROLE]
You are a senior full-stack architect and lead engineer. Upgrade the existing v4 codebase of “ZomatoX” to v5.
v4 already has: PostgreSQL+Flyway, JWT access+refresh, RBAC, owner+delivery+admin portals, reviews, coupons/pricing, address book, favorites/recents, SSE realtime streams, notifications center, disputes+refunds workflow, audit logs, and outbox pattern (recommended).
You MUST keep existing v1/v2/v3/v4 APIs working while adding v5 features.
Output must be copy-paste ready and compile/run.

[PRIMARY GOAL — v5]
Add “Scale + Reliability + Growth + Ops Automation”:
1) Reliability Hardening:
   - Idempotency for critical write APIs (order placement already; add for delivery status updates, dispute resolution, refund approval, coupon redemption)
   - Outbox mandatory with retries + dead-letter handling + monitoring
   - Optimistic locking on critical tables (orders, refunds, wallets, inventory/stock if present)
2) Caching + Performance:
   - Add Redis caching for restaurant list, restaurant details/menu, coupons lookup, “hot restaurants”
   - Add cache invalidation on owner/admin updates
   - Add Postgres indexes & query tuning (pagination + sort)
3) Payments v2 (More realistic than mock):
   - Payment Intent flow: CREATED → AUTHORIZED → CAPTURED / FAILED
   - Refund integrates with payment intents (still wallet default)
   - Keep mock confirm endpoints working but implement them as wrappers around PaymentIntentService
4) Settlement / Payouts (Owner finance):
   - Weekly payout records to restaurants: compute delivered orders - platform fees - refunds
   - Payout status: PENDING → PROCESSING → PAID
   - Owner can view payout history; admin can trigger payout run
5) Delivery Optimization (Simple but real):
   - Assignment rules: nearest/least-loaded delivery partner + capacity
   - Optional batching: allow 2–3 orders if same restaurant + nearby drop locations (heuristic)
   - SLA tracking: expectedDeliveryAt, deliveredAt, breach flag, breach reason
6) Growth: ZomatoX Gold Subscription + Loyalty
   - Gold subscription: monthly plan enabling free delivery up to X orders/month + extra discount cap
   - Loyalty points: earn points on delivered orders; redeem as wallet credit (optional)
7) Ops & Observability:
   - Metrics dashboards (via Micrometer): outbox lag, cache hit rate, payout totals, SLA breach rate
   - Rate limiting: basic per-user/per-IP limiter for auth & order APIs
   - Admin ops console pages for: outbox monitor, payout runs, SLA breaches

[UPGRADE ORDER — MUST FOLLOW]
A) Reliability hardening + idempotency expansion
B) Redis caching + invalidation
C) PaymentIntent refactor (keep old mock endpoints)
D) Settlement/payouts module
E) Delivery optimization + SLA tracking
F) Growth: Gold + loyalty
G) Ops: rate limiting + metrics + admin monitors + UI pages

[STACK — v5]
Backend:
- Java 17, Spring Boot 3
- Spring Web, Validation, JPA
- Spring Security JWT (existing)
- PostgreSQL + Flyway (existing)
- Redis (new, docker-compose)
- Actuator + Micrometer + OpenAPI
- JUnit 5 tests

Frontend:
- Angular 17 + Tailwind
- Existing role routes + guards
- State: RxJS store (consistent)
- Add new pages for payouts, gold, ops monitors

[RELIABILITY HARDENING — v5 REQUIRED]
1) Idempotency Expansion
Add idempotency for these endpoints (Idempotency-Key header):
- POST /api/orders (already v4/v3; ensure strong)
- POST /api/delivery/orders/{orderId}/status
- POST /api/admin/disputes/{id}/resolve
- POST /api/admin/refunds/{id}/approve
- POST /api/admin/refunds/{id}/mark-paid
- POST /api/checkout/apply-coupon (optional)
Rules:
- Key scope = (userId + endpoint + requestBodyHash)
- Same key must return same result (status+body) without double execution
Data model:
- idempotency_keys(id, user_id, key, endpoint, request_hash, response_code, response_body_json, created_at, expires_at)
- TTL cleanup job.

2) Outbox Mandatory + Dead Letter
- outbox_events must have statuses: PENDING, PUBLISHED, FAILED, DEAD
- retry_count, next_retry_at, last_error
- publisher job processes PENDING/FAILED where next_retry_at <= now
- when retry_count exceeds limit -> DEAD
Admin APIs:
- GET /api/admin/outbox?status=PENDING|FAILED|DEAD&page=
- POST /api/admin/outbox/{id}/retry
- POST /api/admin/outbox/{id}/mark-dead (optional)

3) Optimistic Locking
- Add @Version to: Order, Refund, Wallet, PaymentIntent (and any stock entities)
- Add retry utility for OptimisticLockException in services.

[CACHING + REDIS — v5 REQUIRED]
Add Redis and caching strategy:
- Cache keys:
  - restaurants:list:{city}:{q}:{cuisine}:{sort}:{page}
  - restaurants:detail:{id}
  - restaurants:menu:{id}
  - coupons:code:{code}
- Use Spring Cache abstraction with RedisCacheManager.
- Invalidation:
  - owner/admin updates restaurant/menu -> evict relevant keys
  - coupon updates -> evict coupon key
- Metrics:
  - cache_get_total, cache_hit_total, cache_miss_total (use MeterBinder or custom counters)

Docker-compose:
- postgres + redis + backend

[PAYMENTS v2 — PAYMENT INTENTS]
Introduce Payment Intent:
- payment_intents:
  id, order_id, status(CREATED/AUTHORIZED/CAPTURED/FAILED), amount, provider(MOCK), created_at, updated_at
Keep existing mock endpoints but implement via PaymentIntentService:
- POST /api/payments/{orderId}/confirm?result=SUCCESS|FAIL
  - SUCCESS => authorize+capture
  - FAIL => failed
Add new endpoints (optional but recommended):
- POST /api/payments/{orderId}/authorize
- POST /api/payments/{orderId}/capture
- GET /api/payments/{orderId}/intent

Rules:
- Order transitions:
  - PAYMENT_PENDING -> PAID only when CAPTURED
- Refunds:
  - default wallet, but keep link to payment_intent for reconciliation

[SETTLEMENT / PAYOUTS — v5 REQUIRED]
Add weekly payouts:
- restaurant_payouts:
  id, restaurant_id, period_start, period_end, gross_amount, refunds_amount, platform_fee_amount, net_amount,
  status(PENDING/PROCESSING/PAID), created_at, paid_at
- payout_items:
  id, payout_id, order_id, order_amount, platform_fee, refund_amount, net

Computation:
- include only DELIVERED orders in period
- subtract refunds
- platform fee rule: percentage + fixed (configurable)

Admin APIs:
- POST /api/admin/payouts/run?from=YYYY-MM-DD&to=YYYY-MM-DD
- GET /api/admin/payouts?status=&restaurantId=&page=
- POST /api/admin/payouts/{id}/mark-paid

Owner APIs:
- GET /api/owner/payouts?page=
- GET /api/owner/payouts/{id}

[DELIVERY OPTIMIZATION + SLA — v5 REQUIRED]
Add SLA fields to orders:
- expected_delivery_at
- delivered_at
- sla_breached boolean
- breach_reason

Assignment logic:
- Delivery partner has:
  - active_assignments_count
  - capacity (max active)
- Algorithm:
  - choose least-loaded + capacity available
  - optionally filter by city/zone
Batching (heuristic, optional):
- allow batch of up to 2 orders from same restaurant if both READY_FOR_PICKUP and close drop area (simplify using same pincode)
Metrics:
- sla_breach_rate
- avg_delivery_time_minutes

Admin ops:
- GET /api/admin/sla/breaches?dateFrom=&dateTo=&page=
- GET /api/admin/delivery/partners?sort=load

[GROWTH — GOLD SUBSCRIPTION + LOYALTY]
Gold subscription:
- gold_plans: id, name, monthly_price, free_delivery_limit, extra_discount_percent, max_discount_cap, active
- user_subscriptions: id, user_id, plan_id, status(ACTIVE/CANCELED/EXPIRED), start_at, end_at, remaining_free_deliveries
Rules:
- If subscription active and remaining_free_deliveries > 0 => deliveryFee waived; decrement on delivered
- Extra discount applied after coupon with caps

APIs:
Customer:
- GET /api/gold/plans
- POST /api/gold/subscribe (planId)
- POST /api/gold/cancel
- GET /api/gold/me
Checkout integration:
- pricing engine must include gold benefit breakdown

Loyalty (optional but recommended):
- loyalty_points: user_id, points_balance, updated_at
- loyalty_txns: credit on delivered order, debit on redemption
- redeem -> wallet credit
APIs:
- GET /api/loyalty/me
- POST /api/loyalty/redeem (points)

[OPS — RATE LIMITING + METRICS + ADMIN MONITORS]
Rate limiting:
- implement simple token bucket per IP/user for:
  - /api/auth/login, /api/auth/refresh
  - /api/orders (create)
  - /api/disputes (create)
- Use Redis-backed rate limiter (preferred) or in-memory for dev.
Expose metrics:
- outbox_pending_count, outbox_dead_count, outbox_publish_latency_ms
- cache hit/miss
- payouts_total_amount
- sla breach rate

Admin UI pages:
- Outbox Monitor (PENDING/FAILED/DEAD)
- Payout Runs + Payout list
- SLA Breaches list
- Gold Plans management (optional admin)

[DATA MODEL — v5 MIGRATIONS]
Provide Flyway migration V5__...sql creating/altering:
- idempotency_keys (if not already)
- outbox_events alterations (dead-letter fields)
- payment_intents
- restaurant_payouts, payout_items
- gold_plans, user_subscriptions
- (optional) loyalty_points, loyalty_txns
- indexes for restaurants/menu search + orders by status + events by order_id

[FRONTEND — v5 UI]
Customer:
- Gold plans page + subscribe/cancel
- Checkout shows:
  - coupon discount
  - gold discount
  - delivery fee waiver status
- Loyalty page (optional)
Owner:
- Payouts list + payout detail
Admin:
- Outbox monitor page
- Payout run page
- SLA breaches page
Delivery:
- show assignment load + capacity (optional)

SSE remains from v4; ensure new events (payout/refund updates) can also create notifications.

[TESTS — v5 MINIMUM]
1) IdempotencyExpandedTest:
   - delivery status update with same Idempotency-Key not duplicated
   - refund approve idempotent
2) OutboxDeadLetterTest:
   - failed publishes retry then mark DEAD after max retries
3) CacheInvalidationTest:
   - updating menu item evicts menu cache
4) PaymentIntentFlowTest:
   - confirm success -> CAPTURED -> order PAID
   - confirm fail -> FAILED -> order not PAID
5) PayoutComputationTest:
   - delivered orders in window computed correctly with refunds deducted
6) GoldBenefitPricingTest:
   - active subscription waives delivery fee and reduces remaining count
7) RateLimitTest:
   - too many login attempts blocked

[RUN INSTRUCTIONS — MUST OUTPUT]
- docker-compose up -d (postgres + redis)
- mvn spring-boot:run
- npm i && ng serve
Provide curl examples:
- login
- apply coupon + gold pricing preview
- place order with Idempotency-Key
- confirm payment (mock wrapper)
- admin run payouts
- admin view outbox dead letters

[OUTPUT FORMAT — MUST FOLLOW]
1) Upgrade Diff Checklist (what changed from v4)
2) Updated repository tree
3) Full code for new/changed files (copy-paste ready)
4) Flyway migration V5 SQL
5) Updated docker-compose including redis
6) Updated seed data:
   - gold plans
   - some delivered orders in past week for payouts
   - some outbox failed samples (optional)
7) Updated Angular pages/services/interceptors/guards
8) Tests + run steps + curl
9) End with:
[GIT TAG COMMANDS]
git status
git add .
git commit -m "v5: reliability hardening + redis cache + payment intents + payouts + SLA + gold + ops monitors"
git tag v5-zomatox-scale-growth
git tag -n

[QUALITY BAR]
- Must compile and run on PostgreSQL with Flyway and Redis
- No placeholders that break compilation
- Keep v4 realtime/refunds/disputes working
- Clean validation + error responses
- No copyrighted assets; generic icons only

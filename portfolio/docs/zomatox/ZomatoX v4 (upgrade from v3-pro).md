[TITLE]
ZomatoX v4 Upgrade (from v3-pro) — Realtime Tracking + Notifications + Refunds/Disputes + Audit + Outbox Reliability

[ROLE]
You are a senior full-stack architect and lead engineer. Upgrade the existing v3-pro codebase of “ZomatoX” to v4.
v3-pro already has: PostgreSQL+Flyway, JWT access+refresh, RBAC, owner portal, delivery partner flow, reviews, coupons/pricing, address book, favorites/recents, admin approvals/moderation.
You MUST keep existing v1/v2/v3 APIs working while adding v4 features.
Output must be copy-paste ready and compile/run.

[PRIMARY GOAL — v4]
Add “Ops + Realtime + Trust” capabilities:
1) Realtime updates (SSE preferred): customer order tracking, owner live orders, delivery live assignments
2) Notifications center (read/unread) driven by order events
3) Refunds + disputes workflow (missing/wrong/late) + admin resolution + refund ledger
4) Audit logs for admin/owner actions (moderation, refunds, approvals)
5) Reliability: Outbox pattern for event publishing (optional but recommended)
6) Observability improvements: key metrics + structured logs for order pipeline health

[REALTIME — MUST USE SSE]
Implement SSE endpoints (not WebSocket in v4):
- Customer order stream:
  GET /api/stream/orders/{orderId}
- Owner order stream (owned restaurants only):
  GET /api/stream/owner/orders
- Delivery stream (assigned jobs):
  GET /api/stream/delivery/jobs
- Admin stream (optional):
  GET /api/stream/admin/events

Rules:
- SSE events triggered on every order status change and dispute/refund state change.
- Must be secure: SSE endpoints require JWT and must filter by user role/ownership.

[NOTIFICATIONS MODULE]
- Notifications created from:
  - order status changes (CONFIRMED, PREPARING, READY_FOR_PICKUP, OUT_FOR_DELIVERY, DELIVERED)
  - coupon applied (optional)
  - dispute update (ACK, RESOLVED)
  - refund update (APPROVED, PAID)
- APIs:
  - GET /api/notifications?status=UNREAD|ALL&page=
  - POST /api/notifications/{id}/read
  - POST /api/notifications/read-all
- Table:
  notifications(id, user_id, type, title, body, ref_type, ref_id, is_read, created_at)

[DISPUTES + REFUNDS — OPS/Trust]
Dispute flow:
- Customer can raise dispute after order is DELIVERED or if SLA breached:
  - reasons: MISSING_ITEM, WRONG_ITEM, LATE_DELIVERY, QUALITY_ISSUE, OTHER
  - includes: orderId, reason, comment, optional evidenceUrl (string)
- Owner can respond to dispute (comment + accept/deny)
- Admin resolves:
  - RESOLVED_WITH_REFUND
  - RESOLVED_NO_REFUND
  - NEED_MORE_INFO

Refund rules:
- Refund can be FULL or PARTIAL per order item:
  - store snapshot: itemName, qty, unitPrice, refundQty, refundAmount
- Refund destination:
  - default WALLET (introduce wallet if not in v3; if wallet exists, use it)
  - otherwise mark as MOCK_ORIGINAL (no real gateway)
- Refund must be recorded in ledger tables and reflected in order payable summary.

APIs:
Customer:
- POST /api/disputes (orderId, reason, comment, evidenceUrl?)
- GET /api/disputes
- GET /api/disputes/{id}
Owner:
- GET /api/owner/disputes?status=OPEN|IN_REVIEW
- POST /api/owner/disputes/{id}/respond (accept:boolean, comment)
Admin:
- GET /api/admin/disputes?status=OPEN|IN_REVIEW|RESOLVED
- POST /api/admin/disputes/{id}/resolve (resolutionType, refundPlan)
- GET /api/admin/refunds?status=PENDING|APPROVED|PAID
- POST /api/admin/refunds/{id}/approve
- POST /api/admin/refunds/{id}/mark-paid (wallet credit)

[DATA MODEL — v4 ADDITIONS]
Add via Flyway V4 migration:
- disputes:
  id, order_id, user_id, restaurant_id, status(OPEN/IN_REVIEW/RESOLVED),
  reason, comment, evidence_url, owner_response, admin_resolution, created_at, updated_at
- refunds:
  id, dispute_id nullable, order_id, user_id, status(PENDING/APPROVED/PAID/REJECTED),
  destination(WALLET/MOCK_ORIGINAL), total_amount, created_at, updated_at
- refund_items:
  id, refund_id, item_name_snapshot, unit_price_snapshot, qty_refunded, amount
- notifications (as defined)
- audit_logs:
  id, actor_user_id, actor_role, action, entity_type, entity_id, meta_json, created_at
- outbox_events (recommended):
  id, aggregate_type, aggregate_id, event_type, payload_json, status(PENDING/PUBLISHED/FAILED),
  created_at, published_at, retry_count

If wallet not present in v3, add:
- wallets(id, user_id, balance, updated_at)
- wallet_txns(id, wallet_id, type(CREDIT/DEBIT), amount, ref_type, ref_id, created_at)

[ORDER EVENTS & EVENT PUBLISHING]
- Every order status change already creates order_events (v2+). In v4:
  - Also publish an outbox_event (or direct SSE publish if outbox disabled).
- For disputes/refunds state changes, publish events similarly.

[BACKEND ARCHITECTURE — v4]
Base package: com.example.zomatox
- Add modules:
  - realtime (SSE controllers + emitters)
  - notification (entity/repo/service/controller)
  - dispute (entity/repo/service/controller)
  - refund (entity/repo/service/controller)
  - audit (entity/repo/service/controller)
  - outbox (entity/repo/publisher scheduler)

Reliability:
- OutboxPublisher scheduled job:
  - polls PENDING outbox events
  - publishes to in-memory EventBus + SSE emitters
  - marks as PUBLISHED
  - retries with backoff on failures
- Ensure transactional consistency:
  - when changing order status, create order_event + outbox_event in same transaction

Observability:
- metrics:
  - orders_status_transitions_total{from,to}
  - disputes_created_total{reason}
  - refunds_total{status}
  - sse_connections_active{stream}
- logs:
  - correlation id per request

[FRONTEND — v4 UI REQUIREMENTS]
Angular additions:
Customer:
- Order detail page shows live timeline via SSE (EventSource)
- Notifications page + unread badge
- Disputes:
  - create dispute for delivered order
  - dispute list/detail
- Wallet page (if added) showing balance + txns

Owner:
- Live incoming orders page (SSE)
- Disputes inbox + respond UI

Delivery:
- Live jobs updates (SSE)
- Order/job detail status UI

Admin:
- Disputes queue
- Refund approvals page
- Audit logs viewer

Frontend behavior:
- Use JWT interceptor + refresh flow (from v3)
- SSE must include access token:
  - prefer SSE endpoints that accept token via query param ?token=... (secure enough for demo)
  - OR use a short-lived SSE ticket endpoint that returns a one-time token
Provide the chosen method and implement it end-to-end.

[TESTS — v4 MINIMUM]
1) DisputeFlowTest:
   - customer can create dispute only after DELIVERED (or SLA breach rule if implemented)
2) RefundPlanTest:
   - admin resolve with partial refund creates refund + refund_items
   - approve -> wallet credited
3) NotificationTest:
   - status change generates notification
4) OutboxPublisherTest:
   - outbox events created and marked PUBLISHED after publish job run
5) SecurityTest:
   - customer cannot access owner/admin dispute endpoints
   - owner can access only own restaurant disputes
6) SSE basic test:
   - ensure emitter registers; on status change event published to stream (can be unit test with emitter service)

[RUN INSTRUCTIONS — MUST OUTPUT]
- docker-compose up -d (postgres)
- mvn spring-boot:run
- npm i && ng serve
Provide curl examples:
- create dispute
- owner respond
- admin resolve + approve refund
- view notifications
- connect to SSE stream (customer order stream)

[OUTPUT FORMAT — MUST FOLLOW]
1) Upgrade Diff Checklist (what changed from v3)
2) Updated repository tree
3) Full code for new/changed files (copy-paste ready)
4) Flyway migration V4 SQL
5) Updated seed data:
   - some delivered orders to test dispute/refund
   - admin user
   - wallet balances (if used)
6) Updated Angular pages/services/interceptors/guards + SSE client code
7) Tests + run steps + curl
8) End with:
[GIT TAG COMMANDS]
git status
git add .
git commit -m "v4: SSE realtime + notifications + disputes/refunds + audit + outbox reliability"
git tag v4-zomatox-realtime-refunds
git tag -n

[QUALITY BAR]
- Must compile and run on PostgreSQL with Flyway
- No placeholders that break compilation
- Keep v3-pro features working
- Clean validation + error responses
- No copyrighted assets; generic icons only

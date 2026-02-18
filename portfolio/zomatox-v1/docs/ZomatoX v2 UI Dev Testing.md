# ZomatoX v2 UI Dev Testing Guide

This guide is for frontend/UI development testing of **ZomatoX v2** against the local backend.

## 1) Prerequisites

- Java: JDK 17 or 21 recommended (project currently allows skipping enforcer if needed).
- Node.js: Use an LTS version (v20/v22 recommended).
- Docker Desktop running.

## 2) Start Backend (v2)

From `portfolio/zomatox-v1/backend`:

```bash
docker compose up -d
DB_HOST=localhost DB_PORT=5433 DB_NAME=zomatox DB_USER=zomatox DB_PASS=zomatox \
mvn -Denforcer.skip=true spring-boot:run
```

Backend URLs:

- API base: `http://localhost:8080/api`
- Swagger: `http://localhost:8080/swagger-ui.html`

## 3) Start Frontend

From `portfolio/zomatox-v1/frontend`:

```bash
npm i
npm start
```

Frontend URL:

- UI: `http://localhost:4200`

## 4) Seeded Test Users (UI Role Switch)

Use the header dropdown in UI to switch:

- `Customer (id=1)` role `CUSTOMER`
- `Owner (id=2)` role `OWNER`
- `Delivery (id=3)` role `DELIVERY_PARTNER`

The app sends:

- `X-User-Id`
- `X-User-Role`

on every API call.

## 5) Quick Smoke Checklist

1. Open `/restaurants`.
2. Open one restaurant and add an item to cart.
3. Go to `/cart` and confirm line totals.
4. Go to `/checkout` and place order.
5. Open `/orders/:id` and confirm timeline appears.
6. Try `Mock Pay SUCCESS` and verify status changes to `CONFIRMED` and events update.

## 6) Owner Portal Testing

Route: `/owner/orders`

Checklist:

1. Switch user to `Owner (id=2)`.
2. Open `/owner/restaurants` and verify owned restaurants are visible.
3. Open `/owner/orders` and load `CONFIRMED`.
4. Set order status to `PREPARING`.
5. Set order status to `READY_FOR_PICKUP`.
6. Verify customer order timeline shows both owner transitions.

Expected behavior:

- Owner can only move orders to `PREPARING` or `READY_FOR_PICKUP`.
- Invalid transitions return backend error.

## 7) Delivery Portal Testing

Routes:

- `/delivery/jobs`
- `/delivery/order/:id`

Checklist:

1. Switch user to `Delivery (id=3)`.
2. In `AVAILABLE`, accept a `READY_FOR_PICKUP` order.
3. Open assigned order detail.
4. Set `PICKED_UP`.
5. Set `OUT_FOR_DELIVERY`.
6. Set `DELIVERED`.
7. Verify timeline includes all delivery events.

Expected behavior:

- Delivery cannot update unassigned orders.
- Delivery transition order is enforced by backend state machine.

## 8) Review Testing (Customer)

Route: `/restaurants/:id`

Checklist:

1. Switch to `Customer (id=1)`.
2. Use an order that reached `DELIVERED`.
3. Submit review with:
   - `orderId`
   - `rating (1-5)`
   - `comment`
4. Confirm review appears in list.
5. Try posting again for same order.

Expected behavior:

- Review allowed only for customer’s own `DELIVERED` order.
- Only one review per order.

## 9) API Endpoints to Watch in DevTools

- `GET /api/restaurants`
- `GET /api/restaurants/{id}/menu`
- `GET /api/cart`
- `POST /api/orders`
- `POST /api/payments/{orderId}/confirm?result=SUCCESS|FAIL`
- `GET /api/orders/{id}/events`
- `GET /api/owner/orders?status=...`
- `POST /api/owner/orders/{id}/status?next=...`
- `GET /api/delivery/jobs?status=AVAILABLE|ASSIGNED`
- `POST /api/delivery/jobs/{id}/accept`
- `POST /api/delivery/orders/{id}/status?next=...`
- `GET /api/restaurants/{id}/reviews`
- `POST /api/restaurants/{id}/reviews`

## 10) Common Local Failures and Fixes

1. `Unsupported Database: PostgreSQL 16.x`
   - Ensure backend `pom.xml` includes `flyway-database-postgresql` dependency.

2. `password authentication failed`
   - Ensure app uses `DB_PORT=5433` and `DB_USER/DB_PASS=zomatox`.
   - Recreate DB once if needed:

```bash
cd portfolio/zomatox-v1/backend
docker compose down -v
docker compose up -d
```

3. Port conflict (`5432 already allocated`)
   - This project intentionally maps Postgres to `5433`.

4. Java version blocked by enforcer
   - Use JDK 17/21 or run with `-Denforcer.skip=true`.

## 11) Definition of Done (UI Dev)

- All customer, owner, and delivery routes render and navigate correctly.
- Status transitions are reflected in UI and timeline with no stale state.
- Review create/list flows work with validation errors shown cleanly.
- No console errors in browser during full end-to-end scenario.
- Network calls carry `X-User-Id` and `X-User-Role` headers consistently.

## 12) QA Test Case Table (Quick Execution)

| ID | Area | Precondition | Steps | Expected Result |
|---|---|---|---|---|
| ZX-UI-001 | App Load | Backend + frontend running | Open `/restaurants` | Restaurant list loads without console/API errors |
| ZX-UI-002 | Role Switch | App loaded | Switch dropdown user to Customer/Owner/Delivery | `X-User-Id` and `X-User-Role` headers change accordingly |
| ZX-UI-003 | Restaurant Detail | Have list data | Open one restaurant | Restaurant info + menu render correctly |
| ZX-UI-004 | Add to Cart | Restaurant menu visible | Add item qty=1 | Cart count increases; no cross-restaurant break |
| ZX-UI-005 | Cart Totals | Cart has items | Open `/cart` | Line totals and item total are correct |
| ZX-UI-006 | Create Order | Customer selected + valid address | Checkout and place order | Order created with `PAYMENT_PENDING` event trail |
| ZX-UI-007 | Payment Success | Existing order in order detail | Click `Mock Pay SUCCESS` | Order status becomes `CONFIRMED`; timeline updates |
| ZX-UI-008 | Payment Fail | Existing order in order detail | Click `Mock Pay FAIL` | Order moves to `PAYMENT_FAILED`; error-free UI |
| ZX-UI-009 | Owner Orders View | Switch to Owner (id=2) | Open `/owner/orders` | Owner sees own restaurant orders |
| ZX-UI-010 | Owner PREPARING | Owner has `CONFIRMED` order | Set `PREPARING` | Success response; status chip updates |
| ZX-UI-011 | Owner READY_FOR_PICKUP | Owner has `PREPARING` order | Set `READY_FOR_PICKUP` | Success response; order available for delivery accept |
| ZX-UI-012 | Delivery Jobs | Switch to Delivery (id=3) | Open `/delivery/jobs` AVAILABLE | READY_FOR_PICKUP orders appear |
| ZX-UI-013 | Delivery Accept | AVAILABLE job exists | Click `Accept` | Order moves to ASSIGNED for current delivery user |
| ZX-UI-014 | Delivery Status Flow | Assigned order exists | Set PICKED_UP -> OUT_FOR_DELIVERY -> DELIVERED | All transitions succeed in order |
| ZX-UI-015 | Timeline Integrity | Order transitioned | Open `/orders/:id` as customer | Timeline includes payment, owner, delivery events |
| ZX-UI-016 | Reviews Create | Customer has DELIVERED order | Submit review with valid `orderId` + rating 1-5 | Review created and shown in restaurant reviews list |
| ZX-UI-017 | Review Duplicate Guard | One review already posted for order | Post another review with same orderId | Backend rejects; UI handles error safely |
| ZX-UI-018 | Role Guard (Owner) | Switch to Customer | Access `/owner/orders` actions | Forbidden/failed action handled; no crash |
| ZX-UI-019 | Role Guard (Delivery) | Switch to Owner/Customer | Try delivery status update endpoints via UI | Forbidden state handled; no UI corruption |
| ZX-UI-020 | Regression Smoke | Full cycle complete | Refresh app and revisit routes | Data persists from backend; routes still functional |

### Pass/Fail Template

Use this lightweight format in your QA notes:

```text
ZX-UI-001 PASS
ZX-UI-002 PASS
...
ZX-UI-017 FAIL - Duplicate review error message not user-friendly
```


```text
• 1. Switch to Owner (id=2) and open /owner/orders.
  2. Pick one CONFIRMED order, set PREPARING, then READY_FOR_PICKUP.
  3. Switch to Delivery (id=3) and open /delivery/jobs.
  4. In AVAILABLE, click Accept on that same order.
  5. Open that order and set statuses in order:
     PICKED_UP -> OUT_FOR_DELIVERY -> DELIVERED.
  6. Switch to Customer (id=1) and open /orders.
  7. Open that same order id in /orders/:id.
  8. In Timeline, verify event sequence includes:
     PAYMENT_PENDING, PAID, CONFIRMED, PREPARING, READY_FOR_PICKUP, PICKED_UP, OUT_FOR_DELIVERY, DELIVERED.

  Optional API verification:

  curl -H "X-User-Id: 1" http://localhost:8080/api/orders/<ORDER_ID>/events

  If any step fails, share the order id + failing transition message.
```


```text
• This response is correct for your request data.

  You are posting to:

  - /api/restaurants/4/reviews

  but using:

  - "orderId": 6

  Backend requires that orderId must belong to the same restaurant in URL.
  So order 6 is not from restaurant 4.

  Use matching data:

  1. Find your orders:

  curl -H "X-User-Id: 1" http://localhost:8080/api/orders

  2. Pick an order where:

  - restaurantId = 4
  - status = DELIVERED

  3. Use that orderId in:

  POST /api/restaurants/4/reviews

  If you share the /api/orders output, I can tell you the exact valid orderId to use
```
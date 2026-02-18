# ZomatoX v3-pro: All 4 Roles Testing Guide

This guide validates the implemented v3-pro upgrade with all 4 roles:
- `CUSTOMER`
- `OWNER`
- `DELIVERY_PARTNER`
- `ADMIN`

## 1) Run backend + frontend

```bash
cd portfolio/zomatox-v1/backend
docker compose up -d
DB_HOST=localhost DB_PORT=5433 DB_NAME=zomatox DB_USER=zomatox DB_PASS=zomatox mvn spring-boot:run
```

```bash
cd portfolio/zomatox-v1/frontend
npm i
ng serve
```

Swagger:
- `http://localhost:8080/swagger-ui/index.html`

Actuator:
- `http://localhost:8080/actuator/health`
- `http://localhost:8080/actuator/prometheus`

## 2) Seed users (all 4)

- Customer: `customer@zomatox.local` / `customer123`
- Owner: `owner@zomatox.local` / `owner123`
- Delivery: `delivery@zomatox.local` / `delivery123`
- Admin: `admin@zomatox.local` / `admin123`

## 3) Auth smoke (JWT)

Login:
```bash
curl -s http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"customer@zomatox.local","password":"customer123"}'
```

Refresh:
```bash
curl -s http://localhost:8080/api/auth/refresh \
  -H 'Content-Type: application/json' \
  -d '{"refreshToken":"<REFRESH_TOKEN_FROM_LOGIN>"}'
```

Me:
```bash
curl -s http://localhost:8080/api/auth/me \
  -H 'Authorization: Bearer <ACCESS_TOKEN>'
```

## 4) Role flows

### A) ADMIN flow
1. Login as admin in UI (`/login`) or via API.
2. Open admin pages:
   - `/admin/restaurants`
   - `/admin/reviews`
3. Approve a pending restaurant:
```bash
curl -X POST 'http://localhost:8080/api/admin/restaurants/<ID>/approve' \
  -H 'Authorization: Bearer <ADMIN_ACCESS_TOKEN>'
```
4. Optional moderation:
```bash
curl -X POST 'http://localhost:8080/api/admin/restaurants/<ID>/block?reason=policy' \
  -H 'Authorization: Bearer <ADMIN_ACCESS_TOKEN>'
```

### B) OWNER flow
1. Login as owner.
2. Use owner routes (`/owner/*`) and confirm restaurant status badge shows:
   - `PENDING_APPROVAL` / `APPROVED` / `REJECTED`
3. Owner order actions should succeed only for owner-owned restaurants.

### C) CUSTOMER flow
1. Login as customer.
2. Address book:
   - open `/customer/addresses`
   - add/update/delete address
3. Favorites:
   - open a restaurant detail page
   - click **Add to Favorites**
   - verify in `/customer/favorites`
4. Checkout with coupon:
   - add cart items from one approved/unblocked restaurant
   - open `/checkout`
   - apply coupon (ex: `WELCOME20`)
   - place order

Coupon preview API:
```bash
curl -s http://localhost:8080/api/checkout/apply-coupon \
  -H 'Authorization: Bearer <CUSTOMER_ACCESS_TOKEN>' \
  -H 'Content-Type: application/json' \
  -d '{"restaurantId":1,"couponCode":"WELCOME20"}'
```

### D) DELIVERY_PARTNER flow
1. Login as delivery user.
2. Open `/delivery/jobs` to see `READY_FOR_PICKUP` jobs.
3. Accept one job and move status in order:
   - `PICKED_UP`
   - `OUT_FOR_DELIVERY`
   - `DELIVERED`
4. If you get `Illegal transition`, check current state first and follow valid sequence only.

## 5) Known guard behavior you should expect

- `403 Requires role ...` means wrong role token selected.
- `Order does not belong to user` means you opened order detail as non-owner customer.
- Review creation requires:
  - customer role token
  - delivered order
  - order belongs to customer
  - order restaurant matches review restaurant

## 6) Dev fallback auth (backward compatibility)

In `dev` profile, legacy headers are still accepted:
- `X-User-Id`
- `X-User-Role` (when required)

Production should use JWT bearer tokens only.

## 7) Quick regression checklist

- Auth: login/refresh/me/logout works
- Admin: approve/block/unblock works
- Customer: addresses + favorites + coupon checkout works
- Delivery: accept + full status chain works
- Timeline/events visible on valid customer-owned order


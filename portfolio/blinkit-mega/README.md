# BlinkIt Mega (Single Repo + Tags + Incremental Upgrades)

Monorepo path: `portfolio/blinkit-mega`

## Repository Tree

```text
blinkit-mega/
  backend/
    src/main/java/com/example/blinkit/
      config controller dto entity exception mapper repository security service util
    src/main/resources/
      application.yml
      application-postgres.yml
      db/migration/V1__core.sql
      db/migration/V2__pro_upgrade.sql
      db/migration/V3__pro_plusplus.sql
    src/test/java/com/example/blinkit/BlinkitFlowTests.java
    pom.xml
    Dockerfile
  frontend/
    src/app/core/{api,auth,guards,interceptors,models}
    src/app/features/{customer,admin,warehouse,rider}
    src/app/shared/ui
    angular.json package.json tailwind.config.js
  docker-compose.yml
```

## Milestone Delivery

### v1-core
- H2 profile works via `application.yml`
- Core auth/cart/checkout/orders/payment/admin endpoints active
- Polling order tracking supported through `/api/orders/{id}`

Run:
```bash
cd backend
mvn spring-boot:run

cd ../frontend
npm i
npm run start
```

Video Script (BN cues):
- "এখন আমরা v1-core এ signup/login করে catalog দেখাচ্ছি।"
- "Cart এ item add করে checkout flow end-to-end run হচ্ছে।"
- "Admin panel থেকে product আর inventory update করছি।"

[GIT TAG COMMANDS]
```bash
git status
git add .
git commit -m "v1-core: baseline quick-commerce MVP with H2"
git tag v1-core
git tag -n
```

### v2-pro (Upgrade Order: DB -> JWT -> Store/Inventory -> Reservation/Idempotency)
- PostgreSQL + Flyway via `docker-compose.yml` and `application-postgres.yml`
- JWT access + refresh with `/api/auth/refresh` and `/api/auth/logout`
- Pincode store resolve + single-store cart + store inventory enforcement
- `/api/checkout/preview` does 10-min reservation + release scheduler
- `/api/orders` requires `Idempotency-Key`

Run:
```bash
# docker compose up -d
docker-compose up -d
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

Video Script (BN cues):
- "এখন PostgreSQL + Flyway migration apply হচ্ছে।"
- "Access/Refresh JWT token flow live দেখাচ্ছি।"
- "Idempotency-Key ছাড়া order create blocked, duplicate create বন্ধ।"

[GIT TAG COMMANDS]
```bash
git status
git add .
git commit -m "v2-pro: postgres jwt store inventory reservation idempotency"
git tag v2-pro
git tag -n
```

### v3-pro-plusplus
- Ops: warehouse picking/missing/packed, admin refund approval, rider batching and status timeline
- Growth: coupons, wallet credit refunds, referrals seed
- Realtime: SSE order stream/admin stream + notifications + audit logs

Run:
```bash
docker-compose up -d
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=postgres

cd ../frontend
npm i
npm run start
```

Video Script (BN cues):
- "Warehouse mark-missing করলে partial refund pending তৈরি হচ্ছে।"
- "Admin approve করলেই wallet credit হচ্ছে।"
- "SSE stream দিয়ে order timeline real-time update পাচ্ছি।"

[GIT TAG COMMANDS]
```bash
git status
git add .
git commit -m "v3-pro-plusplus: ops growth realtime notifications audit"
git tag v3-pro-plusplus
git tag -n
```

## API Examples (curl)

```bash
# login
curl -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"user@blinkit.com","password":"user123"}'

# resolve store
curl "http://localhost:8080/api/stores/resolve?pincode=700091" -H "Authorization: Bearer <ACCESS>"

# add cart item
curl -X PUT http://localhost:8080/api/cart/items \
  -H "Authorization: Bearer <ACCESS>" -H 'Content-Type: application/json' \
  -d '{"productId":1,"storeId":1,"qty":2}'

# checkout preview
curl -X POST http://localhost:8080/api/checkout/preview \
  -H "Authorization: Bearer <ACCESS>" -H 'Content-Type: application/json' \
  -d '{"storeId":1}'

# place order (idempotent)
curl -X POST http://localhost:8080/api/orders \
  -H "Authorization: Bearer <ACCESS>" -H 'Idempotency-Key: order-abc-1' -H 'Content-Type: application/json' \
  -d '{"reservationId":1,"couponCode":"WELCOME50","walletAmount":10}'

# confirm payment
curl -X POST "http://localhost:8080/api/payments/1/confirm?result=SUCCESS" \
  -H "Authorization: Bearer <ACCESS>"

# warehouse packed
curl -X POST http://localhost:8080/api/warehouse/orders/1/packed \
  -H "Authorization: Bearer <WAREHOUSE_ACCESS>"

# rider status
curl -X POST http://localhost:8080/api/rider/orders/1/status \
  -H "Authorization: Bearer <RIDER_ACCESS>" -H 'Content-Type: application/json' \
  -d '{"eventType":"OUT_FOR_DELIVERY","message":"Reached sector 5"}'

# get seeded ID map + POST payload templates
curl http://localhost:8080/api/dev/seed-map
```

## Test Plan

Implemented in `backend/src/test/java/com/example/blinkit/BlinkitFlowTests.java`:
- reservation concurrency
- idempotency same key same payload
- coupon validation for min cart
- partial refund to wallet
- role access for cancel rules

Run tests:
```bash
cd backend
mvn test
```

## Notes
- MapStruct is used in `ProductMapper`.
- Global exception format is:
  - `{ "message": "...", "validationErrors": { "field": "error" } }`
- OpenAPI UI: `http://localhost:8080/swagger-ui/index.html`

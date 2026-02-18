[TITLE]
ZomatoX v1 (MVP) — Restaurant Discovery + Menu + Cart + Order + Mock Payment

[ROLE]
You are a senior full-stack architect and lead engineer. Implement ONLY v1 (MVP) of a Zomato-like platform called “ZomatoX”.
Output must be copy-paste ready and compile/run.

[SCOPE — v1 ONLY]
Build end-to-end flow:
Restaurant list → Restaurant details/menu → Add to cart → Checkout → Place order → Mock payment confirm → Orders list → Order detail
No JWT yet. No owner portal. No delivery partner portal. No coupons. No realtime.

[STACK]
Backend:
- Java 17, Spring Boot 3
- Spring Web, Validation, Spring Data JPA
- DB: H2 (in-memory) for v1
- OpenAPI/Swagger
- Actuator
- JUnit 5 tests

Frontend:
- Angular 17+ (standalone components OK)
- Tailwind CSS
- Basic route guards NOT required in v1
- State: simple RxJS service store (cart + auth state)
- No UI libraries

[DOMAIN RULES]
- Restaurant has many MenuItems
- Cart is per user (single active cart)
- Order is created from cart snapshot
- Stock: implement simple stock check per menu item (optional, but recommended). If stock not enough → reject.
- Payment is mock:
  - confirm SUCCESS marks order PAID
  - confirm FAIL keeps PAYMENT_FAILED
Order states (v1 simplified):
CREATED → PAYMENT_PENDING → PAID (or PAYMENT_FAILED)

[DATA MODEL — v1]
Backend entities/tables:
- User (minimal): id, name, email
- Restaurant: id, name, city, cuisineType, ratingAvg, deliveryTimeMin, imageUrl
- MenuItem: id, restaurantId, name, price, isVeg, available, stockQty
- Cart: id, userId, updatedAt
- CartItem: id, cartId, menuItemId, qty
- Address: id, userId, line1, city, pincode, phone
- Order: id, userId, restaurantId, status, itemTotal, deliveryFee, payableTotal, createdAt
- OrderItem: id, orderId, menuItemNameSnapshot, priceSnapshot, qty, lineTotal
- Payment: id, orderId, status, method, createdAt

Seed data required:
- 10 restaurants across 2 cities (e.g., Kolkata, Bengaluru)
- Each restaurant has 10 menu items (veg/non-veg mix)
- 2 users: user@zomatox.local, admin@zomatox.local (admin just for demo endpoints)
- some menu items with low stock to test stock rejection

[REST API CONTRACT — v1]
Users:
- GET /api/users (for demo)
- GET /api/users/{id}

Restaurants:
- GET /api/restaurants?city=&q=&cuisine=&sort=rating|time&page=
- GET /api/restaurants/{id}
- GET /api/restaurants/{id}/menu

Cart (assume userId passed as header X-User-Id for v1):
- GET /api/cart
- PUT /api/cart/items  (menuItemId, qty)  // qty=0 means remove
- DELETE /api/cart/items/{menuItemId}

Checkout/Orders:
- POST /api/orders (addressId)
  - creates order from cart snapshot
  - sets status PAYMENT_PENDING
- GET /api/orders
- GET /api/orders/{id}

Mock Payment:
- POST /api/payments/{orderId}/confirm?result=SUCCESS|FAIL
  - SUCCESS → order status PAID
  - FAIL → order status PAYMENT_FAILED

Admin (simple demo endpoints, protected by X-Admin-Key=dev):
- POST /api/admin/restaurants
- POST /api/admin/restaurants/{id}/menu-items
- PUT /api/admin/menu-items/{id} (update price/stock/available)

[BACKEND ARCHITECTURE]
Base package: com.example.zomatox
Structure:
- controller, dto, entity, repository, service, config, exception, util
Mapping: manual mapping in v1 is OK (MapStruct optional in v1).
Validation: annotate DTOs with javax/jakarta validation.
Global error response:
{ "message": "...", "validationErrors": { "field": "error" } }

Logging: @Slf4j in services.

Swagger: enable OpenAPI UI.

Tests (JUnit 5):
- CartServiceTest: add/update/remove items
- OrderServiceTest: create order from cart, stock check rejects if not enough
- PaymentServiceTest: confirm success updates order to PAID

[FRONTEND ARCHITECTURE]
Angular folder structure:
src/app/
  core/
    api.service.ts
    cart.store.ts (RxJS store)
    user-context.service.ts (stores X-User-Id for demo)
  features/
    restaurants/
      restaurant-list/
      restaurant-detail/
    cart/
    checkout/
    orders/
      order-list/
      order-detail/
  shared/
    ui/
      button, input, card, badge, toast (simple)
Tailwind layout: clean, responsive.

Frontend pages:
- /restaurants (list + search + filters + sort)
- /restaurants/:id (menu list, add to cart)
- /cart
- /checkout (select address, place order)
- /orders
- /orders/:id (show status)

Frontend integration rules:
- Every API call must include header X-User-Id from user-context service.
- Add a simple dropdown on top-right to switch user between seeded users.

[RUN INSTRUCTIONS — MUST OUTPUT]
Backend:
- mvn spring-boot:run
- Swagger URL

Frontend:
- npm i
- ng serve
- Tailwind setup steps included

Provide curl examples:
- list restaurants
- add cart item
- create order
- confirm payment SUCCESS
- view order

[VIDEO SCRIPT CUES — Bengali]
Provide short cues to read while coding:
- “এখন restaurant list API বানাচ্ছি”
- “এখন menu দেখিয়ে cart এ add করবো”
- “এখন order place করে PAYMENT_PENDING করলাম”
- “এখন mock payment confirm করে PAID করলাম”
- “এখন orders list আর details page বানালাম”

[OUTPUT FORMAT — MUST FOLLOW]
1) Provide repository tree (backend + frontend).
2) Provide full code for every file with exact paths.
3) Provide seed data implementation.
4) Provide tests.
5) Provide run steps + curl.
6) At the end output:
[GIT TAG COMMANDS]
git status
git add .
git commit -m "v1: ZomatoX MVP restaurants/menu/cart/order/mock-payment + tests"
git tag v1-zomatox-mvp
git tag -n

[QUALITY BAR]
- Must compile and run
- No broken placeholders
- Clean DTO validation + global error handler
- No copyrighted logos/assets; generic icons only

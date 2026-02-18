নিচে তোমার জন্য **Level-up “Blinkit-style” Full-Stack Assignment** (Instamart / quick commerce) দিলাম—আগের Classroom/Khatabook থেকে অনেক বেশি real-world: **cart, inventory, delivery slots, order lifecycle, rider assignment, refunds/cancellations, admin ops**—সব থাকবে।

---

# ✅ Level-Up Assignment: Build a “BlinkIt” Quick-Commerce Website

## 1) Product Goal

একটা **quick commerce** ওয়েব অ্যাপ বানাবে যেখানে:

* ইউজার 10–30 মিনিটে grocery/order করতে পারবে
* Live-ish cart + inventory validation
* Delivery slot / ETA
* Order placement, tracking, cancellation, refund
* Admin inventory & order ops
* Delivery partner (rider) assignment + status update

---

## 2) Roles & Permissions

### Roles

1. **CUSTOMER**

* browse, search, add to cart
* checkout, pay (mock), track
* cancel within policy

2. **ADMIN**

* products CRUD, pricing, stock, offers
* order manage, assign rider
* refunds/cancellations approve

3. **RIDER**

* see assigned orders
* update delivery status (picked, on the way, delivered)

> Auth: JWT recommended (Spring Security)
> Beginner-friendly alternative: token session (like previous)

---

## 3) Core User Flows (Must Implement)

### A) Customer Flow

1. Login/signup
2. Home page → categories + “Top picks”
3. Product listing + search + filters (price, brand)
4. Product details → add to cart
5. Cart page → quantity change, remove
6. Checkout:

   * address select/create
   * delivery slot
   * payment mock
7. Place order → show Order ID + ETA
8. Track order timeline
9. Cancel order (rules apply)

### B) Admin Flow

1. Add category/product
2. Update price/stock
3. View new orders
4. Assign rider
5. Approve cancellation/refund (policy based)
6. Mark out-of-stock & auto prevent checkout

### C) Rider Flow

1. Assigned orders list
2. Accept order
3. Status updates: PACKED → PICKED_UP → OUT_FOR_DELIVERY → DELIVERED

---

## 4) Order Status Lifecycle (Strict)

`CREATED → PAID → CONFIRMED → PACKING → PACKED → PICKED_UP → OUT_FOR_DELIVERY → DELIVERED`

Cancellation rules:

* Customer can cancel only until `PACKING`
* After `PACKED`, only admin can cancel (refund policy)

---

## 5) Features Checklist (Level-Up)

### Customer UI

* Category chips + carousel section
* Search with debounce
* Product card with:

  * price, mrp, discount badge
  * “Add + / –” quantity stepper
  * stock indicator
* Cart summary:

  * item total, delivery fee, discounts
* Address book
* Orders page + order details + timeline

### Admin UI

* Inventory dashboard:

  * low-stock list
* Product CRUD:

  * category, brand, unit (1kg/500g), images (URL), pricing
* Orders ops:

  * assign rider
  * move status forward
  * refunds/cancellations queue

### Rider UI

* minimal mobile-friendly screen
* one-tap status update buttons

### Real-World Constraints

* **Atomic stock decrement** on order place
* **Idempotent checkout** (avoid double order)
* Server-side validation always (frontend can’t be trusted)
* Pagination for products/orders

---

## 6) Tech Stack (Recommended)

### Backend (Spring Boot)

* Java 17
* Spring Web + Validation + Data JPA
* Spring Security (JWT)
* DB: PostgreSQL (recommended), H2 ok for demo
* Redis (optional) for cart/session (bonus)
* OpenAPI/Swagger (bonus)

### Frontend (Angular + Tailwind)

* Angular 17+
* Tailwind only
* RxJS + services (or signals)
* Guards for role routing
* Component structure: feature-based

---

## 7) Data Model (Minimum)

### Tables / Entities

1. `users` (role, phone/email, password hash)
2. `categories`
3. `products` (categoryId, brand, name, price, mrp, unit, images, active)
4. `inventory` (productId, stockQty, reservedQty)
5. `carts` (userId) *(optional if you store in localStorage)*
6. `cart_items`
7. `addresses`
8. `orders`
9. `order_items`
10. `payments` (mock)
11. `delivery_partners`
12. `delivery_assignments`
13. `audit_logs` (bonus)

---

## 8) APIs (Minimum Required)

### Auth

* `POST /api/auth/signup`
* `POST /api/auth/login`
* `GET /api/auth/me`

### Catalog

* `GET /api/categories`
* `GET /api/products?categoryId=&q=&min=&max=&page=`
* `GET /api/products/{id}`

### Cart

* `POST /api/cart/items` (add/update qty)
* `GET /api/cart`
* `DELETE /api/cart/items/{productId}`

### Checkout & Orders

* `POST /api/checkout/preview`
* `POST /api/orders` (place order)
* `GET /api/orders` (my orders)
* `GET /api/orders/{id}`
* `POST /api/orders/{id}/cancel`

### Admin

* `POST /api/admin/products`
* `PUT /api/admin/products/{id}`
* `PUT /api/admin/inventory/{productId}`
* `GET /api/admin/orders?status=`
* `POST /api/admin/orders/{id}/assign-rider`
* `POST /api/admin/orders/{id}/advance-status`

### Rider

* `GET /api/rider/orders`
* `POST /api/rider/orders/{id}/update-status`

---

## 9) Critical Business Rules (Must)

1. **Stock check on add to cart AND checkout**
2. Place order = **transactional stock decrement**
3. If stock 부족 → return error “Out of stock”
4. If user double clicks pay → prevent duplicate order (idempotency key)
5. Cancellation allowed only before PACKING by customer

---

## 10) UI Pages (Must)

### Customer

* `/home`
* `/category/:id`
* `/product/:id`
* `/cart`
* `/checkout`
* `/orders`
* `/orders/:id`

### Admin

* `/admin/products`
* `/admin/inventory`
* `/admin/orders`

### Rider

* `/rider/orders`

---

## 11) Seed Data (Mandatory)

Create seed data:

* categories: Fruits, Snacks, Dairy, Cleaning
* 30 products
* 1 Admin: [admin@blinkit.local](mailto:admin@blinkit.local) / Admin
* 1 Rider: [rider@blinkit.local](mailto:rider@blinkit.local) / rider
* 1 Customer: [user@blinkit.local](mailto:user@blinkit.local) / user
* inventory stock values (some low stock)

---

## 12) Testing Requirements (Level-Up)

### Backend

* unit tests: stock reservation logic, order placement
* integration tests: place order success + out-of-stock + idempotency

### Frontend

* at least 1 component test or e2e scenario (bonus)

---

# 🎥 YouTube Script Style (You can read while coding)

এই lines গুলো ভিডিওতে বলবে:

* “এখন আমার backend ta baniye ni—catalog entity বানাচ্ছি”
* “এখন cart update করলে server-side stock check হবে”
* “এখন checkout place order এ transactional stock decrement করছি”
* “এখন admin dashboard থেকে rider assign করবো”
* “এখন rider status update করলে customer timeline update হবে”

---

## Bonus “Level-Up++” Ideas (Optional)

* WebSocket for live order status
* promo codes
* dynamic delivery fee
* “Frequently bought together”
* search suggestions

---

তুমি চাইলে আমি এটাকে **একদম production-like** করে দেবো:
✅ **Complete copy-paste code** (Spring Boot JWT + PostgreSQL + Angular Tailwind UI)
✅ Exact folder structure + commands + seed data
✅ Bengali voiceover script “line-by-line”

বল: তুমি কোনটা চাও?

1. **Simpler** (token session, H2, localStorage cart)
2. **Pro** (JWT Security, PostgreSQL, transactional stock + idempotency)

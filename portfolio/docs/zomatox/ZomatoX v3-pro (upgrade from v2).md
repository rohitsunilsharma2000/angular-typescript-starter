[TITLE]
ZomatoX v3-pro Upgrade (from v2) — JWT+RBAC + Coupons/Pricing + Address Book + Favorites/Recents + Admin Ops

[ROLE]
You are a senior full-stack architect and lead engineer. Upgrade an existing v2 codebase of “ZomatoX” to v3-pro.
v2 already has: PostgreSQL+Flyway, roles (CUSTOMER/OWNER/DELIVERY_PARTNER), owner order processing, delivery acceptance flow, reviews, order events timeline.
You MUST keep v1 and v2 endpoints working where applicable (backward compatible) while adding v3-pro features.
Output must be copy-paste ready and compile/run.

[UPGRADE GOAL — v3-pro]
Add production-grade auth & pro features:
1) Spring Security JWT (access+refresh) + RBAC guards for all APIs
2) Replace dev headers (X-User-Id / X-User-Role) with JWT auth, but keep them OPTIONAL for local dev profile only.
3) Coupons + pricing engine (restaurant-specific + global coupons)
4) Address book (multiple addresses per customer)
5) Favorites + Recently Viewed restaurants
6) Admin module: restaurant onboarding approval + moderation (block/unblock restaurant/menu items), basic review moderation
7) Observability: Actuator + Micrometer metrics + structured logs
8) OpenAPI security definitions for Swagger

[STACK — v3-pro]
Backend:
- Java 17, Spring Boot 3
- Spring Web, Validation, Spring Data JPA
- Spring Security 6 + JWT (access+refresh)
- PostgreSQL + Flyway (keep)
- OpenAPI/Swagger + Actuator + Micrometer
- JUnit 5 tests (include security tests)

Frontend:
- Angular 17 + Tailwind
- Role-based routing + guards (based on JWT claims)
- HttpInterceptor attaches access token
- Refresh token flow supported
- State: RxJS store (consistent)

[SECURITY REQUIREMENTS — MUST FOLLOW]
- Access token short-lived (e.g., 10-15 min)
- Refresh token stored server-side in refresh_tokens table
- Endpoints:
  - POST /api/auth/signup
  - POST /api/auth/login  -> returns accessToken + refreshToken + userProfile
  - POST /api/auth/refresh -> returns new accessToken (and optionally rotates refresh)
  - POST /api/auth/logout -> invalidates refresh token
  - GET /api/auth/me
- RBAC:
  - CUSTOMER: browse/menu/cart/checkout/orders/reviews/favorites/addresses
  - OWNER: manage own restaurants/menu, process own orders
  - DELIVERY_PARTNER: accept jobs, update delivery status
  - ADMIN: approve restaurants, moderation, review moderation, view audits/metrics endpoints (optional)
- Keep v2 header-based auth ONLY in "dev" profile for easier testing, disabled in prod profile.

[FEATURES TO ADD — v3-pro]
A) Coupons + Pricing Engine
- Coupons:
  - global coupons (platform) + restaurant-specific coupons
  - types: PERCENT, FLAT
  - constraints: minOrderAmount, maxDiscountCap, validFrom, validTo, usageLimitPerUser
  - optional: applicableCuisineType OR restaurantId
- Pricing breakdown:
  - itemTotal, restaurantPackagingFee(optional), platformFee(optional), deliveryFee, discount, payableTotal
- API:
  - POST /api/checkout/apply-coupon  (couponCode, restaurantId) -> returns updated pricing preview
  - Coupon applied must be stored on order at placement (snapshot)

B) Address Book (Customer)
- CRUD addresses:
  - POST /api/addresses
  - GET /api/addresses
  - PUT /api/addresses/{id}
  - DELETE /api/addresses/{id}
- Checkout uses selected addressId (still supports v1/v2 flow but now address book is the primary source)

C) Favorites + Recently Viewed (Customer)
- Favorites:
  - POST /api/favorites/restaurants/{restaurantId}
  - DELETE /api/favorites/restaurants/{restaurantId}
  - GET /api/favorites/restaurants
- Recently viewed:
  - POST /api/recent/restaurants/{restaurantId} (called when user opens details)
  - GET /api/recent/restaurants?limit=10

D) Admin Ops
- Restaurant onboarding approval:
  - Restaurants created by OWNER start as PENDING_APPROVAL
  - Admin can APPROVE/REJECT
- Moderation:
  - block/unblock restaurant
  - block/unblock menu item
- Review moderation:
  - admin can hide/unhide abusive reviews
APIs:
- GET /api/admin/restaurants?status=PENDING_APPROVAL|APPROVED|REJECTED
- POST /api/admin/restaurants/{id}/approve
- POST /api/admin/restaurants/{id}/reject
- POST /api/admin/restaurants/{id}/block
- POST /api/admin/restaurants/{id}/unblock
- POST /api/admin/menu-items/{id}/block
- POST /api/admin/menu-items/{id}/unblock
- GET /api/admin/reviews?status=VISIBLE|HIDDEN
- POST /api/admin/reviews/{id}/hide
- POST /api/admin/reviews/{id}/unhide

E) Cart/Checkout Updates
- Cart should prevent adding items from BLOCKED restaurant or BLOCKED menu item
- Checkout must fail if restaurant is blocked or not approved
- Apply coupon must validate restaurant approval and coupon validity

[DATA MODEL — v3-pro ADDITIONS/CHANGES]
Add/modify tables via Flyway V3 migration:
- users: add password_hash, is_active, role already exists
- refresh_tokens: (if not in v2) id, user_id, token_hash, expires_at, revoked_at, created_at
- restaurants: add approval_status (PENDING_APPROVAL/APPROVED/REJECTED), is_blocked, blocked_reason
- menu_items: add is_blocked
- coupons: id, code, type(PERCENT/FLAT), value, min_order, max_cap, valid_from, valid_to, active, restaurant_id nullable, usage_limit_per_user
- coupon_redemptions: id, coupon_id, user_id, order_id, redeemed_at
- addresses: id, user_id, fields
- favorites_restaurants: user_id, restaurant_id, created_at (unique per user+restaurant)
- recent_restaurants: user_id, restaurant_id, viewed_at (keep only last N in service)
- reviews: add status (VISIBLE/HIDDEN)
Optional:
- audit_logs (basic in v3-pro if time): admin actions tracked

[BACKEND ARCHITECTURE — v3-pro]
Base package: com.example.zomatox
- Add security package:
  security/jwt, security/config, security/filter, security/auth
- Add @PreAuthorize rules or method-level checks in services
- Global error handler stays:
  { "message": "...", "validationErrors": { "field": "error" } }
- Continue order_events on every status change
- Add coupon snapshot fields on orders:
  appliedCouponCode, discountAmount, pricingJsonSnapshot(optional)

[FRONTEND — v3-pro]
- Add Auth module:
  - login page
  - store tokens (in memory + localStorage)
  - refresh flow on 401
- Replace X-User-Id usage with JWT claims in prod.
- Role-based routes:
  - /customer/*, /owner/*, /delivery/*, /admin/*
- Customer:
  - address book page
  - favorites page
  - recent page (optional)
  - checkout supports coupon code apply & pricing breakdown
- Owner:
  - restaurant create now shows status badge (PENDING/APPROVED/REJECTED)
- Admin:
  - approval queue
  - moderation pages for restaurants/menu items
  - review moderation page

[TESTS — v3-pro MINIMUM]
1) JwtAuthIntegrationTest:
   - login returns tokens
   - protected endpoint requires access token
2) RefreshFlowTest:
   - refresh returns new access token
   - logout revokes refresh token
3) CouponValidationTest:
   - expired coupon rejected
   - min order rule enforced
   - restaurant-specific coupon only works for that restaurant
4) AdminApprovalTest:
   - owner creates restaurant -> PENDING
   - admin approves -> APPROVED
   - blocked restaurant cannot accept orders
5) FavoritesUniquenessTest:
   - cannot favorite same restaurant twice

[RUN INSTRUCTIONS — MUST OUTPUT]
- docker-compose up -d (postgres)
- mvn spring-boot:run
- frontend: npm i && ng serve
- Swagger: demonstrate JWT auth in Swagger UI

Provide curl examples:
- signup/login
- refresh
- admin approve restaurant
- apply coupon
- checkout place order

[OUTPUT FORMAT — MUST FOLLOW]
1) Provide an "Upgrade Diff Checklist" (what to add/modify from v2)
2) Provide updated repository tree
3) Provide full code for new/changed files
4) Provide Flyway migration V3 SQL
5) Provide updated seed data:
   - users: customer/owner/delivery/admin with passwords
   - some restaurants pending approval
   - some coupons
6) Provide updated Angular pages/services/interceptors/guards
7) Provide tests + run steps + curl
8) End with:
[GIT TAG COMMANDS]
git status
git add .
git commit -m "v3-pro: JWT+refresh RBAC, coupons/pricing, addresses, favorites/recents, admin approvals/moderation"
git tag v3-zomatox-pro
git tag -n

[QUALITY BAR]
- Must compile and run on PostgreSQL with Flyway
- No placeholders that break compilation
- v2 flows still work (with JWT in prod profile; dev headers allowed only in dev profile)
- Clean validation + error responses
- No copyrighted assets; generic icons only

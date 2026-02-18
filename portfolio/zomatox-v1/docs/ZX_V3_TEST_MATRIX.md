# ZX v3 Test Matrix (Runnable)

Base URL:
- `API=http://localhost:8080`

## 0) Token bootstrap

```bash
# CUSTOMER
CUST_LOGIN=$(curl -s "$API/api/auth/login" -H 'Content-Type: application/json' -d '{"email":"customer@zomatox.local","password":"customer123"}')
CUST_AT=$(echo "$CUST_LOGIN" | jq -r '.accessToken')
CUST_RT=$(echo "$CUST_LOGIN" | jq -r '.refreshToken')

# OWNER
OWN_LOGIN=$(curl -s "$API/api/auth/login" -H 'Content-Type: application/json' -d '{"email":"owner@zomatox.local","password":"owner123"}')
OWN_AT=$(echo "$OWN_LOGIN" | jq -r '.accessToken')

# DELIVERY
DEL_LOGIN=$(curl -s "$API/api/auth/login" -H 'Content-Type: application/json' -d '{"email":"delivery@zomatox.local","password":"delivery123"}')
DEL_AT=$(echo "$DEL_LOGIN" | jq -r '.accessToken')

# ADMIN
ADM_LOGIN=$(curl -s "$API/api/auth/login" -H 'Content-Type: application/json' -d '{"email":"admin@zomatox.local","password":"admin123"}')
ADM_AT=$(echo "$ADM_LOGIN" | jq -r '.accessToken')
```

## 1) Security + RBAC

| ID | Test | Curl | Expected |
|---|---|---|---|
| SEC-001 | Login success | `curl -s "$API/api/auth/login" -H 'Content-Type: application/json' -d '{"email":"customer@zomatox.local","password":"customer123"}'` | `200`, access+refresh+profile |
| SEC-002 | Login fail | `curl -i "$API/api/auth/login" -H 'Content-Type: application/json' -d '{"email":"customer@zomatox.local","password":"wrong"}'` | `401 Invalid credentials` |
| SEC-003 | Protected API w/o token | `curl -i "$API/api/addresses"` | `401/403` |
| SEC-004 | Refresh | `curl -s "$API/api/auth/refresh" -H 'Content-Type: application/json' -d "{\"refreshToken\":\"$CUST_RT\"}"` | new access token |
| SEC-005 | Logout + refresh blocked | `curl -s -X POST "$API/api/auth/logout" -H 'Content-Type: application/json' -d "{\"refreshToken\":\"$CUST_RT\"}" && curl -i -s "$API/api/auth/refresh" -H 'Content-Type: application/json' -d "{\"refreshToken\":\"$CUST_RT\"}"` | second call `401` |
| SEC-006 | CUSTOMER blocked on OWNER route | `curl -i -X PATCH "$API/api/owner/orders/1/status?next=PREPARING" -H "Authorization: Bearer $CUST_AT"` | `403` |
| SEC-007 | DELIVERY blocked on ADMIN | `curl -i "$API/api/admin/restaurants?status=PENDING_APPROVAL" -H "Authorization: Bearer $DEL_AT"` | `403` |
| SEC-008 | ADMIN allowed on ADMIN | `curl -i "$API/api/admin/restaurants?status=PENDING_APPROVAL" -H "Authorization: Bearer $ADM_AT"` | `200` |

## 2) Dev header fallback (dev only)

| ID | Test | Curl | Expected |
|---|---|---|---|
| DEV-001 | Header auth in dev | `curl -i "$API/api/addresses" -H "X-User-Id: 1"` | works in `dev` profile |
| DEV-002 | Header auth in prod blocked | `SPRING_PROFILES_ACTIVE=prod` then same call | `401/403` |

## 3) Coupons + Pricing

| ID | Test | Curl | Expected |
|---|---|---|---|
| CP-001 | Global coupon preview | `curl -s "$API/api/checkout/apply-coupon" -H "Authorization: Bearer $CUST_AT" -H 'Content-Type: application/json' -d '{"restaurantId":1,"couponCode":"WELCOME20"}'` | pricing with discount |
| CP-002 | Restaurant coupon wrong restaurant | `curl -i -s "$API/api/checkout/apply-coupon" -H "Authorization: Bearer $CUST_AT" -H 'Content-Type: application/json' -d '{"restaurantId":2,"couponCode":"RESTO50"}'` | rejected |
| CP-003 | Min order enforced | same endpoint with low-value restaurant/cart context | rejected |

## 4) Address Book

| ID | Test | Curl | Expected |
|---|---|---|---|
| ADDR-001 | Create | `curl -s -X POST "$API/api/addresses" -H "Authorization: Bearer $CUST_AT" -H 'Content-Type: application/json' -d '{"line1":"Test Lane 1","city":"Kolkata","pincode":"700001","phone":"9000000001"}'` | created |
| ADDR-002 | List own | `curl -s "$API/api/addresses" -H "Authorization: Bearer $CUST_AT"` | only customer addresses |
| ADDR-003 | Update own | `curl -s -X PUT "$API/api/addresses/<ADDR_ID>" -H "Authorization: Bearer $CUST_AT" -H 'Content-Type: application/json' -d '{"line1":"Updated Lane","city":"Kolkata","pincode":"700001","phone":"9000000001"}'` | updated |
| ADDR-004 | Cross-user blocked | use owner address id with customer token | `403/404` |

## 5) Favorites + Recent

| ID | Test | Curl | Expected |
|---|---|---|---|
| FAV-001 | Add favorite | `curl -i -X POST "$API/api/favorites/restaurants/1" -H "Authorization: Bearer $CUST_AT"` | success |
| FAV-002 | Duplicate blocked | repeat same call | safe reject |
| FAV-003 | List favorites | `curl -s "$API/api/favorites/restaurants" -H "Authorization: Bearer $CUST_AT"` | includes restaurant 1 |
| FAV-004 | Remove favorite | `curl -i -X DELETE "$API/api/favorites/restaurants/1" -H "Authorization: Bearer $CUST_AT"` | removed |
| REC-001 | Add recent | `curl -i -X POST "$API/api/recent/restaurants/1" -H "Authorization: Bearer $CUST_AT"` | success |
| REC-002 | List recent | `curl -s "$API/api/recent/restaurants?limit=10" -H "Authorization: Bearer $CUST_AT"` | ordered latest-first |

## 6) Admin approvals + moderation

| ID | Test | Curl | Expected |
|---|---|---|---|
| ADM-001 | Pending list | `curl -s "$API/api/admin/restaurants?status=PENDING_APPROVAL" -H "Authorization: Bearer $ADM_AT"` | pending restaurants |
| ADM-002 | Approve | `curl -i -X POST "$API/api/admin/restaurants/<RID>/approve" -H "Authorization: Bearer $ADM_AT"` | status approved |
| ADM-003 | Reject | `curl -i -X POST "$API/api/admin/restaurants/<RID>/reject" -H "Authorization: Bearer $ADM_AT"` | status rejected |
| ADM-004 | Block/unblock restaurant | `curl -i -X POST "$API/api/admin/restaurants/<RID>/block?reason=policy" -H "Authorization: Bearer $ADM_AT"` then unblock | toggles blocked |
| ADM-005 | Block/unblock menu item | `curl -i -X POST "$API/api/admin/menu-items/<MID>/block" -H "Authorization: Bearer $ADM_AT"` then unblock | toggles blocked |
| ADM-006 | Review hide/unhide | `curl -i -X POST "$API/api/admin/reviews/<REVIEW_ID>/hide" -H "Authorization: Bearer $ADM_AT"` then unhide | status changes |

## 7) Delivery + timeline

| ID | Test | Curl | Expected |
|---|---|---|---|
| DL-001 | Available jobs | `curl -s "$API/api/delivery/jobs?status=AVAILABLE" -H "Authorization: Bearer $DEL_AT"` | shows READY_FOR_PICKUP |
| DL-002 | Accept | `curl -i -X POST "$API/api/delivery/orders/<OID>/accept" -H "Authorization: Bearer $DEL_AT"` | assigned |
| DL-003 | Status flow | `curl -i -X PATCH "$API/api/delivery/orders/<OID>/status?next=PICKED_UP" -H "Authorization: Bearer $DEL_AT"` then `OUT_FOR_DELIVERY` then `DELIVERED` | all valid in order |
| TL-001 | Timeline visible to owner customer | `curl -s "$API/api/orders/<OID>" -H "Authorization: Bearer $CUST_AT"` for own order | includes events |

## 8) Observability + Swagger

| ID | Test | Curl/Steps | Expected |
|---|---|---|---|
| OBS-001 | Health | `curl -s "$API/actuator/health"` | UP |
| OBS-002 | Metrics | `curl -s "$API/actuator/prometheus" \| head` | metrics text |
| OAS-001 | Swagger bearer | Open `http://localhost:8080/swagger-ui/index.html` | bearer auth visible |
| OAS-002 | Swagger authorized call | Click Authorize, paste token, call protected API | success |

## 9) UI test map (quick)

- Customer:
  - `/customer/addresses`
  - `/customer/favorites`
  - `/checkout` (coupon + pricing)
- Owner:
  - `/owner/*` order handling
- Delivery:
  - `/delivery/jobs`, `/delivery/orders/:id`
- Admin:
  - `/admin/restaurants`, `/admin/reviews`

## 10) UI testing matrix (table)

| ID | Role | Route | Precondition | UI Actions | Expected |
|---|---|---|---|---|---|
| ZX-UI-001 | All | `/login` | App running | Login with each seeded user | Redirect to role-allowed pages, token stored |
| ZX-UI-002 | Unauthenticated | `/customer/addresses` | Logged out | Direct URL open | Redirect to login, no crash |
| ZX-UI-003 | Customer | `/customer/addresses` | Customer logged in | Add address from form | Address appears in list |
| ZX-UI-004 | Customer | `/customer/addresses` | Existing address | Edit then save | Updated values shown |
| ZX-UI-005 | Customer | `/customer/addresses` | Existing address | Delete address | Entry removed from list |
| ZX-UI-006 | Customer | `/restaurants/:id` | Customer logged in | Open details page | Restaurant details load; recent-view tracked |
| ZX-UI-007 | Customer | `/restaurants/:id` | Details page open | Click `Add to Favorites` | Success feedback; restaurant appears in favorites |
| ZX-UI-008 | Customer | `/customer/favorites` | Favorite exists | Open page | Favorited restaurants listed |
| ZX-UI-009 | Customer | `/checkout` | Cart has valid items | Enter coupon, click Apply | Pricing breakdown updates with discount/total |
| ZX-UI-010 | Customer | `/checkout` | Address exists, coupon optional | Place order | Order created; navigation to orders/detail works |
| ZX-UI-011 | Owner | `/owner/orders` | Owner logged in | Process order statuses in valid sequence | Each transition succeeds; invalid transition blocked |
| ZX-UI-012 | Delivery | `/delivery/jobs` | Delivery logged in, ready job exists | Open jobs page | AVAILABLE jobs shown |
| ZX-UI-013 | Delivery | `/delivery/jobs` | AVAILABLE job exists | Click Accept | Job moves to ASSIGNED for current delivery user |
| ZX-UI-014 | Delivery | `/delivery/orders/:id` | Assigned order exists | Set `PICKED_UP` -> `OUT_FOR_DELIVERY` -> `DELIVERED` | All transitions succeed in order |
| ZX-UI-015 | Customer | `/orders/:id` | Customer owns transitioned order | Open order detail | Timeline shows payment/owner/delivery events |
| ZX-UI-016 | Customer | `/restaurants/:id` | Customer has DELIVERED own order for that restaurant | Submit review with `orderId`, rating 1-5 | Review created and listed |
| ZX-UI-017 | Customer | `/restaurants/:id` | Same order already reviewed | Submit second review same order | Backend rejection shown safely in UI |
| ZX-UI-018 | Non-owner | `/owner/orders` actions | Logged as customer/delivery | Try owner action buttons | Forbidden handled; no UI corruption |
| ZX-UI-019 | Non-delivery | Delivery update actions | Logged as owner/customer | Try delivery status actions | Forbidden handled; no UI corruption |
| ZX-UI-020 | Admin | `/admin/restaurants` | Admin logged in | Approve/reject pending restaurant | Status updates immediately in UI |
| ZX-UI-021 | Admin | `/admin/restaurants` | Restaurant exists | Block/unblock restaurant | Block state toggles and reflected in UI |
| ZX-UI-022 | Admin | `/admin/reviews` | Reviews exist | Hide/unhide review | Review status toggles in list |
| ZX-UI-023 | All | Header/nav dropdown | Logged in as each role | Open dropdown + observe nav options | Only allowed role options are visible |
| ZX-UI-024 | Regression | Full cycle | Complete one customer->owner->delivery flow | Refresh app and revisit pages | Data persists and routes remain functional |






## 11) Coupons + Pricing matrix (explicit)

| ID | Layer | Scenario | Precondition | Action | Expected |
|---|---|---|---|---|---|
| ZX-CP-001 | UI | Apply global coupon | Customer logged in, valid cart | On `/checkout`, enter `WELCOME20`, click Apply | Discount shown; payable total reduced |
| ZX-CP-002 | UI | Apply restaurant coupon (valid) | Cart restaurant matches coupon restaurant | Enter `RESTO50` and Apply | Flat discount applied within cap rules |
| ZX-CP-003 | UI | Restaurant coupon on wrong restaurant | Cart from different restaurant | Apply `RESTO50` | Error shown safely, no crash |
| ZX-CP-004 | UI | Min order not met | Cart total below coupon min | Apply coupon | Validation error shown |
| ZX-CP-005 | UI | Expired/inactive coupon | Use expired/inactive code | Apply coupon | Validation error shown |
| ZX-CP-006 | UI | Place order with applied coupon | Coupon successfully applied | Click Place Order | Order created with coupon snapshot/discount |
| ZX-CP-007 | API | Pricing preview endpoint | Customer JWT available | `POST /api/checkout/apply-coupon` | Returns `itemTotal`, fees, `discount`, `payableTotal` |
| ZX-CP-008 | API | Coupon persisted on order | Order placed with coupon | Fetch order detail | `appliedCouponCode` and `discountAmount` populated |

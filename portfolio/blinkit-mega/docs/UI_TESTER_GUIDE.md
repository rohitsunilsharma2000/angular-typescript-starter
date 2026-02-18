# BlinkIt Mega UI Tester Guide

## 1. Test Environment Setup

### Backend
```bash
cd /Users/meghnadsaha/IdeaProjects/angular-typescript-starter/portfolio/blinkit-mega
docker compose up -d postgres

cd backend
JAVA_HOME=$(/usr/libexec/java_home -v 17) PATH="$JAVA_HOME/bin:$PATH" mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

### Frontend
```bash
cd /Users/meghnadsaha/IdeaProjects/angular-typescript-starter/portfolio/blinkit-mega/frontend
npm run start
```

### URLs
- UI: `http://localhost:4200/`
- Backend Swagger: `http://localhost:8080/swagger-ui/index.html`
- Seed map API: `http://localhost:8080/api/dev/seed-map`

## 2. Seed Users (Quick Login Buttons Available on Home)

- CUSTOMER: `user@blinkit.com / user123`
- ADMIN: `admin@blinkit.com / admin123`
- RIDER: `rider@blinkit.com / rider123`
- WAREHOUSE_STAFF: `warehouse@blinkit.com / warehouse123`

## 3. Pre-Test Data Check

Run:
```bash
curl http://localhost:8080/api/dev/seed-map
```
Use these IDs during tests:
- `storeId`
- `pickingOrderId`
- `deliveredOrderId`
- `refundId`
- `riderUserId`
- `sampleOrderItemIdForMissing`

## 4. Customer UI Test Cases

### C1: Login + Store Resolve
1. Open `/`
2. Click `Login Customer`
3. Enter pincode `700091` and click `Resolve`

Expected:
- Login success message
- Store ID resolved and visible

### C2: Product -> Cart -> Checkout Preview
1. Open `/customer/products`
2. Add 1-2 products
3. Open `/customer/cart`
4. Open `/customer/checkout`
5. Click `Checkout Preview`

Expected:
- Reservation created
- Reservation ID and expiry shown

### C3: Place Order + Payment
1. On checkout page click `Place Order`
2. In order detail click `Start Payment`
3. Click `Confirm Success`

Expected:
- Order status changes through flow
- Timeline logs update (poll/SSE messages)

### C4: Orders Listing
1. Open `/customer/orders`
2. Open any order via `Track`

Expected:
- Order list visible
- Order details + items visible

## 5. Admin UI Test Cases

### A1: Product Create/Update
1. Open `/admin/products`
2. Submit create form
3. Set `editId` and submit update

Expected:
- Success message shown
- Product table refreshes

### A2: Inventory Update
1. Open `/admin/inventory`
2. Enter `storeId`, `productId`, `stock`
3. Click `Update`

Expected:
- `Inventory updated` message

### A3: Orders Ops (Assign Rider + Advance Status)
1. Open `/admin/orders`
2. Click `Load Seed IDs`
3. Click `Assign Rider`
4. Set status and click `Advance Status`

Expected:
- Action success messages
- Table reflects updated status

### A4: Refund Approvals
1. Open `/admin/refunds`
2. Click `Use Seed Refund ID`
3. Click `Approve`

Expected:
- Refund approved message
- Pending list updates

### A5: Audit Logs
1. Open `/admin/audit`
2. Click `Refresh`

Expected:
- Audit entries visible (inventory/product/refund/order actions)

## 6. Warehouse UI Test Cases

### W1: Picking Queue
1. Open `/warehouse/picking-queue`
2. Click `Open Picklist` on any order

Expected:
- Redirect to picklist with selected order

### W2: Mark Missing + Mark Packed
1. On `/warehouse/picklist`, load order
2. Use `orderItemId` + missing qty and click `Mark Missing`
3. Click `Mark Packed`

Expected:
- Missing action success
- Packed action success

## 7. Rider UI Test Cases

### R1: Active Batches
1. Open `/rider/batches`
2. Click `Refresh`

Expected:
- Active batch list visible (seeded)

### R2: Accept + Status Updates
1. Click `Go to Seeded Order Status`
2. Click `Accept Order`
3. Send statuses: `PICKED_UP`, `OUT_FOR_DELIVERY`, `DELIVERED`

Expected:
- Success message for each action
- Customer order detail timeline updates

## 8. Basic Negative Tests

1. Login as CUSTOMER and open `/admin/orders`
- Expected: blocked/redirected by role guard

2. Trigger checkout with empty cart
- Expected: error shown

3. Wrong IDs in admin/warehouse/rider forms
- Expected: backend error message surfaced in UI

## 9. UI Regression Checklist

- Navigation header links work
- No blank page/loader loop
- Forms do not freeze after submit
- Error messages are readable
- Tables load after refresh

## 10. Bug Report Template

```text
Title:
Role:
Page URL:
Steps to Reproduce:
Expected Result:
Actual Result:
Console Error (if any):
API Error Response (if any):
Screenshot/Video:
Build/Commit:
```

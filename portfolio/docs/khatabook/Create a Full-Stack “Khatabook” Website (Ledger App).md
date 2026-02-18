নিচে তোমার জন্য **আরেকটা নতুন assignment prompt** দিলাম—আগেরটার মতোই কিন্তু এবার থিম হবে **“Khatabook (খাতাবই)”**। এটা তুমি নিজের practice + YouTube সিরিজ দুটোতেই ব্যবহার করতে পারবে।

---

# ✅ Assignment: Create a Full-Stack “Khatabook” Website (Ledger App)

## 1) Project Goal (কি বানাবে)

একটা **Khatabook ওয়েবসাইট** বানাবে যেখানে ছোট দোকান/টিউশন/ফ্রিল্যান্স কাজের জন্য:

* Customer/Supplier add করা যাবে
* Credit (বাকি) / Debit (জমা) entry করা যাবে
* Running balance auto হিসাব হবে
* Due list, search, filter, export—সব থাকবে

---

## 2) Roles & Access (Role-based)

### Roles

1. **OWNER (Admin)**

   * সব access
   * customer/supplier CRUD
   * ledger entry add/edit/delete
   * reports/export
2. **STAFF**

   * customer view
   * entry add (কিন্তু delete না)
   * limited reports
3. **VIEWER**

   * শুধু read-only (ledger view)

> ✅ Auth: JWT (recommended) বা session token (beginner friendly)

---

## 3) Core Modules (Features)

### A) Party Management (Customer/Supplier)

**Party = যাকে টাকা দিতে হবে বা যার কাছে টাকা পাওনা**

* Create Party: name, phone, type (CUSTOMER/SUPPLIER), address, notes
* Party list: search by name/phone, filter by type
* Party detail page:

  * total credit, total debit, current balance
  * recent transactions

### B) Ledger Entries (Transactions)

Each entry:

* partyId
* date
* type: **CREDIT** (বাকি/পাওনা) OR **DEBIT** (জমা/পরিশোধ)
* amount
* description
* paymentMode: CASH/UPI/BANK
* attachment (optional: invoice photo)

Rules:

* amount must be > 0
* date cannot be future (optional)
* staff can add but not delete
* balance calculation:

  * CUSTOMER: CREDIT increases due, DEBIT decreases due
  * SUPPLIER: CREDIT means you owe them more, DEBIT means you paid them

### C) Dashboard

Top cards:

* Total Receivable (Customer due)
* Total Payable (Supplier due)
* Today’s Transactions count
* Net Balance

Charts (optional):

* last 7 days collection vs due created

### D) Reports

* Party-wise statement (date range)
* Daily cashbook summary
* Export CSV/PDF

### E) Notifications (Optional)

* Reminder: party due above X
* due older than Y days

---

## 4) Tech Stack Constraints (Must follow)

### Backend (Spring Boot)

* Java 17
* Spring Web, Spring Data JPA, Validation
* DB: PostgreSQL (preferred) / H2 for demo
* DTO + Service + Repository layered architecture
* Global exception handler (standard JSON error)

### Frontend (Angular + Tailwind)

* Angular latest
* Tailwind only (no extra UI libs required)
* Feature-based folder structure:

  * auth/
  * parties/
  * ledger/
  * reports/
  * shared/

---

## 5) REST APIs (Minimum Required)

### Auth

* `POST /api/auth/login`
* `POST /api/auth/logout` (optional)
* `GET /api/auth/me`

### Parties

* `POST /api/parties`
* `GET /api/parties?type=&q=&page=`
* `GET /api/parties/{id}`
* `PUT /api/parties/{id}`
* `DELETE /api/parties/{id}`

### Ledger

* `POST /api/ledger`
* `GET /api/ledger?partyId=&from=&to=&type=`
* `PUT /api/ledger/{id}`
* `DELETE /api/ledger/{id}`

### Reports

* `GET /api/reports/summary?from=&to=`
* `GET /api/reports/party/{partyId}?from=&to=`
* `GET /api/reports/export.csv?from=&to=`

---

## 6) Database Design (Tables)

1. `users`
2. `parties`
3. `ledger_entries`
4. `attachments` (optional)
5. `audit_logs` (optional)

Must include indexes:

* partyId, date
* phone unique (optional)

---

## 7) UI Screens (Minimum Required)

### Auth

* Login (role based redirect)

### Owner Dashboard

* Cards + quick actions
* “Add Party” / “Add Entry”

### Party List

* filter customer/supplier
* search box
* “Due” column

### Party Details

* party info
* ledger list table
* add entry modal
* download statement button

### Ledger Add Entry

* Credit/Debit toggle
* amount input
* date picker
* payment mode dropdown
* notes

### Reports

* date range
* export csv button

---

## 8) Validation & Edge Cases (Must implement)

* Prevent overlapping edits? (optimistic locking optional)
* Prevent deleting party if ledger exists (or soft-delete)
* If party deleted → show as archived
* Ledger entry edit should recalc balances instantly
* Pagination (at least parties list)
* Loading/error state in Angular UI

---

## 9) Dummy Seed Data (Mandatory)

Create seed:

* 1 OWNER: `owner@khatabook.com / Admin`
* 1 STAFF: `staff@khatabook.com / staff`
* 2 customers, 2 suppliers
* 10 sample entries across 7 days

---

## 10) Deliverables (What you must submit)

1. Backend code
2. Frontend code
3. README with:

   * how to run
   * demo credentials
   * API list
4. Screenshots (optional)
5. Postman collection (optional)

---

# 🎥 YouTube Script Style (Example lines)

তুমি ভিডিওতে এমনভাবে বলবে:

* “এখন আমার backend ta baniye ni—Party entity বানাচ্ছি”
* “এখন Ledger entry add করলে balance auto calculate করবো”
* “এখন Angular dashboard এ receivable/payable cards দেখাবো”
* “এখন export csv endpoint বানালাম”

---

যদি তুমি চাও, আমি এই Khatabook assignment-এর জন্য **exact folder structure + full code starter (Spring Boot + Angular + Tailwind)** আগের মতো “copy-paste ready” করে দিয়েও দিতে পারি।

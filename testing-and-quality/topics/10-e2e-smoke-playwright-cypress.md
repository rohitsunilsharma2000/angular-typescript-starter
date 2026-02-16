# 10) E2E: Playwright বা Cypress smoke flow

লেম্যান-বাংলা: ছোট স্মোক স্ক্রিপ্টে গুরুত্বপূর্ণ ফ্লো কভার করুন (login → তালিকা → ডিটেইল → logout)।

## Hands-on
1) চালান:
   ```bash
   cd testing-and-quality/demos/e2e-smoke-demo
   npm install
   npm run demo
   npm run typecheck
   ```
2) আউটপুটে স্মোক স্টেপ দেখুন; আপনার ফ্লো অনুযায়ী আপডেট করুন।
3) আসল অ্যাপে Playwright/Cypress স্ক্রিপ্ট লিখে এই স্টেপগুলো অটোমেট করুন।

## Done when…
- স্মোক ফ্লো CI তে চলে; লগইন/নেভিগেশন/লগআউট কভার।

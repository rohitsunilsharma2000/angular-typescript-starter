# 13) Keyboard + focus management tests

লেম্যান-বাংলা: Tab ক্রম ঠিক আছে কিনা E2E/ATL দিয়ে চেক করুন; focus-visible দেখুন।

## Hands-on (plan)
1) চালান:
   ```bash
   cd testing-and-quality/demos/keyboard-focus-demo
   npm install
   npm run demo
   npm run typecheck
   ```
2) আউটপুটে প্রত্যাশিত focus order দেখুন; নিজের UI অনুসারে বদলান।
3) বাস্তবে Playwright/Cypress এ Tab সিমুলেট করে order/assert লিখুন।

## Done when…
- কীবোর্ডে পুরো ফ্লো চলে; focus-visible স্টাইল আছে; order সঠিক।

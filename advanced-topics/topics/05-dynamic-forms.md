# 05) Dynamic/typed forms

লেম্যান-বাংলা: ফর্ম ফিল্ড স্কিমা JSON এ রাখুন; রেন্ডারার সেই স্কিমা পড়ে ইনপুট বানাবে।

## Hands-on
1) চালান:
   ```bash
   cd advanced-topics/demos/dynamic-forms-demo
   npm install
   npm run demo
   npm run typecheck
   ```
2) আউটপুটে schema ও জেনারেটেড HTML দেখুন।
3) নতুন ফিল্ড যোগ/required/min সেট করে আবার চালান।

## Common mistakes
- স্কিমা ও UI আলাদা না রাখা, হাডকোড ফর্ম।
- টাইপ না দিয়ে string everywhere।

## Done when…
- স্কিমা বদলালে ফর্ম রেন্ডার বদলায়; duplicate ফর্ম কোড নেই।

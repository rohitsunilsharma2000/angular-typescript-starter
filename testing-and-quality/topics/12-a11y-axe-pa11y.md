# 12) Accessibility checks: axe-core/Pa11y CI gate

লেম্যান-বাংলা: axe/Pa11y দিয়ে পেজ স্ক্যান; ভায়োলেশন হলে CI ফেল।

## Hands-on (concept)
1) চালান:
   ```bash
   cd testing-and-quality/demos/a11y-axe-pa11y-demo
   npm install
   npm run demo
   npm run typecheck
   ```
2) আউটপুটে স্ক্যান আইডিয়া দেখুন; আপনার অ্যাপে বাস্তবে `npx pa11y <url>` বা axe playwright স্ক্রিপ্ট চালান।

## Done when…
- CI তে a11y স্ক্যান; ভায়োলেশন >0 হলে ফেল।

# 03) Signals in hot paths

লেম্যান-বাংলা: signal + computed হট পাথে recompute কমায়; একই ভ্যালু দিলে রান হয় না।

## Hands-on
1) চালান:
   ```bash
   cd performance-and-ux/demos/signals-hot-paths-demo
   npm install
   npm run demo
   npm run typecheck
   ```
2) একই ভ্যালু set করলে computed রান না বাড়ে; নতুন ভ্যালুতে বাড়ে—আউটপুটে দেখুন।

## Common mistakes
- mutable object set না করে clone না করা (reference একই থাকলে recompute হবে না)।

## Done when…
- হট পাথে অযথা recompute হচ্ছে না; signal reference নীতিমালা বোঝেন।

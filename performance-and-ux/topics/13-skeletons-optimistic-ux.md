# 13) Skeletons/shimmers + optimistic UX

লেম্যান-বাংলা: লোডিং এ skeleton দেখান, অপটিমিস্টিক আইটেম আগে দেখান কিন্তু ব্যর্থ হলে ফিরিয়ে নিন।

## Hands-on
1) চালান:
   ```bash
   cd performance-and-ux/demos/skeletons-optimistic-demo
   npm install
   npm run demo
   npm run typecheck
   ```
2) আউটপুটে skeleton, real data, optimistic item দেখুন।
3) delay যোগ বা optimistic আইটেম সরিয়ে পার্থক্য দেখুন।

## Done when…
- লোডিংয়ে skeleton; অপটিমিস্টিক আপডেট ব্যর্থ হলে rollback পরিকল্পনা আছে।

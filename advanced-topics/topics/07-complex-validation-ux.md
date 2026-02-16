# 07) Complex validation UX

লেম্যান-বাংলা: ক্লায়েন্ট + সার্ভার এরর এক সাথে দেখান; ফিল্ড অনুযায়ী গ্রুপ করুন।

## Hands-on
1) চালান:
   ```bash
   cd advanced-topics/demos/complex-validation-demo
   npm install
   npm run demo
   npm run typecheck
   ```
2) আউটপুটে client/server এরর এবং merged structure দেখুন।
3) নতুন এরর যোগ করে rerun করুন; UI তে লিস্ট হিসাবে দেখানো চিন্তা করুন।

## Common mistakes
- সার্ভার এরর overwrite করে ফেলা।
- ফিল্ডভিত্তিক গ্রুপ না করা।

## Done when…
- একই ফিল্ডের একাধিক এরর ইউজার বুঝতে পারে; ক্লায়েন্ট+সার্ভার এক সাথে দেখানো যায়।

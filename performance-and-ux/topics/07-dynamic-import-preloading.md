# 07) import() + preloading strategies

লেম্যান-বাংলা: lazy chunk আগে থেকেই fetch করলে নেভিগেশন দ্রুত লাগে।

## Hands-on
1) চালান:
   ```bash
   cd performance-and-ux/demos/dynamic-import-preloading-demo
   npm install
   npm run demo
   npm run typecheck
   ```
2) আউটপুটে patients/billing preload ক্রম দেখুন।
3) Promise.all সরিয়ে সিরিয়াল লোড দেখুন; নতুন chunk যোগ করুন।

## Done when…
- ক্রিটিক্যাল রুট/ফিচার আগে preload; বাকিগুলো চাহিদামতো।

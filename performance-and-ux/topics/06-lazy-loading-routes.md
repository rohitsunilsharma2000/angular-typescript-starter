# 06) Code splitting: lazy routes

লেম্যান-বাংলা: ফিচার রুট আলাদা chunk; প্রথম লোড ছোট হয়।

## Hands-on
1) চালান:
   ```bash
   cd performance-and-ux/demos/lazy-loading-routes-demo
   npm install
   npm run demo
   npm run typecheck
   ```
2) আউটপুটে lazy route তালিকা দেখুন; প্রতিটা chunk আলাদা ভাবুন।
3) নতুন ফিচার রুট যোগ করে rerun।

## Done when…
- প্রধান রুট chunk ছোট; ফিচার আলাদা lazy মডিউল/standalone route।

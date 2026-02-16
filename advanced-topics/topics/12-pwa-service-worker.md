# 12) PWA + Service Worker updates

লেম্যান-বাংলা: SW আপডেট এলে ব্যানার দেখান, ক্লিক করলে reload/skipWaiting।

## Hands-on
1) চালান:
   ```bash
   cd advanced-topics/demos/pwa-service-worker-demo
   npm install
   npm run demo
   npm run typecheck
   ```
2) আউটপুটে banner মেসেজ দেখুন।
3) `hasUpdate` true/false পাল্টে রিরান; `registered=false` দিয়ে fallback দেখুন।

## Common mistakes
- আপডেট available হলেও UI না দেখানো।
- reload/activate ফ্লো ছাড়া ইউজারকে আটকে রাখা।

## Done when…
- আপডেট সনাক্ত, ব্যানার দেখানো, অ্যাকশন নিলে রিলোড।

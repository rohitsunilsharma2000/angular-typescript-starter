# 05) async pipe vs manual subscribe

লেম্যান-বাংলা: async pipe অটো-unsubscribe করে; manual subscribe করলে লিক/ডুপ্লিকেট রেন্ডারের ঝুঁকি।

## Hands-on
1) চালান:
   ```bash
   cd performance-and-ux/demos/async-pipe-vs-subscribe-demo
   npm install
   npm run demo
   npm run typecheck
   ```
2) আউটপুটে manual সাবস্ক্রিপশন কাউন্টার দেখুন; async-pipe-like অংশ auto সম্পন্ন হয়।
3) manualSubscribe বেশি বার কল করে leak কাউন্টার বাড়িয়ে দেখুন।

## Common mistakes
- manual subscribe করে destroy-এ unsubscribe না করা।
- async pipe ব্যবহার না করে setState ডুপ্লিকেট করা।

## Done when…
- টেমপ্লেটে async pipe প্রাধান্য; manual হলে teardown নিশ্চিত।

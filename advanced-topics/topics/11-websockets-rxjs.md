# 11) WebSockets with RxJS

লেম্যান-বাংলা: RxJS stream দিয়ে socket ইভেন্ট হ্যান্ডল; retry/backoff ভাবুন।

## Hands-on
1) চালান:
   ```bash
   cd advanced-topics/demos/websockets-rxjs-demo
   npm install
   npm run demo
   npm run typecheck
   ```
2) আউটপুটে simulated connected/update মেসেজ দেখুন।
3) take/count বাড়িয়ে দেখুন; retry/catchError যোগ করুন।

## Common mistakes
- share না করে বহু সাবস্ক্রিপশন থেকে ডুপ্লিকেট কল।
- disconnect/reconnect ভাবা না।

## Done when…
- stream থেকে মেসেজ আসে; reconnect/cleanup পরিকল্পনা আছে।

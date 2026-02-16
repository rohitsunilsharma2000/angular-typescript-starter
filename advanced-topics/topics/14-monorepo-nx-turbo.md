# 14) Monorepo (Nx/Turbo) basics

লেম্যান-বাংলা: এক রেপোতে বহু অ্যাপ/লাইব্রেরি; কমান্ড ক্যাশ ও গ্রাফ দিয়ে কাজ দ্রুত।

## Hands-on (mock)
1) চালান:
   ```bash
   cd advanced-topics/demos/monorepo-basics-demo
   npm install
   npm run demo
   npm run typecheck
   ```
2) আউটপুটে projects ও উদাহরণ nx কমান্ড দেখুন।
3) আপনার প্রজেক্টের নাম/কমান্ড বসিয়ে rerun করুন।

## Real workspace এ
- `nx graph`, `nx affected:test`, বা `turbo run lint` চালান।
- cache ব্যবহার হচ্ছে কিনা দেখুন।

## Done when…
- মৌলিক nx/turbo কমান্ড চালাতে পারেন; প্রকল্প গ্রাফ বোঝেন।

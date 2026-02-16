# 15) Generators & affected builds

লেম্যান-বাংলা: জেনারেটর দিয়ে কোড স্ক্যাফোল্ড; affected কমান্ডে বদলানো প্রজেক্টই বিল্ড/টেস্ট।

## Hands-on (mock)
1) চালান:
   ```bash
   cd advanced-topics/demos/generators-affected-demo
   npm install
   npm run demo
   npm run typecheck
   ```
2) আউটপুটে example generator ও affected কমান্ড দেখুন।
3) আপনার ওয়ার্কস্পেসের প্রকৃত কমান্ড বসান (nx বা turbo)।

## Real workspace এ
- `nx g ...` বা `turbo gen ...` দিয়ে কম্পোনেন্ট/লাইব্রেরি বানান।
- `nx affected:test --base=main --head=HEAD` চালিয়ে ছোট সেট টেস্ট করুন।

## Done when…
- জেনারেটর চালাতে পারেন; affected কমান্ডে CI দ্রুত হয়।

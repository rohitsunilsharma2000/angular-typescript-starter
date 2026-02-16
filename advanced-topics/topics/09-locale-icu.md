# 09) Locale + ICU messages

লেম্যান-বাংলা: plural + select logic দিয়ে ICU-ধাঁচের মেসেজ বানান।

## Hands-on
1) চালান:
   ```bash
   cd advanced-topics/demos/icu-messages-demo
   npm install
   npm run demo
   npm run typecheck
   ```
2) আউটপুটে gender + count বদলে বাক্য দেখুন।
3) নতুন locale যোগ করুন বা plural rule পরিবর্তন করুন।

## Common mistakes
- plural/select একই স্ট্রিংয়ে না রাখা।
- locale অনুযায়ী সংখ্যা ফরম্যাট না করা।

## Done when…
- gender/count অনুযায়ী বাক্য পাল্টায়; locale সুইচে সঠিক ভাষা আসে।

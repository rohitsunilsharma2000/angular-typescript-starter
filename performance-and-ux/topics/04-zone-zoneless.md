# 04) Zone + zone-less options

লেম্যান-বাংলা: Zone.js থাকলে সব async ডিটেক্ট; zoneless হলে আপনাকে ম্যানুয়ালি mark করতে হয়।

## Hands-on
1) চালান:
   ```bash
   cd performance-and-ux/demos/zone-zoneless-demo
   npm install
   npm run demo
   npm run typecheck
   ```
2) আউটপুটে Zone vs zoneless ট্রিগার লিস্ট দেখুন।
3) setTimeout/signal উদাহরণ যোগ করে rerun করুন।

## Common mistakes
- zoneless মোডে markForCheck না করা।
- Zone আছে ধরে ব্যয়বহুল change detection চালানো।

## Done when…
- কখন Zone ছাড়বেন, তখন কীভাবে CD চালাবেন—স্পষ্ট।

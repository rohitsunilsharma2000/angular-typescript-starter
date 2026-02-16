# 10) RTL support

লেম্যান-বাংলা: `dir="rtl"` দিলে লেআউট উল্টে যায়; টেক্সট-align/right, icon swap করুন।

## Hands-on
1) চালান:
   ```bash
   cd advanced-topics/demos/rtl-support-demo
   npm install
   npm run demo
   npm run typecheck
   ```
2) আউটপুটে ltr/rtl div দেখুন।
3) আরবি/হিব্রু টেক্সট বসিয়ে rerun করুন; margin/padding চিন্তা করুন।

## Common mistakes
- শুধু টেক্সট উল্টে, icon/chevron না বদলানো।
- dir মিস করে nested কম্পোনেন্টে ভুল অ্যালাইনমেন্ট।

## Done when…
- dir সুইচে টেক্সট ও layout ঠিকভাবে উল্টায়।

# 06) Wizard/stepper flows (draft/save)

লেম্যান-বাংলা: next/prev স্টেট মেশিন রাখুন; খসড়া সেভ করতে চাইলে স্টেপের ডাটা cache করুন।

## Hands-on
1) চালান:
   ```bash
   cd advanced-topics/demos/wizard-stepper-demo
   npm install
   npm run demo
   npm run typecheck
   ```
2) আউটপুটে step flow দেখুন (start → next → next → prev)।
3) নতুন স্টেপ যোগ করুন বা validation guard যোগ করে rerun করুন।

## Common mistakes
- লিনিয়ার ফ্লো ধরে validation ছাড়া next করা।
- স্টেপ ডাটা cache না করা (draft হারায়)।

## Done when…
- next/prev নিয়ন্ত্রিত; draft বা partial save সম্ভব।

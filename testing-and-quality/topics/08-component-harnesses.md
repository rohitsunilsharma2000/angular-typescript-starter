# 08) Component harnesses (CDK) + complex UI

লেম্যান-বাংলা: harness দিয়ে কম্পোনেন্টের public API টেস্ট করুন—DOM সিলেক্টর নির্ভরতা কমে।

## Hands-on
1) চালান:
   ```bash
   cd testing-and-quality/demos/component-harnesses-demo
   npm install
   npm run demo
   npm run typecheck
   ```
2) আউটপুটে harness মেথড ধারণা দেখুন (click/getLabel/isDisabled)।
3) setValue মত মেথড যোগ করে rerun করুন।

## Done when…
- brittle querySelector বাদ; harness দিয়ে UI ইন্টারঅ্যাকশন টেস্ট করেন।

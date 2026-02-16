# 01) Perf baseline + goals

হাসপাতাল ড্যাশবোর্ড (patients/appointments/billing) দ্রুত লোড না হলে নার্সরা মোবাইলে অপেক্ষা করে। কোথায় ধীর, কী মাপবেন, কী লক্ষ্য নেবেন তা স্থির করা এই ধাপ।

## Why this matters (real-world)
- শিফট চেঞ্জ সময় ট্রাফিক বাড়ে; ধীর পেজ মানে কাজ আটকে থাকা।
- বাজেট না থাকলে টিম জানে না “enough” কি।
- ইন্টারভিউতে প্রশ্ন: “How do you set performance goals?”

## Concepts
### Beginner
- LCP, INP, CLS কী এবং hospital dashboard-এ কেন জরুরি।
- Lab বনাম field data; Lighthouse = lab proxy।
### Intermediate
- KB budget: JS < 250KB gzip, CSS < 80KB, critical images < 150KB.
- Before/after রিপোর্টিং—baseline স্কোর সংরক্ষণ।
### Advanced
- Journey goal: `/appointments` LCP ≤ 2.5s (lab), INP ≤ 200ms; hydration errors = 0 (SSR থাকলে)।
- Long task budget (<50ms per task) ও TTI অনুমান।

## Copy-paste Example
```bash
# Lab run (desktop preset)
npx lighthouse http://localhost:4200/appointments \
  --preset=desktop \
  --output=json --output-path=./perf-baseline.json
```
```ts
// perf-baseline.ts (targets for CI)
export const perfTargets = {
  lcpMs: 2500,
  inpMs: 200,
  cls: 0.1,
  jsKb: 250,
  cssKb: 80,
  imgKb: 150,
};
```
```md
<!-- baseline-notes.md -->
Page: /appointments
Date: 2026-02-16
LCP: 3.1s (goal 2.5s)
CLS: 0.06 (good)
JS: 320KB gzip (needs cut)
Actions: lazy-load chart lib; enable OnPush on list component.
```

## Try it
- Beginner: Lighthouse চালিয়ে LCP/CLS নোট করে baseline-notes.md লিখুন।
- Advanced: perfTargets ভঙ্গ হলে CI fail এমন স্ক্রিপ্ট বানান।

## Common mistakes
- Lab স্কোরকেই field ধরে নেওয়া।
- বাজেট নথিভুক্ত না করে নতুন লাইব্রেরি যোগ করা।
- CLS মাপতে aspect-ratio না রাখা।

## Interview points
- Goal setting: LCP/INP/CLS + KB budgets উল্লেখ করুন।
- “Baseline first, optimize later” স্টেপওয়াইজ পদ্ধতি বলুন।

## Done when…
- Lighthouse baseline সংরক্ষিত।
- JS/CSS/image বাজেট লেখা ও শেয়ার করা হয়েছে।
- LCP/INP/CLS লক্ষ্য নির্ধারিত।
- Action list তৈরি।

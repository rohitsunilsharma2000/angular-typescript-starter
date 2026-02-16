# 15) Checkpoint: Definition of Done

এই চেকলিস্ট নিশ্চিত করে HMS অ্যাপের পারফরম্যান্স ও UX টার্গেট পূরণ হয়েছে।

## Why this matters (real-world)
- রিলিজের আগে নিশ্চিত হন যে লক্ষ্য পূরণ হয়েছে।
- রিগ্রেশন ধরা সহজ হয়।
- ইন্টারভিউ: “How do you sign-off performance?”

## Concepts
### Beginner
- Checklist-driven sign-off।
### Intermediate
- Budget বনাম আসল মাপ compare।
### Advanced
- CI gate + auto-report archive।

## Copy-paste Example
```md
# Perf & UX DoD (appointments page)
- [ ] OnPush/signals applied to patient list
- [ ] Lazy route for /appointments chunk verified
- [ ] Skeleton loader present (fixed height)
- [ ] Toast + retry on failures
- [ ] Bundle gzip JS <= 250KB (current: ____)
- [ ] CSS <= 80KB (current: ____)
- [ ] LCP (lab) <= 2.5s (current: ____)
- [ ] Hydration errors: 0 (SSR only)
- [ ] Lighthouse report stored at reports/appointments-lh.json
- [ ] Bundle report stored (stats-json + analyzer screenshot)
```

## Try it
- Beginner: উপরের চেকলিস্ট কপি করে নিজের প্রজেক্টে পূরণ করুন।
- Advanced: CI pipeline-এ checklist markdown স্বয়ংক্রিয়ভাবে পূরণ (scripts) এবং ব্যর্থ হলে PR ব্লক করুন।

## Common mistakes
- Budget সংখ্যা লেখা হলেও রিপোর্ট সংরক্ষণ না করা।
- Hydration errors চেক না করে SSR রিলিজ দেওয়া।

## Interview points
- স্পষ্ট লক্ষ্য: JS/CSS budget, LCP target, hydration=0।
- Evidence: Lighthouse + bundle report + code changes (OnPush/lazy/skeleton/toast)।

## Done when…
- OnPush বা signals হট পাথগুলোতে কার্যকর।
- অন্তত ১ lazy route live।
- Skeleton loader layout-stable।
- Toast/progress UX আছে।
- Bundle gzip JS ≤ 250KB, CSS ≤ 80KB।
- LCP (lab) ≤ 2.5s; hydration errors = 0।
- Lighthouse + bundle রিপোর্ট সংরক্ষিত।

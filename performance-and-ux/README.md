# Performance & UX টিউটোরিয়াল (বাংলা, হাসপাতাল ম্যানেজমেন্ট উদাহরণ)

Angular UI ডেভেলপারদের জন্য ধারাবাহিক রোডম্যাপ: Change Detection → Code Splitting → Rendering → Profiling → Perceived UX → Checkpoint। উদাহরণ সব হাসপাতালে ব্যবহৃত ফিচার (appointments, patients, billing, pharmacy)।

## কীভাবে ব্যবহার করবেন
1. `topics/` ক্রম ধরে পড়ুন; প্রতিটিতে কপি-পেস্ট Angular উদাহরণ চালান।
2. `demos/perf-hot-paths` ও `demos/bundle-reports` নোট ফলো করুন প্র্যাকটিসের জন্য।
3. প্রতিটি টপিক শেষে “Done when…” চেকলিস্ট টিক দিন।

## টপিক লিস্ট (Foundations → Quality)
* [01) Perf baseline + goals](topics/01-perf-baseline-goals.md)
* [02) Change Detection: Default vs OnPush](topics/02-change-detection-onpush.md)
* [03) Signals in hot paths](topics/03-signals-hot-paths.md)
* [04) Zone + zone-less options](topics/04-zone-zoneless.md)
* [05) async pipe vs manual subscribe](topics/05-async-pipe-vs-subscribe.md)
* [06) Code splitting: lazy routes](topics/06-lazy-loading-routes.md)
* [07) import() + preloading strategies](topics/07-dynamic-import-preloading.md)
* [08) SSR/SSG + hydration](topics/08-ssr-ssg-hydration.md)
* [09) Lazy images + CLS control](topics/09-lazy-images-cls.md)
* [10) Virtual scroll + trackBy](topics/10-virtual-scroll-trackby.md)
* [11) Profiling: Angular DevTools + Chrome](topics/11-profiling-devtools-chrome.md)
* [12) Lighthouse + bundle budgets](topics/12-lighthouse-bundle-budgets.md)
* [13) Skeletons/shimmers + optimistic UX](topics/13-skeletons-optimistic-ux.md)
* [14) Toast + progress patterns](topics/14-toast-progress-patterns.md)
* [15) Checkpoint: Definition of Done](topics/15-checkpoint-done.md)

## দ্রুত রিভিশন
- প্রতিটি ফাইলে: কেন দরকার → Beginner/Intermediate/Advanced → Copy-paste Example → Try it → Common mistakes → Interview points → Done when চেকলিস্ট।
- কোড: standalone components, route-level providers, OnPush/Signals, RxJS switchMap/catchError/finalize, trackBy, lazy routes।

## প্রাক-প্রয়োজন
- Node.js >= 18
- Angular >= 17 (standalone)
- RxJS 7+
- Chrome DevTools, Angular DevTools, Lighthouse

## Demo
- `demos/perf-hot-paths`: OnPush + trackBy + virtual scroll + lazy route + skeleton/toast।
- `demos/bundle-reports`: bundle analyzer ও Lighthouse নির্দেশনা।

## What to ship (checkpoint লক্ষ্য)
- Hot path এ OnPush বা signals চালু
- অন্তত ১টি lazy-loaded route
- Layout-stable skeleton loader
- Bundle size রিপোর্ট + KB budget উল্লেখ
- LCP লক্ষ্য পূরণ (lab) ও hydration errors শূন্য (যদি SSR থাকে)

## Checkpoint
- Bundle < নির্ধারিত বাজেট
- LCP ≤ লক্ষ্য
- No major hydration errors
- Skeleton/optimistic UI/toast in place
- Lazy route verified

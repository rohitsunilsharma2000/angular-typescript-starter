# 13) Quality gates checklist

ফিচার শেষ করার আগে দ্রুত চেকলিস্ট।

## Why this matters (real world)
- Release আগে common breakage আটকায়।
- ইন্টারভিউ: “How do you ensure quality?” উত্তর।

## Concepts (beginner → intermediate → advanced)
- Beginner: lint, no circular imports।
- Intermediate: selector memoization; heavy compute template-এ নয়।
- Advanced: shared UI docs; a11y audit; state tests।

## Copy-paste Example
```ts
// tools/check-circular.ts (simple guard)
import madge from 'madge';
(async () => {
  const res = await madge('src/app');
  const cycles = res.circular();
  if (cycles.length) { console.error('Circular deps', cycles); process.exit(1); }
  console.log('No circular deps');
})();
```
> Note: `npm i -D madge` প্রয়োজন।
```ts
// Memoized selector pattern
import { createSelector } from '@ngrx/store';
const selectPatients = (s: any) => s.patients;
export const selectIcuCount = createSelector(selectPatients, p => p.data.filter((x: any) => x.ward.startsWith('ICU')).length);
```
```md
<!-- shared UI docs template: architecture-and-state/demos/shared-ui-kit/button.md -->
Props: label, kind, disabled
Events: click
Examples: primary/ghost, loading state
A11y: role="button", keyboard focus-visible
```

## Try it (exercise)
- Beginner: madge চালিয়ে রিপোর্ট করুন; প্রথম cycle ঠিক করুন।
- Advanced: selector heavy compute হলে memo test লিখুন (same input → same reference)।

## Common mistakes
- selector memoize না করে প্রতি change detection এ heavy filter।
- shared UI props ডকুমেন্ট না করা।
- circular dep স্ক্রিপ্ট CI তে না চালানো।

## Interview points
- Mention CI gates: lint, circular check, selector memo check, minimal tests।
- Shared UI docs = onboarding speed।

## Done when…
- Circular check পাস।
- Memoized selectors ব্যবহার।
- Shared UI docs আপডেটেড।
- a11y basics (aria/keyboard) চেকড।

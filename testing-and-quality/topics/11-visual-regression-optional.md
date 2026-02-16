# 11) Visual regression (optional)

Login, patient list, invoice summary স্ক্রিনের screenshot diff নিয়ে layout break ধরুন।

## Why this matters (real-world)
- CSS refactor এ অদৃশ্য ব্রেক দ্রুত ধরা যায়।
- Stakeholder-approved visuals রক্ষা পায়।

## Concepts
### Beginner
- Screenshot baseline + compare (Playwright expect.toHaveScreenshot)।
### Intermediate
- Noise কমাতে: স্থির ডেটা, fixed dates, disable animations।
### Advanced
- Threshold tuning, per-viewport snapshots, CI artifact upload।

## Copy-paste Example (Playwright)
```ts
import { test, expect } from '@playwright/test';

test('patient list visual', async ({ page }) => {
  await page.route('**/api/patients', route => route.fulfill({ json: [{ id:'P1', name:'Rima', ward:'ICU' }] }));
  await page.goto('http://localhost:4200/patients');
  await page.waitForTimeout(200); // ensure fonts loaded (or better: font preload)
  await expect(page).toHaveScreenshot('patients.png', { maxDiffPixelRatio: 0.01 });
});
```

## Try it
- Beginner: single viewport snapshot নিন।
- Advanced: dark mode + mobile viewport snapshot; animation disable করুন (`prefers-reduced-motion`).

## Common mistakes
- Random data/clock না ফ্রিজ করা → noisy diff।
- Fonts/CDN লোড না হওয়া।

## Interview points
- Visual tests optional; control noise via deterministic fixtures।

## Done when…
- Key screen snapshot baseline আছে।
- Noise controls (fixed data/time, animations off) নথিভুক্ত।

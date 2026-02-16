# 12) Accessibility checks: axe-core/Pa11y CI gate

Login ও patient list স্ক্রিনে axe/Pa11y চালিয়ে গুরুতর violation না রেখে CI-তে গেট বসান।

## Why this matters (real-world)
- হাসপাতাল অ্যাপ সার্বজনীন ব্যবহারযোগ্য; a11y লঙ্ঘন আইনগত ঝুঁকি।
- CI gate না থাকলে রিগ্রেশন হয়।

## Concepts
### Beginner
- axe-core basics; Pa11y CLI run।
### Intermediate
- Severity filter (serious/critical fail); ignore list with expiry।
- Component test বা e2e তে axe run।
### Advanced
- CI artifact (JSON/HTML) সংরক্ষণ; custom rules; color-contrast caching।

## Copy-paste Example
```ts
// a11y-axe.spec.ts (Playwright + axe-core)
import { test, expect } from '@playwright/test';
import AxeBuilder from '@axe-core/playwright';

test('dashboard a11y', async ({ page }) => {
  await page.goto('http://localhost:4200/dashboard');
  const results = await new AxeBuilder({ page }).analyze();
  const serious = results.violations.filter(v => ['serious','critical'].includes(v.impact ?? '')); 
  expect(serious).toEqual([]);
});
```
```bash
# Pa11y CLI example
npx pa11y http://localhost:4200/login --reporter cli --threshold 1
```

## Try it
- Beginner: axe run করে প্রথম তিনটি violation নোট করুন।
- Advanced: serious/critical না হলেও minor violation waiver লিখে expiry তারিখ দিন।

## Common mistakes
- Axe run শুধু হ্যাপি পাথ; modal/dialog খোলা অবস্থায় পরীক্ষা না করা।
- Waiver expiry না রাখা।

## Interview points
- Serious/critical gate; waiver policy; CI integration।

## Done when…
- axe/Pa11y CI step আছে।
- Serious/critical violation শূন্য বা documented waiver + expiry।
- রিপোর্ট সংরক্ষণ।

# 08) Perf budgets & bundle guard

Billing dashboardে chart যোগ করলে bundle ফুলে যায়। CI-তে বাজেট গার্ড রাখুন।

## Why this matters (real-world)
- JS বেশি হলে ধীর লোড; স্লো হাসপাতালে সময় অপচয়।
- বাজেট গার্ড রিগ্রেশন আটকে।

## Concepts
### Beginner
- KB budget নির্ধারণ (JS/CSS)।
- Simple size check script।
### Intermediate
- CI step দিয়ে guard চালানো; gzip size মাপা।
### Advanced
- Lighthouse CI optional; per-chunk budgets; diff-based alert।

## Copy-paste Example
```js
// bundle-size-guard.js
import fs from 'node:fs';
import zlib from 'node:zlib';

const BUDGET_KB = 350; // set your JS gzip budget
const file = process.argv[2] || 'dist/browser/main.js';
const gz = zlib.gzipSync(fs.readFileSync(file));
const sizeKb = gz.length / 1024;
if (sizeKb > BUDGET_KB) {
  console.error(`Bundle ${sizeKb.toFixed(1)}KB exceeds budget ${BUDGET_KB}KB`);
  process.exit(1);
} else {
  console.log(`Bundle OK: ${sizeKb.toFixed(1)}KB <= ${BUDGET_KB}KB`);
}
```
```yaml
# github-actions-ci.yml excerpt
      - run: npm run build
      - run: node build-ops-security/demos/snippets/bundle-size-guard.js dist/browser/main.js
```

## Try it
- Beginner: budget 350KB সেট করে guard চালান।
- Advanced: multiple entry (main/polyfills) iterate করে fail earliest।

## Common mistakes
- raw size মাপা, gzip নয়।
- budget undefined রেখে script success।

## Interview points
- Budget enforcement in CI; gzip metric; failing fast।

## Done when…
- Budget সংখ্যা লেখা।
- Guard script CI তে wired।
- Fail হলে পাইপলাইন লাল।

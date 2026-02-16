# 02) Test runner setup: Jest বা Vitest

HMS অ্যাপের unit/component টেস্ট দ্রুত চালাতে Jest বা Vitest সেটআপ জরুরি।

## Why this matters (real-world)
- ধীর টেস্ট মানে dev feedback slow।
- CI তে স্থিতিশীল runner দরকার।
- ইন্টারভিউ: “কোন runner কেন?” প্রশ্ন।

## Concepts
### Beginner
- Jest বনাম Vitest পার্থক্য; ts-jest/ SWC/ esbuild ট্রান্সপাইল।
- package scripts: `test`, `test:watch`, `test:ci`।
### Intermediate
- Jest + Angular Testing Library config; jsdom vs happy-dom।
- Vitest setupFiles, globals, coverage config।
### Advanced
- Path alias, ESM config, transforming Angular templates (vite-plugin-angular or jest-preset-angular)।

## Copy-paste Example
```json
// package.json (scripts)
{
  "scripts": {
    "test": "vitest",
    "test:watch": "vitest --watch",
    "test:ci": "vitest run --coverage"
  }
}
```
```ts
// vitest.config.ts
import { defineConfig } from 'vitest/config';
export default defineConfig({
  test: {
    globals: true,
    environment: 'jsdom',
    setupFiles: './test-setup.ts',
    coverage: { reporter: ['text', 'lcov'], thresholds: { lines: 0.8 } },
  },
});
```
```ts
// test-setup.ts
import '@testing-library/jest-dom';
```

## Try it
- Beginner: vitest config কপি করে `vitest` চালান; একটি trivial test পাস করুন।
- Advanced: coverage threshold 80% সেট করে ব্যর্থ হলে CI fail করুন।

## Common mistakes
- jsdom env ভুলে রাখা → DOM API মিসিং।
- coverage threshold 0 রেখে গেট অকার্যকর।

## Interview points
- Jest বনাম Vitest trade-offs (speed, ecosystem)।

## Done when…
- Runner সেটআপ ও চলমান।
- Scripts CI-ready।
- Coverage threshold নির্ধারিত।

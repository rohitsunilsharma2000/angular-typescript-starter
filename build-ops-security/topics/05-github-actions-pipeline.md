# 05) GitHub Actions pipeline (lint→test→build→preview)

HMS অ্যাপের জন্য Node 22 দিয়ে lint, test, build, preview deploy চালানো CI।

## Why this matters (real-world)
- ভাঙা কোড প্রডে যাওয়ার আগে ধরা যায়।
- একই পাইপলাইন preview deploy দিলে QA তৎক্ষণাৎ যাচাই করতে পারে।

## Concepts
### Beginner
- Workflow trigger (push/PR), jobs, steps; Node version pin।
### Intermediate
- Cache npm + build; artifacts upload।
- matrix optional (browser?).
### Advanced
- Conditional steps (docs skip), concurrency cancel-in-progress, required checks।

## Copy-paste Example
```yaml
# .github/workflows/github-actions-ci.yml
name: CI
on:
  pull_request:
  push:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-node@v4
        with:
          node-version: '22'
          cache: 'npm'
      - run: npm ci
      - run: npm run lint
      - run: npm run test:ci
      - run: npm run build
      - name: Upload dist
        uses: actions/upload-artifact@v4
        with:
          name: dist
          path: dist
```

## Try it
- Beginner: workflow ফাইল যোগ করে PR তুলুন; রান দেখুন।
- Advanced: build cache (turbo/vite cache) artifact যোগ করুন; concurrency cancel করুন।

## Common mistakes
- Node version pin না করা → ভিন্ন পরিবেশে ফেইল।
- cache key ভুলে stale build।

## Interview points
- Node pin, cache, job order (lint→test→build), artifacts।

## Done when…
- Workflow ফাইল কমিট; lint/test/build রান।
- Node 22 pinned; cache active।

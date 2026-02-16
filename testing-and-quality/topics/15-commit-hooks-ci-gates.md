# 15) Commit hooks + CI quality gates

lint/test/e2e না চালিয়ে commit করলে রিগ্রেশন ঢোকে। HMS অ্যাপে pre-commit ও CI pipeline গেট রাখুন।

## Why this matters (real-world)
- তাড়াহুড়ো commit এ ভাঙা কোড প্রডে যায়।
- দল জুড়ে অভিন্ন মান।

## Concepts
### Beginner
- husky + lint-staged দিয়ে pre-commit।
- CI steps: lint → unit → coverage → e2e → a11y।
### Intermediate
- Caching (pnpm/npm cache) দ্রুত CI; matrix (node versions)।
- Coverage threshold enforce।
### Advanced
- Flaky test quarantine policy; rerun limit; slack alerts।

## Copy-paste Example
```bash
npx husky add .husky/pre-commit "npm run lint && npm run test -- --runInBand"
```
```yaml
# .github/workflows/ci.yml (excerpt)
name: CI
on: [push, pull_request]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-node@v4
        with: { node-version: '20' }
      - run: npm ci
      - run: npm run lint
      - run: npm run test:ci
      - run: npm run e2e:ci
      - run: npm run a11y:ci
```
```md
# Flake policy
- No `sleep` waits; use waitFor assertions
- If flaky: mark @flaky, file issue, fix ETA; max 1 rerun allowed
```

## Try it
- Beginner: husky hook যোগ করে intentional lint error দিয়ে commit করে দেখুন বাধা দেয় কিনা।
- Advanced: CI workflow তে coverage threshold ব্যর্থ হলে PR red করুন।

## Common mistakes
- Hooks লোকালেই; CI-তে একই গেট না রাখা।
- Flaky টেস্ট rerun করে উপেক্ষা করা।

## Interview points
- Hook + CI গেট; flake policy; coverage enforcement।

## Done when…
- Pre-commit hooks কাজ করে।
- CI pipeline lint/test/e2e/a11y চালায়।
- Flake policy নথিভুক্ত ও অনুসৃত।

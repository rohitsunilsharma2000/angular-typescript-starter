# Testing & Quality টিউটোরিয়াল (বাংলা, হাসপাতাল ম্যানেজমেন্ট উদাহরণ)

Angular UI ডেভেলপারদের জন্য ধারাবাহিক রোডম্যাপ: Unit → Component → Integration → E2E → A11y → Static Quality → CI Checkpoint। উদাহরণ সব হাসপাতালে ব্যবহৃত ফিচার (auth, patients, appointments, billing)।

## কীভাবে ব্যবহার করবেন
1. `topics/` তালিকা অনুযায়ী পড়ুন; প্রতিটিতে কপি-পেস্ট Angular উদাহরণ চালান।
2. `demos/testing-critical-path` ও `demos/test-utils` থেকে নোট/স্ক্রিপ্ট নিয়ে দ্রুত সেটআপ করুন।
3. প্রতিটি টপিক শেষে “Done when…” চেকলিস্ট টিক দিন; CI গেটে মিলিয়ে দেখুন।

## টপিক লিস্ট (Sequence)
* [01) Testing pyramid + critical path plan](topics/01-testing-pyramid-critical-path.md)
* [02) Test runner setup: Jest বা Vitest](topics/02-jest-vitest-setup.md)
* [03) Angular Testing Library basics](topics/03-angular-testing-library-basics.md)
* [04) TestBed fundamentals (standalone friendly)](topics/04-testbed-standalone.md)
* [05) Mocking HttpClient properly](topics/05-mocking-httpclient.md)
* [06) Mocking Router/Guards/Navigation](topics/06-mocking-router-navigation.md)
* [07) Service + Facade/Store tests](topics/07-service-store-tests.md)
* [08) Component harnesses (CDK) + complex UI](topics/08-component-harnesses.md)
* [09) Integration tests (feature route level)](topics/09-integration-tests.md)
* [10) E2E: Playwright বা Cypress smoke flow](topics/10-e2e-smoke-playwright-cypress.md)
* [11) Visual regression (optional)](topics/11-visual-regression-optional.md)
* [12) Accessibility checks: axe-core/Pa11y CI gate](topics/12-a11y-axe-pa11y.md)
* [13) Keyboard + focus management tests](topics/13-keyboard-focus-tests.md)
* [14) Static quality: ESLint + TS strictness](topics/14-eslint-ts-strict.md)
* [15) Commit hooks + CI quality gates](topics/15-commit-hooks-ci-gates.md)
* [16) Checkpoint: Ship list + Definition of Done](topics/16-checkpoint-done.md)

## দ্রুত রিভিশন
- প্রতিটি টপিক: কেন দরকার → Beginner/Intermediate/Advanced → Copy-paste Example → Try it → Common mistakes → Interview points → Done when।
- কোড: standalone components, route-level providers, Testing Library queries (`getByRole/getByLabelText`), HttpTestingController, RouterTesting, Playwright/Cypress smoke।

## প্রাক-প্রয়োজন
- Angular + TypeScript + RxJS basics
- Jest বা Vitest বেসিক
- Playwright/Cypress বেসিক
- Accessibility fundamentals (roles, labels, focus)

## Demo
- `demos/testing-critical-path`: auth + patient list + create/update smoke + a11y gate।
- `demos/test-utils`: common mocks (router/http), render helpers, fixture builders।

## What to ship (target)
- Critical path কভারেজ ≥ 80%
- E2E smoke (auth + main flow)
- A11y gate (axe/Pa11y) CI-তে
- Lint + TS strict + formatting gates পাস

## Checkpoint
- CI সবুজ: lint/test/coverage/e2e/a11y
- Flaky tests triaged (no sleeps, stable locators)
- axe গুরুতর ভায়োলেশন নেই বা waiver সহ expiry

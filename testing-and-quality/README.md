# Testing & Quality টিউটোরিয়াল (বাংলা, হাসপাতাল ম্যানেজমেন্ট উদাহরণ)

এই ফোল্ডার থেকে Angular UI Developer হিসেবে **Testing + Quality Gates** শিখবেন—Unit tests (TestBed + Testing Library), mocking (HttpClient/Router), Component harness, Integration/E2E (Playwright/Cypress), Accessibility checks (axe/Pa11y), এবং Static quality (ESLint/TS strict/commit hooks/CI gates)—সবই **হাসপাতাল ম্যানেজমেন্ট** (auth, patient list, appointment flow, billing) উদাহরণে।

## কীভাবে ব্যবহার করবেন

1. প্রথমে `00-setup/testing-setup.md` পড়ে Jest/Vitest + Angular Testing Library + Playwright সেটআপ করুন।
2. তারপর `topics` ফোল্ডারের নোটগুলো **ক্রম অনুযায়ী** পড়ুন।
3. হাতে-কলম অনুশীলনের জন্য `demos/testing-critical-path` রান করুন (auth + main flow + a11y gate)।

---

## টপিক লিস্ট (Sequence অনুসারে)

* [01) Testing pyramid + critical path plan](topics/01-testing-pyramid-critical-path.md)
  unit/integration/e2e ভাগ, “critical path” কী (login → dashboard → patient list → create/update), behavior testing mindset, flaky test এড়ানো।

* [02) Test runner setup: Jest বা Vitest](topics/02-jest-vitest-setup.md)
  Jest vs Vitest পার্থক্য, config ধারণা, scripts, coverage baseline, দ্রুত feedback loop।

* [03) Angular Testing Library basics](topics/03-angular-testing-library-basics.md)
  user-centric queries (`getByRole/getByLabelText`), `userEvent` interactions, async asserts, component behavior test pattern।

* [04) TestBed fundamentals (standalone friendly)](topics/04-testbed-standalone.md)
  standalone component render, providers/imports ঠিক করা, stubs, schema misuse এড়ানো, DI control।

* [05) Mocking HttpClient properly](topics/05-mocking-httpclient.md)
  `HttpTestingController`, success/error/retry cases, loading/skeleton state test, API error mapping assert।

* [06) Mocking Router/Guards/Navigation](topics/06-mocking-router-navigation.md)
  `provideRouter`/RouterTesting, navigation intent tests, guards basic smoke, route param/query param testing।

* [07) Service + Facade/Store tests](topics/07-service-store-tests.md)
  API service contract tests, state store (RxJS store/ComponentStore/NgRx) selector/updater/effect tests, optimistic rollback test।

* [08) Component harnesses (CDK) + complex UI](topics/08-component-harnesses.md)
  dialog/menu/table harness concept, focus trap test, Esc close, keyboard navigation, stable selectors strategy।

* [09) Integration tests (feature route level)](topics/09-integration-tests.md)
  router + mocked network সহ feature mount, realistic integration assertions, network stubbing patterns, brittle test এড়ানো।

* [10) E2E: Playwright বা Cypress smoke flow](topics/10-e2e-smoke-playwright-cypress.md)
  auth + main flow smoke, network stubbing/fixtures, stable locators, retries vs real flake fix, test data strategy।

* [11) Visual regression (optional)](topics/11-visual-regression-optional.md)
  screenshot diff basics, noise কমানো (dates/random ids/fonts), key screens snapshot (login/patient list/invoice)।

* [12) Accessibility checks: axe-core/Pa11y CI gate](topics/12-a11y-axe-pa11y.md)
  axe run কোথায় (component tests/e2e), common violations (label/role/contrast), severity gate, waiver policy।

* [13) Keyboard + focus management tests](topics/13-keyboard-focus-tests.md)
  keyboard trap detection, dialog focus trap, return focus, tab order, form errors a11y association tests।

* [14) Static quality: ESLint + TS strictness](topics/14-eslint-ts-strict.md)
  Angular lint rules, template linting, strictNullChecks, typed forms hints, unsafe any ban strategy।

* [15) Commit hooks + CI quality gates](topics/15-commit-hooks-ci-gates.md)
  husky + lint-staged, formatting gates, CI pipeline (lint → unit → coverage → e2e → a11y), flaky tests policy।

* [16) Checkpoint: Ship list + Definition of Done](topics/16-checkpoint-done.md)
  80%+ critical path coverage, e2e smoke for auth + main flow, a11y lint in CI, CI green, flake free, axe violations fixed—final checklist।

---

## What to ship (এই সিরিজ শেষ করলে যা বানাতে পারবেন)

* **80%+ critical-path coverage** (auth + main flow কেন্দ্রিক)
* **E2E smoke**: login → navigate → main action (create/update) → logout
* **a11y CI gate**: axe/Pa11y run + fail on serious/critical
* Lint + TS strict + formatting gates (CI + pre-commit)

---

## দ্রুত রিভিশন

* প্রতিটি ফাইলে থাকবে:

  * “কেন দরকার”
  * “Beginner → Intermediate → Advanced”
  * “Copy-paste Example” (compile-ready snippets)
  * “Try it” tasks (beginner + advanced)
  * “Common mistakes” + “Interview points”
  * “Done when…” checklist

---

## প্রাক-প্রয়োজন

* Angular + TypeScript + RxJS basics
* Jest/Vitest basics
* Playwright/Cypress basics (optional but recommended)
* Accessibility fundamentals (roles, labels, focus)

---

## Demo

* `demos/testing-critical-path`

  * auth flow
  * patient list flow
  * create/update flow
  * e2e smoke + a11y gate sample

* `demos/test-utils`

  * common mocks (router/http)
  * render helpers
  * fixture builders (patient/appointment/invoice)

---

চাইলে আমি এর জন্যও Codex prompt বানিয়ে দিচ্ছি যাতে `testing-and-quality/` ফোল্ডার + সব `topics/*.md` auto-generate হয় (Beginner→Advanced + working copy-paste examples সহ)।

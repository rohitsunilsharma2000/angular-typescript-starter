You are working inside my repository.

PRIMARY SOURCE OF TRUTH (MUST READ FIRST)
- testing-and-quality/README.md
If it does not exist, create it in the same format as my other learning modules (intro/how to use/topic list/quick revision/prereq/demo).

GOAL
1) Create (or update) a folder: testing-and-quality/
2) Ensure testing-and-quality/README.md exists and follows this format:
   - Intro (Bangla + HMS context)
   - How to use
   - Topic list with links (sequence-wise)
   - Quick revision
   - Prerequisites
   - Demo section
3) Create all topic markdown files referenced by the README under:
   testing-and-quality/topics/*.md
4) Each topic file must include these sections in THIS EXACT ORDER:
   1) শিরোনাম + 3–5 লাইন পরিচিতি (বাংলা, হাসপাতাল উদাহরণ)
   2) Why this matters (real-world)
   3) Concepts (Beginner → Intermediate → Advanced) as separate subsections
   4) Copy-paste Example (Angular, compile-ready snippets)
   5) Try it (Beginner task + Advanced task)
   6) Common mistakes
   7) Interview points
   8) Done when… (checklist)

LANGUAGE & STYLE RULES
- Write in Bengali with necessary technical English terms.
- No emojis.
- Examples must be HMS flavored (auth, patients, appointments, billing).
- Prefer Angular standalone components, route-level providers.
- Keep examples minimal but working.

TESTING & QUALITY CONTENT REQUIREMENTS (must be covered across topics)
- Unit: TestBed + Jest or Vitest + Angular Testing Library
- Component harnesses (CDK), complex UI tests (dialog/menu/table)
- Mocking HttpClient + Router (provideRouter/RouterTesting, HttpTestingController)
- Integration tests: feature route mount + network stubbing
- E2E: Playwright or Cypress smoke suite (auth + main flow)
- Optional: visual regression (screenshot diff) with stability notes
- Accessibility: axe-core or Pa11y checks + CI gate; keyboard traps; focus tests
- Static quality: ESLint (Angular rules), TypeScript strictness, commit hooks (husky + lint-staged), formatting gates in CI
- What to ship:
  - 80%+ critical-path coverage (define critical path pages and assert on them)
  - e2e smoke for auth + main flow
  - a11y lint step in CI
- Checkpoint:
  - CI green on lint/test/e2e
  - flaky tests addressed (no sleeps, stable locators, deterministic fixtures)
  - axe violations fixed (or clearly waived with reason + expiry)

FILES TO CREATE (DEFAULT SET)
If README lists different filenames, follow README exactly and adjust links.
A) testing-and-quality/README.md

B) testing-and-quality/topics/
  01-testing-pyramid-critical-path.md
  02-jest-vitest-setup.md
  03-angular-testing-library-basics.md
  04-testbed-standalone.md
  05-mocking-httpclient.md
  06-mocking-router-navigation.md
  07-service-store-tests.md
  08-component-harnesses.md
  09-integration-tests.md
  10-e2e-smoke-playwright-cypress.md
  11-visual-regression-optional.md
  12-a11y-axe-pa11y.md
  13-keyboard-focus-tests.md
  14-eslint-ts-strict.md
  15-commit-hooks-ci-gates.md
  16-checkpoint-done.md

C) Demos scaffold (docs + minimal code snippets; do not build a full app unless repo already expects it)
  testing-and-quality/demos/testing-critical-path/README.md
  testing-and-quality/demos/test-utils/README.md
  testing-and-quality/demos/snippets/
    patient-list.component.spec.ts
    patient.service.spec.ts
    router-navigation.spec.ts
    a11y-axe.spec.ts
    playwright-smoke.spec.ts

COPY-PASTE EXAMPLE REQUIREMENTS
- Snippets must be compile-ready when placed in an Angular project:
  - include imports
  - include minimal models/interfaces
  - show stable queries (getByRole/getByLabelText)
- Must include at least one unit test that covers:
  - loading → success → error UI states
  - form validation errors
- Must include HttpTestingController example:
  - success + error response
- Must include Router navigation example:
  - clicking button triggers navigation
- Must include one store/facade test:
  - selector/derived state test OR optimistic rollback test
- Must include one e2e smoke example:
  - auth + main flow with network stubbing
- Must include one a11y gate example:
  - axe run + failing on serious/critical

PROCESS
1) Read testing-and-quality/README.md first.
2) Create/update README topic list with correct links.
3) Create topics in sequence. Keep each topic beginner-friendly but include advanced notes.
4) At the end, print a short summary of created files.

ACCEPTANCE CRITERIA
- All files exist and README links match filenames exactly.
- Every topic has Beginner → Intermediate → Advanced sections + copy-paste Angular examples.
- Final checkpoint topic contains CI gates + flaky test policy + axe fixes checklist.

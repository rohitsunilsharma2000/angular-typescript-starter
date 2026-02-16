# Quality gates checklist demo

Mock quality gates (lint, typecheck, tests, a11y, bundle size). You can simulate failures via FAIL env var.

## Commands
```bash
npm install
npm run demo                # runs mocked gates
FAIL=lint,tests npm run demo # simulate failing lint + tests
npm run typecheck           # tsc --noEmit
```

## Expected output (pass)
```
Quality gates demo (set FAIL=lint,tests to simulate failures)
✅ lint — ESLint/import-boundary rules (mocked ESLint run)
✅ typecheck — tsc --noEmit (mocked tsc run)
✅ tests — unit/integration tests (mocked test suite)
✅ a11y — basic accessibility checks (mocked axe run)
✅ bundle-size — bundle size budget (mocked budget check)

Summary: all gates passed
```

## Break/fix ideas
- Run with `FAIL=a11y` to see failure handling.
- Add a new gate (e.g., `e2e`) in `src/gates.ts` and rerun.
- Wire these commands into your CI as separate steps.

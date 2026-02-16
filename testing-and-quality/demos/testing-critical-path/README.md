# Testing critical path demo

Coverage: auth → dashboard → patients list → appointment create → billing view.

Steps
1) Copy snippets from `../snippets` into your Angular app's `src/app/testing/`.
2) Ensure `HttpClientTestingModule`, `provideRouter`, Playwright setup present.
3) Run
   - Unit: `npm run test:ci`
   - Integration: feature route spec
   - E2E: `npx playwright test demos/snippets/playwright-smoke.spec.ts`
4) Check a11y: `npx playwright test demos/snippets/a11y-axe.spec.ts`

Success criteria
- Auth + main flow smoke green
- Loading→success→error component test covers patient list
- Axe serious/critical = 0

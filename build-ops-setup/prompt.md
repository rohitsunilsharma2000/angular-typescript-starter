You are working inside my repository.

PRIMARY SOURCE OF TRUTH (MUST READ FIRST)
- build-ops-security/README.md
If it does not exist, create it in the same format style as my other learning modules:
  - Intro (Bangla + HMS context)
  - How to use
  - Topic list with links (sequence-wise)
  - Quick revision
  - Prerequisites
  - Demo section

GOAL
1) Create (or update) folder: build-ops-security/
2) Ensure build-ops-security/README.md exists and matches the style above.
3) Create all topic markdown files referenced by the README under:
   build-ops-security/topics/*.md
4) Every topic file must include these sections in THIS EXACT ORDER:
   1) শিরোনাম + 3–5 লাইন পরিচিতি (বাংলা, হাসপাতাল উদাহরণ)
   2) Why this matters (real-world)
   3) Concepts (Beginner → Intermediate → Advanced) as separate subsections
   4) Copy-paste Example (compile-ready snippets: Angular + YAML + config)
   5) Try it (Beginner task + Advanced task)
   6) Common mistakes
   7) Interview points
   8) Done when… (checklist)

LANGUAGE & STYLE RULES
- Write in Bengali with necessary technical English terms.
- No emojis.
- Examples must be HMS flavored: auth, patients, appointments, billing dashboards.
- Prefer Angular standalone components and route-level providers.
- Keep examples minimal but working and realistic.

CONTENT REQUIREMENTS (must be covered across topics)
A) Environments & configs
   - build-time environment.ts vs runtime config (assets/config.json)
   - env var strategy for API base URL, feature flags, Sentry DSN
   - what cannot be a “secret” in frontend
B) Secret handling
   - GitHub Actions secrets / Vercel / Netlify env vars
   - never commit secrets; document do/don’t list
C) API gateway auth + HttpInterceptor auth/refresh
   - attach token per request
   - 401 refresh + single-flight refresh + queued retry/replay
   - logout on refresh failure
D) CI/CD: GitHub Actions
   - pipeline: lint → test → build → preview deploy
   - Node 22 LTS pin
   - artifact caching (npm + build cache)
E) Deploy targets
   - static hosting (Netlify/Vercel) + SPA fallback
   - SSR container deploy for Angular Universal (Node 22), Dockerfile basics
F) Perf budgets in CI
   - bundle size guard with explicit KB budget
   - optional lighthouse CI note
G) Sourcemaps + error monitoring
   - sourcemaps upload strategy (don’t expose in prod)
   - Angular global error handler hook
   - mention Sentry and basic OpenTelemetry idea (concept)
H) Security hygiene
   - CSP basics (starter policy + report-only rollout)
   - sanitization pitfalls; trusted types concept; avoid bypassSecurityTrust*
I) Dependency auditing
   - npm audit policy; depcheck; lockfile discipline

WHAT TO SHIP (must appear in README + final checkpoint topic)
- CI/CD workflow file
- env var strategy doc + runtime config loader example
- auth interceptor + refresh + queued retry
- error monitoring hook (Sentry init + global error handler pattern)
- bundle-size guard + dependency scan

CHECKPOINT (final topic must include)
- Preview deploy per PR
- bundle-size check in pipeline
- dependency scan passes
- no secrets in repo; documented policy

DEFAULT FILES TO CREATE (if README specifies different names, follow README)
A) build-ops-security/README.md

B) build-ops-security/topics/
  01-environments-config-strategy.md
  02-secrets-handling.md
  03-auth-model-ui.md
  04-auth-interceptor-refresh.md
  05-github-actions-pipeline.md
  06-preview-deploy-per-pr.md
  07-deploy-targets-static-ssr.md
  08-perf-budgets-bundle-guard.md
  09-sourcemaps-error-monitoring.md
  10-csp-basics.md
  11-sanitization-trusted-types.md
  12-dependency-auditing.md
  13-checkpoint-done.md

C) Demos scaffold (docs + minimal snippets; do not build a full app unless repo already expects it)
  build-ops-security/demos/ops-pipeline-auth/README.md
  build-ops-security/demos/config-runtime/README.md
  build-ops-security/demos/snippets/
    runtime-config-loader.ts
    auth.interceptor.ts
    auth-refresh.service.ts
    github-actions-ci.yml
    bundle-size-guard.js
    sentry-init.ts
    global-error-handler.ts
    Dockerfile.ssr

COPY-PASTE EXAMPLE REQUIREMENTS
- Angular snippets must include imports and be compile-ready.
- Provide at least one complete interceptor + refresh queue example.
- Provide a complete GitHub Actions workflow YAML example using Node 22 + caching.
- Provide a simple bundle-size guard script example with a KB budget variable.
- Provide a simple CSP starter policy example (as docs/snippet).
- Provide npm audit + depcheck commands and CI step example.

PROCESS
1) Read build-ops-security/README.md first.
2) If missing, create README with the requested format and include the topic list.
3) Create topics in sequence. Keep each topic beginner-friendly with advanced notes.
4) Ensure all README links match filenames exactly.
5) At the end, print a short summary of created files.

ACCEPTANCE CRITERIA
- All files exist and README links match filenames.
- Every topic includes Beginner → Intermediate → Advanced sections and copy-paste examples.
- Final checkpoint topic includes preview deploy + bundle guard + dependency scan gates.

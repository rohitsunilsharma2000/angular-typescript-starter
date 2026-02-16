# Build, Ops, Security টিউটোরিয়াল (বাংলা, হাসপাতাল ম্যানেজমেন্ট উদাহরণ)

এই ফোল্ডার থেকে Angular UI Developer হিসেবে **Build + Ops + Security hygiene** শিখবেন—Environments/config, secret handling, auth interceptor + refresh, GitHub Actions CI/CD, preview deploy, SSR container deploy, perf budgets in CI, sourcemaps + error monitoring (Sentry/OpenTelemetry ধারণা), CSP/sanitization/trusted types, dependency audit—সবই **হাসপাতাল ম্যানেজমেন্ট** (HMS) উদাহরণে।

## কীভাবে ব্যবহার করবেন

1. প্রথমে `build-ops-setup/README.md` পড়ে env strategy + GitHub Actions + deploy target সেট করুন।
2. তারপর `topics` ফোল্ডারের নোটগুলো **ক্রম অনুযায়ী** পড়ুন।
3. হাতে-কলম অনুশীলনের জন্য `demos/ops-pipeline-auth` দেখুন (CI pipeline + interceptor + bundle guard sample)।

---

## টপিক লিস্ট (Sequence অনুসারে)

* [01) Environments & config strategy](topics/01-environments-config-strategy.md)
  `environment.ts` বনাম runtime config (`assets/config.json`), feature flags, API base URL, per-env mapping, “কী কখন build-time” বনাম “runtime” — HMS deployment উদাহরণ।

* [02) Secrets handling (frontend reality)](topics/02-secrets-handling.md)
  ব্রাউজারে secret রাখা যায় না—কি public, কি private; GitHub/Vercel/Netlify secrets; token vs secret পার্থক্য; “never commit” rules।

* [03) Auth fundamentals (JWT/OAuth2/OIDC) + UI responsibilities](topics/03-auth-model-ui.md)
  UI কী করবে (login flow, token attach, guard) আর backend কী করবে; token storage tradeoffs (memory/localStorage/cookie), role/permission UI gating।

* [04) HttpInterceptor: auth attach + refresh + retry queue](topics/04-auth-interceptor-refresh.md)
  access token attach, 401 handling, refresh single-flight (একটাই refresh in-flight), pending requests queue + replay, refresh fail → logout; concurrency edge cases।

* [05) CI/CD: GitHub Actions pipeline (lint → test → build → preview deploy)](topics/05-github-actions-pipeline.md)
  Node 22 LTS pin, caching (npm + build), artifact upload/download, job separation, PR checks structure।

* [06) Preview deploy per PR (Netlify/Vercel pattern)](topics/06-preview-deploy-per-pr.md)
  PR preview URL comment, env mapping for preview, minimal deploy targets, rollback concept; HMS staging vs preview workflow।

* [07) Deploy targets: Static hosting vs SSR container](topics/07-deploy-targets-static-ssr.md)
  SPA fallback routing, caching headers, functions/edge basics; SSR হলে Node 22 container, Dockerfile concept, health checks, reverse proxy idea।

* [08) Perf budgets in CI + bundle size guard](topics/08-perf-budgets-bundle-guard.md)
  KB budgets define (initial JS/CSS), fail pipeline if exceeded, bundle analysis workflow, optional Lighthouse CI concept।

* [09) Sourcemaps + error monitoring (Sentry/OpenTelemetry basics)](topics/09-sourcemaps-error-monitoring.md)
  prod sourcemaps strategy (upload but don’t expose), GlobalErrorHandler pattern, user/session context tagging, basic tracing idea (frontend সীমাবদ্ধতা সহ)।

* [10) Security hygiene: CSP basics](topics/10-csp-basics.md)
  CSP কেন দরকার, starter policy, script-src restrictions, nonce/hash concept, report-only mode, rollout strategy।

* [11) Sanitization + Trusted Types (XSS defense)](topics/11-sanitization-trusted-types.md)
  Angular sanitization কীভাবে কাজ করে, `bypassSecurityTrust*` pitfalls, Trusted Types ধারণা, safe rendering patterns (HMS rich text note example)।

* [12) Dependency auditing: npm audit + depcheck](topics/12-dependency-auditing.md)
  audit severity policy, allowlist strategy, lockfile discipline, depcheck দিয়ে unused deps remove, supply-chain hygiene।

* [13) Checkpoint: Ship list + Definition of Done](topics/13-checkpoint-done.md)
  CI/CD workflow present, env var strategy documented, auth interceptor+refresh shipped, error monitoring hook exists, preview deploy per PR, bundle-size guard + dependency scan passes—final checklist।

---

## What to ship (এই সিরিজ শেষ করলে যা বানাতে পারবেন)

* `.github/workflows/ci.yml` (lint → test → build → preview deploy)
* env var strategy (build-time vs runtime config)
* Auth HttpInterceptor + refresh + queued retry
* Error monitoring hook (Sentry init/GlobalErrorHandler)
* Bundle-size guard + dependency scan step in CI

---

## দ্রুত রিভিশন

* প্রতিটি ফাইলে থাকবে:

  * “কেন দরকার”
  * “Beginner → Intermediate → Advanced”
  * “Copy-paste Example” (config/yaml/angular snippets)
  * “Try it” tasks
  * “Common mistakes” + “Interview points”
  * “Done when…” checklist

---

## প্রাক-প্রয়োজন

* Angular + TypeScript + RxJS basics
* GitHub Actions basics
* JWT/OAuth2 high-level understanding
* Basic web security concepts (XSS/CSP)

---

## Demo

* `demos/ops-pipeline-auth`

  * Auth interceptor + refresh sample
  * Bundle-size guard example
  * CI pipeline skeleton

* `demos/config-runtime`

  * runtime config json + loader pattern docs

---

চাইলে আমি এর জন্যও Codex prompt বানিয়ে দিচ্ছি যাতে `build-ops-security/` ফোল্ডার + সব `topics/*.md` auto-generate হয় (Beginner→Advanced + working copy-paste examples সহ)।

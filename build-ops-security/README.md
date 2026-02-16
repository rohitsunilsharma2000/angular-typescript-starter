# Build, Ops & Security (বাংলা, হাসপাতাল ম্যানেজমেন্ট উদাহরণ)

Angular HMS অ্যাপ (auth, patients, appointments, billing) সুরক্ষিত ও নির্ভরযোগ্যভাবে বিল্ড/ডেপ্লয় করতে এই মডিউল। কভারেজ: env/config, secrets, auth interceptor+refresh, CI/CD (GitHub Actions), deploy targets (static/SSR), perf budgets, sourcemaps/error monitoring, CSP/sanitization, dependency auditing।

## কীভাবে ব্যবহার করবেন
1) `topics/` তালিকা ক্রমে পড়ুন; প্রতিটিতে কপি-পেস্টযোগ্য কোড/কনফিগ আছে।
2) ডেমো দেখুন: `demos/ops-pipeline-auth` (CI + auth interceptor) ও `demos/config-runtime` (runtime config loader)।
3) প্রতিটি টপিক শেষে “Done when…” চেক করুন; README-র “What to ship” মিলিয়ে নিন।

## টপিক লিস্ট
* [01) Environments & runtime config strategy](topics/01-environments-config-strategy.md)
* [02) Secrets handling (GitHub Actions/Vercel/Netlify)](topics/02-secrets-handling.md)
* [03) Auth model in UI (tokens, refresh)](topics/03-auth-model-ui.md)
* [04) Auth interceptor + refresh queue](topics/04-auth-interceptor-refresh.md)
* [05) GitHub Actions pipeline (lint→test→build→preview)](topics/05-github-actions-pipeline.md)
* [06) Preview deploy per PR](topics/06-preview-deploy-per-pr.md)
* [07) Deploy targets: static SPA + SSR container](topics/07-deploy-targets-static-ssr.md)
* [08) Perf budgets & bundle guard](topics/08-perf-budgets-bundle-guard.md)
* [09) Sourcemaps + error monitoring](topics/09-sourcemaps-error-monitoring.md)
* [10) CSP basics (report-only rollout)](topics/10-csp-basics.md)
* [11) Sanitization & trusted types](topics/11-sanitization-trusted-types.md)
* [12) Dependency auditing & lockfile discipline](topics/12-dependency-auditing.md)
* [13) Checkpoint: Definition of Done](topics/13-checkpoint-done.md)

## দ্রুত রিভিশন
- প্রতিটি ফাইলে: কেন দরকার → Beginner/Intermediate/Advanced → Copy-paste Example → Try it → Common mistakes → Interview points → Done when।
- Angular standalone + route providers, HttpInterceptor, YAML workflow, bundle guard JS, CSP policy, Sentry init অন্তর্ভুক্ত।

## প্রাক-প্রয়োজন
- Node 18+ (pipeline Node 22 LTS), Angular/TypeScript, GitHub Actions basics, Docker মৌলিক জ্ঞান (SSR হলে)।

## Demo
- `demos/ops-pipeline-auth`: GitHub Actions workflow, auth interceptor + refresh queue snippets।
- `demos/config-runtime`: runtime config loader + env doc।

## What to ship
- CI/CD workflow file
- env var strategy doc + runtime config loader
- auth interceptor + refresh + queued retry
- error monitoring hook (Sentry init + global error handler)
- bundle-size guard + dependency scan

## Checkpoint
- Preview deploy per PR
- Bundle-size check passes
- Dependency scan passes
- No secrets committed; policy documented

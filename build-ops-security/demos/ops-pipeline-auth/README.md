# ops-pipeline-auth demo

কি পাবেন
- GitHub Actions workflow (`demos/snippets/github-actions-ci.yml`)
- Auth interceptor + refresh queue (`auth.interceptor.ts`, `auth-refresh.service.ts`)
- Bundle-size guard script

চালানোর ধাপ
1) workflow ফাইল `.github/workflows/` এ কপি করুন; secrets সেট করুন (API_BASE, SENTRY_DSN)।
2) `auth.interceptor.ts` ও `auth-refresh.service.ts` অ্যাপে যুক্ত করে `provideHttpClient(withInterceptors(...))` এ যোগ করুন।
3) `bundle-size-guard.js` CI স্টেপে চালান।

লক্ষ্য
- CI: lint → test → build → bundle guard → preview deploy।
- Auth কলে 401 হলে single refresh + queued retry।

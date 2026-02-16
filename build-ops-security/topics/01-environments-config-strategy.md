# 01) Environments & runtime config strategy

হাসপাতাল অ্যাপে API URL, feature flag, Sentry DSN build-time না runtime কোনটা হবে ঠিক করা জরুরি। env.ts, assets/config.json উভয় উদাহরণ।

## Why this matters (real-world)
- Prod/stage আলাদা URL; ভুল config হলে patient data ভুল সার্ভারে যায়।
- Secrets ভুল করে bundle এ গেলে লিক।

## Concepts
### Beginner
- Angular `environment.ts` build-time replace।
- Runtime config (e.g., `assets/config.json`) fetch করে bootstrap।
### Intermediate
- Feature flags (billing_v2), API base, Sentry DSN env var strategy।
- What is NOT secret in frontend (tokens/keys visible to client)।
### Advanced
- Multi-tenant config; fallback order; cache busting; config validation।

## Copy-paste Example
```ts
// src/environments/environment.ts
export const environment = {
  production: false,
  apiBase: 'http://localhost:3000',
  featureFlags: { billingV2: false },
  sentryDsn: ''
};
```
```ts
// src/environments/environment.prod.ts
export const environment = {
  production: true,
  apiBase: 'https://api.hms.example.com',
  featureFlags: { billingV2: true },
  sentryDsn: '__SENTRY_DSN__'
};
```
```json
// src/assets/config.json (runtime override)
{
  "apiBase": "https://api.hms.example.com",
  "featureFlags": { "billingV2": true },
  "sentryDsn": "__RUNTIME_SENTRY__"
}
```
```ts
// src/main.ts (runtime config loader)
import { enableProdMode, importProvidersFrom } from '@angular/core';
import { bootstrapApplication } from '@angular/platform-browser';
import { HttpClient, provideHttpClient } from '@angular/common/http';
import { AppComponent } from './app/app.component';
import { environment as buildEnv } from './environments/environment';

fetch('/assets/config.json')
  .then(res => res.ok ? res.json() : {})
  .catch(() => ({}))
  .then((runtime) => {
    const config = { ...buildEnv, ...runtime };
    if (config.production) enableProdMode();
    return bootstrapApplication(AppComponent, {
      providers: [
        provideHttpClient(),
        { provide: 'APP_CONFIG', useValue: config }
      ]
    });
  });
```

## Try it
- Beginner: apiBase runtime JSON দিয়ে ওভাররাইড করে দেখুন; console.log(AppConfig)।
- Advanced: config validation যোগ করুন (zod), invalid হলে bootstrap ব্যর্থ করুন।

## Common mistakes
- Sentry DSN বা API key Git-এ কমিট করা।
- Runtime config cache না bust করা (stale JSON)।

## Interview points
- Build-time vs runtime config trade-off; কী সিক্রেট নয় (frontend)।

## Done when…
- Build env + runtime config উভয় নমুনা সেট।
- APP_CONFIG injection কাজ করে।
- Secret নয় এমন মানসমূহ ডকুমেন্টেড; secret গুলো env থেকে আসে।

# 09) Sourcemaps + error monitoring

HMS অ্যাপের JS ত্রুটি ট্রেস করতে sourcemap আপলোড ও Sentry/OpenTelemetry হুক রাখুন; প্রডে public sourcemap উন্মুক্ত নয়।

## Why this matters (real-world)
- প্রড বাগ খুঁজে বের করা সহজ হয়।
- Sourcemap লিক হলে কোড এক্সপোজ।

## Concepts
### Beginner
- build: hidden sourcemap (`sourceMap: true, hiddenSourceMap: true`), upload to Sentry।
- Angular global error handler।
### Intermediate
- Release version/tag; DSN env var; sampling rate।
- Do not serve sourcemap publicly।
### Advanced
- OpenTelemetry trace context, link errors to deploy; filter PII।

## Copy-paste Example
```ts
// sentry-init.ts
import * as Sentry from '@sentry/angular-ivy';
Sentry.init({
  dsn: import.meta.env['SENTRY_DSN'],
  tracesSampleRate: 0.1,
  release: 'hms-frontend@1.0.0'
});
```
```ts
// global-error-handler.ts
import { ErrorHandler, Injectable } from '@angular/core';
import * as Sentry from '@sentry/angular-ivy';
@Injectable({ providedIn: 'root' })
export class GlobalErrorHandler implements ErrorHandler {
  handleError(error: any): void {
    Sentry.captureException(error);
    console.error(error);
  }
}
```
```yaml
# angular.json excerpt
"configurations": {
  "production": {
    "sourceMap": true,
    "hiddenSourceMap": true
  }
}
```
```bash
# Sentry upload (example)
npx sentry-cli releases files hms-frontend@1.0.0 upload-sourcemaps dist/browser
```

## Try it
- Beginner: hiddenSourceMap true করে build করুন; dist/browser map public নয় কিনা দেখুন।
- Advanced: sentry-cli দিয়ে sourcemap আপলোড dry-run; release name মিলান।

## Common mistakes
- Sourcemap public serve; DSN কমিট করা।
- Release/tag mismatch → events unmapped।

## Interview points
- Hidden sourcemap + upload; global error handler; PII safe logging।

## Done when…
- Sentry init + error handler wired।
- hiddenSourceMap enabled; upload ধাপ নথিভুক্ত।
- DSN/env strategy লেখা।

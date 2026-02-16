import * as Sentry from '@sentry/angular-ivy';

export function initSentry(config: { dsn: string; release: string }) {
  Sentry.init({
    dsn: config.dsn,
    release: config.release,
    tracesSampleRate: 0.1,
  });
}

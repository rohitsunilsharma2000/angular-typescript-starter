// Example interceptor placeholder
export function httpLogger(request: unknown, next: (req: unknown) => unknown) {
  // no-op: pass through
  return next(request);
}

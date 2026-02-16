import { Observable, Subscription } from 'rxjs';

// Minimal signal-like shape for demo (Angular নয়, কিন্তু একই মনস্তত্ত্ব)
export type Signal<T> = { (): T; destroy(): void };

export function fromObservable<T>(source$: Observable<T>): Signal<T> {
  let latest: T | undefined;
  const sub: Subscription = source$.subscribe(v => { latest = v; });
  const fn = (() => latest as T) as Signal<T>;
  fn.destroy = () => sub.unsubscribe();
  return fn;
}

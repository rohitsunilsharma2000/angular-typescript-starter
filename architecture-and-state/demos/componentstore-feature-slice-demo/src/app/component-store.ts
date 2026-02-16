import { BehaviorSubject, Observable, Subject, Subscription } from 'rxjs';
import { distinctUntilChanged, map } from 'rxjs/operators';

// Tiny ComponentStore-like helper (no Angular needed)
export class ComponentStore<S> {
  private stateSubject: BehaviorSubject<S>;

  constructor(initialState: S) {
    this.stateSubject = new BehaviorSubject(initialState);
  }

  select<R>(project: (state: S) => R): Observable<R> {
    return this.stateSubject.pipe(map(project), distinctUntilChanged());
  }

  setState(next: S) {
    this.stateSubject.next(next);
  }

  patchState(partial: Partial<S>) {
    this.stateSubject.next({ ...this.stateSubject.value, ...partial });
  }

  updater<Args extends any[]>(recipe: (state: S, ...args: Args) => S) {
    return (...args: Args) => {
      this.stateSubject.next(recipe(this.stateSubject.value, ...args));
    };
  }

  effect<Origin>(generator: (origin$: Observable<Origin>) => Observable<any>) {
    const origin$ = new Subject<Origin>();
    const subscription = generator(origin$).subscribe();
    const trigger = (value: Origin) => origin$.next(value);
    // expose unsubscribe for cleanup
    (trigger as any).destroy = () => subscription.unsubscribe();
    return trigger as typeof trigger & { destroy: () => void };
  }

  get snapshot(): S {
    return this.stateSubject.value;
  }
}

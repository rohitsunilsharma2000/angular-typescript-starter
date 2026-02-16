# 08) ComponentStore feature slice

বেগিনার-বাংলা: ComponentStore (বা এই ডেমোতে তার মিনিমাল সংস্করণ) দিয়ে একেকটা ফিচারের state + effect আলাদা রাখা যায়। নিচের CLI ডেমো চালিয়ে selectors/updaters/effects বুঝে নিন।

## Things to learn (Bengali + beginner)
1) State local রাখুন: feature slice = শুধু ওই রুট/ডোমেনের ডাটা।
2) Updater vs effect: updater pure state change; effect async কাজ (API), শেষে updater কল।
3) Selector: শুধু view-model পড়ার জন্য; shared observable vs snapshot।
4) Loading/error field স্পষ্ট রাখুন: store নিজে সেট করবে, UI শুধু consume করবে।
5) Cleanup: effect subscription আনসাবস্ক্রাইব করতে ভুলবেন না (ডেমোতে destroy হুক দেয়া আছে)।

## Hands-on (commands + কী দেখবেন)
1) রেডি ডেমো চালান:
   ```bash
   cd architecture-and-state/demos/componentstore-feature-slice-demo
   npm install
   npm run demo       # load + add success + add fail
   npm run typecheck  # টাইপ/কনফিগ ঠিক আছে কিনা
   ```
2) Expected লগ: load idle → add success (নতুন todo) → add failure (error='Network 500', data আগের মতো থাকে, loading idle)।
3) Break/fix: `catchError` কমেন্ট করলে error হ্যান্ডল না হওয়া দেখুন; `shouldFail: true` বদলে success ফ্লো দেখুন।
4) নিজে বানাতে চাইলে: `mkdir -p src/app` করে নিচের "Demos" ফাইল কপি করুন।

### Done when
- Updater/effect/selector পার্থক্য বলতে পারেন, লগ মিলে যায়।
- Failure-এ loading idle ও error সেট হচ্ছে, data নষ্ট হচ্ছে না।

## Demos (copy-paste)
`architecture-and-state/demos/componentstore-feature-slice-demo/src/` থেকে মূল কোড:
```ts
// app/component-store.ts (mini helper)
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { map, distinctUntilChanged } from 'rxjs/operators';
export class ComponentStore<S> {
  private stateSubject: BehaviorSubject<S>;
  constructor(initialState: S) { this.stateSubject = new BehaviorSubject(initialState); }
  select<R>(project: (state: S) => R): Observable<R> { return this.stateSubject.pipe(map(project), distinctUntilChanged()); }
  setState(next: S) { this.stateSubject.next(next); }
  patchState(partial: Partial<S>) { this.stateSubject.next({ ...this.stateSubject.value, ...partial }); }
  updater<Args extends any[]>(recipe: (state: S, ...args: Args) => S) { return (...args: Args) => this.stateSubject.next(recipe(this.stateSubject.value, ...args)); }
  effect<Origin>(generator: (origin$: Observable<Origin>) => Observable<any>) {
    const origin$ = new Subject<Origin>();
    const sub = generator(origin$).subscribe();
    const trigger = (v: Origin) => origin$.next(v);
    (trigger as any).destroy = () => sub.unsubscribe();
    return trigger as typeof trigger & { destroy: () => void };
  }
  get snapshot(): S { return this.stateSubject.value; }
}
```
```ts
// app/api.ts
import { Todo } from './types';
import { delay, of, throwError } from 'rxjs';
export class TodoApi {
  private todos: Todo[] = [
    { id: 't1', title: 'Admit patient', done: false },
    { id: 't2', title: 'Prep discharge', done: false }
  ];
  list() { return of(this.todos).pipe(delay(40)); }
  add(title: string, shouldFail = false) {
    if (shouldFail) return throwError(() => new Error('Network 500'));
    this.todos = [...this.todos, { id: crypto.randomUUID(), title, done: false }];
    return of(this.todos).pipe(delay(40));
  }
}
```
```ts
// app/todo.store.ts
import { ComponentStore } from './component-store';
import { TodoApi } from './api';
import { Todo, ViewState } from './types';
import { catchError, of, switchMap, tap } from 'rxjs';

export class TodoStore extends ComponentStore<ViewState> {
  constructor(private api: TodoApi) {
    super({ todos: [], loading: 'idle' });
    this.loadTodos();
  }
  readonly setTodos = this.updater((s, todos: Todo[]) => ({ ...s, todos }));
  readonly setLoading = this.updater((s, l: ViewState['loading']) => ({ ...s, loading: l }));
  readonly setError = this.updater((s, e?: string) => ({ ...s, error: e }));

  readonly loadTodos = this.effect<void>(origin$ => origin$.pipe(
    tap(() => { this.setLoading('loading'); this.setError(undefined); }),
    switchMap(() => this.api.list().pipe(
      tap(todos => this.setTodos(todos)),
      tap(() => this.setLoading('idle')),
      catchError(err => { this.setError(err.message ?? 'Failed'); this.setLoading('idle'); return of([]); })
    ))
  ));

  readonly addTodo = this.effect<{ title: string; shouldFail?: boolean }>(origin$ => origin$.pipe(
    tap(() => { this.setLoading('saving'); this.setError(undefined); }),
    switchMap(({ title, shouldFail }) => this.api.add(title, !!shouldFail).pipe(
      tap(todos => this.setTodos(todos)),
      tap(() => this.setLoading('idle')),
      catchError(err => { this.setError(err.message ?? 'Failed'); this.setLoading('idle'); return of([]); })
    ))
  ));
}
```
```ts
// main.ts
import { TodoApi } from './app/api';
import { TodoStore } from './app/todo.store';

async function run() {
  const store = new TodoStore(new TodoApi());
  await new Promise(r => setTimeout(r, 60));
  console.log('\n=== after load ===');
  console.log(store.snapshot);
  store.addTodo({ title: 'Check vitals' });
  await new Promise(r => setTimeout(r, 60));
  console.log('\n=== after add success ===');
  console.log(store.snapshot);
  store.addTodo({ title: 'Failing task', shouldFail: true });
  await new Promise(r => setTimeout(r, 60));
  console.log('\n=== after add failure ===');
  console.log(store.snapshot);
  (store.loadTodos as any).destroy?.();
  (store.addTodo as any).destroy?.();
}
run();
```

## Ready-to-run demo (repo bundle)
- Path: `architecture-and-state/demos/componentstore-feature-slice-demo`
- Commands:
  ```bash
  cd architecture-and-state/demos/componentstore-feature-slice-demo
  npm install
  npm run demo
  npm run typecheck
  ```
- Expected output: load idle → add success (todo বাড়ে) → add failure (error='Network 500', loading idle)।
- Test ideas: `shouldFail` false করে সবুজ ফ্লো দেখুন; `catchError` কমেন্ট করে দেখুন error bubble হলে কী হয়; নতুন selector (completed count) যোগ করে লগ করুন।

## Common mistakes
- effect থেকে state সেট না করা (UI stale)।
- error সেট করলেও loading idle না করা।
- destroy না করে effect সাবস্ক্রিপশন ঝুলিয়ে রাখা।

## Interview points
- ComponentStore কেন NgRx global store এর চেয়ে lightweight feature slice।
- updater/effect separation, memoized selectors এর প্রয়োজনীয়তা।
- Cleanup ও side-effect isolation।

## Quick practice
- `npm run demo` চালিয়ে আউটপুট মিলান।
- `addTodo` তে delay বাড়িয়ে optimistic UI ভাবুন।
- ছোট ফিচার (e.g., Notifications) এর জন্য নিজের slice লিখে দেখুন।

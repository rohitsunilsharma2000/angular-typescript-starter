# 05) RxJS service store + signals bridge

বেগিনারদের জন্য: RxJS BehaviorSubject-ভিত্তিক সার্ভিস স্টোর থেকে কীভাবে signal-ধাঁচের getter বানিয়ে UI/কম্পোনেন্টে ফিড দেবেন (Angular ছাড়াই বোঝার জন্য মিনিমাল ডেমো)।

## Things to learn (Bengali + layman)
1) **Service store (RxJS)**: BehaviorSubject এ state রাখা, `select` করে Observable আউট করা।
2) **Signals bridge**: Observable → signal getter wrapper, যাতে `todosSignal()` কল করলেই লেটেস্ট ভ্যালু মেলে।
3) **Where to use**: Angular signals UI-তে, RxJS data/side-effect এ; ব্রিজ রাখলে দুই দুনিয়ার মাঝে coupling কমে।
4) **Double-subscribe এড়ানো**: distinctUntilChanged/selectors দিয়ে duplicate emission কমান।
5) **Cleanup**: signal/destroy সময় subscription ছাড়তে হবে, নইলে মেমরি লিক।

## Hands-on (step-by-step + commands)
1) রেডি ডেমো চালান:
   ```bash
   cd architecture-and-state/demos/rxjs-store-signals-bridge-demo
   npm install
   npm run demo       # BehaviorSubject -> signal আউটপুট দেখুন
   npm run typecheck  # টাইপ/কনফিগ ঠিক আছে কিনা
   ```
2) কী দেখবেন:
   - শুরুতে `undefined` (signal এখনও ভ্যালু পায়নি)।
   - দুইটা `add` পরে signal array দেখাবে।
   - `toggle` এর পর প্রথম আইটেমের `done` true হবে।
3) ব্রেক/ফিক্স এক্সপেরিমেন্ট:
   - `fromObservable` এর `destroy()` কল না করে দেখুন (কোড কমেন্ট করুন) → সাবস্ক্রিপশন ঝুলে থাকে; বাস্তবে এটি মেমরি/teardown ইস্যু।
   - `distinctUntilChanged` সরিয়ে দিন → একই স্টেট রেফারেন্সে-ও emission পাবেন; লগ ভরবে।
4) নিজে বানাতে চাইলে: `mkdir -p src/app` করে নিচের "Demos" কোড কপি করুন।

### Done when
- কমান্ড চালিয়ে প্রত্যাশিত তিন ধাপের আউটপুট পেয়েছেন।
- ব্রিজ/ডেস্ট্রয় ও distinctUntilChanged এর প্রভাব বোঝেন।

## Demos (copy-paste)
`architecture-and-state/demos/rxjs-store-signals-bridge-demo/src/` থেকে মূল ফাইল:
```ts
// app/store.ts
import { BehaviorSubject, Observable } from 'rxjs';
import { map, distinctUntilChanged } from 'rxjs/operators';

export type Todo = { id: string; title: string; done: boolean };
export type TodoState = { todos: Todo[] };

export class TodoStore {
  private state$ = new BehaviorSubject<TodoState>({ todos: [] });

  add(title: string) {
    const todo: Todo = { id: crypto.randomUUID(), title, done: false };
    const next = { todos: [...this.state$.value.todos, todo] };
    this.state$.next(next);
  }

  toggle(id: string) {
    const next = {
      todos: this.state$.value.todos.map(t =>
        t.id === id ? { ...t, done: !t.done } : t
      )
    };
    this.state$.next(next);
  }

  selectAll(): Observable<Todo[]> {
    return this.state$.pipe(map(s => s.todos), distinctUntilChanged());
  }
}
```
```ts
// app/signals-bridge.ts
import { Observable, Subscription } from 'rxjs';

export type Signal<T> = { (): T; destroy(): void };

export function fromObservable<T>(source$: Observable<T>): Signal<T> {
  let latest: T | undefined;
  const sub: Subscription = source$.subscribe(v => { latest = v; });
  const fn = (() => latest as T) as Signal<T>;
  fn.destroy = () => sub.unsubscribe();
  return fn;
}
```
```ts
// main.ts
import { TodoStore } from './app/store';
import { fromObservable } from './app/signals-bridge';

async function run() {
  const store = new TodoStore();
  const todosSignal = fromObservable(store.selectAll());

  console.log('--- Initial ---');
  console.log(todosSignal());

  store.add('Write docs');
  store.add('Ship demo');
  await new Promise(r => setTimeout(r, 0));
  console.log('\n--- After adds (signal) ---');
  console.log(todosSignal());

  const firstId = todosSignal()?.[0]?.id;
  if (firstId) store.toggle(firstId);
  await new Promise(r => setTimeout(r, 0));
  console.log('\n--- After toggle (signal) ---');
  console.log(todosSignal());

  todosSignal.destroy();
}

run();
```

## Ready-to-run demo (repo bundle)
- Path: `architecture-and-state/demos/rxjs-store-signals-bridge-demo`
- Commands:
  ```bash
  cd architecture-and-state/demos/rxjs-store-signals-bridge-demo
  npm install
  npm run demo
  npm run typecheck
  ```
- Expected output (সংক্ষিপ্ত):
  ```
  --- Initial ---
  undefined

  --- After adds (signal) ---
  [ { id: '...', title: 'Write docs', done: false }, { id: '...', title: 'Ship demo', done: false } ]

  --- After toggle (signal) ---
  [ { id: '...', title: 'Write docs', done: true }, { id: '...', title: 'Ship demo', done: false } ]
  ```
- Test ideas:
  - `distinctUntilChanged` সরালে duplicate emission লক্ষ্য করুন।
  - `destroy()` না করলে সম্ভাব্য লিক/লগ বাড়বে—ডেমোতে নিজে পরীক্ষা করুন।

## Common mistakes
- signal ব্রিজে subscription teardown ভুলে যাওয়া।
- একই store থেকে সরাসরি `subscribe` করে UI-তে setState কল করা (double source of truth)।
- selectors এ memo/distinct না করা।

## Interview points
- কেন RxJS data/side-effect আর signals UI reactive tree—ব্রিজে separation রাখার সুবিধা।
- Teardown/cleanup কেন জরুরি (component destroy, route change)।
- selectors + distinctUntilChanged এর পারফরম্যান্স ইফেক্ট।

## Quick practice
- `npm run demo` চালান; আউটপুট মিলে কিনা দেখুন।
- `toggle` অন্য আইডিতে দিন, লগ কীভাবে বদলায় দেখুন।
- fromObservable কে পরিবর্তন করে ব্যাক-প্রেশার/বাফার যোগ করার চিন্তা করুন।

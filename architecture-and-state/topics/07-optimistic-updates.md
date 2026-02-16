# 07) Optimistic updates + rollback

লেম্যান-বাংলা: সার্ভার রেসপন্সের আগে UI আপডেট দেখাই (optimistic), ব্যর্থ হলে আগের state-এ ফিরে যাই (rollback)। ছোট ডেমো চালিয়ে ধারণা নিন।

## Things to learn (Bengali + beginner)
1) Optimistic path: server round-trip অপেক্ষা না করে UI তাত্ক্ষণিক আপডেট; UX দ্রুত মনে হয়।  
2) Rollback path: API ফেল করলে পুরনো state ধরে রাখতে হবে, নইলে UI ভুল।  
3) Loading signal: `loading: 'optimistic' | 'rollback'` রাখলে UI স্পিনার/লেবেল ঠিক করা সহজ।  
4) Error surfacing: rollback পর error বার্তা দেখিয়ে দিন (toast/inline), কিন্তু state ঠিক আগের মতো করুন।  
5) Retry option: ব্যর্থ হলে পুনরায় চেষ্টা—state সাফ করে নতুন optimistic রান।  

## Hands-on (commands + কী দেখবেন)
1) রেডি ডেমো চালান:  
   ```bash
   cd architecture-and-state/demos/optimistic-updates-demo
   npm install
   npm run demo       # success + failure + rollback লগ দেখুন
   npm run typecheck  # টাইপ সেফটি
   ```
2) Expected লগ: load → optimistic success (t1 done=true) → optimistic fail+rollback (t2 ব্যর্থ, error='Network 500', state আগের মতো)।  
3) Break/fix: `store.toggle('t2', true)` কে `false` দিন → সব success; rollback লাইন কমেন্ট করলে দেখবেন ব্যর্থতার পরে UI ভুল থাকে।  

## Demos (copy-paste)
`architecture-and-state/demos/optimistic-updates-demo/src/` থেকে মূল কোড:
```ts
// app/api.ts
import { Todo } from './types';
const wait = (ms: number) => new Promise(res => setTimeout(res, ms));
export class FakeApi {
  private todos: Todo[] = [
    { id: 't1', title: 'Draft SOP', done: false },
    { id: 't2', title: 'Review PR', done: false }
  ];
  async toggle(id: string, shouldFail = false): Promise<Todo[]> {
    await wait(40);
    if (shouldFail) throw new Error('Network 500');
    this.todos = this.todos.map(t => (t.id === id ? { ...t, done: !t.done } : t));
    return this.todos;
  }
  async list(): Promise<Todo[]> {
    await wait(10);
    return this.todos;
  }
}
```
```ts
// app/store.ts
import { FakeApi } from './api';
import { Todo } from './types';

export type ViewState = {
  todos: Todo[];
  loading: 'idle' | 'loading' | 'optimistic' | 'rollback';
  error?: string;
};

export class TodoStore {
  private state: ViewState = { todos: [], loading: 'idle' };
  constructor(private api: FakeApi) {}

  snapshot(): ViewState { return { ...this.state, todos: [...this.state.todos] }; }

  async load(): Promise<void> {
    this.set({ loading: 'loading', error: undefined });
    this.set({ todos: await this.api.list(), loading: 'idle' });
  }

  async toggle(id: string, simulateFail = false): Promise<void> {
    const prev = this.snapshot();
    this.set({
      todos: this.state.todos.map(t => (t.id === id ? { ...t, done: !t.done } : t)),
      loading: 'optimistic',
      error: undefined
    });
    try {
      const serverTodos = await this.api.toggle(id, simulateFail);
      this.set({ todos: serverTodos, loading: 'idle' });
    } catch (err: any) {
      this.set({ ...prev, loading: 'rollback', error: err?.message ?? 'Failed' });
      this.set({ ...this.state, loading: 'idle' });
    }
  }

  private set(patch: Partial<ViewState>) { this.state = { ...this.state, ...patch }; }
}
```
```ts
// main.ts
import { FakeApi } from './app/api';
import { TodoStore } from './app/store';

function logState(label: string, state: ReturnType<TodoStore['snapshot']>) {
  console.log(`\\n=== ${label} ===`);
  console.log({ loading: state.loading, error: state.error, todos: state.todos });
}

async function run() {
  const store = new TodoStore(new FakeApi());
  await store.load();
  logState('after load', store.snapshot());
  await store.toggle('t1', false);
  logState('after optimistic success', store.snapshot());
  await store.toggle('t2', true);
  logState('after optimistic fail+rollback', store.snapshot());
}
run();
```

## Ready-to-run demo (repo bundle)
- Path: `architecture-and-state/demos/optimistic-updates-demo`
- Commands:
  ```bash
  cd architecture-and-state/demos/optimistic-updates-demo
  npm install
  npm run demo
  npm run typecheck
  ```
- Expected output (সংক্ষিপ্ত): load idle → t1 done=true → t2 fail causes rollback + error='Network 500', final state সঠিক থাকে।
- Test ideas: rollback লাইন কমেন্ট করুন; দুই টগল-ই success করে UI পার্থক্য দেখুন; optimistic state-এ স্পিনার/disabled UI কল্পনা করে যুক্ত করুন।

## Common mistakes
- rollback না করে optimistic UI রেখে দেওয়া (ghost success)।  
- error বার্তা রেখে state ফিরিয়ে না নেওয়া (UI inconsistent)।  
- optimistic flag না দেখিয়ে ইউজারকে pending/disabled ইঙ্গিত না দেওয়া।  

## Interview points
- কেন optimistic UX দ্রুত, এবং rollback প্রয়োজনীয়।  
- API idempotency ও conflict পরিস্থিতিতে optimistic কীভাবে নিরাপদ করবেন।  
- Logging: server error হলেও rollback/previous-state log রাখা।  

## Quick practice
- `npm run demo` চালিয়ে তিনটি স্টেপের লগ মিলিয়ে দেখুন।  
- simulateFail=false করে pure success ফ্লো চালান।  
- ডিবাউন্স/কিউ: একাধিক optimistic অপ দ্রুত পাঠালে কী করবেন ভাবুন (queue বা versioning)।  

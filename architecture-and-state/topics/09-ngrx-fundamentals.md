# 09) NgRx fundamentals

লেম্যান-বাংলা: action → reducer → selector → effect এই চার ধাপ বুঝুন; এখানে Angular ছাড়া ছোট ডেমো আছে, পরে একই প্যাটার্ন Angular NgRx API তে বসান।

## Things to learn (beginner → intermediate → advanced)
- Beginner: action স্ট্রিং, pure reducer, state shape `{data, loading, error}`; selector = state পড়ার ফাংশন; store.dispatch → reducer.
- Intermediate: effect দিয়ে async API call (switchMap), error হ্যান্ডলিং; loading flag; success/failure action।
- Advanced: feature-level provideState/provideEffects (Angular), memoized selectors with props, guard against duplicate loads (lastUpdated/force flag)।

## Hands-on (commands + কী দেখবেন)
1) রেডি ডেমো চালান:
   ```bash
   cd architecture-and-state/demos/ngrx-fundamentals-demo
   npm install
   npm run demo       # load success + failure
   npm run typecheck  # টাইপ ঠিক আছে কিনা
   ```
2) Expected আউটপুট: প্রথম লোডে data আসে, দ্বিতীয় লোডে error='Server 500' কিন্তু আগের data থাকে, loading=false।
3) Break/fix: `store.setFailNext(true/false)` বদলে success/fail দেখুন; reducer-এ loading flag যোগ/সরিয়ে লগে পার্থক্য দেখুন; নতুন selector যোগ করে `main.ts` এ ব্যবহার করুন।

## Demos (copy-paste)
`architecture-and-state/demos/ngrx-fundamentals-demo/src/` থেকে মূল ফাইল:
```ts
// app/actions.ts
import { Appointment } from './types';
export type Action =
  | { type: 'load' }
  | { type: 'load success'; data: Appointment[] }
  | { type: 'load failure'; error: string };
export const AppointmentsActions = {
  load: (): Action => ({ type: 'load' }),
  loadSuccess: (data: Appointment[]): Action => ({ type: 'load success', data }),
  loadFailure: (error: string): Action => ({ type: 'load failure', error })
};
```
```ts
// app/reducer.ts
import { Action } from './actions';
import { State } from './types';
export const initialState: State = { data: [], loading: false };
export function reducer(state: State, action: Action): State {
  switch (action.type) {
    case 'load': return { ...state, loading: true, error: undefined };
    case 'load success': return { ...state, loading: false, data: action.data };
    case 'load failure': return { ...state, loading: false, error: action.error };
    default: return state;
  }
}
```
```ts
// app/store.ts
import { BehaviorSubject, Subject, filter, map, switchMap } from 'rxjs';
import { Action, AppointmentsActions } from './actions';
import { FakeApi } from './fake-api';
import { reducer, initialState } from './reducer';
import { State } from './types';
export class Store {
  private state$ = new BehaviorSubject<State>(initialState);
  private actions$ = new Subject<Action>();
  private subscriptions: Array<{ unsubscribe: () => void }> = [];
  private shouldFail = false;
  constructor(private api: FakeApi) {
    const sub = this.actions$.pipe(map(a => reducer(this.state$.value, a))).subscribe(s => this.state$.next(s));
    this.subscriptions.push(sub);
    const loadEffect = this.actions$.pipe(filter(a => a.type === 'load')).pipe(
      switchMap(() => this.api.list(this.shouldFail).then(
        data => AppointmentsActions.loadSuccess(data),
        err => AppointmentsActions.loadFailure(err.message ?? 'Failed')
      ))
    ).subscribe(a => this.dispatch(a));
    this.subscriptions.push(loadEffect);
  }
  setFailNext(v: boolean) { this.shouldFail = v; }
  dispatch(action: Action) { this.actions$.next(action); }
  select(): State { return this.state$.value; }
  destroy() { this.subscriptions.forEach(s => s.unsubscribe()); }
}
```
```ts
// app/fake-api.ts
import { Appointment } from './types';
const wait = (ms: number) => new Promise(res => setTimeout(res, ms));
export class FakeApi {
  private data: Appointment[] = [
    { id: 'a1', patient: 'Ayman', slot: '10:00' },
    { id: 'a2', patient: 'Nadia', slot: '10:30' }
  ];
  async list(simulateFail = false): Promise<Appointment[]> {
    await wait(50);
    if (simulateFail) throw new Error('Server 500');
    return this.data;
  }
}
```
```ts
// main.ts
import { Store } from './app/store';
import { AppointmentsActions } from './app/actions';
import { FakeApi } from './app/fake-api';
function logState(label: string, state: ReturnType<Store['select']>) {
  console.log(`\n=== ${label} ===`); console.log(state);
}
async function run() {
  const store = new Store(new FakeApi());
  store.dispatch(AppointmentsActions.load());
  await new Promise(r => setTimeout(r, 70));
  logState('after load success', store.select());
  store.setFailNext(true);
  store.dispatch(AppointmentsActions.load());
  await new Promise(r => setTimeout(r, 70));
  logState('after load failure', store.select());
  store.destroy();
}
run();
```

## Ready-to-run demo (repo bundle)
- Path: `architecture-and-state/demos/ngrx-fundamentals-demo`
- Commands:
  ```bash
  cd architecture-and-state/demos/ngrx-fundamentals-demo
  npm install
  npm run demo
  npm run typecheck
  ```
- Expected output: success load (data 2 আইটেম, loading=false) → failure load (error='Server 500', data আগের মতো, loading=false)।
- Test ideas: loading flag reducer-এ যোগ/সরান; আরেকটি action (Add) বানিয়ে reduce করুন; selector ফাংশন লিখে `main.ts` এ ব্যবহার করুন।

## Common mistakes
- reducer pure না রাখা (API call ভিতরে করা)।
- error set করেও loading false না করা।
- effect subscribe না করে থাকা (stream চালু হয় না)।

## Interview points
- Action → reducer → selector → effect গল্প টাইটভাবে বলতে পারা।
- কেন pure reducer গুরুত্বপূর্ণ; কেন effects এ side-effect রাখা হয়।
- Standalone Angular এ provideState/provideEffects দিয়ে slice scope করা যায়।

## Quick practice
- `npm run demo` চালান; তারপর `setFailNext(false)` করে দুইবার success দেখে নিন।
- New selector যোগ করুন (e.g., selectLoading) এবং লগ করুন।
- Debounce/throttle যোগ করার কথা ভাবুন যখন লোড বাটন বারবার চাপা হয়।

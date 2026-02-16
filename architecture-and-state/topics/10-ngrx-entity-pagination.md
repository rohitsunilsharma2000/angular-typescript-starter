# 10) NgRx Entity + pagination/filtering

লেম্যান-বাংলা: Entity adapter দিয়ে IDs + entities রাখলে pagination/filter সহজ হয়। এই ডেমো Angular ছাড়া—প্যাটার্ন বুঝে নিলে NgRx Entity API-তে map হবে।

## Things to learn (beginner → intermediate → advanced)
- Beginner: Entity shape `{ ids, entities }` + state `{ pagination, loading, error }`; action = load page/filter; reducer pure।
- Intermediate: Effect দিয়ে API কল; total গণনা; failure এ পুরনো data রাখা।
- Advanced: Memo selectors (page slice, byId), cache guard (same page/filter skip), delete/insert updateOne/removeOne।

## Hands-on (commands + কী দেখবেন)
1) রেডি ডেমো চালান:
   ```bash
   cd architecture-and-state/demos/ngrx-entity-pagination-demo
   npm install
   npm run demo       # page 1 → page 2 → filter → failure
   npm run typecheck  # টাইপ সেফটি
   ```
2) Expected লগ: page1 মোট 25; page2 অন্য ids; filter "5" এ total 3; failure এর পর error আসে কিন্তু আগের filtered data বজায় থাকে।
3) Break/fix: `setFailNext(true)` সরিয়ে success-only দেখুন; `pageSize` 3 করে pagination বদলান; `removeOne` ব্যবহার করে delete সিমুলেট করুন।

## Demos (copy-paste)
`architecture-and-state/demos/ngrx-entity-pagination-demo/src/` থেকে মূল কোড:
```ts
// app/entity-adapter.ts
export type EntityState<T extends { id: string }> = { ids: string[]; entities: Record<string, T> };
export function getInitialState<T extends { id: string }>(): EntityState<T> { return { ids: [], entities: {} }; }
export function setAll<T extends { id: string }>(state: EntityState<T>, items: T[]): EntityState<T> {
  const ids = items.map(i => i.id); const entities: Record<string, T> = {}; items.forEach(i => (entities[i.id] = i));
  return { ids, entities };
}
export function addMany<T extends { id: string }>(state: EntityState<T>, items: T[]): EntityState<T> {
  const ids = [...state.ids]; const entities = { ...state.entities } as Record<string, T>;
  for (const item of items) { if (!entities[item.id]) ids.push(item.id); entities[item.id] = item; }
  return { ids, entities };
}
export function removeOne<T extends { id: string }>(state: EntityState<T>, id: string): EntityState<T> {
  const ids = state.ids.filter(x => x !== id); const { [id]: _, ...entities } = state.entities; return { ids, entities } as EntityState<T>;
}
```
```ts
// app/fake-api.ts
import { Patient } from './types';
const wait = (ms: number) => new Promise(res => setTimeout(res, ms));
const MASTER: Patient[] = Array.from({ length: 25 }).map((_, idx) => ({ id: `p${idx + 1}`, name: `Patient ${idx + 1}`, ward: idx % 2 === 0 ? 'A' : 'B' }));
export class FakeApi {
  async list(page: number, pageSize: number, filter = '', fail = false) {
    await wait(40);
    if (fail) throw new Error('Server 500');
    const filtered = filter ? MASTER.filter(p => p.name.toLowerCase().includes(filter.toLowerCase())) : MASTER;
    const start = (page - 1) * pageSize;
    const data = filtered.slice(start, start + pageSize);
    return { data, total: filtered.length };
  }
}
```
```ts
// app/actions.ts
import { Patient } from './types';
export type Action =
  | { type: 'load page'; page: number; pageSize: number; filter: string }
  | { type: 'load success'; data: Patient[]; total: number; page: number; pageSize: number; filter: string }
  | { type: 'load failure'; error: string };
export const Actions = {
  loadPage: (page: number, pageSize: number, filter = ''): Action => ({ type: 'load page', page, pageSize, filter }),
  loadSuccess: (data: Patient[], total: number, page: number, pageSize: number, filter: string): Action => ({ type: 'load success', data, total, page, pageSize, filter }),
  loadFailure: (error: string): Action => ({ type: 'load failure', error })
};
```
```ts
// app/reducer.ts
import { Action } from './actions';
import { EntityState, getInitialState, setAll } from './entity-adapter';
import { Patient, ViewState } from './types';
const initialState: ViewState = {
  data: getInitialState<Patient>(),
  pagination: { page: 1, pageSize: 5, total: 0, filter: '' },
  loading: false,
  error: undefined
};
export function reducer(state: ViewState = initialState, action: Action): ViewState {
  switch (action.type) {
    case 'load page':
      return { ...state, loading: true, error: undefined, pagination: { ...state.pagination, page: action.page, pageSize: action.pageSize, filter: action.filter } };
    case 'load success':
      return { ...state, loading: false, data: setAll(state.data, action.data), pagination: { page: action.page, pageSize: action.pageSize, total: action.total, filter: action.filter } };
    case 'load failure':
      return { ...state, loading: false, error: action.error };
    default: return state;
  }
}
export { initialState };
```
```ts
// app/store.ts
import { BehaviorSubject, Subject, filter, map, switchMap } from 'rxjs';
import { Actions, Action } from './actions';
import { reducer, initialState } from './reducer';
import { FakeApi } from './fake-api';
import { ViewState } from './types';
export class Store {
  private state$ = new BehaviorSubject<ViewState>(initialState);
  private actions$ = new Subject<Action>();
  private subs: Array<{ unsubscribe: () => void }> = [];
  private failNext = false;
  constructor(private api: FakeApi) {
    const sub = this.actions$.pipe(map(a => reducer(this.state$.value, a))).subscribe(s => this.state$.next(s));
    this.subs.push(sub);
    const loadEffect = this.actions$.pipe(filter(a => a.type === 'load page')).pipe(
      switchMap(a => this.api.list(a.page, a.pageSize, a.filter, this.failNext).then(
        ({ data, total }) => Actions.loadSuccess(data, total, a.page, a.pageSize, a.filter),
        err => Actions.loadFailure(err.message ?? 'Failed')
      ))
    ).subscribe(next => this.dispatch(next));
    this.subs.push(loadEffect);
  }
  dispatch(action: Action) { this.actions$.next(action); }
  select(): ViewState { return this.state$.value; }
  setFailNext(v: boolean) { this.failNext = v; }
  destroy() { this.subs.forEach(s => s.unsubscribe()); this.actions$.complete(); this.state$.complete(); }
}
```
```ts
// main.ts
import { Store } from './app/store';
import { Actions } from './app/actions';
import { FakeApi } from './app/fake-api';
function logState(label: string, state: ReturnType<Store['select']>) {
  console.log(`\n=== ${label} ===`);
  console.log({ page: state.pagination.page, pageSize: state.pagination.pageSize, total: state.pagination.total, filter: state.pagination.filter, loading: state.loading, error: state.error, ids: state.data.ids, first: state.data.ids[0] ? state.data.entities[state.data.ids[0]] : undefined });
}
async function run() {
  const store = new Store(new FakeApi());
  store.dispatch(Actions.loadPage(1, 5, ''));
  await new Promise(r => setTimeout(r, 70));
  logState('page 1', store.select());
  store.dispatch(Actions.loadPage(2, 5, ''));
  await new Promise(r => setTimeout(r, 70));
  logState('page 2', store.select());
  store.dispatch(Actions.loadPage(1, 5, '5'));
  await new Promise(r => setTimeout(r, 70));
  logState('filter name contains "5"', store.select());
  store.setFailNext(true);
  store.dispatch(Actions.loadPage(1, 5, ''));
  await new Promise(r => setTimeout(r, 70));
  logState('failure (keeps old data)', store.select());
  store.destroy();
}
run();
```

## Ready-to-run demo (repo bundle)
- Path: `architecture-and-state/demos/ngrx-entity-pagination-demo`
- Commands:
  ```bash
  cd architecture-and-state/demos/ngrx-entity-pagination-demo
  npm install
  npm run demo
  npm run typecheck
  ```
- Expected output: page1 total=25 → page2 অন্য IDs → filter "5" total=3 → failure পরে error দেখায় কিন্তু আগের filtered data থাকে।
- Test ideas: `pageSize` বদলান; `setFailNext(false)` রাখুন; delete scenario জন্য `removeOne` কল যোগ করুন।

## Common mistakes
- Entity state আপডেট না করে array replace করা (ids/entities mismatch)।
- total না সেট করে pagination UI বিভ্রান্ত করা।
- error এ পুরনো data মুছে ফেলা (UX ঝাঁকুনি)।

## Interview points
- কেন Entity adapter IDs + map দ্রুত lookup ও আপডেট দেয়।
- Pagination/filter request key (page+filter) ক্যাশিং; stale data vs UX trade-off।
- Effects এ debounce/mergeMap vs switchMap পছন্দ (newest wins)।

## Quick practice
- ডেমো চালিয়ে আউটপুট মিলান।
- filter + pagination একসাথে পরিবর্তন করে দেখুন total/ids কিভাবে বদলায়।
- cache guard ভাবুন: একই page/filter এ already-loaded হলে effect skip।

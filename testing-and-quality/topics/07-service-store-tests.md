# 07) Service + Facade/Store tests

RxJS store বা ComponentStore দিয়ে patient list state; optimistic rollback যাচাইয়ের টেস্ট লিখুন।

## Why this matters (real-world)
- Optimistic বাগ রোগীদের ডুপ্লিকেট এন্ট্রি দেখাতে পারে।
- Selector/derived state ভুলে UI glitch।

## Concepts
### Beginner
- Pure service test; selector equals expected.
### Intermediate
- ComponentStore updater/effect test; loading flags।
### Advanced
- Optimistic update + rollback assertion; memoized selector equality।

## Copy-paste Example
```ts
// patient.store.ts
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
export type Patient = { id: string; name: string };
@Injectable({ providedIn: 'root' })
export class PatientStore {
  private state = new BehaviorSubject<Patient[]>([]);
  readonly patients$ = this.state.asObservable();
  addOptimistic(p: Omit<Patient, 'id'>, save: (p: Omit<Patient,'id'>) => Promise<Patient>) {
    const temp: Patient = { id: 'temp-' + Math.random(), ...p } as Patient;
    this.state.next([...this.state.value, temp]);
    return save(p).then(saved => {
      this.state.next(this.state.value.map(x => x.id === temp.id ? saved : x));
    }).catch(err => {
      this.state.next(this.state.value.filter(x => x.id !== temp.id));
      throw err;
    });
  }
}
```
```ts
// patient.store.spec.ts
import { PatientStore } from './patient.store';
import { lastValueFrom } from 'rxjs';

describe('PatientStore', () => {
  it('reconciles optimistic update', async () => {
    const store = new PatientStore();
    const saved = { id: 'P1', name: 'Rima' };
    await store.addOptimistic({ name: 'Rima' }, async () => saved);
    const last = await lastValueFrom(store.patients$);
    expect(last).toEqual([saved]);
  });

  it('rolls back on error', async () => {
    const store = new PatientStore();
    await expect(store.addOptimistic({ name: 'Rima' }, async () => { throw new Error('fail'); })).rejects.toThrow('fail');
    const last = await lastValueFrom(store.patients$);
    expect(last).toEqual([]);
  });
});
```
```ts
// componentstore example selector test (ComponentStore)
import { TestBed } from '@angular/core/testing';
import { ComponentStore } from '@ngrx/component-store';
import { map } from 'rxjs/operators';

type State = { data: Patient[]; filter: string };
class PStore extends ComponentStore<State> {
  filter$ = this.select(s => s.filter);
  filtered$ = this.select(s => s.data, s => s.filter, (d,f) => d.filter(x => x.name.includes(f)));
}

describe('ComponentStore selectors', () => {
  it('filters by name', (done) => {
    const store = new PStore({ data: [{id:'1',name:'Rima'}], filter: 'Ri' });
    store.filtered$.subscribe(list => { expect(list.length).toBe(1); done(); });
  });
});
```

## Try it
- Beginner: selector result পরিবর্তন না হলে same reference থাকে কিনা পরীক্ষা করুন।
- Advanced: addOptimistic এ loading flag যোগ করে test করুন।

## Common mistakes
- BehaviorSubject value mutate করে next না দেওয়া।
- ComponentStore select combine misuse → recompute explosion।

## Interview points
- Optimistic rollback test; selector memoization।

## Done when…
- Service/store success + rollback টেস্ট আছে।
- Selector/derived state পরীক্ষা করা হয়েছে।

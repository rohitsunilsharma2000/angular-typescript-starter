# 08) ComponentStore feature slice

Patients তালিকা + filter + create optimistic উদাহরণ ComponentStore দিয়ে।

## Why this matters (real world)
- Feature-স্কোপড state; টেস্টেবল; side-effect আলাদা।
- ইন্টারভিউতে “ComponentStore vs Service store vs NgRx” প্রশ্নে কোড দেখাতে পারবেন।

## Concepts (beginner → intermediate → advanced)
- Beginner: state shape, updater, selector, vm$.
- Intermediate: effect with switchMap/catchError/finalize; loading flags; optimistic create (see topic 07 for rollback nuance)।
- Advanced: normalized entities, derived selectors (filtered list), tearDown().

## Copy-paste Example
```ts
// app/features/patients/patients.store.ts
import { Injectable } from '@angular/core';
import { ComponentStore } from '@ngrx/component-store';
import { HttpClient } from '@angular/common/http';
import { switchMap, tap, catchError, finalize, of } from 'rxjs';

type Patient = { id: string; name: string; ward: string };
interface State {
  data: Patient[];
  loading: boolean;
  filter: 'all' | 'icu' | 'general';
  error?: string;
}

@Injectable()
export class PatientsStore extends ComponentStore<State> {
  constructor(private http: HttpClient) {
    super({ data: [], loading: false, filter: 'all' });
  }

  readonly data$ = this.select(s => s.data);
  readonly loading$ = this.select(s => s.loading);
  readonly filter$ = this.select(s => s.filter);
  readonly filtered$ = this.select(this.data$, this.filter$, (data, filter) =>
    filter === 'all' ? data : data.filter(p => filter === 'icu' ? p.ward.startsWith('ICU') : !p.ward.startsWith('ICU'))
  );
  readonly vm$ = this.select({ patients: this.filtered$, loading: this.loading$, filter: this.filter$ });

  readonly setFilter = this.updater<'all' | 'icu' | 'general'>((state, filter) => ({ ...state, filter }));

  readonly load = this.effect((trigger$) => trigger$.pipe(
    tap(() => this.patchState({ loading: true, error: undefined })),
    switchMap(() => this.http.get<Patient[]>('/api/patients').pipe(
      tap(data => this.patchState({ data, loading: false })),
      catchError(err => {
        this.patchState({ loading: false, error: err.message ?? 'Load failed' });
        return of([]);
      }),
      finalize(() => this.patchState({ loading: false }))
    ))
  ));

  readonly createOptimistic = this.effect((input$: any) => input$.pipe(
    tap(({ name, ward }) => {
      const tempId = 'temp-' + crypto.randomUUID();
      this.patchState(s => ({ data: [...s.data, { id: tempId, name, ward }], loading: true }));
    }),
    switchMap(({ name, ward }) => this.http.post<Patient>('/api/patients', { name, ward }).pipe(
      tap(saved => this.patchState(s => ({ data: s.data.map(p => p.id.startsWith('temp-') ? saved : p), loading: false }))),
      catchError(err => {
        this.patchState(s => ({ data: s.data.filter(p => !p.id.startsWith('temp-')), loading: false, error: err.message }));
        return of(null);
      })
    ))
  ));
}
```
```ts
// app/features/patients/patients.container.ts
import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PatientsStore } from './patients.store';
import { PatientsView } from './patients.view';
@Component({
  standalone: true,
  selector: 'hms-patients-container-cs',
  imports: [CommonModule, PatientsView],
  providers: [PatientsStore],
  template: `<hms-patients-view [vm]="store.vm$ | async" (filterChange)="store.setFilter($event)" (create)="store.createOptimistic($event)"></hms-patients-view>`
})
export class PatientsContainerCs {
  store = inject(PatientsStore);
  constructor() { this.store.load()(undefined); }
}
```
```ts
// app/features/patients/patients.view.ts
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
@Component({
  standalone: true,
  selector: 'hms-patients-view',
  imports: [CommonModule],
  template: `
  <section class="space-y-2 bg-white p-4 rounded shadow">
    <div class="flex gap-2 items-center">
      <select class="border px-2 py-1" [value]="vm.filter" (change)="filterChange.emit(($event.target as HTMLSelectElement).value as any)">
        <option value="all">All</option>
        <option value="icu">ICU</option>
        <option value="general">General</option>
      </select>
      <button class="border px-2" (click)="create.emit({ name: 'Temp', ward: 'ICU-1' })">Quick Add</button>
    </div>
    <p *ngIf="vm.loading">Loading…</p>
    <p *ngIf="vm.error" class="text-red-600">{{ vm.error }}</p>
    <ul>
      <li *ngFor="let p of vm.patients">{{ p.name }} — {{ p.ward }}</li>
    </ul>
  </section>
  `
})
export class PatientsView {
  @Input({ required: true }) vm!: { patients: any[]; loading: boolean; filter: string; error?: string };
  @Output() filterChange = new EventEmitter<'all'|'icu'|'general'>();
  @Output() create = new EventEmitter<{ name: string; ward: string }>();
}
```

## Try it (exercise)
- Beginner: filter dropdown এ “OT” যোগ করে derived selector আপডেট করুন।
- Advanced: state normalize করুন (ids/entities) এবং selectedId, pagination যোগ করুন।

## Common mistakes
- effect এর মধ্যে subscribe করা (nested subscribe) → avoid।
- patchState না করে state mutate করা।
- error/ loading reset না করা।

## Interview points
- ComponentStore = scoped, testable, effect cancellation switchMap।
- Optimistic update rollback logic দেখাতে পারবেন।

## Done when…
- vm$ template এ চলছে; effect switchMap + catchError + finalize আছে।
- Optimistic create rollback করে।
- Derived selector filter কাজ করে।

## How to test this topic
1) VS Code: ComponentStore imports ও selectors টাইপ ঠিক আছে কিনা দেখুন; no implicit any.
2) Unit test: store.filtered$ observable expect output for a filter; effect success ও error কেস spy করুন (using TestingModule + HttpTestingController).
3) Runtime: `ng serve` → patients container এ filter dropdown ও quick add চাপুন; temp entry replace/rollback কাজ করছে কিনা লক্ষ্য করুন।  

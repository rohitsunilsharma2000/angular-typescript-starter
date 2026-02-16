# 09) NgRx fundamentals

Appointments cross-feature শেয়ার্ড হলে NgRx slice উপযোগী।

## Why this matters (real world)
- বহুল ব্যবহারযোগ্য ডেটা (appointments) বহু পেজে লাগবে।
- Effects দিয়ে side-effect আলাদা; testing সহজ।
- ইন্টারভিউ: action → reducer → selector স্টোরি।

## Concepts (beginner → intermediate → advanced)
- Beginner: actions, reducer, selectors, provideState/provideEffects (standalone-ready)।
- Intermediate: effects with switchMap/catchError; loading flags; router integration।
- Advanced: memoized selectors with props; derived UI flags; feature-level provideState.

## Copy-paste Example
```ts
// app/features/appointments/state/appointments.actions.ts
import { createActionGroup, props } from '@ngrx/store';
export const AppointmentsActions = createActionGroup({
  source: 'Appointments',
  events: {
    'Load': props<{ force?: boolean }>(),
    'Load Success': props<{ data: Appointment[] }>(),
    'Load Failure': props<{ error: string }>(),
  }
});
export type Appointment = { id: string; patient: string; slot: string };
```
```ts
// app/features/appointments/state/appointments.reducer.ts
import { createFeature, createReducer, on } from '@ngrx/store';
import { AppointmentsActions } from './appointments.actions';
export type State = { data: Appointment[]; loading: boolean; error?: string; lastUpdated?: number };
const initialState: State = { data: [], loading: false };
const feature = createFeature({
  name: 'appointments',
  reducer: createReducer(
    initialState,
    on(AppointmentsActions.load, (state) => ({ ...state, loading: true, error: undefined })),
    on(AppointmentsActions.loadSuccess, (state, { data }) => ({ ...state, data, loading: false, lastUpdated: Date.now() })),
    on(AppointmentsActions.loadFailure, (state, { error }) => ({ ...state, loading: false, error }))
  )
});
export const { name: appointmentsFeatureKey, reducer: appointmentsReducer, selectAppointmentsState } = feature;
export const { selectData, selectLoading, selectError } = feature;
```
```ts
// app/features/appointments/state/appointments.effects.ts
import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { HttpClient } from '@angular/common/http';
import { AppointmentsActions } from './appointments.actions';
import { switchMap, map, catchError, of, finalize } from 'rxjs';
@Injectable()
export class AppointmentsEffects {
  private actions$ = inject(Actions);
  private http = inject(HttpClient);

  load$ = createEffect(() => this.actions$.pipe(
    ofType(AppointmentsActions.load),
    switchMap(() => this.http.get<any[]>('/api/appointments').pipe(
      map(data => AppointmentsActions.loadSuccess({ data })),
      catchError(err => of(AppointmentsActions.loadFailure({ error: err.message ?? 'Failed' })))
    ))
  ));
}
```
```ts
// app/features/appointments/state/appointments.selectors.ts
import { createSelector } from '@ngrx/store';
import { selectAppointmentsState } from './appointments.reducer';
export const selectAppointmentsVm = createSelector(
  selectAppointmentsState,
  s => ({ data: s.data, loading: s.loading, error: s.error })
);
```
```ts
// app/features/appointments/appointments.provider.ts
import { provideState, provideEffects } from '@ngrx/store';
import { appointmentsFeatureKey, appointmentsReducer } from './state/appointments.reducer';
import { AppointmentsEffects } from './state/appointments.effects';
export const provideAppointmentsStore = [
  provideState(appointmentsFeatureKey, appointmentsReducer),
  provideEffects(AppointmentsEffects),
];
```
```ts
// app/features/appointments/appointments.routes.ts
import { Routes } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideAppointmentsStore } from './appointments.provider';
import { AppointmentsContainer } from './appointments.container';
export const APPT_ROUTES: Routes = [
  { path: '', component: AppointmentsContainer, providers: [provideHttpClient(), ...provideAppointmentsStore] }
];
```
```ts
// app/features/appointments/appointments.container.ts
import { Component, inject } from '@angular/core';
import { Store } from '@ngrx/store';
import { CommonModule } from '@angular/common';
import { selectAppointmentsVm } from './state/appointments.selectors';
import { AppointmentsActions } from './state/appointments.actions';
@Component({
  standalone: true,
  selector: 'hms-appointments-ngrx',
  imports: [CommonModule],
  template: `
    <button class="border px-2" (click)="reload()">Load</button>
    <p *ngIf="vm$ | async as vm">
      <span *ngIf="vm.loading">Loading…</span>
      <span *ngIf="vm.error" class="text-red-600">{{ vm.error }}</span>
      <ul><li *ngFor="let a of vm.data">{{ a.patient }} — {{ a.slot }}</li></ul>
    </p>
  `
})
export class AppointmentsContainer {
  private store = inject(Store);
  vm$ = this.store.select(selectAppointmentsVm);
  constructor() { this.reload(); }
  reload() { this.store.dispatch(AppointmentsActions.load({})); }
}
```

## Try it (exercise)
- Beginner: Load action dispatch করে selector তে empty state দেখান।
- Advanced: lastUpdated চেক করুন; 5s এর মধ্যে হলে load skip করতে guard effect লিখুন।

## Common mistakes
- provideState root এ দিয়ে lazy feature double registration।
- selector memoization ভুলে যাওয়া → recompute heavy।
- effects এ finalize ভুলে loading reset হয় না।

## Interview points
- Standalone provideState/provideEffects উল্লেখ করুন।
- Memoized selector + props উদাহরণ দিতে প্রস্তুত থাকুন।

## Done when…
- Actions/Reducer/Effects/Selectors সম্পূর্ণ ও compile হয়।
- Route-level providers দিয়ে slice scoped।
- Loading/error UI হ্যান্ডল।

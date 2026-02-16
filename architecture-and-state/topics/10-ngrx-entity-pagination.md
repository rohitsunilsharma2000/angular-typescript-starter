# 10) NgRx Entity + pagination/filtering

Billing invoice তালিকা normalized রাখলে pagination ও filter দ্রুত হয়।

## Why this matters (real world)
- বড় লিস্টে O(1) lookup; memoized selectors দ্রুত।
- Pagination/filter UI lightweight হয়।
- ইন্টারভিউ: Entity adapter ও selectors প্রশ্ন আসে।

## Concepts (beginner → intermediate → advanced)
- Beginner: Entity adapter ids/entities shape।
- Intermediate: selectors for page, filter params; derived total pages।
- Advanced: server-driven cursor pagination; parametric selectors; memoization।

## Copy-paste Example
```ts
// app/features/billing/state/billing.actions.ts
import { createActionGroup, props } from '@ngrx/store';
export const BillingActions = createActionGroup({
  source: 'Billing',
  events: {
    'Load Page': props<{ page: number; size: number; status?: string }>(),
    'Load Success': props<{ data: Invoice[]; total: number }>(),
    'Load Failure': props<{ error: string }>(),
  }
});
export type Invoice = { id: string; amount: number; status: 'paid' | 'due'; patient: string };
```
```ts
// app/features/billing/state/billing.reducer.ts
import { createEntityAdapter, EntityState } from '@ngrx/entity';
import { createFeature, createReducer, on } from '@ngrx/store';
import { BillingActions, Invoice } from './billing.actions';

type State = EntityState<Invoice> & { loading: boolean; error?: string; total: number; page: number; size: number; status?: string };
const adapter = createEntityAdapter<Invoice>();
const initialState: State = adapter.getInitialState({ loading: false, total: 0, page: 1, size: 10 });
const feature = createFeature({
  name: 'billing',
  reducer: createReducer(
    initialState,
    on(BillingActions.loadPage, (state, { page, size, status }) => ({ ...state, loading: true, error: undefined, page, size, status })),
    on(BillingActions.loadSuccess, (state, { data, total }) => adapter.setAll(data, { ...state, loading: false, total })),
    on(BillingActions.loadFailure, (state, { error }) => ({ ...state, loading: false, error }))
  )
});
export const { name: billingFeatureKey, reducer: billingReducer, selectBillingState } = feature;
export const { selectIds, selectEntities, selectAll } = adapter.getSelectors(selectBillingState);
export const selectPagination = feature.selectors.selectBillingState;
```
```ts
// app/features/billing/state/billing.selectors.ts
import { createSelector } from '@ngrx/store';
import { selectAll, selectBillingState } from './billing.reducer';
export const selectBillingVm = createSelector(selectBillingState, selectAll, (state, list) => ({
  list,
  loading: state.loading,
  error: state.error,
  page: state.page,
  size: state.size,
  total: state.total,
  pages: Math.ceil(state.total / state.size || 1),
  status: state.status,
}));
```
```ts
// app/features/billing/state/billing.effects.ts
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BillingActions } from './billing.actions';
import { switchMap, map, catchError, of } from 'rxjs';
@Injectable()
export class BillingEffects {
  private actions$ = inject(Actions);
  private http = inject(HttpClient);
  loadPage$ = createEffect(() => this.actions$.pipe(
    ofType(BillingActions.loadPage),
    switchMap(({ page, size, status }) => this.http.get<{ data: any[]; total: number }>(`/api/invoices?page=${page}&size=${size}&status=${status ?? ''}`).pipe(
      map(resp => BillingActions.loadSuccess({ data: resp.data, total: resp.total })),
      catchError(err => of(BillingActions.loadFailure({ error: err.message ?? 'Failed' })))
    ))
  ));
}
```
```ts
// app/features/billing/billing.routes.ts
import { Routes } from '@angular/router';
import { provideState, provideEffects } from '@ngrx/store';
import { billingFeatureKey, billingReducer } from './state/billing.reducer';
import { BillingEffects } from './state/billing.effects';
import { BillingContainer } from './billing.container';
import { provideHttpClient } from '@angular/common/http';
export const BILLING_ROUTES: Routes = [
  { path: '', component: BillingContainer, providers: [provideHttpClient(), provideState(billingFeatureKey, billingReducer), provideEffects(BillingEffects)] }
];
```
```ts
// app/features/billing/billing.container.ts
import { Component, inject } from '@angular/core';
import { Store } from '@ngrx/store';
import { CommonModule } from '@angular/common';
import { BillingActions } from './state/billing.actions';
import { selectBillingVm } from './state/billing.selectors';
@Component({
  standalone: true,
  selector: 'hms-billing-container',
  imports: [CommonModule],
  template: `
    <div *ngIf="vm$ | async as vm" class="space-y-2">
      <div class="flex gap-2 items-center">
        <button class="border px-2" (click)="page(-1)" [disabled]="vm.page===1">Prev</button>
        <button class="border px-2" (click)="page(1)">Next</button>
        <select [value]="vm.status ?? ''" (change)="status(($event.target as HTMLSelectElement).value)">
          <option value="">All</option>
          <option value="paid">Paid</option>
          <option value="due">Due</option>
        </select>
      </div>
      <p *ngIf="vm.loading">Loading…</p>
      <p *ngIf="vm.error" class="text-red-600">{{ vm.error }}</p>
      <ul>
        <li *ngFor="let i of vm.list">{{ i.patient }} — ₹{{ i.amount }} ({{ i.status }})</li>
      </ul>
      <p class="text-xs">Page {{ vm.page }} / {{ vm.pages }}</p>
    </div>
  `
})
export class BillingContainer {
  private store = inject(Store);
  vm$ = this.store.select(selectBillingVm);
  constructor() { this.store.dispatch(BillingActions.loadPage({ page: 1, size: 5 })); }
  page(delta: number) { this.store.dispatch(BillingActions.loadPage({ page: Math.max(1, (this.getPage() + delta)), size: 5, status: this.currentStatus })); }
  status(status: string) { this.currentStatus = status || undefined; this.store.dispatch(BillingActions.loadPage({ page: 1, size: 5, status: this.currentStatus })); }
  private currentStatus?: string;
  private getPage() { let value = 1; this.vm$.subscribe(vm => value = vm.page).unsubscribe(); return value; }
}
```

## Try it (exercise)
- Beginner: size 10 করলে pages হিসাব ঠিক আছে কিনা দেখুন।
- Advanced: cursor-based API ধরুন (`nextCursor`)—state এ রাখুন, selector আপডেট করুন।

## Common mistakes
- Entity adapter ব্যবহার না করে বড় লিস্টে O(n) lookup।
- memoized selector ছাড়া filter করলে change detection heavy।

## Interview points
- Entity adapter ids/entities shape ব্যাখ্যা করুন; selectors বের করার কথা বলুন।
- Pagination/filter state reducer-এ রাখার কথা উল্লেখ করুন।

## Done when…
- Entity adapter wired; pagination/filter selectors কাজ করে।
- Route-level provideState/provideEffects করা।

# 06) Loading/Error model standardization

HMS অ্যাপে patients list, billing invoice, pharmacy stock—সব জায়গায় এক loading/error ভোকাব দরকার।

## Why this matters (real world)
- UI আচরণ একরকম থাকলে QA কম প্রশ্ন করে।
- Observability সহজ: state log এ একই ফিল্ড।
- ইন্টারভিউ: error handling standard জিজ্ঞেস করা হয়।

## Concepts (beginner → intermediate → advanced)
- Beginner: state shape `{ loading: 'idle' | 'loading'; error?: string }`।
- Intermediate: আরও স্টেট `refreshing | saving | deleting`; error model `{ code, message, fieldErrors?, correlationId? }`।
- Advanced: empty/no-permission states; toast বনাম inline; retry/backoff; global error boundary।

## Copy-paste Example
```ts
// app/shared/data-access/state.model.ts
export type LoadingState = 'idle' | 'loading' | 'refreshing' | 'saving' | 'deleting';
export type ErrorModel = { code?: string; message: string; fieldErrors?: Record<string, string>; correlationId?: string };
export interface ViewState<T> {
  data: T;
  loading: LoadingState;
  error?: ErrorModel;
  lastUpdated?: number;
}
```
```ts
// app/features/billing/billing.store.ts
import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, finalize, of, tap } from 'rxjs';
import { ViewState } from '../../shared/data-access/state.model';

type Invoice = { id: string; amount: number; patient: string };
@Injectable({ providedIn: 'root' })
export class BillingStore {
  private state = signal<ViewState<Invoice[]>>({ data: [], loading: 'idle' });
  vm = computed(() => this.state());
  constructor(private http: HttpClient) {}
  load() {
    this.state.update(s => ({ ...s, loading: s.data.length ? 'refreshing' : 'loading', error: undefined }));
    this.http.get<Invoice[]>('/api/invoices').pipe(
      tap(data => this.state.set({ data, loading: 'idle', lastUpdated: Date.now() })),
      catchError(err => {
        this.state.update(s => ({ ...s, loading: 'idle', error: { message: err.message ?? 'Failed', code: err.status } }));
        return of([]);
      }),
      finalize(() => this.state.update(s => ({ ...s, loading: 'idle' })))
    ).subscribe();
  }
}
```
```ts
// app/features/billing/billing.view.ts
import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
@Component({
  standalone: true,
  selector: 'hms-billing-view',
  imports: [CommonModule],
  template: `
  <section class="p-4 bg-white rounded shadow">
    <ng-container [ngSwitch]="vm.loading">
      <p *ngSwitchCase="'loading'">Loading bills…</p>
      <p *ngSwitchCase="'refreshing'">Refreshing…</p>
    </ng-container>
    <p *ngIf="vm.error" class="text-red-600">{{ vm.error.message }}</p>
    <ul *ngIf="!vm.loading && !vm.error && vm.data.length">
      <li *ngFor="let i of vm.data">{{ i.patient }} — ₹{{ i.amount }}</li>
    </ul>
    <p *ngIf="!vm.loading && !vm.error && !vm.data.length">No invoices</p>
  </section>
  `
})
export class BillingView {
  @Input({ required: true }) vm!: { data: any[]; loading: string; error?: { message: string } };
}
```

## Try it (exercise)
- Beginner: `loading` enum-এ `saving` যোগ করে create flow-এ ব্যবহার করুন।
- Advanced: correlationId যোগ করে error toast এ দেখান; logger এ push করুন।

## Common mistakes
- loading boolean একটাই রেখে refresh/saving পৃথক না করা।
- error shape free-form রেখে UI তে null checks বাড়ানো।

## Interview points
- Standard state shape দেখান; refresh বনাম loading ভিন্ন করার কথা বলুন।

## Done when…
- Common state model ফাইল আছে; সব feature একই enum ব্যবহার করছে।
- UI তে loading/refreshing/error/empty আলাদা ভাবে হ্যান্ডল।

## How to test this topic
1) VS Code: state interface imports resolve; enum values autocomplete হচ্ছে কিনা দেখুন।
2) Component test: HttpTestingController দিয়ে success ও error stub করে loading/refresh/error UI assert করুন।
3) Browser: slow network throttle এ `/billing` পেজ চালিয়ে refresh state দেখুন; error হলে inline alert আসে কিনা।  

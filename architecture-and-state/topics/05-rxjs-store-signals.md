# 05) RxJS service store + signals bridge

Appointments তালিকা feature-এ RxJS store service দিয়ে state রাখা, signals দিয়ে template simple করা।

## Why this matters (real world)
- Http + caching + loading/error মান統; template সরল।
- Signals bridge করলে ভবিষ্যতে Angular signal APIs কাজে লাগে।

## Concepts (beginner → intermediate → advanced)
- Beginner: BehaviorSubject store shape {data, loading, error}।
- Intermediate: switchMap + catchError + finalize; shareReplay(1) দিয়ে caching।
- Advanced: signals bridge via `toSignal`; staleTime/forceReload; tearDown।

## Copy-paste Example
```ts
// app/features/appointments/appointments.store.ts
import { Injectable, computed, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, of } from 'rxjs';
import { switchMap, catchError, finalize, shareReplay } from 'rxjs/operators';

type State = { data: Appointment[]; loading: boolean; error?: string; lastUpdated?: number };
export type Appointment = { id: string; patient: string; slot: string };

@Injectable({ providedIn: 'root' })
export class AppointmentStore {
  private trigger$ = new BehaviorSubject<boolean>(true);
  private state$ = this.trigger$.pipe(
    switchMap(force => this.load(force)),
    shareReplay(1)
  );

  private signalState = signal<State>({ data: [], loading: false });
  vm = computed(() => this.signalState());

  constructor(private http: HttpClient) {
    this.state$.subscribe();
  }

  refresh(force = false) { this.trigger$.next(force); }

  private load(force: boolean) {
    const now = Date.now();
    const s = this.signalState();
    if (!force && s.lastUpdated && now - s.lastUpdated < 5000 && s.data.length) {
      return of(s); // cached
    }
    this.signalState.update(v => ({ ...v, loading: true, error: undefined }));
    return this.http.get<Appointment[]>('/api/appointments').pipe(
      switchMap(data => {
        this.signalState.set({ data, loading: false, lastUpdated: Date.now() });
        return of(this.signalState());
      }),
      catchError(err => {
        this.signalState.update(v => ({ ...v, loading: false, error: err.message ?? 'Load failed' }));
        return of(this.signalState());
      }),
      finalize(() => this.signalState.update(v => ({ ...v, loading: false })))
    );
  }
}
```
```ts
// app/features/appointments/appointments.container.ts
import { Component, computed, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { toSignal } from '@angular/core/rxjs-interop';
import { AppointmentStore } from './appointments.store';
import { AppointmentList } from './appointments.list';
@Component({
  standalone: true,
  selector: 'hms-appointments-container',
  imports: [CommonModule, AppointmentList],
  template: `<hms-appointment-list [vm]="vm()" (refresh)="store.refresh(true)"></hms-appointment-list>`
})
export class AppointmentContainer {
  store = inject(AppointmentStore);
  vm = computed(() => this.store.vm());
}
```
```ts
// app/features/appointments/appointments.list.ts
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
@Component({
  standalone: true,
  selector: 'hms-appointment-list',
  imports: [CommonModule],
  template: `
    <section class="p-4 bg-white rounded shadow space-y-2">
      <header class="flex justify-between items-center">
        <h2 class="font-semibold">Appointments</h2>
        <button class="text-sm px-3 py-1 border rounded" (click)="refresh.emit()">Refresh</button>
      </header>
      <ng-container *ngIf="!vm.loading && !vm.error; else stateBlock">
        <ul>
          <li *ngFor="let a of vm.data" class="flex justify-between">
            <span>{{ a.patient }}</span><span class="text-xs text-slate-500">{{ a.slot }}</span>
          </li>
        </ul>
      </ng-container>
      <ng-template #stateBlock>
        <p *ngIf="vm.loading">Loading…</p>
        <p *ngIf="vm.error" class="text-red-600">{{ vm.error }}</p>
      </ng-template>
    </section>
  `
})
export class AppointmentList {
  @Input({ required: true }) vm!: { data: any[]; loading: boolean; error?: string };
  @Output() refresh = new EventEmitter<void>();
}
```

## Try it (exercise)
- Beginner: stale cache সময় 5s থেকে 1s করুন এবং log করুন কতবার API কল হয়।
- Advanced: `refresh(force=true)` এ loading state `refreshing` আলাদা ফ্ল্যাগ যোগ করুন ও UI তে দেখান।

## Common mistakes
- BehaviorSubject initial undefined রেখে template ত্রুটি।
- catchError ছাড়া switchMap করলে stream break হয়ে যায়।
- shareReplay ছাড়া একই ডেটার জন্য বহু সাবস্ক্রিপশন।

## Interview points
- Signals bridge বোঝাতে `toSignal` বা signal state mention করুন।
- switchMap + finalize ব্যবহার করে loading reset করা দেখান।

## Done when…
- Store shape documented; cache policy লেখা।
- shareReplay(1) ব্যবহার হয়েছে।
- Template এ loading/error/empty branches আছে।

## How to test this topic
1) VS Code: hover করে store state টাইপ ঠিক আছে কিনা দেখুন; missing return/any নেই।
2) Unit/component test: ATL দিয়ে container render করে loading → success → error UI assert করুন; `HttpTestingController` দিয়ে API stub করুন।
3) Runtime: `ng serve` চালিয়ে Refresh বোতাম চাপুন; Network panel এ 5s cache window respected কিনা ও UI loading state কাজ করছে কিনা দেখুন।  

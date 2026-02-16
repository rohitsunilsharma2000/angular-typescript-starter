# 07) Optimistic updates + rollback

HMS appointments create ফর্ম সাবমিট করলে UI তৎক্ষণাৎ দেখায়, API ফেল করলে rollback।

## Why this matters (real world)
- ফাস্ট UX: নেটওয়ার্ক লেটেন্সি লুকায়।
- কিন্তু ডাটা কনসিস্টেন্সি: ফেল হলে ফিরিয়ে দিতে হবে।
- ইন্টারভিউতে “optimistic + rollback” জনপ্রিয় প্রশ্ন।

## Concepts (beginner → intermediate → advanced)
- Beginner: temp ID, UI তে সাথে সাথে যোগ।
- Intermediate: queue রাখুন; failure হলে rollback; toast/message।
- Advanced: conflict detection (version), retry policy, dedupe inflight।

## Copy-paste Example
```ts
// app/features/appointments/appointments.facade.ts
import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';

type Appointment = { id: string; patient: string; slot: string };
@Injectable({ providedIn: 'root' })
export class AppointmentsFacade {
  private list = signal<Appointment[]>([]);
  private loading = signal(false);
  private error = signal<string | undefined>(undefined);
  vm = computed(() => ({ items: this.list(), loading: this.loading(), error: this.error() }));
  constructor(private http: HttpClient) {}

  async load() {
    this.loading.set(true);
    try { this.list.set(await this.http.get<Appointment[]>('/api/appointments').toPromise()); }
    finally { this.loading.set(false); }
  }

  async createOptimistic(payload: Omit<Appointment, 'id'>) {
    const tempId = 'temp-' + crypto.randomUUID();
    const optimistic: Appointment = { id: tempId, ...payload };
    this.list.update(arr => [...arr, optimistic]);
    try {
      const saved = await this.http.post<Appointment>('/api/appointments', payload).toPromise();
      this.list.update(arr => arr.map(a => a.id === tempId ? saved : a));
    } catch (err: any) {
      this.list.update(arr => arr.filter(a => a.id !== tempId));
      this.error.set(err?.message ?? 'Create failed, rolled back');
    }
  }
}
```
```ts
// app/features/appointments/appointments.container.ts
import { Component, inject, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppointmentsFacade } from './appointments.facade';
import { AppointmentForm } from './appointments.form';
import { AppointmentList } from './appointments.list';
@Component({
  standalone: true,
  selector: 'hms-appointments-container',
  imports: [CommonModule, AppointmentForm, AppointmentList],
  template: `
  <div class="space-y-4">
    <hms-appointment-form (submitted)="facade.createOptimistic($event)"></hms-appointment-form>
    <hms-appointment-list [vm]="vm()"></hms-appointment-list>
    <p *ngIf="vm().error" class="text-red-600">{{ vm().error }}</p>
  </div>`
})
export class AppointmentContainer {
  facade = inject(AppointmentsFacade);
  vm = computed(() => this.facade.vm());
  constructor() { this.facade.load(); }
}
```
```ts
// app/features/appointments/appointments.form.ts
import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
@Component({
  standalone: true,
  selector: 'hms-appointment-form',
  imports: [CommonModule],
  template: `
  <form class="space-y-2" (ngSubmit)="submit()">
    <input class="border px-2 py-1" [(ngModel)]="patient" name="patient" placeholder="Patient" required />
    <input class="border px-2 py-1" [(ngModel)]="slot" name="slot" placeholder="Slot" required />
    <button class="border px-3 py-1" type="submit">Create</button>
  </form>`
})
export class AppointmentForm {
  patient = '';
  slot = '';
  @Output() submitted = new EventEmitter<{ patient: string; slot: string }>();
  submit() {
    this.submitted.emit({ patient: this.patient, slot: this.slot });
    this.patient = ''; this.slot = '';
  }
}
```

## Try it (exercise)
- Beginner: error হলে toast দেখান এবং rollback নিশ্চিত করুন।
- Advanced: optimistic queue রাখুন; ব্যর্থ হলে ব্যাচে rollback করুন এবং retry বাটন যোগ করুন।

## Common mistakes
- temp ID গুলো reconcile না করে stale UI রেখে দেওয়া।
- loading flag reset না করা।
- server থেকে ভিন্ন ordering এলে sort না করা।

## Interview points
- Explain temp id → replace/rollback flow; mention correlationId logging।
- উল্লেখ করুন কোন API গুলো optimistic safe (POST/PUT যা idempotent নয় তা ভাবতে হবে)।

## Done when…
- temp ID ব্যবহৃত; success এ replace, fail এ remove।
- error message surfacing আছে।
- unit test বা manual check দিয়ে rollback দেখা হয়েছে।

## How to test this topic
1) VS Code: interceptor/facade imports resolve; no red squiggles; signals typed। 
2) Unit/component test: mock API success ও failure দুইটাই করে দেখুন temp row replace বনাম rollback হয় কিনা। Testing Library + HttpTestingController ব্যবহার করুন।
3) Browser: `ng serve` → Create বাটন দু-তিনবার চাপুন; Network tab এ একবার error ইনজেক্ট করলে rollback ও error toast দেখুন, success এ temp আইটেম real data দিয়ে replace হচ্ছে।  

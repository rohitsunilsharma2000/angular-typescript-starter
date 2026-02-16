# 02) Smart vs Presentational

সংক্ষিপ্ত ভূমিকা: HMS ড্যাশবোর্ডে PatientsContainer (smart) ডেটা টানে, PatientsList (presentational) শুধু render করে।

## Why this matters (real world)
- দায়িত্ব আলাদা থাকায় UI বদলালে API স্তর ভাঙে না।
- টেস্ট সহজ: presentational component pure template হিসেবে টেস্ট করা যায়।
- ইন্টারভিউতে layering বোঝানো যায়।

## Concepts (beginner → intermediate → advanced)
- Beginner: smart/container data fetch + pass @Input; presentational কেবল template.
- Intermediate: ViewModel (vm) object/signal; change detection OnPush; route-level providers।
- Advanced: Facade service hide store choice (signals/ComponentStore/NgRx); composable UI (slots/inputs) reuse।

## Copy-paste Example
```ts
// app/features/patients/patients.routes.ts
import { Routes } from '@angular/router';
import { PatientsContainer } from './patients.container';
export const PATIENT_ROUTES: Routes = [
  { path: '', component: PatientsContainer, providers: [] }
];
```
```ts
// app/features/patients/patients.container.ts
import { Component, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PatientsListComponent } from './patients.list';
import { PatientApi } from './patients.api';
@Component({
  standalone: true,
  selector: 'hms-patients-container',
  imports: [CommonModule, PatientsListComponent],
  template: `<hms-patients-list [vm]="vm()" />`,
})
export class PatientsContainer {
  private api = inject(PatientApi);
  private patients = signal<{ id: string; name: string; bed: string }[]>([]);
  private loading = signal(false);
  vm = computed(() => ({ patients: this.patients(), loading: this.loading() }));
  constructor() { this.load(); }
  async load() {
    this.loading.set(true);
    try { this.patients.set(await this.api.fetchAll()); }
    finally { this.loading.set(false); }
  }
}
```
```ts
// app/features/patients/patients.list.ts
import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
@Component({
  standalone: true,
  selector: 'hms-patients-list',
  imports: [CommonModule],
  template: `
  <section class="p-4 bg-white rounded shadow">
    <h2 class="font-semibold">Patients</h2>
    <ng-container *ngIf="!vm.loading; else loading">
      <ul>
        <li *ngFor="let p of vm.patients" class="flex justify-between">
          <span>{{ p.name }}</span><span class="text-xs text-slate-500">{{ p.bed }}</span>
        </li>
      </ul>
    </ng-container>
    <ng-template #loading>Loading patients…</ng-template>
  </section>`
})
export class PatientsListComponent {
  @Input({ required: true }) vm!: { patients: { id: string; name: string; bed: string }[]; loading: boolean };
}
```
```ts
// app/features/patients/patients.api.ts
import { Injectable } from '@angular/core';
@Injectable({ providedIn: 'root' })
export class PatientApi {
  async fetchAll() { return [{ id: 'P1', name: 'Rima', bed: 'ICU-1' }]; }
}
```

## Try it (exercise)
- Beginner: PatientsList এ skeleton state (3টি gray div) যোগ করুন, loading true হলে দেখান।
- Advanced: PatientsContainer এ `computed` vm-এ a11y-ready empty/error flags যোগ করুন এবং template এ state-based UI দিন।

## Common mistakes
- Smart component থেকে DOM logic রাখা (JSX-style) → presentational এ নামিয়ে আনুন।
- Facade না করে সরাসরি HttpClient কল করে testability নষ্ট।

## Interview points
- Container-presentational split বুঝে বলুন: “low churn UI vs stable data flow”.
- উল্লেখ করুন route-level provider scope কিভাবে memory leak কমায়।

## Done when…
- Container শুধু data/orchestration; Presentational শুধু template/style.
- vm object/signal আছে; template এ state branches স্পষ্ট।
- Route-level providers ঠিকভাবে scoped।

## How to test this topic
1) VS Code: Ctrl/Cmd+Click করে container থেকে facade/service imports resolve হচ্ছে কিনা দেখুন; Problems panel এ unused input/output নেই নিশ্চিত করুন।
2) Unit/component test: Angular Testing Library দিয়ে container render করে loading→success assert করুন; presentational component এ @Input required ঠিক আছে কিনা।
3) Boundary: নিশ্চিত করুন container কোনো heavy UI barrel থেকে ইমপোর্ট করছে না (direct path); optional guard স্ক্রিপ্ট চালান।
4) Browser sanity: `ng serve` → patients route খুলে container-presentational split কাজ করছে কিনা (state updates reflect).  

# 03) Standalone routing + NgModule interop

Angular 17+ এ standalone default, কিন্তু অনেক কোডবেসে পুরনো NgModule আছে। HMS উদাহরণ: appointments feature standalone, পুরনো billing module পুনঃব্যবহার।

## Why this matters (real world)
- ধীরে ধীরে migration করা যায়; monolith ভাগ করা সহজ।
- Route-level provider scope ঠিক থাকলে memory leak কমে।
- ইন্টারভিউতে “interop strategy?” প্রশ্নে পয়েন্ট।

## Concepts (beginner → intermediate → advanced)
- Beginner: standalone component, standalone route, lazy load।
- Intermediate: route-level providers, mixing legacy NgModule via `imports`. 
- Advanced: provideHttpClient scoped, feature guard/resolver providers, avoiding provider duplication।

## Copy-paste Example
```ts
// app/app.routes.ts
import { Routes } from '@angular/router';
export const routes: Routes = [
  {
    path: 'appointments',
    loadChildren: () => import('./features/appointments/appointments.routes').then(m => m.APPOINTMENT_ROUTES),
  },
  { path: '', redirectTo: 'appointments', pathMatch: 'full' }
];
```
```ts
// app/features/appointments/appointments.routes.ts
import { Routes } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { AppointmentContainer } from './appointments.container';
import { AppointmentApi } from './appointments.api';
export const APPOINTMENT_ROUTES: Routes = [
  {
    path: '',
    component: AppointmentContainer,
    providers: [AppointmentApi, provideHttpClient()],
  },
];
```
```ts
// app/features/appointments/appointments.container.ts
import { Component, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppointmentList } from './appointments.list';
import { AppointmentApi } from './appointments.api';
@Component({
  standalone: true,
  selector: 'hms-appointments-container',
  imports: [CommonModule, AppointmentList],
  template: `<hms-appointment-list [vm]="vm()" />`,
})
export class AppointmentContainer {
  private api = inject(AppointmentApi);
  private loading = signal(false);
  private items = signal<{ id: string; patient: string; slot: string }[]>([]);
  vm = computed(() => ({ loading: this.loading(), items: this.items() }));
  constructor() { this.load(); }
  async load() {
    this.loading.set(true);
    try { this.items.set(await this.api.list()); }
    finally { this.loading.set(false); }
  }
}
```
```ts
// app/features/appointments/appointments.api.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
@Injectable()
export class AppointmentApi {
  constructor(private http: HttpClient) {}
  list() { return this.http.get<{ id: string; patient: string; slot: string }[]>('/api/appointments').toPromise(); }
}
```
```ts
// app/shared/ui/legacy-ui.module.ts (legacy NgModule reuse)
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LegacyCardComponent } from './legacy-card.component';
@NgModule({ declarations: [LegacyCardComponent], imports: [CommonModule], exports: [LegacyCardComponent] })
export class LegacyUiModule {}
```
```ts
// app/features/appointments/appointments.list.ts
import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LegacyUiModule } from '../../shared/ui/legacy-ui.module';
@Component({
  standalone: true,
  selector: 'hms-appointment-list',
  imports: [CommonModule, LegacyUiModule],
  template: `
    <legacy-card *ngFor="let a of vm.items">
      {{ a.patient }} — {{ a.slot }}
    </legacy-card>
  `,
})
export class AppointmentList {
  @Input({ required: true }) vm!: { loading: boolean; items: { id: string; patient: string; slot: string }[] };
}
```

## Try it (exercise)
- Beginner: provideHttpClient সরিয়ে অ্যাপ চালিয়ে দেখুন কোথায় fail হয়; পরে ফিরিয়ে দিন।
- Advanced: legacy billing NgModule import করে standalone container-এ ব্যবহার করুন এবং provider scope leakage পরীক্ষা করুন।

## Common mistakes
- provideHttpClient root scope-এ দিয়ে সব lazy feature একই interceptor শেয়ার করে unintended effect।
- standalone route-এ providers ভুলে যাওয়া, ফলে service singleton হয়ে থাকে।

## Interview points
- “Interop during migration” উত্তর দিতে এই উদাহরণ দিন।
- Route-level providers দিয়ে scoped HttpClient দেখানো যায়।

## Done when…
- Standalone routes lazy-load হয়।
- Legacy NgModule interop কাজ করে।
- Providers feature scope-এ সীমাবদ্ধ।

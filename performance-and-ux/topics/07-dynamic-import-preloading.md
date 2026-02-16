# 07) import() + preloading strategies

হাসপাতাল রিপোর্টে chart/pdf heavy লাইব্রেরি থাকে; dynamic import + idle/role-based preloading দিয়ে bundle হালকা রাখুন।

## Why this matters (real-world)
- Rarely-used heavy lib প্রথম লোড ব্লক করে না।
- Idle সময়ে preload করলে UX মসৃণ।
- ইন্টারভিউ: “How do you preload only for certain roles?”

## Concepts
### Beginner
- Component-level dynamic import()।
- Simple Quicklink/NgOptimized preloading ধারণা।
### Intermediate
- Custom PreloadingStrategy (role/connection type ভিত্তিক)।
- idle callback (`requestIdleCallback`) দিয়ে optional chunk লোড।
### Advanced
- Prefetch headers / guess-based prefetch; analytics-driven preloading; abort signal হ্যান্ডল।

## Copy-paste Example
```ts
// app/features/reports/report-widget.loader.ts
export async function loadChartComponent() {
  const { ReportChartComponent } = await import('./report-chart.component');
  return ReportChartComponent;
}
```
```ts
// app/features/reports/report-chart.component.ts
import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
@Component({
  standalone: true,
  selector: 'hms-report-chart',
  imports: [CommonModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<p>Chart for {{ title }}</p>`
})
export class ReportChartComponent { @Input() title = 'Billing'; }
```
```ts
// app/features/reports/report-shell.component.ts
import { Component, ChangeDetectionStrategy, ViewContainerRef, inject } from '@angular/core';
import { loadChartComponent } from './report-widget.loader';
@Component({
  standalone: true,
  selector: 'hms-report-shell',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<button (click)="load()">Load chart</button><ng-container #host></ng-container>`
})
export class ReportShellComponent {
  private vcr = inject(ViewContainerRef);
  async load() {
    const Comp = await loadChartComponent();
    this.vcr.clear();
    this.vcr.createComponent(Comp);
  }
}
```
```ts
// app/app.routes.ts (custom preloader skeleton)
import { Routes, PreloadingStrategy, Route } from '@angular/router';
import { Injectable } from '@angular/core';
import { of } from 'rxjs';
@Injectable({ providedIn: 'root' })
export class RolePreloader implements PreloadingStrategy {
  preload(route: Route, load: () => any) {
    const role = localStorage.getItem('role');
    if (route.data?.['preloadRole'] && route.data['preloadRole'] === role) return load();
    return of(null);
  }
}
export const routes: Routes = [
  { path: 'reports', loadChildren: () => import('./features/reports/reports.routes').then(m => m.REPORT_ROUTES), data: { preloadRole: 'admin' } }
];
```

## Try it
- Beginner: dynamic import বাটন ক্লিক করলে Network panel-এ নতুন chunk আসে কিনা দেখুন।
- Advanced: RolePreloader এ connection type (navigator.connection?.saveData) চেক যোগ করুন।

## Common mistakes
- import() path এ barrel ব্যবহার করে সব lib টেনে আনা।
- Preload strategy সব route-এ চালিয়ে initial load বাড়ানো।

## Interview points
- Dynamic import for heavy libs; custom preloading based on role/device।
- Idle-time preload উল্লেখ করুন।

## Done when…
- অন্তত এক heavy widget dynamic import দিয়ে লোড হয়।
- Preloading strategy documented।
- Chunk split Network panelে দৃশ্যমান।

# 14) Routing (Startup app এ mandatory) – বাংলা + হাসপাতাল উদাহরণ

SPA-তে দ্রুত পেজ সুইচ, ডীপ লিঙ্ক, 404 ও রোল-বেসড অ্যাক্সেস—সবকিছুর জন্য রাউটিং। এখানে হাসপাতাল ম্যানেজমেন্ট উদাহরণে সম্পূর্ণ পথ দেখানো হলো।

## কী শিখবেন
- বেসিক Routes config (`path`, `component`, `redirectTo`, `pathMatch`)
- Route params (`/patients/:id`), query params (`?tab=lab`)
- Child routes (e.g., `/patients/:id/labs`)
- Lazy load (loadComponent)
- 404 (wildcard)
- Guard hook (auth) – সংক্ষিপ্ত রিমাইন্ডার

## হাসপাতাল উদাহরণ (ধারণা)
- `/dashboard` → স্ট্যাটস কার্ড
- `/patients` → তালিকা
- `/patients/:id` → ডিটেল
- `/patients/:id/labs` → child view
- `/pharmacy` → lazy component
- `**` → 404

## পূর্ণ রানযোগ্য ন্যূনতম কোড (folder tree + files)
**ট্রি (`src/app/`):**
```
src/app/
  app.routes.ts
  app.component.ts
  app.component.html
  pages/
    dashboard.component.ts
    patients.component.ts
    patient-detail.component.ts
    labs.component.ts
    pharmacy.component.ts        (lazy)
    not-found.component.ts
  guards/auth.guard.ts           (optional)
```

### app.routes.ts
```ts
import { Routes } from '@angular/router';
import { DashboardComponent } from './pages/dashboard.component';
import { PatientsComponent } from './pages/patients.component';
import { PatientDetailComponent } from './pages/patient-detail.component';
import { LabsComponent } from './pages/labs.component';
import { NotFoundComponent } from './pages/not-found.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'patients', component: PatientsComponent, canActivate: [authGuard] },
  {
    path: 'patients/:id',
    component: PatientDetailComponent,
    canActivate: [authGuard],
    children: [
      { path: 'labs', component: LabsComponent },
    ],
  },
  { path: 'pharmacy', loadComponent: () => import('./pages/pharmacy.component').then(m => m.PharmacyComponent) },
  { path: '**', component: NotFoundComponent },
];
```

### app.component.ts
```ts
import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.component.html',
})
export class AppComponent {}
```

### app.component.html
```html
<div class="min-h-screen bg-slate-50">
  <header class="bg-white shadow px-4 py-3 flex items-center gap-4">
    <a routerLink="/dashboard" routerLinkActive="text-blue-600 font-semibold">Dashboard</a>
    <a routerLink="/patients" routerLinkActive="text-blue-600 font-semibold">Patients</a>
    <a routerLink="/pharmacy" routerLinkActive="text-blue-600 font-semibold">Pharmacy (lazy)</a>
  </header>
  <main class="p-4">
    <router-outlet></router-outlet>
  </main>
  <footer class="text-center text-xs text-slate-500 py-4 bg-white">Routing demo · CityCare HMS</footer>
  </div>
```

### pages/dashboard.component.ts
```ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'hms-dashboard',
  imports: [CommonModule],
  template: `
  <div class="grid gap-3 md:grid-cols-3">
    <div class="card">Beds: 42</div>
    <div class="card">Patients: 31</div>
    <div class="card">ICU Free: 4</div>
  </div>
  `,
})
export class DashboardComponent {}
```

### pages/patients.component.ts
```ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  standalone: true,
  selector: 'hms-patients',
  imports: [CommonModule, RouterLink],
  template: `
  <ul class="divide-y divide-slate-200">
    <li *ngFor="let p of patients" class="py-2 flex justify-between">
      <span>{{ p.name }}</span>
      <div class="flex gap-2 text-sm">
        <a [routerLink]="['/patients', p.id]" class="text-blue-600">Details</a>
        <a [routerLink]="['/patients', p.id, 'labs']" class="text-emerald-600">Labs</a>
      </div>
    </li>
  </ul>
  `,
})
export class PatientsComponent {
  patients = [
    { id: 1, name: 'Aisha' },
    { id: 2, name: 'Rahul' },
  ];
}
```

### pages/patient-detail.component.ts
```ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterOutlet } from '@angular/router';

@Component({
  standalone: true,
  selector: 'hms-patient-detail',
  imports: [CommonModule, RouterOutlet],
  template: `
  <div class="card space-y-2">
    <h2 class="text-lg font-semibold">Patient ID: {{ id }}</h2>
    <p class="text-sm text-slate-600">Details view (parent route)</p>
    <router-outlet></router-outlet>
  </div>
  `,
})
export class PatientDetailComponent {
  id = this.route.snapshot.paramMap.get('id');
  constructor(private route: ActivatedRoute) {}
}
```

### pages/labs.component.ts
```ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'hms-labs',
  imports: [CommonModule],
  template: `
  <div class="card">
    <h3 class="font-semibold">Labs</h3>
    <p class="text-sm text-slate-600">Child route content.</p>
  </div>
  `,
})
export class LabsComponent {}
```

### pages/pharmacy.component.ts  *(lazy)*
```ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'hms-pharmacy',
  imports: [CommonModule],
  template: `<div class="card">Pharmacy (lazy loaded)</div>`,
})
export class PharmacyComponent {}
```

### pages/not-found.component.ts
```ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'hms-not-found',
  imports: [CommonModule],
  template: `<div class="card text-rose-600">404 | Page not found</div>`,
})
export class NotFoundComponent {}
```

### guards/auth.guard.ts  *(সিম্পল ডেমো)*
```ts
import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = () => {
  const token = localStorage.getItem('token');
  return token ? true : inject(Router).createUrlTree(['/dashboard']);
};
```

## দ্রুত স্টাইল হেল্পার (ইচ্ছাকৃত)
`src/styles.scss` (বা Tailwind CDN রাখলে দরকার নেই):
```scss
.card { @apply bg-white border border-slate-200 rounded-xl p-4 shadow-sm; }
.btn  { @apply bg-blue-600 text-white rounded px-3 py-2; }
```

## VS Code + Chrome রান স্টেপ (বিগিনার)
1) `ng new hms-routing --standalone --routing --style=scss` → `cd hms-routing`
2) `src/index.html` এ Tailwind CDN যোগ করুন: `<script src="https://cdn.tailwindcss.com"></script>`
3) উপরের ফাইলগুলো `src/app/` তে তৈরি করে পেস্ট করুন (নাম মেলান)।
4) `ng serve` → http://localhost:4200 খুলুন।
5) টেস্ট চেকলিস্ট:
   - `/dashboard` লোড (redirect কাজ করছে)।
   - `/patients` দেখা; patient link ও child `labs` লিংক কাজ করছে।
   - `/pharmacy` গেলে Network ট্যাবে lazy chunk দেখা।
   - `/random` এ গেলে 404 কম্পোনেন্ট দেখাবে।
   - localStorage থেকে `token` মুছলে `/patients` গার্ড আপনাকে `/dashboard` এ ফেরত পাঠাবে।

## Interview ঝটপট
- `pathMatch: 'full'` কেন দরকার? → খালি path redirect লুপ এড়াতে।
- Lazy component বনাম lazy module? → loadComponent হালকা; module পদ্ধতি legacy/complex feature এ।
- Guard order? → array order; প্রথম false হলে শর্ট-সার্কিট। Content projection বনাম child route? → ভিন্ন সমস্যা; projection = template slot, child route = URL-based view swap.

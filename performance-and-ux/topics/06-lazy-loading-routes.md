# 06) Code splitting: lazy routes

Appointments module শুধু প্রয়োজন হলে লোড করাই ভালো। Standalone lazy route দিয়ে JS চাঙ্ক কমান।

## Why this matters (real-world)
- প্রথম লোডে JS কম; স্লো নেটওয়ার্কে দ্রুত ফার্স্ট পেইন্ট।
- বড় ফিচার (reports/charts) পৃথক চাঙ্কে।
- ইন্টারভিউ: “How do you lazy-load in standalone Angular?”

## Concepts
### Beginner
- `loadChildren` with standalone routes।
- Prefetch redirect route।
### Intermediate
- Shared module leakage এড়ানো; feature boundary respected।
- Route-level providers scoped।
### Advanced
- Preloading strategies (see topic 07) এবং chunk naming; analytics-based splitting।

## Copy-paste Example
```ts
// app/app.routes.ts
import { Routes } from '@angular/router';
export const routes: Routes = [
  { path: 'appointments', loadChildren: () => import('./features/appointments/appointments.routes').then(m => m.APPOINTMENT_ROUTES) },
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
  { path: '', component: AppointmentContainer, providers: [provideHttpClient(), AppointmentApi] }
];
```

## Try it
- Beginner: Chrome devtools Network → JS filter দেখুন অ্যাপ খুললে appointments chunk আলাদা আসে কিনা।
- Advanced: route preloading বন্ধ/চালু করে Lighthouse TTI তুলনা করুন।

## Common mistakes
- Shared barrel থেকে সব export করে চাঙ্কে tree-shake বাধা।
- Lazy routeেও provideState root scope এ দেওয়া → duplicate store registration।

## Interview points
- Standalone `loadChildren` সিনট্যাক্স।
- Providers scoped to route; reduces global singleton bleed।

## Done when…
- অন্তত এক ফিচার lazy route হয়েছে।
- Network panelে নতুন চাঙ্ক দেখা যায়।
- Providers feature scope-এ সীমিত।

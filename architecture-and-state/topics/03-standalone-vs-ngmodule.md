# 03) Standalone routing + NgModule interop

লেম্যানদের জন্য: নতুন Angular প্রজেক্টে standalone route/component রাখা বেস্ট; পুরনো NgModule ফিচার থাকলে “interop layer” দিয়ে একসাথে চালান। নিচের হাতে-কলমে ডেমো দিয়ে শুরু করুন।

## Things to learn (Bengali + simple)
1) **Standalone route**: `loadComponent` দিয়ে সরাসরি কম্পোনেন্ট lazy-load হয়; NgModule দরকার নেই।
2) **Legacy NgModule interop**: পুরনো মডিউল থাকলে `loadChildren` বা wrapper module দিয়ে রুটে প্লাগ করুন।
3) **Boundary রুল**: standalone ফিচার নিজের ভিতরে ডিপেনডেন্সি রাখবে; legacy জিনিস আলাদা ফোল্ডারে রাখুন।
4) **Public surface**: route config + minimal provider/facade; module/ component ইন্টার্নাল ডিটেলস বাইরে শেয়ার করবেন না।
5) **Migration পাথ**: আগে route → তারপর ফিচারের UI/সার্ভিসগুলো ধাপে ধাপে standalone এ উঠিয়ে আনুন।

## Hands-on (step-by-step + commands)
1) রেডি ডেমো চালান:
   ```bash
   cd architecture-and-state/demos/standalone-vs-ngmodule-demo
   npm install
   npm run demo       # home (standalone) + legacy (NgModule) রাউট আউটপুট
   npm run typecheck  # টাইপ সেফটি ও path ঠিক আছে কিনা
   ```
2) কী দেখবেন:
   - `home` রুট: `[standalone] Home standalone works`
   - `legacy` রুট: `[ngmodule] Legacy dashboard via NgModule`
   - অজানা রুট: `404: unknown route`
3) ইচ্ছাকৃত পরীক্ষা (interop বুঝতে):
   - `app.routes.ts` এ `legacy` রুটের `kind` কে `standalone` করে দিন → `npm run demo` এ দেখবেন module route ঠিকমতো কাজ করবে না (কারণ loadChildren দরকার)।
   - আবার `kind: 'ngmodule'` ফিরিয়ে দিন, `npm run demo` ঠিক হবে।
4) নিজে বানাতে চাইলে (সল্প কপি):
   ```bash
   mkdir -p src/app/features/{home,legacy}
   # নিচের "Demos" সেকশনের কোড কপি করুন
   ```

### Done when
- Standalone রুট থেকে কম্পোনেন্ট লোড হয়, NgModule রুট থেকে module→component সঠিকভাবে resolve হয়।
- Commands চালালে প্রত্যাশিত তিনটি আউটপুট লাইন পান।

## Demos (copy-paste)
`architecture-and-state/demos/standalone-vs-ngmodule-demo/src/` থেকে মূল অংশ:
```ts
// app/router.ts (mini router to illustrate loadComponent vs loadChildren)
export type StandaloneComponent = { render(): string };
export type NgModule = { getRootComponent(): StandaloneComponent };
export type Route =
  | { path: string; kind: 'standalone'; loadComponent: () => Promise<StandaloneComponent> }
  | { path: string; kind: 'ngmodule'; loadChildren: () => Promise<NgModule> };

export async function navigate(routes: Route[], path: string): Promise<string> {
  const route = routes.find(r => r.path === path);
  if (!route) return '404: unknown route';
  if (route.kind === 'standalone') {
    const cmp = await route.loadComponent();
    return `[standalone] ${cmp.render()}`;
  }
  const mod = await route.loadChildren();
  return `[ngmodule] ${mod.getRootComponent().render()}`;
}
```
```ts
// app/features/home/home.component.ts (standalone-style component)
export class HomeComponent {
  render() { return 'Home standalone works'; }
}
```
```ts
// app/features/legacy/legacy-dashboard.component.ts
export class LegacyDashboardComponent {
  render() { return 'Legacy dashboard via NgModule'; }
}
```
```ts
// app/features/legacy/legacy.module.ts (shim for legacy NgModule)
import { LegacyDashboardComponent } from './legacy-dashboard.component';
export class LegacyModule {
  getRootComponent() { return new LegacyDashboardComponent(); }
}
```
```ts
// app/app.routes.ts (interop route table)
import { Route } from './router';

export const routes: Route[] = [
  {
    path: 'home',
    kind: 'standalone',
    loadComponent: async () => new (await import('./features/home/home.component')).HomeComponent()
  },
  {
    path: 'legacy',
    kind: 'ngmodule',
    loadChildren: async () => new (await import('./features/legacy/legacy.module')).LegacyModule()
  }
];
```
```ts
// main.ts (demo runner)
import { routes } from './app/app.routes';
import { navigate } from './app/router';

async function run() {
  console.log(await navigate(routes, 'home'));
  console.log(await navigate(routes, 'legacy'));
  console.log(await navigate(routes, 'missing'));
}

run();
```

## Ready-to-run demo (repo bundle)
- Path: `architecture-and-state/demos/standalone-vs-ngmodule-demo`
- Commands:
  ```bash
  cd architecture-and-state/demos/standalone-vs-ngmodule-demo
  npm install
  npm run demo       # দেখুন standalone বনাম NgModule রাউট আউটপুট
  npm run typecheck  # টাইপচেক/paths ঠিক আছে
  ```
- Expected output:
  ```
  [standalone] Home standalone works
  [ngmodule] Legacy dashboard via NgModule
  404: unknown route
  ```
- Test ideas:
  - `kind` ভুল সেট করে interop ভাঙা দেখুন, পরে ফিরিয়ে দিন।
  - `legacy.module.ts` এ `getRootComponent` অন্য কম্পোনেন্ট রিটার্ন করিয়ে আউটপুট বদলান।

## Common mistakes
- Legacy module-কে সরাসরি standalone route-এ বসানো (`loadComponent`)—রুট রেজল্ভ হবে না।
- Standalone কম্পোনেন্টকে module-এর declarations/provider-এর উপর নির্ভর করানো—interop কঠিন হয়।
- Mixed barrel export থেকে module/standalone path গুলিয়ে ফেলা।

## Interview points
- কেন standalone route সরল ও tree-shakable, আর কখন NgModule interop দরকার।
- Migration strategy: route shell প্রথমে, পরে ধাপে ধাপে providers/components standalone করা।
- Lazy loading surface মিনিমাল রাখার উপকারিতা (bundle size, testability)।

## Quick practice
- `npm run demo` চালিয়ে আউটপুট মিলিয়ে নিন।
- `legacy` রুটকে standalone বানিয়ে ব্যর্থতা দেখুন, তারপর revert।
- নতুন feature (e.g., `reports`) standalone route হিসেবে যোগ করুন, একই pattern অনুসরণ করে।

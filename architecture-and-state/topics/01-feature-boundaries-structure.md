# 01) Feature boundaries + folder structure

হাসপাতাল ম্যানেজমেন্ট অ্যাপ বড় হলে feature-first স্ট্রাকচার ছাড়া import জটলা ও circular deps বাড়ে।

## Folder structure (tree)
```txt
src/
|-- app/
|   |-- core/              # cross-cutting infra (auth, env, guards)
|   |   |-- auth.guard.ts
|   |   `-- http-interceptors.ts
|   |-- shared/
|   |   |-- ui/            # dumb/presentational components
|   |   |   |-- button.component.ts
|   |   |   `-- card.component.ts
|   |   `-- data-access/   # facades, state adapters, services
|   |       |-- http-client.service.ts
|   |       `-- state-store.ts
|   |-- features/
|   |   |-- patients/
|   |   |   |-- components/
|   |   |   |   `-- patient-list.component.ts
|   |   |   |-- pages/
|   |   |   |   `-- patients.page.ts
|   |   |   |-- data-access/
|   |   |   |   |-- patients.api.ts
|   |   |   |   `-- patients.facade.ts
|   |   |   `-- patients.routes.ts
|   |   `-- billing/
|   |       `-- billing.routes.ts
|   `-- app.config.ts
|-- tools/
|   `-- check-boundary.ts
`-- tsconfig.json
```

Note: Minimal stub versions of the files shown above now live in `src/` so you can run the guard and path-alias checks without adding extra Angular packages.

### Ready-to-run demo
- লোকাল স্যান্ডবক্স: `architecture-and-state/demos/feature-boundaries-demo`
- চালান:
  ```bash
  cd architecture-and-state/demos/feature-boundaries-demo
  npm install
  npm run demo          # boundary guard + typecheck
  # বা পৃথকভাবে:
  npm run check:boundaries
  npm run typecheck
  ```

## Things to learn (Angular Feature / Module Boundaries)
1) Feature slice কী, boundary কেন: Feature = user-facing capability (Appointment, Billing, Pharmacy, Auth); boundary মানে ভেতরের routes/state/API/UI বাইরে leak করবে না—goal হলো change blast কমানো, reuse বাড়ানো, circular আটকানো।  
2) Folder & import hard rules: Feature code কেবল নিজের feature বা shared থেকে নেবে; shared → feature নয়। cross-feature লাগলে facade/service contract দিয়ে bridge, direct component import নয়; barrel (index.ts) শুধু ছোট surface এ safe, over-export করলে boundary ভাঙে।  
3) Layering (feature-এর ভিতর): ui/ (presentational), pages/ বা containers/ (smart, route-driven), data-access/ (API/adapters/facades), state/ (ComponentStore/NgRx), models/ (types/DTO mapping), utils/ (pure helpers). Rule: UI layer কখনো HttpClient/Store/Effect জানবে না—container/state/data-access সামলাবে।  
4) Smart vs Presentational: Presentational = `@Input()` + `@Output()` only, no API calls; Smart = data fetch/state select/routing/orchestration. Pattern: “data down, events up” with typed outputs।  
5) Standalone বনাম NgModule interop: Prefer standalone feature shell; legacy NgModule হলে wrapper/interoperability layer রাখুন। Feature পাবলিক API ছোট রাখুন (routes + facade types), বাকি internals private।  
6) Routing boundary: Per-feature `feature.routes.ts`, lazy load করলে feature-এর state/data-access-ই লোড হবে; resolver/guard feature-level এ রাখুন।  
7) Shared vs Core vs Feature: `core/` = app-wide singleton (auth, interceptors, config); `shared/ui` = reusable UI; `shared/util` = pure helpers/constants/tokens; Feature = domain logic + screens + state + API mapping. Anti-pattern: “mega shared” এ domain ঢুকে যাওয়া।  
8) Data contracts: API DTO ↔ ViewModel mapping data-access বা state layer এ করুন; component এ raw API ব্যবহার করবেন না; error/loading shape feature state মালিকানায় থাকবে (shared shapes চলবে)।  
9) Dependency direction ও circular detection: Allowed উদাহরণ → feature/pages → feature/state → feature/data-access → shared/util. Disallowed → shared/ui → feature/state (circular শুরু). Practice: path alias + lint/guard enforce করুন।  
10) Public API কী expose করবে: routes (lazy entry), facade interface (optional), ছোট কিছু models (প্রয়োজনে), UI components সাধারণত export করবে না—shared/ui প্রার্থী হলে তবেই। Demo: `architecture-and-state/demos/feature-boundaries-demo`।  

## Why this matters (real world)
- দ্রুত অনবোর্ডিং: নতুন ডেভেলপার বুঝতে পারে patients/billing/appointments কোথায়।
- ডেপ্লয় ঝুঁকি কম: feature আলাদা থাকলে blast radius কমে।
- ইন্টারভিউ: “How do you scale Angular?” প্রশ্নে ফোল্ডার ও dependency দিক নির্দেশা বড় পয়েন্ট।

## Concepts (beginner → intermediate → advanced)
- Beginner: feature-first ফোল্ডার, shared/ui, shared/data-access, core/ infrastructure।
- Intermediate: barrel (index.ts) কখন ব্যবহার/কখন এড়ানো; path alias দিয়ে পরিষ্কার import।
- Advanced: import boundary guard স্ক্রিপ্ট; route-level provider scopes; avoiding cross-feature imports।

## Beginner Setup (step-by-step)
1) প্রজেক্ট রুটে থেকে ফোল্ডার বানান:
   ```bash
   mkdir -p src/app/features/patients src/app/shared/ui src/app/shared/data-access src/app/core src/tools
   ```
2) দরকারি dev dependencies যোগ করুন (একবারই যথেষ্ট):
   ```bash
   npm i -D ts-node @types/node source-map-explorer
   ```
3) `tsconfig.json` (বা থাকলে `tsconfig.base.json`) এ path alias যোগ করুন।
4) নিচের "Copy-paste Example" এর ফাইলগুলো একই path-এ তৈরি করুন।

## Demos (copy-paste)
```ts
// tsconfig.json (excerpt)
{
  "compilerOptions": {
    "baseUrl": "src",
    "paths": {
      "@core/*": ["app/core/*"],
      "@shared/*": ["app/shared/*"],
      "@features/*": ["app/features/*"]
    }
  }
}
```
```ts
// docs-only shim: skip if you're inside a real Angular workspace
// src/types/angular-core-shim.d.ts
declare module '@angular/core' {
  export function Injectable(opts: any): ClassDecorator;
  export function signal<T>(v: T): { (): T; set(value: T): void };
  export function computed<T>(fn: () => T): { (): T };
}
```
```ts
// app/features/patients/patients.facade.ts
import { Injectable, signal, computed } from '@angular/core';
import { PatientApi } from './patients.api';
@Injectable({ providedIn: 'root' })
export class PatientsFacade {
  private patients = signal<{ id: string; name: string; bed: string }[]>([]);
  vm = computed(() => ({ patients: this.patients() }));
  constructor(private api: PatientApi) {}
  async load() { this.patients.set(await this.api.fetchAll()); }
}
```
```ts
// app/features/patients/patients.api.ts
// Minimal stub so `tsc --noEmit`/VS Code doesn't complain.
export type PatientDto = { id: string; name: string; bed: string };

export class PatientApi {
  async fetchAll(): Promise<PatientDto[]> {
    // swap with real HttpClient call in an Angular app
    return [
      { id: '1', name: 'Jane Doe', bed: 'B-12' },
      { id: '2', name: 'John Smith', bed: 'A-03' },
    ];
  }
}
```
```ts
// tools/check-boundary.ts (run with ts-node)
import * as fs from 'node:fs';
import * as path from 'node:path';
const bad: string[] = [];
function walk(dir: string) {
    for (const f of fs.readdirSync(dir)) {
        const p = path.join(dir, f);
        if (fs.statSync(p).isDirectory()) walk(p);
        else if (p.endsWith('.ts')) scan(p);
    }
}
function scan(file: string) {
    const text = fs.readFileSync(file, 'utf-8');
    const imports = [...text.matchAll(/from ['"](.+?)['"]/g)].map(m => m[1]);
    for (const i of imports) {
        if (i.includes('features/') && !file.includes('/features/')) bad.push(`${file} -> ${i}`);
        if (i.includes('shared/ui') && file.includes('/core/')) bad.push(`${file} -> ${i}`);
    }
}
walk(path.join(process.cwd(), 'src/app'));
if (bad.length) { console.error('Boundary violations:\n' + bad.join('\n')); process.exit(1); }
console.log('No boundary issues');
```

## Try it (exercise)
- Beginner: `src/app/features/patients`, `src/app/shared/ui`, `src/app/shared/data-access`, `src/app/core` ফোল্ডার বানিয়ে path alias যোগ করুন; তারপর `npx tsc --noEmit` দিয়ে alias resolve হচ্ছে কিনা দেখুন।
- Intermediate: `src/tools/check-boundary.ts` চালিয়ে boundary স্ক্রিপ্ট ঠিক চলছে কিনা যাচাই করুন।
- Advanced: ইচ্ছাকৃত cross-feature import তৈরি করে দেখুন স্ক্রিপ্ট রিপোর্ট আসে কিনা।

## হাতেকলমে চালিয়ে দেখুন (আয়মানের জন্য beginner গাইড)
1) ফোল্ডার ও স্যাম্পল ফাইল তৈরি করুন (এই রেপোতে আগেই আছে, না থাকলে চালান):
   ```bash
   mkdir -p src/app/core src/app/shared/ui src/app/shared/data-access src/app/features/patients/{components,pages,data-access} src/app/features/billing src/tools src/types
   ```
2) tsconfig alias চেক করুন (paths আছে কিনা):
   ```bash
   cat tsconfig.json
   ```
3) স্যাম্পল ফাইল মিলিয়ে নিন (না থাকলে আগের সেকশন থেকে কপি):
   - core: `src/app/core/auth.guard.ts`, `http-interceptors.ts`
   - shared: `src/app/shared/ui/button.component.ts`, `card.component.ts`, `src/app/shared/data-access/http-client.service.ts`, `state-store.ts`
   - features/patients: `patients.api.ts`, `patients.facade.ts`, `components/patient-list.component.ts`, `pages/patients.page.ts`, `patients.routes.ts`
   - features/billing: `billing.routes.ts`
   - root: `src/app/app.config.ts`, `src/tools/check-boundary.ts`, `src/types/angular-core-shim.d.ts`
4) boundary guard চালান:
   ```bash
   npx ts-node src/tools/check-boundary.ts
   ```
   - আশা করা আউটপুট: `No boundary issues`.
5) ইচ্ছাকৃত ভুল করে boundary ভাঙা দেখুন:
   ```bash
   echo "import '../shared/ui/button.component';" >> src/app/core/auth.guard.ts
   npx ts-node src/tools/check-boundary.ts  # এখন লাল রিপোর্ট দেখাবे
   ```
6) ভুল সরিয়ে আবার চালান:
   ```bash
   # macOS/BSD
   sed -i '' '/shared\/ui/d' src/app/core/auth.guard.ts
   # Linux হলে: sed -i '/shared\/ui/d' src/app/core/auth.guard.ts
   npx ts-node src/tools/check-boundary.ts
   ```
7) alias resolve হচ্ছে কিনা নিশ্চিত করতে টাইপচেক চালান:
   ```bash
   npx tsc --noEmit
   ```
   - কোনো error না এলে ফোল্ডার boundary + paths ঠিক আছে।

## Common mistakes
- Barrel থেকে heavy UI export করে bundle ফোলানো।
- shared থেকে feature import করা (dependency দিক উল্টো)।
- core থেকে UI import করে circular তৈরি করা।

## Interview points
- Feature-first বনাম layer-first ট্রেড-অফ ব্যাখ্যা করতে পারবেন।
- Boundary guard চালিয়ে CI-তে ব্যর্থ করানোর উপায় জানেন।

## Done when…
- ফোল্ডার লেয়ার: feature → shared → core স্পষ্ট।
- tsconfig paths সেট; relative import সাফ।
- boundary স্ক্রিপ্ট চালালে ০ violation।

## How to test this topic
1) Sample folder structure তৈরি করুন: `src/app/features`, `src/app/shared`, `src/app/core`, `src/tools/check-boundary.ts`।
2) Boundary guard স্ক্রিপ্ট চালান:
   ```bash
   npx ts-node src/tools/check-boundary.ts
   ```
   - ভায়োলেশন থাকলে তালিকা দেখাবে; ঠিক করার পর পুনরায় চালিয়ে “No boundary issues” নিশ্চিত করুন।
3) Path alias যাচাই করুন: `tsconfig.json` এ paths যোগ করে `npx tsc --noEmit` চালান; alias import ঠিকমতো resolve হচ্ছে কিনা দেখুন।
4) Barrel vs direct import: heavy UI barrel থেকে ইমপোর্ট করলে ওজন বাড়ে কিনা `npm run build -- --stats-json && npx source-map-explorer dist/**/main*.js` দিয়ে দেখে তুলনা করুন।
5) Clean-up: ইচ্ছাকৃত ভুল import সরিয়ে guard পুনরায় চালিয়ে নিশ্চিত হন ০ violation।
6) VS Code এ quick check:
   - Ctrl/Cmd+Click করে alias import জাম্প করুন; কোনো unresolved পথ থাকলে fix করুন।
   - Problems প্যানেলে boundary guard output দেখতে `Tasks: Run Task` থেকে `check-boundaries` স্ক্রিপ্ট (scripts এ যোগ করলে) চালাতে পারেন।
7) Browser রান (optional sanity):
   - `npm start` বা `ng serve` চালিয়ে অ্যাপ খুলুন; console এ boundary errors থাকবে না (guard CI/CLI তে চললেও রানটাইমে কিছু আসে না)।
   - Feature routes (e.g., `/patients`) লোড হলে নিশ্চিত করুন import path ঠিক আছে এবং circular error দেখা যাচ্ছে না।

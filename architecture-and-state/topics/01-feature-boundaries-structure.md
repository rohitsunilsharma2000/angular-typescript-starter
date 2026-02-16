# 01) Feature boundaries + folder structure

হাসপাতাল ম্যানেজমেন্ট অ্যাপ বড় হলে feature-first স্ট্রাকচার ছাড়া import জটলা ও circular deps বাড়ে।

## Why this matters (real world)
- দ্রুত অনবোর্ডিং: নতুন ডেভেলপার বুঝতে পারে patients/billing/appointments কোথায়।
- ডেপ্লয় ঝুঁকি কম: feature আলাদা থাকলে blast radius কমে।
- ইন্টারভিউ: “How do you scale Angular?” প্রশ্নে ফোল্ডার ও dependency দিক নির্দেশা বড় পয়েন্ট।

## Concepts (beginner → intermediate → advanced)
- Beginner: feature-first ফোল্ডার, shared/ui, shared/data-access, core/ infrastructure।
- Intermediate: barrel (index.ts) কখন ব্যবহার/কখন এড়ানো; path alias দিয়ে পরিষ্কার import।
- Advanced: import boundary guard স্ক্রিপ্ট; route-level provider scopes; avoiding cross-feature imports।

## Copy-paste Example
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
// tools/check-boundary.ts (run with ts-node)
import fs from 'node:fs'; import path from 'node:path';
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
- Beginner: নিজের প্রজেক্টে `features/patients`, `shared/ui`, `core` ফোল্ডার বানিয়ে path alias যোগ করুন।
- Advanced: উপরের boundary স্ক্রিপ্ট চালিয়ে ইচ্ছাকৃত cross-feature import তৈরি করে দেখুন রিপোর্ট আসে কিনা।

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

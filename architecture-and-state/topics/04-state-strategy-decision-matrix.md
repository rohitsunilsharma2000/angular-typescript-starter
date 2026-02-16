# 04) State strategy decision matrix

HMS-এ কখন local signal, কখন feature store, কখন NgRx? এখানে স্পষ্ট ম্যাট্রিক্স।

## Why this matters (real world)
- ওভার-ইঞ্জিনিয়ারিং এড়ায় (ছোট UI তে NgRx নয়)।
- পারফ ঝুঁকি কম: heavy global store এড়িয়ে feature scope রাখা।
- ইন্টারভিউ: “how do you choose state tool?” উত্তর।

## Concepts (beginner → intermediate → advanced)
- Beginner: local signal for tiny widgets; feature RxJS store for one route।
- Intermediate: ComponentStore for medium slice; derived selectors; cache/refresh।
- Advanced: NgRx for cross-feature data; entity normalization; SSR/data hydration hints।

## Copy-paste Example
```ts
// state-decision.ts
export type Scope = 'local' | 'feature' | 'app';
export function chooseState(params: { teamSize: 'solo'|'squad'; reuse: boolean; crossPage: boolean; perfRisk: 'low'|'high' }): Scope {
  if (params.crossPage || params.reuse) return 'app';
  if (params.perfRisk === 'high') return 'feature';
  return 'local';
}
console.log(chooseState({ teamSize: 'squad', reuse: true, crossPage: true, perfRisk: 'low' }));
```
```ts
// Angular example: combine signals + store + global
import { signal, computed, inject, Injectable } from '@angular/core';
import { ComponentStore } from '@ngrx/component-store';
import { Store } from '@ngrx/store';
import { selectBillingSummary } from '../store/billing.selectors';
@Injectable({ providedIn: 'root' })
export class StateChooser {
  // local signal for inline filter
  filter = signal<'all'|'icu'|'general'>('all');
  // feature ComponentStore for appointments page
  constructor(private cs: ComponentStore<{ list: any[] }>, private ngrx: Store) {}
  billing$ = this.ngrx.select(selectBillingSummary);
  vm = computed(() => ({ filter: this.filter() }));
}
```

## Try it (exercise)
- Beginner: chooseState ফাংশনে latency/real-time flag যোগ করুন; ICU monitor হলে local signal prefer করুন।
- Advanced: আপনার প্রজেক্টে একটি feature নিন (pharmacy cart) এবং লিখুন কেন ComponentStore বনাম NgRx বেছে নেবেন—criteria log করুন।

## Common mistakes
- ছোট ফর্মেও NgRx বসানো → dev overhead।
- Local signal দিয়ে cross-route data share করে stale state তৈরি।

## Interview points
- Decision matrix বলুন: scope, team, reuse, performance, caching needs।
- Mention derive vs store: derived computations template-এ নয়, selector/signal-এ।

## Done when…
- নিজস্ব প্রজেক্টে প্রতিটি ফিচারের state choice লিখিত।
- derive vs store পরিষ্কার; cache/refresh পরিকল্পনা আছে।

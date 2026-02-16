# 04) Zone + zone-less options

Hospital live dashboardে frequent events থাকলে zone.js overhead দেখা যায়। zone-aware থেকে zone-less রোডম্যাপ বোঝা জরুরি।

## Why this matters (real-world)
- High-frequency UI (bed monitor, vitals) এ অপ্রয়োজনীয় CD কমে।
- Background timers/intervals কম bubble করলে ব্যাটারি বাঁচে।
- ইন্টারভিউ: “zone-less Angular?”

## Concepts
### Beginner
- zone.js কি করে; সব async → change detection ট্রিগার।
### Intermediate
- `bootstrapApplication` এ `provideZoneChangeDetection({ eventCoalescing: true })` ব্যবহার; noop zone ধারণা।
- Task-heavy widgets এ runOutsideAngular।
### Advanced
- সম্পূর্ণ zone-less build (`zone.js` বাদ) + manual `ApplicationRef.tick()` বা signals ভিত্তিক।
- Third-party লাইব্রেরি event থেকে CD isolate।

## Copy-paste Example
```ts
// main.ts (event coalescing reduce CD noise)
import { bootstrapApplication, provideZoneChangeDetection } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
bootstrapApplication(AppComponent, {
  providers: [provideZoneChangeDetection({ eventCoalescing: true })]
});
```
```ts
// runOutsideAngular for high-frequency chart
import { Component, NgZone, ChangeDetectionStrategy } from '@angular/core';
@Component({
  standalone: true,
  selector: 'hms-vitals-chart',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<canvas id="chart"></canvas>`
})
export class VitalsChart {
  constructor(private zone: NgZone) {
    this.zone.runOutsideAngular(() => {
      const id = setInterval(() => {
        // update chart library
      }, 500);
      // optionally reenter only when data batch ready
    });
  }
}
```

## Try it
- Beginner: eventCoalescing true করে Angular DevTools এ change detection count তুলনা করুন।
- Advanced: runOutsideAngular এ setInterval রাখুন, manual `markForCheck()` দিয়ে প্রতি 2s এ একবার UI আপডেট করুন।

## Common mistakes
- zone-less করলে third-party libs যা zone এর উপর নির্ভর করে ভেঙে যেতে পারে—চেক না করা।
- runOutsideAngular ব্যবহার করে কখনই zone-এ ফেরত না এসে UI আপডেট বাদ যাওয়া।

## Interview points
- event coalescing + runOutsideAngular উল্লেখ করুন।
- zone-less পথে signals/OnPush মিলিয়ে manual tick ধারণা দিন।

## Done when…
- event coalescing সেট বা বিবেচিত।
- High-frequency কাজ runOutsideAngular এ এবং প্রয়োজনমতো markForCheck।
- zone-less প্রভাব এবং ঝুঁকি নথিভুক্ত।

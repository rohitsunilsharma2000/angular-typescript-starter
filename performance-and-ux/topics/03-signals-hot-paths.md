# 03) Signals in hot paths

Appointments filter, badge counts, quick status indicators—signals দিয়ে template re-render কমান।

## Why this matters (real-world)
- Hot UI (live queue) কম computation-এ চলবে।
- Fine-grained updates; ছোট অংশ repaint।
- ইন্টারভিউ: “signals vs RxJS vs OnPush” তুলনা।

## Concepts
### Beginner
- signal, computed, effect কী এবং OnPush-এর সাথে বেসিক ব্যবহার।
### Intermediate
- Derived state computed(); effect cleanup; avoiding effect → signal loop।
- toObservable/toSignal ব্রিজ।
### Advanced
- Signal-based local cache; selective rerender; zoneless setup (if applicable); batching microtasks।

## Copy-paste Example
```ts
// app/features/appointments/appointments-signals.component.ts
import { ChangeDetectionStrategy, Component, computed, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
type Appointment = { id: string; patient: string; slot: string; status: 'scheduled' | 'done' };
@Component({
  standalone: true,
  selector: 'hms-appointments-signals',
  imports: [CommonModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex gap-2 mb-2">
      <button class="border px-2" (click)="filter.set('all')">All</button>
      <button class="border px-2" (click)="filter.set('scheduled')">Scheduled</button>
      <button class="border px-2" (click)="filter.set('done')">Done</button>
    </div>
    <ul>
      <li *ngFor="let a of filtered()">{{ a.patient }} — {{ a.slot }} ({{ a.status }})</li>
    </ul>
  `
})
export class AppointmentsSignalsComponent {
  private list = signal<Appointment[]>([
    { id: 'A1', patient: 'Rima', slot: '10:00', status: 'scheduled' },
    { id: 'A2', patient: 'Arko', slot: '10:30', status: 'done' },
  ]);
  filter = signal<'all' | 'scheduled' | 'done'>('all');
  filtered = computed(() => {
    const f = this.filter();
    return f === 'all' ? this.list() : this.list().filter(a => a.status === f);
  });
}
```

## Try it
- Beginner: নতুন status “cancelled” যোগ করে filter বাটন যুক্ত করুন।
- Advanced: API observable থেকে toSignal ব্যবহার করে তালিকা ফিড করুন; filter computed রয়ে গেছে কিনা দেখুন।

## Common mistakes
- effect এর ভেতর signal সেট করে লুপ তৈরি করা।
- বড় computed এ heavy filter যা প্রতিবার full scan করে—selective caching না করা।

## Interview points
- signals = pull-less change detection; combine with OnPush to shrink re-render zone।
- toSignal/toObservable bridge উল্লেখ করুন।

## Done when…
- Hot pathে signals প্রয়োগ; computed দিয়ে derived state।
- Template re-render range সংকুচিত; heavy কাজ বাইরে।

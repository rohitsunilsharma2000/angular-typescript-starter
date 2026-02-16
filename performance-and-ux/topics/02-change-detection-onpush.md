# 02) Change Detection: Default vs OnPush

Patients তালিকা প্রতি সেকেন্ডে আপডেট হলে Default change detection অপ্রয়োজনীয় অনেক চেক করে। OnPush + trackBy দিয়ে hot path হালকা করুন।

## Why this matters (real-world)
- তালিকা/ড্যাশবোর্ডে অপ্রয়োজনীয় re-render কমায়।
- মোবাইলে ব্যাটারি ও ফ্রেম ড্রপ কমে।
- ইন্টারভিউতে “OnPush কখন?” প্রশ্ন নিয়মিত।

## Concepts
### Beginner
- Default বনাম OnPush ট্রিগার কীভাবে কাজ করে।
- Immutable input + trackBy।
### Intermediate
- OnPush + async pipe; event/observable push only।
- Derived state (vm) template থেকে বের করা।
### Advanced
- OnPush + signals synergy; zone-less + markDirty?; changeDetection ref.markForCheck ব্যবহারের ন্যূনতম প্রয়োজন।

## Copy-paste Example
```ts
// app/features/patients/patient-list.component.ts
import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
export interface Patient { id: string; name: string; ward: string; }
@Component({
  standalone: true,
  selector: 'hms-patient-list',
  imports: [CommonModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul>
      <li *ngFor="let p of patients; trackBy: trackById">
        {{ p.name }} — {{ p.ward }}
      </li>
    </ul>
  `
})
export class PatientListComponent {
  @Input({ required: true }) patients: Patient[] = [];
  trackById = (_: number, item: Patient) => item.id;
}
```
```ts
// app/features/patients/patient-container.component.ts
import { Component, ChangeDetectionStrategy, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PatientListComponent, Patient } from './patient-list.component';
@Component({
  standalone: true,
  selector: 'hms-patient-container',
  imports: [CommonModule, PatientListComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<hms-patient-list [patients]="vm().patients"></hms-patient-list>`
})
export class PatientContainerComponent {
  private patients = signal<Patient[]>([
    { id: 'P1', name: 'Rima', ward: 'ICU-1' },
    { id: 'P2', name: 'Arko', ward: 'GEN-3' },
  ]);
  vm = computed(() => ({ patients: this.patients() }));
}
```

## Try it
- Beginner: trackBy না দিলে Angular DevTools দিয়ে re-render count তুলনা করুন।
- Advanced: একটি editable row যোগ করে immutable update (map) ব্যবহার করুন; mutate করলে OnPush কেন detect করে না তা দেখুন।

## Common mistakes
- OnPush দিয়ে mutable array push/pull করে আপডেট না হওয়া।
- trackBy বাদ দিয়ে DOM churn বাড়ানো।
- Template-এ heavy compute রেখে প্রতি change detection-এ CPU খরচ।

## Interview points
- OnPush trigger: input reference change, event, async pipe emit।
- Mention trackBy + immutable pattern; signals সঙ্গে synergy।

## Done when…
- Hot list component OnPush + trackBy ব্যবহার করে।
- Inputs immutable way আপডেট হচ্ছে।
- Template থেকে ভারী compute বাইরে আনা হয়েছে।

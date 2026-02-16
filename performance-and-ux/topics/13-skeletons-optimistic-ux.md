# 13) Skeletons/shimmers + optimistic UX

Patients list লোডের আগে লেআউট-স্টেবল skeleton, আর status update এ optimistic UI দেখিয়ে perceived স্পিড বাড়ান।

## Why this matters (real-world)
- Skeleton দিয়ে CLS এড়িয়ে ব্যবহারকারীকে “alive” অনুভূতি দেয়।
- Optimistic UI নেটওয়ার্ক লেটেন্সি ঢেকে দেয়।
- ইন্টারভিউ: perceived performance উদাহরণ।

## Concepts
### Beginner
- Skeleton: fixed height/width, neutral colors; spinner এড়ানো।
- Optimistic update flow: temp state, rollback।
### Intermediate
- Shimmer animation CSS; reusable skeleton component।
- Empty/error vs skeleton আলাদা করা।
### Advanced
- Optimistic batching; rollback toast; cache update strategies।

## Copy-paste Example
```ts
// app/shared/ui/skeleton-loader.component.ts
import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
@Component({
  standalone: true,
  selector: 'hms-skeleton-loader',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<div class="skeleton" [style.height.px]="height" [style.width.%]="width"></div>`,
  styles: [`
    .skeleton { background: linear-gradient(90deg, #e2e8f0 25%, #f8fafc 37%, #e2e8f0 63%); background-size: 400% 100%; animation: shimmer 1.4s ease infinite; border-radius: 8px; }
    @keyframes shimmer { 0% { background-position: 100% 0; } 100% { background-position: -100% 0; } }
  `]
})
export class SkeletonLoaderComponent {
  @Input() height = 56;
  @Input() width = 100;
}
```
```ts
// app/features/patients/patients-optimistic.component.ts
import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SkeletonLoaderComponent } from '../../shared/ui/skeleton-loader.component';
import { HttpClient } from '@angular/common/http';

type Patient = { id: string; name: string; ward: string };
@Component({
  standalone: true,
  selector: 'hms-patients-optimistic',
  imports: [CommonModule, SkeletonLoaderComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <section class="space-y-3">
      <button class="border px-2" (click)="createOptimistic()">Quick Admit</button>
      <ng-container *ngIf="loading(); else list">
        <hms-skeleton-loader *ngFor="let _ of placeholders" [height]="52" [width]="100"></hms-skeleton-loader>
      </ng-container>
      <ng-template #list>
        <ul>
          <li *ngFor="let p of patients()">{{ p.name }} — {{ p.ward }}</li>
        </ul>
      </ng-template>
      <p *ngIf="error()" class="text-red-600">{{ error() }}</p>
    </section>
  `
})
export class PatientsOptimisticComponent {
  private http = inject(HttpClient);
  patients = signal<Patient[]>([]);
  loading = signal(true);
  error = signal<string | undefined>(undefined);
  placeholders = Array(3).fill(0);

  constructor() { this.load(); }

  async load() {
    this.loading.set(true);
    try { this.patients.set(await this.http.get<Patient[]>('/api/patients').toPromise()); }
    finally { this.loading.set(false); }
  }

  async createOptimistic() {
    const temp: Patient = { id: 'temp-' + crypto.randomUUID(), name: 'New Admit', ward: 'ER-1' };
    this.patients.update(list => [...list, temp]);
    try {
      const saved = await this.http.post<Patient>('/api/patients', { name: temp.name, ward: temp.ward }).toPromise();
      this.patients.update(list => list.map(p => p.id === temp.id ? saved : p));
    } catch (err: any) {
      this.patients.update(list => list.filter(p => p.id !== temp.id));
      this.error.set(err?.message ?? 'Rollback: failed to create');
    }
  }
}
```

## Try it
- Beginner: skeleton height বাড়ান/কমান; CLS হয় কিনা দেখুন।
- Advanced: optimistic failure কেস ম্যানুয়ালি ট্রিগার করে toast দেখান (topic 14 কোড)।

## Common mistakes
- Skeleton height নির্দিষ্ট না রেখে লেআউট ঝাঁকুনি।
- Optimistic rollback না করা; double entry থেকে বিভ্রান্তি।

## Interview points
- Layout-stable skeleton vs spinner; CLS এড়ানো।
- Optimistic flow: temp add → replace/rollback → toast।

## Done when…
- Skeleton কম্পোনেন্ট আছে ও layout-stable।
- Optimistic create rollback করে।
- Error প্রদর্শন/alert ব্যবস্থা আছে।

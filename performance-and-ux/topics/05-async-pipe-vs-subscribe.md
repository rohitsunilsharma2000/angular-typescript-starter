# 05) async pipe vs manual subscribe

Patient search তালিকা দেখাতে observable দরকার। async pipe লিক কমায়; কিছু ক্ষেত্রে manual subscribe দরকার (side-effect)।

## Why this matters (real-world)
- Memory leak এড়ানো; change detection predictable।
- Template সহজ; detach/OnPush সহ ভাল কাজ।
- ইন্টারভিউতে subscription hygiene প্রশ্ন আসে।

## Concepts
### Beginner
- async pipe auto-subscribe/unsubscribe; template-এ safe।
### Intermediate
- Manual subscribe + `takeUntilDestroyed`/DestroyRef; shareReplay cache।
### Advanced
- combineLatest heavy? → derive outside template; manual subscribe only for imperative side-effect (toast/logging)।

## Copy-paste Example
```ts
// app/features/patients/patient-search.component.ts
import { ChangeDetectionStrategy, Component, DestroyRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { debounceTime, distinctUntilChanged, switchMap, shareReplay, tap } from 'rxjs/operators';
import { Subject, of } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

interface Patient { id: string; name: string; ward: string; }

@Component({
  standalone: true,
  selector: 'hms-patient-search',
  imports: [CommonModule, FormsModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <input class="border px-2 py-1" [(ngModel)]="query" (ngModelChange)="search$.next($event)" placeholder="Search patient" />
    <ul>
      <li *ngFor="let p of results$ | async; trackBy: trackById">{{ p.name }} — {{ p.ward }}</li>
    </ul>
  `
})
export class PatientSearchComponent {
  private http = inject(HttpClient);
  private destroyRef = inject(DestroyRef);
  query = '';
  search$ = new Subject<string>();

  results$ = this.search$.pipe(
    debounceTime(200),
    distinctUntilChanged(),
    switchMap(q => q ? this.http.get<Patient[]>(`/api/patients?q=${q}`) : of([])),
    shareReplay({ bufferSize: 1, refCount: true })
  );

  // manual subscribe only for logging side-effect
  constructor() {
    this.results$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(list => console.log('search hits', list.length));
  }

  trackById = (_: number, p: Patient) => p.id;
}
```

## Try it
- Beginner: manual subscribe বাদ দিয়ে দেখুন ফাংশনালিটি একই থাকে কিনা; console log হারাবেন।
- Advanced: shareReplay ছাড়া রাখুন, একই query দুইবার করলে network কল সংখ্যা তুলনা করুন।

## Common mistakes
- subscribe করে DestroyRef/takeUntil না করা → leak।
- shareReplay ছাড়া multiple subscribers এ duplicate HTTP।

## Interview points
- async pipe default; manual subscribe only for side-effect।
- takeUntilDestroyed mention; shareReplay for caching।

## Done when…
- Template observable async pipe দিয়ে ব্যবহার।
- Manual subscribe করলে teardown আছে।
- Hot path-এ shareReplay/trackBy সেট।

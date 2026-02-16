# Feature slice state demo (patients)

উদ্দেশ্য: RxJS service store + ComponentStore + optimistic create একটি ছোট উদাহরণে দেখানো।

Steps
1) Angular প্রজেক্টে `app/features/patients` ফোল্ডার বানান।
2) নিচের ফাইলগুলো কপি করুন: `patients.store.ts` (ComponentStore) বা `patients.facade.ts` (service store + signals), `patients.routes.ts`, `patients.view.ts`।
3) Route যোগ: `loadChildren: () => import('./features/patients/patients.routes').then(m => m.PATIENT_ROUTES)`।
4) `npm i @ngrx/component-store` যদি না থাকে।

Minimal store snippet (ComponentStore)
```ts
import { Injectable } from '@angular/core';
import { ComponentStore } from '@ngrx/component-store';
import { HttpClient } from '@angular/common/http';
import { switchMap, tap, catchError, of } from 'rxjs';
interface Patient { id: string; name: string; ward: string }
interface State { data: Patient[]; loading: boolean; error?: string }
@Injectable() export class PatientsStore extends ComponentStore<State> {
  constructor(private http: HttpClient) { super({ data: [], loading: false }); }
  readonly vm$ = this.select(s => s);
  readonly load = this.effect((t$) => t$.pipe(
    tap(() => this.patchState({ loading: true })),
    switchMap(() => this.http.get<Patient[]>('/api/patients').pipe(
      tap(data => this.patchState({ data, loading: false })),
      catchError(err => { this.patchState({ loading: false, error: err.message }); return of([]); })
    ))
  ));
}
```

Optimistic create snippet
```ts
readonly create = this.effect((input$) => input$.pipe(
  tap(({ name, ward }) => this.patchState(s => ({ data: [...s.data, { id: 'temp-'+Date.now(), name, ward }], loading: true }))),
  switchMap(({ name, ward }) => this.http.post<Patient>('/api/patients', { name, ward }).pipe(
    tap(saved => this.patchState(s => ({ data: s.data.map(p => p.id.startsWith('temp-') ? saved : p), loading: false }))),
    catchError(err => { this.patchState(s => ({ data: s.data.filter(p => !p.id.startsWith('temp-')), loading: false, error: err.message })); return of(null); })
  ))
));
```

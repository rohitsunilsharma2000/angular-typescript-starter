# 07) Routing + params + lazy intro

- Router config: `Routes` array, `path`, `component`/`loadComponent`
- Route param: `route.paramMap.get('id')`
- Query param: `route.queryParamMap.get('tab')`
- Lazy load: `loadComponent: () => import('./patient-details.component').then(m => m.PatientDetailsComponent)`

**হাসপাতাল উদাহরণ**
```ts
export const routes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'patients/:id', loadComponent: () => import('./patient-details.component').then(m => m.PatientDetailsComponent) },
  { path: '**', redirectTo: '' }
];
```

```ts
// patient-details.component.ts (snippet)
ngOnInit() {
  this.route.paramMap.subscribe(p => {
    const id = p.get('id');
    this.loadPatient(id);
  });
}
```

**Try it**
- Query param `?tab=lab` পড়ে কোন ট্যাব অ্যাকটিভ হবে সেট করুন।

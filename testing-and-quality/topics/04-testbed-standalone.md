# 04) TestBed fundamentals (standalone friendly)

Standalone component render করতে TestBed কীভাবে configure করবেন—patients table উদাহরণসহ।

## Why this matters (real-world)
- ভুল provider/import দিলে flake বা failing test।
- Legacy NgModule ছাড়াই টেস্ট চালাতে পারবেন।

## Concepts
### Beginner
- `TestBed.configureTestingModule` + `imports: [Component]` for standalone।
- `ComponentFixture` basics।
### Intermediate
- Override provider; route-level providers mimic।
- ChangeDetectionStrategy OnPush with fixture.detectChanges().
### Advanced
- Test harness + TestBed combo; performance: `teardown: { destroyAfterEach: true }`।

## Copy-paste Example
```ts
// patient-table.component.ts
import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
export type Patient = { id: string; name: string; ward: string };
@Component({
  standalone: true,
  selector: 'hms-patient-table',
  imports: [CommonModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<table><tr *ngFor="let p of patients"><td>{{p.name}}</td><td>{{p.ward}}</td></tr></table>`
})
export class PatientTableComponent {
  @Input() patients: Patient[] = [];
}
```
```ts
// patient-table.component.spec.ts
import { TestBed } from '@angular/core/testing';
import { PatientTableComponent } from './patient-table.component';

describe('PatientTableComponent', () => {
  it('renders rows', () => {
    TestBed.configureTestingModule({ imports: [PatientTableComponent] });
    const fixture = TestBed.createComponent(PatientTableComponent);
    fixture.componentInstance.patients = [{ id: '1', name: 'Rima', ward: 'ICU' }];
    fixture.detectChanges();
    const rows = fixture.nativeElement.querySelectorAll('tr');
    expect(rows.length).toBe(1);
  });
});
```

## Try it
- Beginner: OnPush component detectChanges() বাদ দিয়ে ফেল করুন; কারণ বুঝুন।
- Advanced: provider override করে mock service ইনজেক্ট করুন ও assert করুন।

## Common mistakes
- Standalone component imports এ component না দেওয়া।
- OnPush এ detectChanges ভুলে assertion stale হওয়া।

## Interview points
- Standalone components TestBed দিয়ে directly import করা যায়।
- OnPush change detection ও fixture.detectChanges importance।

## Done when…
- Standalone component TestBed দিয়ে রেন্ডার হয়েছে।
- OnPush awareness; provider override বুঝেছেন।

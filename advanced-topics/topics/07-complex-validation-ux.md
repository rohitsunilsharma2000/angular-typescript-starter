# 07) Complex validation UX

Billing ফর্মে card + address + GST; patient intake ফর্মে cross-field চেক—UX ঠিক না হলে ইউজার কনফিউজ।

## Why this matters (real-world)
- ভুল ইনভয়েস/ডাটা এড়ানো যায়।
- Async validation (MRN unique) ফ্লেকি হলে friction বাড়ে।

## Concepts
### Beginner
- Inline error vs summary; touched/dirty logic।
### Intermediate
- Cross-field validator (e.g., endDate after startDate); async validator; throttle।
### Advanced
- Debounced server validation with cancel; accessibility: aria-describedby; error summary focus।

## Copy-paste Example
```ts
// validation-utils.ts
import { AbstractControl, ValidationErrors } from '@angular/forms';
export function endAfterStart(ctrl: AbstractControl): ValidationErrors | null {
  const start = ctrl.get('start')?.value;
  const end = ctrl.get('end')?.value;
  return start && end && end < start ? { endBeforeStart: true } : null;
}
```
```ts
// billing-form.component.ts
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { endAfterStart } from './validation-utils';
@Component({
  standalone: true,
  selector: 'hms-billing-form',
  imports: [CommonModule, ReactiveFormsModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <form [formGroup]="form" (ngSubmit)="submit()">
      <label>Start<input formControlName="start" type="date" aria-describedby="err-start"></label>
      <label>End<input formControlName="end" type="date" aria-describedby="err-end"></label>
      <div id="err-range" *ngIf="form.errors?.['endBeforeStart']" role="alert">End must be after start</div>
      <button type="submit">Save</button>
    </form>
  `
})
export class BillingFormComponent {
  form = this.fb.group({
    start: ['', Validators.required],
    end: ['', Validators.required],
  }, { validators: [endAfterStart] });
  constructor(private fb: FormBuilder) {}
  submit() { console.log(this.form.valid); }
}
```

## Try it
- Beginner: touched/dirty না হলে error দেখাবেন না—টেস্ট করুন।
- Advanced: async validator লিখুন যা patient MRN uniqueness চেক করে debounce সহ।

## Common mistakes
- Error summary না দিয়ে শুধু inline → screen reader মিস।
- Async validator cancel না করে race condition।

## Interview points
- Inline vs summary; cross-field; async validator cancel/debounce।

## Done when…
- Cross-field validator কাজ করে; a11y-friendly errors।
- Async validator পরিকল্পিত।

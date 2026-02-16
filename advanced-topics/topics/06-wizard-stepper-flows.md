# 06) Wizard/stepper flows (draft/save)

Patient admission wizard (demographics → bed assign → billing) একাধিক ধাপ; draft save/resume দরকার।

## Why this matters (real-world)
- মাঝপথে বন্ধ হলে ডেটা হারালে নার্সের সময় নষ্ট।
- Step validation ছাড়া ভুল বিলিং হতে পারে।

## Concepts
### Beginner
- Angular Material stepper/DIY wizard; per-step form group।
### Intermediate
- Draft save in IndexedDB/localStorage; resume logic।
- Cross-step validation (e.g., bed availability check)।
### Advanced
- Server-side draft; optimistic sync; versioning।

## Copy-paste Example
```ts
// patient-wizard.component.ts
import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
@Component({
  standalone: true,
  selector: 'hms-patient-wizard',
  imports: [CommonModule, ReactiveFormsModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <form [formGroup]="form">
      <section>
        <h3>Step 1: Demographics</h3>
        <input formControlName="name" placeholder="Name" />
      </section>
      <section>
        <h3>Step 2: Bed</h3>
        <input formControlName="bed" placeholder="Bed ID" />
      </section>
      <section>
        <h3>Step 3: Billing</h3>
        <input formControlName="payer" placeholder="Payer" />
      </section>
      <button type="button" (click)="saveDraft()">Save draft</button>
      <button type="submit" (click)="submit()">Submit</button>
    </form>
  `
})
export class PatientWizardComponent implements OnInit {
  form = this.fb.group({
    name: ['', Validators.required],
    bed: ['', Validators.required],
    payer: ['', Validators.required],
  });
  constructor(private fb: FormBuilder) {}
  ngOnInit() {
    const draft = localStorage.getItem('wizardDraft');
    if (draft) this.form.patchValue(JSON.parse(draft));
  }
  saveDraft() { localStorage.setItem('wizardDraft', JSON.stringify(this.form.value)); }
  submit() { if (this.form.valid) console.log('submit', this.form.value); }
}
```

## Try it
- Beginner: draft save/resume পরীক্ষা করুন।
- Advanced: bed availability API async validator যোগ করুন; failed হলে step 2 তে error summary দেখান।

## Common mistakes
- Step change এ untouched controls validation না করা।
- Draft version না রেখে stale data।

## Interview points
- Multi-step validation; draft save/resume; async validator per step।

## Done when…
- Wizard steps + draft save কাজ করে।
- Cross-step/async validation পরিকল্পনা আছে।

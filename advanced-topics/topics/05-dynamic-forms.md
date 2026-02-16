# 05) Dynamic/typed forms

Patient registration ফর্মে clinic অনুযায়ী fields বদলায়; typed + schema-driven approach নিন।

## Why this matters (real-world)
- ফর্ম পরিবর্তন দ্রুত; বাগ কম।
- Typed controls এ compile-time নিরাপত্তা।

## Concepts
### Beginner
- Typed FormGroup/Control; simple model binding।
### Intermediate
- Schema array → dynamic controls; async options।
### Advanced
- JSON schema mapper; reusable field components; server-driven layout।

## Copy-paste Example
```ts
// patient-form.schema.ts
export type Field = { key: string; label: string; type: 'text' | 'select'; required?: boolean; options?: string[] };
export const patientSchema: Field[] = [
  { key: 'name', label: 'Patient Name', type: 'text', required: true },
  { key: 'ward', label: 'Ward', type: 'select', options: ['ICU', 'GEN'] },
];
```
```ts
// patient-dynamic-form.component.ts
import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Field } from './patient-form.schema';
@Component({
  standalone: true,
  selector: 'hms-patient-dynamic-form',
  imports: [CommonModule, ReactiveFormsModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <form [formGroup]="form" (ngSubmit)="submit()">
      <ng-container *ngFor="let f of schema">
        <label>{{ f.label }}
          <ng-container [ngSwitch]="f.type">
            <input *ngSwitchCase="'text'" [formControlName]="f.key" />
            <select *ngSwitchCase="'select'" [formControlName]="f.key">
              <option *ngFor="let o of f.options" [value]="o">{{ o }}</option>
            </select>
          </ng-container>
        </label>
      </ng-container>
      <button type="submit">Save</button>
    </form>
  `
})
export class PatientDynamicFormComponent implements OnInit {
  @Input({ required: true }) schema: Field[] = [];
  form = this.fb.group({});
  constructor(private fb: FormBuilder) {}
  ngOnInit() {
    this.schema.forEach(f => {
      this.form.addControl(f.key, this.fb.control('', f.required ? Validators.required : []));
    });
  }
  submit() { console.log(this.form.value); }
}
```

## Try it
- Beginner: schema তে required=true দিলে error প্রদর্শন করুন।
- Advanced: async validator যোগ করুন (duplicate MRN চেক)।

## Common mistakes
- Any-typed FormGroup; control নাম mismatch runtime error।
- Schema change এ control add/remove না করা।

## Interview points
- Typed forms + schema-driven UI; async validators।

## Done when…
- Schema থেকে ফর্ম জেনারেট; typed controls।
- Required/async validator কাজ করে।

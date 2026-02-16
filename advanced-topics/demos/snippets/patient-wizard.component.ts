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
        <h3>Demographics</h3>
        <input formControlName="name" placeholder="Name" />
      </section>
      <section>
        <h3>Bed</h3>
        <input formControlName="bed" placeholder="Bed ID" />
      </section>
      <section>
        <h3>Billing</h3>
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
  submit() { if (this.form.valid) console.log(this.form.value); }
}

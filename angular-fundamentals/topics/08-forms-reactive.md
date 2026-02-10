# 08) Reactive Forms (hospital forms)

- ReactiveFormModule → `FormGroup`, `FormControl`, `FormArray`
- Validators: required, minLength, pattern, custom

**হাসপাতাল উদাহরণ**
```ts
form = new FormGroup({
  name: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.minLength(2)] }),
  age: new FormControl<number | null>(null),
  symptoms: new FormArray([ new FormControl('') ]),
});

addSymptom() { this.form.controls.symptoms.push(new FormControl('')); }
```

```html
<form [formGroup]="form" (ngSubmit)="submit()">
  <input formControlName="name" placeholder="Patient name" />
  <input formControlName="age" type="number" />
  <div formArrayName="symptoms">
    <input *ngFor="let c of form.controls.symptoms.controls; let i = index" [formControlName]="i" placeholder="Symptom" />
  </div>
  <button type="submit" [disabled]="form.invalid">Save</button>
</form>
```

**Try it**
- Custom validator: age < 120 না হলে invalid; error মেসেজ দেখান।

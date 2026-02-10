# 04) @Input/@Output ও কম্পোনেন্ট যোগাযোগ

- Parent → Child: `@Input()`
- Child → Parent: `@Output() event = new EventEmitter<...>()`
- ViewChild বেসিক: template reference ধরতে ব্যবহার।

**হাসপাতাল উদাহরণ** (patient-card শিশুকম্পোনেন্ট)
```ts
@Component({ selector: 'hms-patient-card', standalone: true, template: `
  <div class="card">
    <h4>{{ patient.name }}</h4>
    <p>Bed: {{ patient.bed }}</p>
    <button (click)="discharge.emit(patient.id)">Discharge</button>
  </div>
` })
export class PatientCardComponent {
  @Input() patient!: { id: string; name: string; bed: string };
  @Output() discharge = new EventEmitter<string>();
}
```

**Parent usage**
```html
<hms-patient-card
  *ngFor="let p of patients"
  [patient]="p"
  (discharge)="onDischarge($event)">
</hms-patient-card>
```

```ts
patients = [ { id: 'P1', name: 'Aisha', bed: 'ICU' } ];
onDischarge(id: string) { this.patients = this.patients.filter(p => p.id !== id); }
```

**Interview Q**
- ChangeDetection OnPush থাকলে Input mutation-এর ঝুঁকি কী?
- Output EventEmitter বনাম RxJS Subject কখন?

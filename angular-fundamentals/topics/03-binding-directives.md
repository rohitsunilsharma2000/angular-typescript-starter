# 03) Data binding ও directives

**Binding summary**
- Interpolation: `{{ patient.name }}`
- Property: `[disabled]="isICUFull"`
- Event: `(click)="admit()"`
- Two-way: `[(ngModel)]="form.name"`

**Built-in directives**
- Structural: `*ngIf`, `*ngFor`, `ngSwitch`
- Attribute: `[ngClass]`, `[ngStyle]`

**হাসপাতাল উদাহরণ**
```html
<ul>
  <li *ngFor="let bed of beds; trackBy: trackById">
    {{ bed.id }} - {{ bed.type }}
    <span [ngClass]="{ 'text-red-600': bed.occupied }">
      {{ bed.occupied ? 'Occupied' : 'Free' }}
    </span>
  </li>
</ul>
<button (click)="admitNew()" [disabled]="isICUFull">Admit ICU</button>
```

```ts
beds = [
  { id: 'B1', type: 'ICU', occupied: true },
  { id: 'B2', type: 'ICU', occupied: false },
];
get isICUFull() { return this.beds.every(b => b.occupied); }
trackById = (_: number, item: any) => item.id;
```

**Try it**
- `ngSwitch` দিয়ে role অনুযায়ী badge রঙ বদলান (doctor/nurse/admin)।

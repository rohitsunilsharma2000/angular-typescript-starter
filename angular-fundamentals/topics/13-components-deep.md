# 13) Components Deep (Input/Output, ViewChild, Projection) – বাংলা + হাসপাতাল উদাহরণ

এই টপিকে parent-child যোগাযোগ, content projection, `ViewChild`, lifecycle, change detection, এবং smart/dumb প্যাটার্ন হাসপাতাল ম্যানেজমেন্ট উদাহরণে দেখানো হয়েছে।

## মূল ধারণা
- `@Input()` দিয়ে parent → child ডেটা।
- `@Output()` + `EventEmitter` দিয়ে child → parent ইভেন্ট।
- Content projection: `<ng-content>`; reusable card/slot।
- `ViewChild` দিয়ে DOM বা child instance ধরুন।
- Lifecycle: `ngOnInit`, `ngOnChanges`, `ngAfterViewInit`, `ngOnDestroy`.
- OnPush + immutable data → পারফর্মেন্স।

## হাসপাতাল উদাহরণ (ধারণাগত)
- Parent তালিকা = Ward overview
- Child কার্ড = Patient card (`@Input` পায়), discharge ইভেন্ট `@Output`
- Parent “Add note” বাটন → child মেথড `ViewChild` দিয়ে কল
- Content projection: Card header/body ফুটার স্লট

## Tailwind-ready স্নিপেট (অংশ)
```ts
@Component({selector:'patient-card',standalone:true,imports:[CommonModule],template:`
<div class="border rounded-lg p-3 space-y-2">
  <header class="flex justify-between items-center">
    <div>
      <p class="font-semibold">{{patient.name}}</p>
      <p class="text-xs text-slate-500">Bed: {{patient.bed}}</p>
    </div>
    <button class="text-rose-600 text-sm" (click)="discharge.emit(patient.id)">Discharge</button>
  </header>
  <ng-content select="[card-body]"></ng-content>
  <footer class="text-xs text-slate-500">Status: {{patient.status}}</footer>
</div>`})
export class PatientCard {
  @Input() patient!: { id:string; name:string; bed:string; status:string };
  @Output() discharge = new EventEmitter<string>();
  addNote(note:string){ console.log('note saved', note); }
}
```

## পূর্ণ রানযোগ্য ন্যূনতম কোড (folder tree + files)
**ট্রি (Angular CLI `src/app/`):**
```
src/app/
  app.component.ts
  app.component.html
  patient-card.component.ts
```

**app.component.ts**
```ts
import { Component, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PatientCard } from './patient-card.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, PatientCard],
  templateUrl: './app.component.html',
})
export class AppComponent {
  @ViewChild(PatientCard) card?: PatientCard;

  patients = [
    { id: 'P1', name: 'Aisha', bed: 'ICU', status: 'Admitted' },
    { id: 'P2', name: 'Rahul', bed: 'GENERAL', status: 'New' },
  ];

  onDischarge(id: string) {
    this.patients = this.patients.filter(p => p.id !== id);
  }

  callChild() {
    this.card?.addNote('Follow-up after 24h');
  }
}
```

**app.component.html**
```html
<div class="p-4 space-y-3">
  <button class="btn w-auto px-3 py-2" (click)="callChild()">Call child addNote()</button>

  <patient-card
    *ngFor="let p of patients; let first = first"
    [patient]="p"
    (discharge)="onDischarge($event)">
    <div card-body class="text-sm text-slate-700">
      {{ first ? 'First patient in list' : 'Regular patient' }}
    </div>
  </patient-card>
</div>
```

**patient-card.component.ts**
```ts
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'patient-card',
  standalone: true,
  imports: [CommonModule],
  template: `
  <div class="border rounded-lg p-3 space-y-2">
    <header class="flex justify-between items-center">
      <div>
        <p class="font-semibold">{{patient.name}}</p>
        <p class="text-xs text-slate-500">Bed: {{patient.bed}}</p>
      </div>
      <button class="text-rose-600 text-sm" (click)="discharge.emit(patient.id)">Discharge</button>
    </header>
    <ng-content select="[card-body]"></ng-content>
    <footer class="text-xs text-slate-500">Status: {{patient.status}}</footer>
  </div>
  `,
})
export class PatientCard {
  @Input() patient!: { id: string; name: string; bed: string; status: string };
  @Output() discharge = new EventEmitter<string>();
  addNote(note: string) { console.log('note saved', note); }
}
```

## VS Code + Chrome রান ধাপ (বিগিনার)
1) `ng new hms-deep --standalone --routing --style=scss` → `cd hms-deep`
2) `src/index.html` এ Tailwind CDN যোগ করুন: `<script src="https://cdn.tailwindcss.com"></script>`
3) উপরের তিনটি ফাইল `src/app/` এ বানিয়ে কোড পেস্ট করুন।
4) `ng serve` → http://localhost:4200 খুলুন।
5) Inspect:  
   - Discharge ক্লিক করলে patient লিস্ট কমে।  
   - “Call child addNote()” ক্লিক করলে console এ note লগ হবে (`ViewChild` কাজ করছে)।  
   - Content projection (`card-body`) অংশে টেক্সট দেখা যাচ্ছে।

## Interview দ্রুত রিভিশন
- Input change হলে OnPush কম্পোনেন্ট rerender? → হ্যাঁ, নতুন reference পেলে।
- `ViewChild` lifecycle order? → `ngAfterViewInit` এর পর DOM/child নিশ্চিতে পাওয়া যায়।
- Content projection বনাম @Input দিয়ে template পাঠানো? → projection DOM slot, Input ডেটা।

---

# পূর্ণ রানযোগ্য ডেমো (সব পয়েন্ট কাভার: Input/Output, Parent-Child, ViewChild, ContentChild, Lifecycle, OnPush, TrackBy)

**ফোল্ডার ট্রি (`src/app/`):**
```
src/app/
  app.component.ts
  app.component.html
  patient-item.component.ts
```

## app.component.ts
```ts
import { Component, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PatientItemComponent, Patient } from './patient-item.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, PatientItemComponent],
  templateUrl: './app.component.html',
})
export class AppComponent {
  @ViewChild(PatientItemComponent) firstItem?: PatientItemComponent;

  patients: Patient[] = [
    { id: 'P1', name: 'Aisha', bed: 'ICU', status: 'Admitted' },
    { id: 'P2', name: 'Rahul', bed: 'GENERAL', status: 'New' },
    { id: 'P3', name: 'Mitu', bed: 'ICU', status: 'Admitted' },
  ];

  trackById = (_: number, p: Patient) => p.id;

  discharge(id: string) {
    this.patients = this.patients.filter(p => p.id !== id); // new reference -> OnPush works
  }

  callChild() {
    this.firstItem?.highlight();
  }
}
```

## app.component.html
```html
<div class="p-4 space-y-3">
  <button class="btn w-auto px-3 py-2" (click)="callChild()">ViewChild highlight()</button>

  <div class="bg-white border rounded-xl p-3 shadow">
    <h2 class="text-lg font-semibold mb-2">Patients</h2>
    <ng-container *ngFor="let p of patients; trackBy: trackById; let first = first">
      <patient-item
        [patient]="p"
        [badgeTone]="p.status === 'Admitted' ? 'green' : 'amber'"
        (discharge)="discharge($event)">
        <ng-template #extra let-name="name">
          <span class="text-xs text-slate-500">Note: {{ name }} requires follow-up</span>
          <span *ngIf="first" class="text-xs text-blue-600 ml-1">(first)</span>
        </ng-template>
      </patient-item>
    </ng-container>
  </div>
</div>
```

## patient-item.component.ts
```ts
import {
  Component, Input, Output, EventEmitter, ChangeDetectionStrategy,
  OnInit, OnChanges, OnDestroy, SimpleChanges, ViewChild, ElementRef, ContentChild, TemplateRef
} from '@angular/core';
import { CommonModule } from '@angular/common';

export interface Patient { id: string; name: string; bed: string; status: string; }

@Component({
  selector: 'patient-item',
  standalone: true,
  imports: [CommonModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
  <article class="border rounded-lg p-3 mb-2 shadow-sm">
    <header class="flex justify-between items-center">
      <div>
        <p #titleEl class="font-semibold">{{ patient.name }}</p>
        <p class="text-xs text-slate-500">Bed: {{ patient.bed }}</p>
      </div>
      <span [ngClass]="badgeClasses" class="px-2 py-1 rounded text-xs font-medium">{{ patient.status }}</span>
    </header>

    <section class="mt-2 text-sm text-slate-700">
      <ng-container *ngTemplateOutlet="extraTpl; context:{ name: patient.name }"></ng-container>
    </section>

    <footer class="mt-2 flex gap-2">
      <button class="text-blue-600 text-sm" (click)="highlight()">Highlight</button>
      <button class="text-rose-600 text-sm" (click)="discharge.emit(patient.id)">Discharge</button>
    </footer>
  </article>
  `,
})
export class PatientItemComponent implements OnInit, OnChanges, OnDestroy {
  @Input() patient!: Patient;
  @Input() badgeTone: 'green' | 'amber' | 'red' = 'green';
  @Output() discharge = new EventEmitter<string>();

  @ViewChild('titleEl') titleEl?: ElementRef<HTMLParagraphElement>;
  @ContentChild('extra') extraTpl!: TemplateRef<any>;

  get badgeClasses() {
    return {
      green: 'bg-emerald-100 text-emerald-700',
      amber: 'bg-amber-100 text-amber-700',
      red: 'bg-rose-100 text-rose-700',
    }[this.badgeTone];
  }

  ngOnInit()   { console.log('OnInit', this.patient?.id); }
  ngOnChanges(changes: SimpleChanges) { console.log('OnChanges', changes['patient']); }
  ngOnDestroy(){ console.log('OnDestroy', this.patient?.id); }

  highlight() {
    if (this.titleEl) this.titleEl.nativeElement.classList.add('text-blue-600');
  }
}
```

## রান ধাপ (VS Code + Chrome)
1) `ng new hms-deep --standalone --routing --style=scss` → `cd hms-deep`
2) `src/index.html` এ Tailwind CDN যোগ করুন: `<script src="https://cdn.tailwindcss.com"></script>`
3) উপরের তিনটি ফাইল `src/app/` এ তৈরি করে কোড পেস্ট করুন।
4) `ng serve` → http://localhost:4200 খুলুন।
5) টেস্ট:
   - Discharge ক্লিক → লিস্ট থেকে আইটেম বাদ (OnPush + immutable)।
   - ViewChild বোতাম → প্রথম রোগীর নাম নীল হবে।
   - Console এ ngOnInit/ngOnChanges/ngOnDestroy লগ দেখুন।
   - TrackBy এর কারণে DOM node reuse (Elements প্যানেল তুলনা)।

# 02) Components & templates বেসিক

- Component = TS class + template + styles.
- Decorator: `@Component({ selector, templateUrl/inline, styleUrls, standalone })`
- Template binding: interpolation `{{ }}`, property `[value]`, event `(click)`, two-way `[(ngModel)]` (FormsModule)

**হাসপাতাল উদাহরণ (inline template)**
```ts
import { Component } from '@angular/core';

@Component({
  selector: 'hms-hello',
  standalone: true,
  template: `
    <h2>Welcome {{ hospitalName }}</h2>
    <p>Total beds: {{ beds }}</p>
  `,
})
export class HelloComponent {
  hospitalName = 'CityCare';
  beds = 42;
}
```

**Console টেস্ট**
- এই কম্পোনেন্টকে `main.ts` এ bootstrap করে ব্রাউজারে `hospitalName` বদলিয়ে devtools Elements প্যানেলে live change দেখুন।

**Interview Q**
- selector naming convention?
- templateUrl বনাম inline template কখন?

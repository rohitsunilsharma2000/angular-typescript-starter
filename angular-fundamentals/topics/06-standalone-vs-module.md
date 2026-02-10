# 06) Standalone vs Module

- Standalone component (Angular 15+): `standalone: true`, `imports: []`, NgModule ছাড়াই bootstrap করা যায়।
- Benefits: ছোট bundle, কম boilerplate, সহজ lazy route.
- এখনো module দরকার? বড় লিগ্যাসি বা লাইব্রেরি compat কেসে।

**হাসপাতাল উদাহরণ**
```ts
@Component({
  selector: 'hms-shell',
  standalone: true,
  imports: [CommonModule, RouterOutlet],
  template: `<router-outlet></router-outlet>`
})
export class ShellComponent {}
```

**Bootstrap (main.ts)**
```ts
import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter, RouterOutlet } from '@angular/router';
import { routes } from './app.routes';
import { ShellComponent } from './shell.component';

bootstrapApplication(ShellComponent, { providers: [provideRouter(routes)] });
```

**Interview Q**
- Standalone lazy route config কীভাবে করবেন? (route object-এ `loadComponent`)।

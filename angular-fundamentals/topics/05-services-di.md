# 05) Services & Dependency Injection (DI)

- DI pattern: provider registry → constructor injection
- Provider scopes: root, component-level
- Value/token use-case: API_BASE_URL

**হাসপাতাল উদাহরণ**
```ts
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class PatientService {
  private http = inject(HttpClient);
  private base = 'https://dummyjson.com';

  list() { return this.http.get(`${this.base}/users?limit=5`); }
}
```

**Component usage**
```ts
patients$ = this.patientService.list();
constructor(private patientService: PatientService) {}
```

**Interview Q**
- providedIn: 'root' বনাম feature provider এর পার্থক্য?
- multi provider (HTTP_INTERCEPTORS) কীভাবে কাজ করে?

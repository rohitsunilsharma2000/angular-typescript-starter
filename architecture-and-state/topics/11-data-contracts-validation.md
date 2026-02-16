# 11) Data contracts + runtime validation

API থেকে Patient DTO আসে, UI model এ date/enum normalize করি; zod দিয়ে runtime validate।

## Why this matters (real world)
- Backend bug থেকে UI crash কমে।
- Interview: “type safety vs runtime safety” উত্তর।

## Concepts (beginner → intermediate → advanced)
- Beginner: interface + DTO→UI mapping।
- Intermediate: zod schema parse; friendly error।
- Advanced: optional fields normalization, enum fallback, server pagination contract।

## Copy-paste Example
> Note: zod লাগবে `npm i zod`.
```ts
// app/shared/data-contracts/patient.dto.ts
export interface PatientDto {
  id: string;
  name: string;
  ward?: string | null;
  admittedAt: string; // ISO
}
export interface PatientUi {
  id: string;
  name: string;
  ward: string;
  admittedAt: Date;
}
```
```ts
// app/shared/data-contracts/patient.mapper.ts
import { PatientDto, PatientUi } from './patient.dto';
import { z } from 'zod';
const patientSchema = z.object({
  id: z.string().min(1),
  name: z.string().min(1),
  ward: z.string().nullable().optional(),
  admittedAt: z.string().datetime(),
});
export function mapPatient(dto: PatientDto): PatientUi {
  const parsed = patientSchema.parse(dto); // throws with readable errors
  return {
    id: parsed.id,
    name: parsed.name,
    ward: parsed.ward ?? 'UNKNOWN',
    admittedAt: new Date(parsed.admittedAt),
  };
}
```
```ts
// app/features/patients/patient.api.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { mapPatient } from '../../shared/data-contracts/patient.mapper';
import { map } from 'rxjs/operators';
@Injectable({ providedIn: 'root' })
export class PatientApi {
  constructor(private http: HttpClient) {}
  list() {
    return this.http.get<any[]>('/api/patients').pipe(map(arr => arr.map(mapPatient)));
  }
}
```
```ts
// Error handling sample (friendly message)
import { ZodError } from 'zod';
try { /* parse */ } catch (e) {
  if (e instanceof ZodError) {
    console.error('Bad payload', e.errors);
  }
}
```

## Try it (exercise)
- Beginner: `ward` null হলে fallback টেক্সট বদলান (“TBD”).
- Advanced: pagination contract schema বানান `{ data, total, page, size }`; invalid হলে toast + fallback empty।

## Common mistakes
- DTO string date template-এ সরাসরি ব্যবহার → timezone bugs।
- zod parse না করে unsafe casting।

## Interview points
- “compile-time vs runtime” তুলনা করুন; zod mention।
- Mapping layer রাখলে API change isolation হয়।

## Done when…
- DTO/interface + mapper + zod schema আছে।
- Errors human-readable; fallback মান সেট।

## How to test this topic
1) VS Code: mapper import path ঠিক; zod types inference কাজ করছে কিনা hover করে দেখুন (zod না থাকলে install hint অনুসরণ করুন বা ts error দেখুন)। 
2) Unit test: valid DTO parse → UI model; invalid DTO → ZodError throw এবং message map; date normalization equals expected ISO.
3) Runtime: `ng serve` → patients fetch; console এ mapper চলেছে ও UI তে normalized values (e.g., date formatted) দেখুন; error হলে toast/log দেখা যায় কিনা।  

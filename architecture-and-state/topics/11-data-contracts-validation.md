# 11) Data contracts + runtime validation

লেম্যান-বাংলা: DTO shape আগে ঠিক করুন (contract), তারপর runtime এ validate করুন—ভুল পেলেই UI/log-এ ধরা পড়বে, API দোষ কাটাছেঁড়া কমবে। নিচের CLI ডেমো Zod দিয়ে দেখায়।

## Things to learn (beginner → intermediate → advanced)
- Beginner: DTO schema define করুন; success pathে parse; failure হলে error বার্তা দেখান।
- Intermediate: default values, coercion (string→number), safeParse বনাম parse, ফিল্ড-লেভেল error।
- Advanced: versioned contracts, optional fields handled via `partial`, union schemas (v1/v2), contract tests in CI।

## Hands-on (commands + কী দেখবেন)
1) রেডি ডেমো চালান:
   ```bash
   cd architecture-and-state/demos/data-contracts-validation-demo
   npm install
   npm run demo       # valid + invalid payload
   npm run typecheck  # টাইপ সেফটি
   ```
2) Expected আউটপুট: প্রথম ব্লকে valid array; দ্বিতীয় ব্লকে validation error তালিকা (ward number, empty id, negative age)।
3) Break/fix: `contracts.ts` এ `age` কে `z.coerce.number()` করুন → string ages ও চলবে; `nonnegative` মুছে দিন → error যাবে; `bloodType` required করলে API/fixtures না বদলালে আবার ব্যর্থ হবে।

## Demos (copy-paste)
`architecture-and-state/demos/data-contracts-validation-demo/src/` থেকে মূল অংশ:
```ts
// app/contracts.ts
import { z } from 'zod';
export const PatientDtoSchema = z.object({
  id: z.string().min(1),
  name: z.string().min(1),
  age: z.number().int().nonnegative(),
  ward: z.string().min(1),
  allergies: z.array(z.string()).default([])
});
export type PatientDto = z.infer<typeof PatientDtoSchema>;
```
```ts
// app/validator.ts
import { z } from 'zod';
import { PatientDtoSchema, PatientDto } from './contracts';
export function validatePatients(input: unknown): PatientDto[] {
  const schema = z.array(PatientDtoSchema);
  const result = schema.safeParse(input);
  if (!result.success) {
    const issues = result.error.issues.map(i => `${i.path.join('.') || 'root'}: ${i.message}`);
    throw new Error('Validation failed: ' + issues.join('; '));
  }
  return result.data;
}
```
```ts
// app/service.ts
import { validatePatients } from './validator';
import { PatientDto } from './contracts';
async function fetchPatients(kind: 'good' | 'bad'): Promise<unknown> {
  if (kind === 'good') return [
    { id: 'p1', name: 'Ayman', age: 32, ward: 'A', allergies: ['penicillin'] },
    { id: 'p2', name: 'Nadia', age: 45, ward: 'B', allergies: [] }
  ];
  return [
    { id: 'p3', name: 'Invalid', ward: 123 },
    { id: '', name: 'EmptyId', age: -1, ward: 'C', allergies: ['dust'] }
  ];
}
export async function getPatients(kind: 'good' | 'bad'): Promise<PatientDto[]> {
  const raw = await fetchPatients(kind);
  return validatePatients(raw);
}
```
```ts
// main.ts
import { getPatients } from './app/service';
async function run() {
  console.log('--- Valid payload ---');
  console.log(await getPatients('good'));
  console.log('\n--- Invalid payload (expect throw) ---');
  try { await getPatients('bad'); } catch (err: any) { console.error('Validation error:', err.message); }
}
run();
```

## Ready-to-run demo (repo bundle)
- Path: `architecture-and-state/demos/data-contracts-validation-demo`
- Commands:
  ```bash
  cd architecture-and-state/demos/data-contracts-validation-demo
  npm install
  npm run demo
  npm run typecheck
  ```
- Expected output: valid list, তারপর validation error লাইন যেখানে কোন ফিল্ডে কী ভুল বোঝা যাচ্ছে।
- Test ideas: coercion যোগ/সরান; নতুন required ফিল্ড দিন; optional করে দেখুন; union schema দিয়ে v1/v2 একইসাথে সাপোর্ট করুন।

## Common mistakes
- DTO shape না ঠিক করে সরাসরি UI ম্যাপ করা → runtime blow-up।
- safeParse ফলাফল না দেখেই data ব্যবহার করা।
- Error message সাধারণ না করে actionable list না দেওয়া।

## Interview points
- Contract-first কেন (backend/frontend sync, caching safety)।
- Zod/Valibot/TypeBox ইত্যাদি vs manual validation ট্রেড-অফ।
- Parse vs validate vs sanitize (default/transform)।

## Quick practice
- `npm run demo` চালান → error মেসেজ পড়ে ব্যর্থ ফিল্ড চিহ্নিত করুন।
- একই schema দিয়ে form validation ভাবুন; server response আর form data একই স্কিমায় validate করলে কোড কমে।
- CI তে contract test লিখতে `safeParse` fail হলে টেস্ট ফেল করার ব্যবস্থা করুন।

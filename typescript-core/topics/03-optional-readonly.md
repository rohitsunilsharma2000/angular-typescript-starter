# 03) **TypeScript Optional (?) ও Readonly সহজ ভাষায়**

**Thumbnail Text:**  
TypeScript এ Optional (?) ও Readonly

**Video Title:**  
TypeScript এ Optional (?) ও Readonly সহজ ভাষায় ব্যাখ্যা


**optional**
- property থাকতে বা না-ও থাকতে পারে; handle করতে guard/ডিফল্ট দরকার।

**readonly**
- runtime নয়, compile-time সুরক্ষা; immutability বাড়ায়।

**হাসপাতাল উদাহরণ**
```ts
interface PatientProfile {
  readonly id: string;
  name: string;
  emergencyContact?: string;
}

const p: PatientProfile = { id: 'P-100', name: 'Rima' };
// p.id = 'X'; // error: readonly

function formatContact(profile: PatientProfile) {
  return profile.emergencyContact ?? 'not provided';
}
```

**Interview Q**
- readonly shallow না deep? → শ্যালো। nested object সুরক্ষার জন্য readonly utility বা `as const` দরকার।

**Try it**
- `readonly wardList: string[]` দিলে array mutate করলে কী হবে? পরীক্ষা করুন।

## ব্রাউজারে কনসোল টেস্ট (Optional + Readonly)

1) **Optional property guard**
```ts
interface PatientProfile { name: string; emergencyContact?: string; }
function showContact(p: PatientProfile) {
  console.log(p.emergencyContact ?? 'not provided');
}
showContact({ name: 'Asha' });
```

2) **Readonly object**
```ts
interface Bed { readonly id: string; type: 'ICU' | 'GENERAL'; }
const bed: Bed = { id: 'B1', type: 'ICU' };
// bed.id = 'X'; // uncomment করলে error (TS)
console.log(bed);
```

3) **Readonly array reference vs element mutate**
```ts
const wards: readonly string[] = ['ICU-A', 'Ward-1'];
// wards.push('Ward-2'); // TS error
console.log('Readonly array length:', wards.length);
```

4) **Optional chaining + nullish coalescing**
```ts
const patient = { name: 'Tomal', address: { city: 'Dhaka' } };
console.log(patient.address?.city ?? 'Unknown');
console.log(patient.insurance?.provider ?? 'Self-pay');
```

# 07) `null` / `undefined` + strict mode

**strictNullChecks**
- চালু থাকলে `string` এ `null` assign করা যায় না; union দরকার (`string | null`).

**হাসপাতাল উদাহরণ**
```ts
interface Bed {
  id: string;
  patientId?: string | null; // undefined = not recorded, null = empty bed
}

function bedLabel(bed: Bed) {
  if (bed.patientId == null) { // null বা undefined দুটোই cover
    return 'Empty bed';
  }
  return `Occupied by ${bed.patientId}`;
}
```

**Tips**
- optional chaining `patient?.address?.city` ব্যবহার করুন।
- default value: `const name = patient.name ?? 'Anonymous';`

**Try it**
- `getNextAppointment(patientId?: string)` লিখে missing id এর জন্য error throw করুন।

## ব্রাউজারে কনসোল টেস্ট (null/undefined + strict)

1) **Nullish coalescing**
```ts
const patientName: string | null = null;
console.log(patientName ?? 'Anonymous');
```

2) **Optional chaining**
```ts
const patient = { profile: { city: 'Kolkata' } };
console.log(patient.profile?.city); // Kolkata
console.log(patient.insurance?.provider); // undefined
```

3) **Equality check with == to cover null+undefined**
```ts
function bedLabel(id?: string | null) {
  if (id == null) return 'Empty'; // null বা undefined
  return 'Occupied by ' + id;
}
console.log(bedLabel(), bedLabel('P1'));
```

4) **Strict check failing without union**
```ts
// In TS strictNullChecks: let city: string = null; // error
let city: string | null = null; // ok
city = 'Dhaka';
console.log(city);
```

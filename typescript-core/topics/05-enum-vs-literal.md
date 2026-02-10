# 05) `enum` বনাম string literal union

**নিয়ম**
- ছোট নির্দিষ্ট ভ্যালু? → string literal union (`'ICU' | 'GENERAL'`).
- runtime-এ iterate/lookup দরকার? → `enum` সুবিধাজনক (object হিসাবে থাকে)।

**হাসপাতাল উদাহরণ**
```ts
type BedType = 'ICU' | 'GENERAL' | 'PRIVATE';

enum AdmissionStatus {
  NEW = 'NEW',
  ADMITTED = 'ADMITTED',
  DISCHARGED = 'DISCHARGED',
}

function allocate(bed: BedType) {
  if (bed === 'ICU') console.log('High priority');
}

function statusLabel(s: AdmissionStatus) {
  return s === AdmissionStatus.NEW ? 'Waiting for bed' : s;
}
```

**Trade-off**
- union → treeshake সহজ, autocomplete ভালো, অতিরিক্ত কোড জেন হয় না।
- enum → value lookup (`AdmissionStatus['NEW']`) ও reverse mapping (numeric enum এ) সম্ভব।

**Try it**
- `enum Role { DOCTOR='DOCTOR', NURSE='NURSE' }` ও একই union `'DOCTOR' | 'NURSE'` লিখে bundle size পার্থক্য সম্পর্কে নোট করুন।

## ব্রাউজারে কনসোল টেস্ট (Enum vs Literal)

1) **Literal union usage**
```ts
type BedType = 'ICU' | 'GENERAL' | 'PRIVATE';
const bed: BedType = 'ICU';
console.log('Literal bed:', bed);
```

2) **String enum usage**
```ts
enum AdmissionStatus { NEW = 'NEW', ADMITTED = 'ADMITTED' }
const status: AdmissionStatus = AdmissionStatus.NEW;
console.log('Enum status:', status);
```

3) **Object key lookup with enum**
```ts
const statusLabel: Record<AdmissionStatus, string> = {
  [AdmissionStatus.NEW]: 'Waiting',
  [AdmissionStatus.ADMITTED]: 'Inside'
};
console.log(statusLabel[AdmissionStatus.NEW]);
```

4) **Comparing emitted JS (quick check)**
```ts
console.log('Enum object keys:', Object.keys(AdmissionStatus)); // see runtime object
```

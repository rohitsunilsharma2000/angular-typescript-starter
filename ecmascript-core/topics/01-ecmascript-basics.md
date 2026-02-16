# 01) ECMAScript basics — var/let/const + types (hospital context)

**কি শিখবেন**
- ES editions কীভাবে নামকরণ হয় (ES1 → ES2024)
- var vs let vs const, hoisting effect
- primitives vs objects, truthy/falsy হাসপাতালে ডেটা দিয়ে

**Code**
```js
// patient admission stub
var patient = { id: 'P-101', name: 'Asha', admitted: false };
let bed = null;          // reassignable within block
const hospital = 'CityCare'; // constant reference

function admit(p, bedNo) {
  p.admitted = true;
  bed = bedNo; // allowed because `bed` is let
  return `${p.name} -> Bed ${bed}`;
}

console.log(admit(patient, 'ICU-3'));
console.log(typeof patient, typeof bed, typeof hospital);
```

**Interview takeaways**
- `var` hoists to function scope → accidental globals হতে পারে।  
- `let/const` block scope → predictable; const means binding স্থির, value ইমমিউটেবল নয়।  
- Truthy/falsy: `""`, `0`, `null`, `undefined`, `NaN`, `false` → falsy; admission flags চেক করার সময় খেয়াল রাখুন।  

**আরো উদাহরণ (beginner → advanced)**
1) Beginner — var/let/const scope
```js
var dept = 'ER';
let shift = 'day';
const hospital = 'CityCare';

function moveBed() {
  dept = 'ICU';
  shift = 'night';
}

moveBed();
console.log({ dept, shift, hospital });
```

2) Beginner — truthy/falsy guard
```js
const bed = '';
console.log(bed ? 'Bed occupied' : 'Bed empty');

const fallback = bed ?? 'ICU-0';
console.log('Fallback bed ->', fallback);
```

3) Intermediate — const object but mutable fields
```js
const patient = { id: 'P-1' };
patient.name = 'Ria'; // allowed
patient.admitted = false;
console.log(patient);
```

4) Intermediate — template literal + Number parsing
```js
const days = Number('3');
const bill = days * 5000;
console.log(`Stay: ${days} days -> $${bill}`);
```

5) Advanced — default param with destructuring
```js
function badge({ id = 'N/A', ward = 'ER' } = {}) {
  return `${id} @ ${ward}`;
}

console.log(badge({ id: 'P-9' }));
console.log(badge());
```

**Try it**
- `discharge` ফাংশন লিখুন: `admitted` false, `bed` null করুন।  
- একটি `isReadyForSurgery` লিখে truthy/falsy পরীক্ষা করুন: `consentSigned`, `fastingHours`, `insuranceApproved`।  
- hoisting দেখতে `console.log(room)` এর পরে `var room = 'Ward-5';` চালান; তারপর `let room = ...` দিয়ে পার্থক্য বুঝুন।  

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

**Try it**
- `discharge` ফাংশন লিখুন: `admitted` false, `bed` null করুন।  
- একটি `isReadyForSurgery` লিখে truthy/falsy পরীক্ষা করুন: `consentSigned`, `fastingHours`, `insuranceApproved`।  
- hoisting দেখতে `console.log(room)` এর পরে `var room = 'Ward-5';` চালান; তারপর `let room = ...` দিয়ে পার্থক্য বুঝুন।  

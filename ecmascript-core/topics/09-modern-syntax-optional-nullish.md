# 09) Modern syntax — destructuring, rest/spread, optional chaining, nullish

**কি শিখবেন**
- Destructuring objects/arrays
- Rest/spread for safe copies
- Optional chaining `?.` এবং nullish coalescing `??`

**Code**
```js
const patient = { id: 'P-30', name: 'Anik', contacts: [{ phone: '999' }] };
const { name, contacts: [primary] } = patient;
console.log(name, primary.phone);

const vitals = [98, 101, 97];
const [first, ...rest] = vitals;
console.log(first, rest);

const patch = { admitted: true };
const updated = { ...patient, ...patch }; // shallow copy

const admission = { room: { name: 'ICU-3' } };
const roomLabel = admission?.room?.name ?? 'TBD';
console.log(roomLabel);
```

**আরো উদাহরণ (beginner → advanced)**
1) Beginner — array destructuring default  
```js
const coords = [10];
const [x, y = 0] = coords;
console.log(x, y);
```

2) Beginner — object rest/spread copy  
```js
const base = { id: 'P-1', admitted: false };
const updated = { ...base, admitted: true };
console.log(base, updated);
```

3) Intermediate — nested optional chaining  
```js
const lab = { result: { value: 4.5 } };
console.log(lab?.result?.value ?? 'pending');
console.log(lab?.comment?.text ?? 'no comment');
```

4) Intermediate — rest params  
```js
function sumVitals(...vals) {
  return vals.reduce((s, v) => s + v, 0);
}
console.log(sumVitals(98, 101, 97));
```

5) Advanced — destructuring with rename + default  
```js
const reading = { hr: 88, bp: '120/80' };
const { hr: heartRate = 0, bp: bloodPressure = 'N/A' } = reading;
console.log({ heartRate, bloodPressure });
```

**Interview takeaways**
- Spread makes shallow copies; nested objects still shared.  
- Optional chaining avoids “Cannot read property” crashes on missing data.  
- Nullish coalescing only defaults on `null/undefined`, not on `0`/`''`.  

**Try it**
- Use nested destructuring to grab `contacts[1]?.phone` with default `'N/A'`.  
- Clone `vitals` and append a new entry without mutating the original.  
- Build a function `safeBedLabel(admission)` using optional chaining + nullish.  

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

**Interview takeaways**
- Spread makes shallow copies; nested objects still shared.  
- Optional chaining avoids “Cannot read property” crashes on missing data.  
- Nullish coalescing only defaults on `null/undefined`, not on `0`/`''`.  

**Try it**
- Use nested destructuring to grab `contacts[1]?.phone` with default `'N/A'`.  
- Clone `vitals` and append a new entry without mutating the original.  
- Build a function `safeBedLabel(admission)` using optional chaining + nullish.  

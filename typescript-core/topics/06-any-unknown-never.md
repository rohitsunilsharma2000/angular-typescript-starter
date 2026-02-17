# 06) `any` / `unknown` / `never`


**Thumbnail Text:**  
any / unknown / never

**Video Title:**  
TypeScript এ any, unknown, never — পার্থক্য ও সঠিক ব্যবহার


**any**
- টাইপ চেক বন্ধ; দ্রুত কিন্তু বাগ-প্রবণ।

**unknown**
- safe any; আগে narrow করতে হয়। API/JSON input এর জন্য ideal।

**never**
- কোনো মান ফেরত না দেয় (error throw, infinite loop), অথবা exhaustive check।

**হাসপাতাল উদাহরণ**
```ts
function parsePatient(json: unknown): Patient {
  if (typeof json === 'object' && json !== null && 'name' in json) {
    const obj = json as { name: string; id?: string };
    return { id: obj.id ?? crypto.randomUUID(), name: obj.name };
  }
  throw new Error('invalid patient'); // returns never
}

function assertNever(x: never): never {
  throw new Error(`Unhandled variant: ${String(x)}`);
}
```

**Interview Q**
- `unknown` কে সরাসরি property অ্যাক্সেস করা যায়? → না, narrow দরকার।
- exhaustive check কেন গুরুত্বপূর্ণ? → future role যোগ হলে কম্পাইলার সতর্ক করবে।

**Try it**
- `type PaymentMethod = 'cash' | 'card' | 'upi'` নিয়ে switch লিখে `assertNever` কল করুন।

## ব্রাউজারে কনসোল টেস্ট (any / unknown / never)

1) **any bypasses safety**
```ts
let a: any = 5;
a.toFixed(); // ok
a.noSuchMethod(); // runtime error possible
console.log('any value:', a);
```

2) **unknown needs narrowing**
```ts
function parseNumber(input: unknown) {
  if (typeof input === 'number') return input * 2;
  throw new Error('not a number');
}
console.log(parseNumber(4));
```

3) **Exhaustive switch with never**
```ts
type Payment = 'cash' | 'card';
function pay(p: Payment) {
  switch (p) {
    case 'cash': return 'Collect notes';
    case 'card': return 'Swipe card';
    default: return assertNever(p);
  }
}
function assertNever(x: never): never { throw new Error('Unhandled ' + x); }
console.log(pay('cash'));
```

4) **unknown + type predicate**
```ts
function isPatient(x: unknown): x is { id: string } {
  return typeof x === 'object' && x !== null && 'id' in x;
}
console.log(isPatient({ id: 'P1', name: 'Asha' }), isPatient('nope'));
```

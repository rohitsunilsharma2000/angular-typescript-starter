# 04) **Generics <T>**


**Thumbnail Text:**  
TypeScript এ Generics <T>

**Video Title:**  
TypeScript এ Generics <T> কী? সহজ উদাহরণসহ ব্যাখ্যা


**কেন দরকার?**
- পুনঃব্যবহারযোগ্য ফাংশন/ক্লাসে টাইপ নিরাপত্তা রাখা।

**হাসপাতাল উদাহরণ**
```ts
// API response wrapper
interface ApiResponse<T> {
  data: T;
  message: string;
  success: boolean;
}

function wrapResponse<T>(data: T, message = 'ok'): ApiResponse<T> {
  return { data, message, success: true };
}

const patientResp = wrapResponse<Patient>({ id: 'P1', name: 'Asha' });
const billResp = wrapResponse<number>(4500);

// Generic repository
class Repo<T extends { id: string }> {
  private store = new Map<string, T>();
  upsert(entity: T) { this.store.set(entity.id, entity); }
  find(id: string) { return this.store.get(id); }
}

const patientRepo = new Repo<Patient>();
patientRepo.upsert({ id: 'P2', name: 'Tomal' });
```

**Interview Q**
- `T extends` কবে? constraint দিলে ভেতরে property safe হয়।
- default generic: `function fetchOne<T = Patient>() {}`

**Try it**
- `PaginatedResponse<T>` বানান: `{ items: T[]; total: number; page: number; }` এবং `wrapResponse` এর মতো ব্যবহার করুন।

## ব্রাউজারে কনসোল টেস্ট (Generics)

1) **Generic function**
```ts
function identity<T>(value: T): T { return value; }
console.log(identity<number>(5), identity('hello'));
```

2) **Constraint with extends**
```ts
function withId<T extends { id: string }>(item: T) {
  return `ID is ${item.id}`;
}
console.log(withId({ id: 'P7', name: 'Asha' }));
```

3) **Default generic**
```ts
function createList<T = string>(...items: T[]) { return items; }
console.log(createList(1, 2, 3));       // number[]
console.log(createList());              // string[] default empty
```

4) **Generic class Repo**
```ts
class Repo<T extends { id: string }> {
  store = new Map<string, T>();
  upsert(e: T) { this.store.set(e.id, e); }
  all() { return [...this.store.values()]; }
}
const patientRepo = new Repo<{ id: string; name: string }>();
patientRepo.upsert({ id: 'P1', name: 'Rahul' });
console.log('Repo:', patientRepo.all());
```

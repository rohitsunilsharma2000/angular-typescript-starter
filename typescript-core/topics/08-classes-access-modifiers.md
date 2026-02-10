# 08) Classes + access modifiers + getter/setter

**ধারণা**
- `public` (default), `private`, `protected`।
- getter/setter দিয়ে validation/derived field।

**হাসপাতাল উদাহরণ**
```ts
class BillingService {
  private taxRate = 0.05;
  constructor(private readonly hospitalName: string) {}

  calculate(amount: number) {
    return amount + amount * this.taxRate;
  }
}

class PatientAccount {
  constructor(private _due: number = 0) {}
  get due() { return this._due; }
  set due(value: number) {
    if (value < 0) throw new Error('Negative due not allowed');
    this._due = value;
  }
}
```

**Interview Q**
- `private` vs `#private` field? → `#` সত্যিকারের JS প্রাইভেট, TS private শুধুই compile-time।

**Try it**
- `class BedManager` বানান; private array-এ beds রাখুন; public method দিয়ে নতুন bed add করুন।

## ব্রাউজারে কনসোল টেস্ট (Classes + modifiers)

1) **Private field guard**
```ts
class BillingService {
  #taxRate = 0.05; // JS private
  calculate(amount) { return amount + amount * this.#taxRate; }
}
console.log(new BillingService().calculate(1000));
```

2) **TS private (compile-time)**
```ts
class Repo {
  private store = [];
  add(x) { this.store.push(x); }
  all() { return this.store; }
}
const r = new Repo(); r.add('P1'); console.log(r.all());
// r.store // TS error if typed
```

3) **Getter/Setter validation**
```ts
class PatientAccount {
  #due = 0;
  get due() { return this.#due; }
  set due(v) { if (v < 0) throw new Error('Negative'); this.#due = v; }
}
const acc = new PatientAccount(); acc.due = 500; console.log(acc.due);
```

4) **Protected for inheritance**
```ts
class Staff { protected role = 'staff'; }
class Nurse extends Staff { describe() { return 'Role: ' + this.role; } }
console.log(new Nurse().describe());
```

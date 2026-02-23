## Interview Theory Questions (Beginner → Intermediate → Advanced)

---

## 01) Type বনাম Interface

### Beginner

* `type` আর `interface` কীভাবে আলাদা?
* কোনটা object shape define করতে বেশি used?
* `interface` কেন “contract” হিসেবে বলা হয়?
* `type` দিয়ে কী কী define করা যায় (object ছাড়াও)?

### Intermediate

* `extends` কীভাবে কাজ করে (`interface extends interface`, `type` intersection)?
* Declaration merging কী? এটা কেবল `interface` এ কেন হয়?
* `implements` keyword কাদের সাথে কাজ করে?
* `type` alias re-open করা যায় না—এর practical implication কী?

### Advanced

* Public library typings এ `interface` বনাম `type` choose করার criteria কী?
* Structural typing এ `type/interface` choice কি runtime behavior change করে?
* `interface` merging করলে compatibility/maintenance risk কী?
* Complex union/intersection heavy codebase এ `type` কেন বেশি convenient?

---

## 02) Union `|` এবং Intersection `&`

### Beginner

* Union type মানে কী? Intersection type মানে কী?
* `A | B` হলে variable এ কী guarantee থাকে?
* `A & B` হলে variable এ কী guarantee থাকে?

### Intermediate

* Narrowing কী এবং কেন দরকার?
* Discriminated union কী? কেন production code এ জনপ্রিয়?
* Union এ common property access কখন safe/unsafe?
* Intersection এ property conflict হলে কী হয়?

### Advanced

* Union distribution concept (যেমন conditional type এ) কীভাবে চিন্তা করবে?
* Exhaustiveness checking ধারণা কী (theory level)?
* “Impossible states” prevent করার জন্য union কীভাবে সাহায্য করে?
* Complex domain state machine design এ union-এর ভূমিকা কী?

---

## 03) Optional `?`, `readonly`

### Beginner

* `prop?: T` মানে কী?
* Optional property আর `T | undefined`—এরা conceptually আলাদা কেন?
* `readonly` কেন দরকার?

### Intermediate

* `readonly` কি shallow নাকি deep? কেন?
* `ReadonlyArray<T>` আর `readonly T[]`—concept difference কী?
* Optional chaining `?.` কবে ব্যবহার করা উচিত?

### Advanced

* `readonly` compile-time only নাকি runtime enforced? implication কী?
* API contract এ optional fields overuse করলে কী সমস্যা হয়?
* Immutability pattern (theory): কেন predictability বাড়ায়?
* “Defensive copying” concept কী, আর `readonly` এর সাথে সম্পর্ক কী?

---

## 04) Generics

### Beginner

* Generics কেন দরকার?
* Generic type parameter `T` আসলে কী represent করে?
* Generics ছাড়া কী সমস্যা হয় (duplication / any)?

### Intermediate

* Generic constraints `T extends X` কেন দরকার?
* Inference কী? compiler কীভাবে `T` infer করে?
* `keyof` conceptually কী?

### Advanced

* Generic code এ “type safety বনাম flexibility” tradeoff কী?
* API design এ generic result wrapper (Result<T,E>) কেন ভালো?
* Variance intuition (covariance/contravariance) theory level এ কী বোঝায়?
* Overload বনাম generic signature—কখন কোনটা conceptually better?

---

## 05) Enum বনাম String Literal Union

### Beginner

* `enum` কী? string literal union কী?
* Literal union কেন বেশি lightweight?

### Intermediate

* `enum` runtime এ কীভাবে exist করে? (JS output ধারণা)
* refactor safety / autocomplete—কোনটা কোথায় strong?
* `const enum` ধারণা কী?

### Advanced

* Tree-shaking/bundle size perspective এ enum কেন issue হতে পারে?
* Cross-package/shared contract এ enum বনাম literal union—maintainability tradeoff কী?
* Server-client boundary এ enum পাঠালে কী ঝুঁকি?
* Migration strategy: enum → literal union করলে কী কী বিবেচনা করবে?

---

## 06) `any`, `unknown`, `never`

### Beginner

* `any` কেন dangerous?
* `unknown` কেন safer?
* `never` মানে কী?

### Intermediate

* Type guard conceptually কী?
* `unknown` value ব্যবহার করতে হলে কী করতে হয় (narrowing/validation)?
* `never` কখন naturally arise করে?

### Advanced

* Exhaustive checking এ `never` এর role কী?
* “Soundness” ধারণায় `any` কীভাবে hole তৈরি করে?
* Runtime validation না থাকলে `unknown`-এর benefit কতটা?
* Error handling modeling: throw বনাম Result union—conceptually পার্থক্য কী?

---

## 07) `null/undefined` + strict mode

### Beginner

* `null` আর `undefined` পার্থক্য কী?
* `strictNullChecks` চালু হলে কী বদলায়?

### Intermediate

* `??` (nullish coalescing) আর `||`—conceptual difference কী?
* Optional chaining `?.` কেন safe?
* Non-null assertion `!` conceptually কী করে?

### Advanced

* `!` overuse করলে bug risk কেন বাড়ে?
* “Fail fast” vs “silent fallback”—null handling philosophy কী?
* Strict mode codebase এ API design কেমন হওয়া উচিত?
* Nullable types modeling pattern (theory) কী?

---

## 08) Classes + Access Modifiers + Getter/Setter

### Beginner

* Class কী, object থেকে কী আলাদা?
* `public/private/protected` মানে কী?
* Encapsulation বলতে কী বোঝায়?

### Intermediate

* Inheritance vs composition—conceptually কোনটা কবে?
* Getter/setter কেন দরকার?
* `readonly` field বনাম private field—পার্থক্য কী?

### Advanced

* Abstraction + encapsulation মিলিয়ে domain modeling কেন ভালো হয়?
* Class invariant concept কী (যেমন qty কখনো negative হবে না)?
* `protected` misuse করলে design smell কেন হয়?
* DTO বনাম Domain model (class) আলাদা রাখার theory benefit কী?

---

## 09) async/await + Promise basics

### Beginner

* Promise কী? async function কী return করে?
* `await` কি করে?
* Promise resolve/reject মানে কী?

### Intermediate

* Sequential await vs parallel (Promise.all) conceptually পার্থক্য কী?
* Error handling: try/catch কিভাবে relate করে reject-এর সাথে?
* Promise chain (`then/catch`) বনাম async/await—concept difference কী?

### Advanced

* `Promise.all` বনাম `Promise.allSettled`—conceptual difference + use case?
* Concurrency limit concept কী? (সব parallel দিলে risk)
* Cancellation concept (AbortController idea) theory level এ কী?
* Async boundary তে typed error modeling কেন কঠিন?

---

## 10) Pharmacy scenario: inventory + literal unions (এটা তুমি non-HMS হিসেবে ধরতে পারো “Warehouse/Store Inventory”)

### Beginner

* Inventory item model করতে literal unions কেন useful?
* Status union (`"in_stock" | "out_of_stock"`) benefit কী?

### Intermediate

* Guard/validation conceptually কীভাবে prevent করে invalid state?
* “Business rule as type” ধারণা কী?

### Advanced

* State transition modeling (finite states) theory কী?
* Compile-time constraints দিয়ে runtime bug কমানোর idea explain করো
* Domain-driven modeling এ enums/literal unions এর role
* Invariants + guard + exhaustive switch—why strong pattern?

---

## 11) Lab scenario: report workflow + exhaustive checks (non-HMS: “Content Moderation workflow”/“Order pipeline” ভাবতে পারো)

### Beginner

* Workflow status modeling কেন দরকার?
* “Exhaustive handling” ধারণা কী?

### Intermediate

* Discriminated union দিয়ে workflow states model করলে লাভ কী?
* Unhandled case থাকলে কী ঝুঁকি?

### Advanced

* “Illegal state unrepresentable” principle কী?
* Refactor safety: নতুন status যোগ হলে compiler কীভাবে help করে?
* Domain events বনাম state—conceptual difference কী?
* Switch + never guard pattern-এর philosophy কী?

---

## 12) Tailwind UI স্নিপেট (Theory: UI consistency + utility CSS)

### Beginner

* Tailwind (utility-first CSS) ধারণা কী?
* Component library ছাড়াও Tailwind কেন use করা হয়?

### Intermediate

* Design consistency (spacing, typography scale) Tailwind এ কীভাবে achieve হয়?
* Responsive classes (sm/md/lg) ধারণা কী?
* Reusability: class duplication সমস্যা কীভাবে manage করা যায়?

### Advanced

* Utility-first বনাম semantic CSS—tradeoff কী?
* Tailwind config (theme extend) কেন important?
* Large codebase এ UI tokens / design system conceptually কিভাবে map হয় Tailwind এ?
* Maintainability: “class soup” risk কমানোর strategy (theory) কী?

---
 
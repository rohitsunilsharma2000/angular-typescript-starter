 
# TypeScript Interview Questions (Beginner → Advanced) — Topic-wise Top 20 (01–12)
Non-HMS practical coding prompts for screen-share interviews (type while explaining).

 
---

## Table of Contents
- [01) type vs interface](#01-type-vs-interface)
- [02) union and intersection](#02-union-and-intersection)
- [03) optional and readonly](#03-optional-and-readonly)
- [04) generics](#04-generics)
- [05) enum vs string literal union](#05-enum-vs-string-literal-union)
- [06) any unknown never](#06-any-unknown-never)
- [07) null undefined strict mode](#07-null-undefined-strict-mode)
- [08) classes access modifiers getter setter](#08-classes-access-modifiers-getter-setter)
- [09) async await promise](#09-async-await-promise)
- [10) inventory literal unions](#10-inventory-literal-unions)
- [11) workflow exhaustive checks](#11-workflow-exhaustive-checks)
- [12) tailwind ui snippets (non-hms)](#12-tailwind-ui-snippets-non-hms)

---

## Problem Format
Each problem includes:
- Problem: Clear statement
- Example I/O: Minimal example
- Edge cases: What to handle
- Concepts: TS concepts interviewer expects

---

## 01) type vs interface
Top 20 coding prompts.

## 1) User Shape (interface + type) — Interview Assignment

### Objective

`User` model বানাও: `readonly id: string`, `name: string`, `email?: string` — একবার `interface` দিয়ে, একবার `type` দিয়ে।

### Requirements

1. `id` অবশ্যই `readonly` হবে (reassign করলে compile error)।
2. `email` optional (না দিলেও valid)।

### Example (Expected Behavior)

```ts
// Input object literal should compile
const u1: User = { id: "u1", name: "A" };
const u2: UserType = { id: "u2", name: "B", email: "b@x.com" };
```

### Should Fail (TypeScript error expected)

```ts
u1.id = "u999"; // ❌ readonly error
```

### Concepts Being Tested

* interface vs type
* readonly
* optional property

### Deliverables

* `interface User`
* `type UserType`
* 2–3 usage examples (pass + fail)

---

## 2) Admin Extends User — Interview Assignment

### Objective

`AdminUser` বানাও যা `User` extend করে এবং `permissions: string[]` যোগ করবে।

### Requirements

1. `AdminUser` অবশ্যই `User`-এর সব field পাবে।
2. অতিরিক্ত `permissions: string[]` থাকবে।

### Example (Expected Behavior)

```ts
const admin: AdminUser = {
  id: "a1",
  name: "Admin",
  permissions: ["read", "write"],
};
```

### Notes / Edge Cases

* `permissions: []` allowed.

### Concepts Being Tested

* `extends`
* structural typing

### Deliverables

* `AdminUser` type/interface
* example usage

---

## 3) Declaration Merging Demo — Interview Assignment

### Objective

`interface AppConfig` দুইবার declare করো যাতে final shape merge হয়।

### Requirements

1. First declaration: `{ apiBaseUrl: string }`
2. Second declaration: `{ timeoutMs: number }`
3. Final type should have both properties.

### Example (Expected Behavior)

```ts
const cfg: AppConfig = { apiBaseUrl: "https://api.x.com", timeoutMs: 5000 };
```

### Should Fail (TypeScript error expected)

```ts
// If same prop name but different type in second merge -> ❌ error
```

### Concepts Being Tested

* interface declaration merging
* conflict detection

### Deliverables

* Two `interface AppConfig` declarations
* Working example + one conflict example

---

## 4) Type Alias Union Model — Interview Assignment

### Objective

`type Account = PersonalAccount | BusinessAccount` বানাও যেখানে `kind` discriminant থাকবে।

### Requirements

1. `PersonalAccount` has `kind: "personal"` and `pan: string`
2. `BusinessAccount` has `kind: "business"` and `gst: string`
3. Invalid `kind` should fail.

### Example (Expected Behavior)

```ts
const a1: Account = { kind: "personal", pan: "ABCDE1234F" };
const a2: Account = { kind: "business", gst: "19ABCDE1234F1Z5" };
```

### Should Fail

```ts
const bad: Account = { kind: "company", gst: "x" }; // ❌ invalid kind
```

### Concepts Being Tested

* type alias
* discriminated unions

### Deliverables

* Account union types
* sample narrowing (optional)

---

## 5) Excess Property Check — Interview Assignment

### Objective

`createUser(u: User)` লিখে object literal pass করলে extra key error দেখাও।

### Requirements

1. Function signature: `createUser(u: User): void`
2. Extra property in object literal should error.
3. Variable assigned first may pass — explain why.

### Example (Expected Behavior)

```ts
createUser({ id: "u1", name: "A" }); // ✅
createUser({ id: "u1", name: "A", role: "x" }); // ❌ excess property
```

### Edge Case (Explain)

```ts
const temp = { id: "u1", name: "A", role: "x" };
createUser(temp); // may ✅ depending on inference (explain excess check behavior)
```

### Concepts Being Tested

* excess property checks
* structural typing nuance

### Deliverables

* function + 2 examples + short explanation comment

---

## 6) Function Type via Interface — Interview Assignment

### Objective

Callable interface বানাও: `interface Validator { (value: string): boolean }` এবং `isEmail` implement করো।

### Requirements

1. `Validator` is callable
2. `isEmail: Validator` returns boolean

### Example (Expected Behavior)

```ts
const isEmail: Validator = (v) => v.includes("@");
isEmail("a@b.com"); // true-ish
```

### Edge Cases

* `""` returns false.

### Concepts Being Tested

* callable interface

### Deliverables

* `Validator` interface
* `isEmail` implementation + basic tests

---

## 7) Hybrid Function + Property — Interview Assignment

### Objective

`logger` এমন বানাও যেটা function হিসেবে call হবে এবং `.level` property থাকবে।

### Requirements

1. `logger("msg")` callable
2. `logger.level = "debug"` allowed
3. `level` should be a union like `"debug" | "info" | "warn" | "error"`

### Example (Expected Behavior)

```ts
logger("hello");
logger.level = "debug";
logger.level = "trace"; // ❌ should fail
```

### Concepts Being Tested

* callable objects
* intersection types
* string literal unions

### Deliverables

* logger type + minimal runtime implementation
* usage examples

---

## 8) Pick User Preview — Interview Assignment

### Objective

`type UserPreview = Pick<User, "id" | "name">` বানাও এবং `toPreview()` বানাও।

### Requirements

1. `UserPreview` has only `id` and `name`
2. `toPreview(user: User): UserPreview` returns subset

### Example (Expected Behavior)

```ts
toPreview({ id: "u1", name: "A", email: "a@x.com" }); // {id, name}
```

### Concepts Being Tested

* `Pick<>`
* function return typing

### Deliverables

* `UserPreview` + `toPreview()`

---

## 9) Omit Sensitive Fields — Interview Assignment

### Objective

`PublicUser = Omit<User, "email"> & { displayName: string }` বানাও।

### Requirements

1. `email` removed from public type
2. `displayName` added

### Example (Expected Behavior)

```ts
const p: PublicUser = { id: "u1", name: "A", displayName: "A (u1)" };
p.email; // ❌ should not exist
```

### Concepts Being Tested

* `Omit<>`
* intersection types

### Deliverables

* `PublicUser` type + example

---

## 10) Readonly Enforcement — Interview Assignment

### Objective

`readonly id` change attempt লিখে compile error দেখাও; fix by creating new object.

### Requirements

1. Demonstrate mutation fails
2. Show correct “immutable update” approach

### Example

```ts
const u: User = { id: "u1", name: "A" };
u.id = "x"; // ❌

const u2: User = { ...u, id: "u2" }; // ✅ (new object)
```

### Concepts Being Tested

* immutability mindset
* readonly

### Deliverables

* fail + fix snippet

---

## 11) Interface vs Type for Extensibility — Interview Assignment

### Objective

`interface Shape { area(): number }` এবং `type Shape2 = { area(): number }` বানাও এবং pros/cons explain করো।

### Requirements

1. `class Circle implements Shape` example
2. Short explanation: interface merging/extending vs type unions/intersections

### Example (Expected Behavior)

```ts
class Circle implements Shape {
  constructor(private r: number) {}
  area() { return Math.PI * this.r * this.r; }
}
```

### Concepts Being Tested

* `implements`
* contracts
* interface vs type tradeoffs

### Deliverables

* Shape + Shape2 + Circle + brief notes

---

## 12) Index Signature Flags — Interview Assignment

### Objective

Feature flags model: `Record<string, boolean>` এবং `isEnabled(flags, "newUI")` implement করো।

### Requirements

1. `flags` is `Record<string, boolean>`
2. Missing key returns `false`

### Example (Expected Behavior)

```ts
const flags = { newUI: true };
isEnabled(flags, "newUI"); // true
isEnabled(flags, "x");     // false
```

### Concepts Being Tested

* `Record<>`
* index signature behavior

### Deliverables

* type + function + examples

---

## 13) Generic ApiResponse — Interview Assignment

### Objective

`interface ApiResponse<T> { ok: boolean; data?: T; error?: string }` বানাও এবং `getOrThrow<T>(r)` implement করো।

### Requirements

1. If `ok === true` and `data` exists → return data
2. Otherwise throw (include message)
3. Edge: ok true but data missing → throw

### Example (Expected Behavior)

```ts
getOrThrow({ ok: true, data: 1 }); // 1
getOrThrow({ ok: false, error: "bad" }); // throws
getOrThrow({ ok: true }); // throws
```

### Concepts Being Tested

* generics
* runtime guards / narrowing

### Deliverables

* `ApiResponse<T>` + `getOrThrow<T>()` + examples

---

## 14) Namespace-like Types — Interview Assignment

### Objective

`type Models = { User: ..., Product: ... }` বানাও, তারপর `Models["User"]` ব্যবহার দেখাও।

### Requirements

1. `Models` contains at least 2 models
2. Use indexed access to extract a model type

### Example (Expected Behavior)

```ts
type Models = {
  User: { id: string; name: string };
  Product: { sku: string; price: number };
};

type U = Models["User"]; // {id,name}
```

### Concepts Being Tested

* indexed access types

### Deliverables

* Models type + extraction examples

---

## 15) Result<T,E> Union — Interview Assignment

### Objective

`type Result<T,E> = {ok:true;data:T} | {ok:false;error:E}` এবং `unwrap()` implement করো।

### Requirements

1. `unwrap(r)` returns `data` when ok
2. else throws with error message

### Example (Expected Behavior)

```ts
unwrap({ ok: true, data: 1 }); // 1
unwrap({ ok: false, error: "Nope" }); // throws
```

### Concepts Being Tested

* discriminated union
* narrowing
* runtime error handling

### Deliverables

* `Result<T,E>` + `unwrap()` + examples

---

## 16) Exact Type Helper — Interview Assignment

### Objective

Helper type `Exact<T, Shape>` বানাও যাতে function arg এ extra keys forbid করা যায়।

### Requirements

1. `Exact<Actual, Shape>` should fail if `Actual` has keys not in `Shape`
2. Use it in a function signature to enforce “no extra props”

### Example (Expected Behavior)

```ts
type Shape = { id: string; name: string };

declare function acceptExact<T>(x: Exact<T, Shape>): void;

acceptExact({ id: "u1", name: "A" });           // ✅
acceptExact({ id: "u1", name: "A", role: "x" }); // ❌
```

### Notes / Edge Cases

* Mention limitations briefly (TypeScript structural typing, inference quirks).

### Concepts Being Tested

* advanced types
* key exclusion / conditional typing

### Deliverables

* `Exact<>` type
* function example + short limitation note

## 17) DeepReadonly Utility — Interview Assignment

### Objective  
Implement a generic utility type `DeepReadonly<T>` that makes a type **recursively readonly**.

### Requirements  
1. **Nested objects become readonly** at every depth.  
2. **Arrays become `ReadonlyArray`** and their element types are also deep-readonly.  
3. **Functions remain unchanged** (do not transform callable types).  
4. **Primitives remain unchanged**.

### Example (Expected Behavior)

```ts
type Input = {
  nested: { value: number; meta: { ok: boolean } };
  items: { id: string }[];
  fn: (x: number) => string;
};

type Output = DeepReadonly<Input>;

/*
Output should behave like:
{
  readonly nested: { readonly value: number; readonly meta: { readonly ok: boolean } };
  readonly items: ReadonlyArray<{ readonly id: string }>;
  readonly fn: (x: number) => string; // unchanged
}
*/
```

### Mutation Should Fail (TypeScript errors expected)

```ts
declare const x: DeepReadonly<{ nested: { value: number }; arr: { n: number }[] }>;

x.nested.value = 2;   // ❌ error
x.arr.push({ n: 1 }); // ❌ error
x.arr[0].n = 10;      // ❌ error
```

### Concepts Being Tested  
- Conditional types  
- Mapped types  
- Recursive type definitions  
- Special-casing arrays and functions  

### Deliverables  
- `DeepReadonly<T>` implementation  
- A few example/type tests showing it works  

---

## 18) Simplify Utility — Interview Assignment

### Objective  
Implement a utility type `Simplify<T>` that “flattens”/prettifies intersection types so they display as a readable object type in tooling.

### Requirements  
1. `Simplify<T>` should return a type that looks like a normal object instead of `A & B`.  
2. It should preserve property names and types from `T`.

### Example (Expected Behavior)

```ts
type A = { a: number };
type B = { b: string };

type X = A & B;
type Y = Simplify<X>; // should display as { a: number; b: string }
```

### Notes / Edge Cases  
- None required (basic version is enough).

### Concepts Being Tested  
- Mapped types  

### Deliverables  
- `Simplify<T>` implementation  
- A couple of examples showing improved readability  

---

## 19) Fluent Builder (typed) — Interview Assignment

### Objective  
Create a **typed fluent builder** where each `.set(key, value)` call accumulates fields into the builder’s type, and `.build()` returns the fully built object type.

### Requirements  
1. `set("name", "A")` should add `name: string` to the builder’s accumulated type.  
2. Chaining multiple `set()` calls should merge fields into the final type.  
3. `build()` returns an object whose type contains all collected fields.

### Example (Expected Behavior)

```ts
const b = createBuilder();

const out = b
  .set("name", "A")
  .set("age", 1)
  .build();

// out should be typed as: { name: string; age: number }
```

### Advanced (Optional)  
- **Required fields**: calling `build()` before required keys are set should be a compile-time error.

### Concepts Being Tested  
- Generics  
- Type accumulation across chained calls  
- (Optional) conditional types for required-field enforcement  

### Deliverables  
- Builder implementation (types + minimal runtime)  
- Examples showing type inference works  
- (Optional) required-field enforcement  

---

## 20) Typed Routes Contract — Interview Assignment

### Objective  
Define a `Routes` type mapping API paths to `{ req, res }` contracts, then implement a function `callApi(path, req)` that is **fully type-safe**.

### Requirements  
1. `Routes` maps each path (string literal) to `{ req: ..., res: ... }`.  
2. `callApi("/login", req)` should:
   - enforce the correct request type for that path
   - return the correct response type for that path  
3. Passing the wrong request shape must cause a **TypeScript compile error**.

### Example (Expected Behavior)

```ts
type Routes = {
  "/login": { req: { username: string; password: string }; res: { token: string } };
  "/me": { req: { token: string }; res: { id: string; name: string } };
};

declare function callApi<P extends keyof Routes>(
  path: P,
  req: Routes[P]["req"]
): Promise<Routes[P]["res"]>;

callApi("/login", { username: "a", password: "b" }); // ✅ OK
callApi("/login", { username: "a" });                // ❌ error (missing password)
```

### Notes / Edge Cases  
- None required for the base version.

### Concepts Being Tested  
- Indexed access types (`Routes[P]["req"]`)  
- Generics over keys (`P extends keyof Routes`)  
- Type-safe API contracts  

### Deliverables  
- `Routes` contract type  
- `callApi()` function signature (runtime can be stubbed)  
- A few examples showing correct and incorrect usage

---
## 02) Union and Intersection — Interview Assignments (Top 20)

---

## 1) Payment Union — Interview Assignment

### Objective

`type Payment = "card" | "upi" | "cod"` বানাও এবং `isOnline(p)` লিখে `card/upi` হলে `true`, `cod` হলে `false` রিটার্ন করো।

### Requirements

* Only union literals allowed (`"card"|"upi"|"cod"`).
* `isOnline(p: Payment): boolean`

### Example (Expected Behavior)

```ts
isOnline("cod");  // false
isOnline("upi");  // true
```

### Concepts Being Tested

* Union types

### Deliverables

* `Payment` type + `isOnline()` + examples

---

## 2) LoginResult Union Objects — Interview Assignment

### Objective

`LoginResult` union বানাও: `{ ok: true; token: string } | { ok: false; message: string }`
এবং `printLogin(result)` লিখে token/message print করো।

### Requirements

* Narrowing using `if (result.ok)`.

### Example (Expected Behavior)

```ts
printLogin({ ok: true, token: "t123" });   // prints token
printLogin({ ok: false, message: "bad" }); // prints message
```

### Concepts Being Tested

* Union narrowing via boolean discriminant

### Deliverables

* `LoginResult` + `printLogin()`

---

## 3) Intersection Profile — Interview Assignment

### Objective

`BasicUser & Address` intersection করে `UserProfile` বানাও।

### Requirements

* `type UserProfile = BasicUser & Address`

### Example (Expected Behavior)

```ts
const profile: UserProfile = { id: "u1", name: "A", city: "Kolkata", pin: "700001" };
```

### Edge Case

* Key overlap হলে কী হয় (type conflict) — demo দাও।

### Concepts Being Tested

* Intersection types

### Deliverables

* `BasicUser`, `Address`, `UserProfile` + overlap example

---

## 4) Narrowing with `in` — Interview Assignment

### Objective

একটা union type বানাও যেখানে শুধু একটাতে `token` আছে।
`if ("token" in x)` দিয়ে safe access দেখাও।

### Requirements

* `token` based narrowing using `in`.

### Example (Expected Behavior)

```ts
if ("token" in x) {
  x.token; // ✅ safe
}
```

### Edge Case

* `token?: string` হলে property “exists” but could be `undefined` — explain in comment.

### Concepts Being Tested

* `in` type guard

### Deliverables

* union type + function demonstrating `in` guard

---

## 5) typeof Narrowing — Interview Assignment

### Objective

`string | number` param নাও।

* number হলে double
* string হলে uppercase

### Example (Expected Behavior)

```ts
transform("ab"); // "AB"
transform(2);    // 4
```

### Edge Case

* empty string -> `""` stays `""`

### Concepts Being Tested

* `typeof` guard

### Deliverables

* `transform()` function

---

## 6) Filter Union Array Safely — Interview Assignment

### Objective

`(number | string)[]` থেকে শুধু `number[]` বের করো type-safe ভাবে (predicate/type guard দিয়ে)।

### Example (Expected Behavior)

```ts
onlyNumbers([1, "a", 2]); // [1, 2]
```

### Optional Edge Case

* `NaN` keep/remove choice (comment)

### Concepts Being Tested

* Type predicate (`x is number`)

### Deliverables

* `onlyNumbers()` + predicate

---

## 7) Free vs Paid Video — Interview Assignment

### Objective

Discriminated union বানাও:

* `{ kind: "free" }`
* `{ kind: "paid"; price: number }`
  এবং `getPriceOrZero(v)` implement করো।

### Example (Expected Behavior)

```ts
getPriceOrZero({ kind: "free" }); // 0
getPriceOrZero({ kind: "paid", price: 99 }); // 99
```

### Edge Case

* negative price invalid → runtime validation (throw / clamp) define in comment.

### Concepts Being Tested

* Discriminated unions

### Deliverables

* types + `getPriceOrZero()`

---

## 8) Event Union Handler — Interview Assignment

### Objective

`page_view | add_to_cart | purchase` event union বানাও (payload fields সহ)
এবং `toLog(e)` string return করবে।

### Example (Expected Behavior)

```ts
toLog({ type: "purchase", orderId: "o1", amount: 100 });
// "Purchased orderId=o1 amount=100"
```

### Compile-time Rule

* Missing required fields should not compile.

### Concepts Being Tested

* Discriminated union with payload

### Deliverables

* event types + handler function

---

## 9) Error Union to Message — Interview Assignment

### Objective

`NetworkError | ValidationError | AuthError` union বানাও
`toUserMessage(e)` user-friendly message return করবে।

### Example (Expected Behavior)

```ts
toUserMessage({ type: "network", code: "ETIMEDOUT" }); // "Try again later"
```

### Edge Case

* unknown/default branch handling

### Concepts Being Tested

* Unions + switch

### Deliverables

* error union + `toUserMessage()`

---

## 10) Exhaustive Check with `never` — Interview Assignment

### Objective

Switch-case এ exhaustive check enforce করো: default branch এ `assertNever(x)`।

### Requirements

* `function assertNever(x: never): never`

### Example (Expected Behavior)

* নতুন union case add করলে switch compile error হওয়া উচিত।

### Concepts Being Tested

* `never` exhaustive checking

### Deliverables

* `assertNever()` + one switch example

---

## 11) Intersection of Capabilities — Interview Assignment

### Objective

`Trackable` + `Serializable` intersection type satisfy করে এমন object বানাও।

### Example (Expected Behavior)

```ts
const obj: Trackable & Serializable = {
  track: () => {},
  toJSON: () => ({ ok: true }),
};
```

### Edge Case

* method name conflict হলে কী হবে — comment/demo.

### Concepts Being Tested

* Intersections

### Deliverables

* capability types + sample object

---

## 12) Union Parameter vs Overload — Interview Assignment

### Objective

`formatDate(input: string | Date)` implement করো।
string হলে parse করে safe ভাবে Date বানাও।

### Example (Expected Behavior)

```ts
formatDate("2026-01-01"); // "01 Jan 2026"
formatDate(new Date("2026-01-01"));
```

### Edge Case

* invalid string → return `"Invalid date"` or throw (decide + document)

### Concepts Being Tested

* Union parameters + runtime checks

### Deliverables

* `formatDate()` + parsing strategy

---

## 13) Type Predicate Guard — Interview Assignment

### Objective

`isPaidVideo(v): v is { kind: "paid"; price: number }` guard লিখো
এবং filter এ ব্যবহার দেখাও।

### Example (Expected Behavior)

```ts
const paidOnly = videos.filter(isPaidVideo);
```

### Edge Case

* `unknown` input handle (optional): `isPaidVideo(v: unknown): v is PaidVideo`

### Concepts Being Tested

* Type predicates

### Deliverables

* `isPaidVideo()` + usage example

---

## 14) Compose Search Filters (intersection) — Interview Assignment

### Objective

`Filter = ByPrice & ByCategory & ByRating` বানাও এবং products এ apply করে filtered list রিটার্ন করো।

### Requirements

* Each filter field optional (missing means “don’t filter by it”).

### Example (Expected Behavior)

```ts
applyFilters(products, { minPrice: 100, category: "phone" });
```

### Edge Case

* filter values missing/undefined

### Concepts Being Tested

* Intersections + optional props

### Deliverables

* Filter types + `applyFilters()`

---

## 15) UnionToIntersection Utility — Interview Assignment

### Objective

Type-level utility `UnionToIntersection<U>` implement করো।

### Requirements

* Use conditional types distribution trick.

### Example (Concept Check)

```ts
type X = UnionToIntersection<{a:1} | {b:2}>; // {a:1} & {b:2}
```

### Concepts Being Tested

* Conditional types distribution

### Deliverables

* `UnionToIntersection<U>` + 1–2 type examples

---

## 16) Simplify Intersection Output — Interview Assignment

### Objective

`Simplify<T>` implement করো যাতে IDE তে intersection output flattened দেখায়।

### Example

```ts
type Y = Simplify<{a:number} & {b:string}>; // shows {a:number;b:string}
```

### Concepts Being Tested

* Mapped types

### Deliverables

* `Simplify<T>` + example

---

## 17) Order State Machine (union + transitions) — Interview Assignment

### Objective

`Status` union বানাও এবং `transition(status, action)` লিখো যাতে invalid transitions prevent হয়।

### Requirements

* `Status = "created"|"paid"|"packed"|"shipped"|"delivered"|"cancelled"`
* Example: `created + pay -> paid`
* Example: `delivered + cancel -> error`

### Edge Case

* cancel allowed only from some states (define rules)

### Concepts Being Tested

* Unions + exhaustive checks + state transitions

### Deliverables

* status/action types + transition function + invalid demo

---

## 18) ParseResult Union — Interview Assignment

### Objective

string থেকে number parse করে `Result<number, string>` return করো।

### Requirements

* success → `{ ok: true; data: number }`
* failure → `{ ok: false; error: string }`

### Example (Expected Behavior)

```ts
parseNumber("12.5"); // ok(12.5)
parseNumber("x");    // err("Invalid number")
```

### Edge Cases

* whitespace trimming
* `NaN` handling

### Concepts Being Tested

* Result union pattern

### Deliverables

* `Result<T,E>` + `parseNumber()`

---

## 19) Handler Map from Union — Interview Assignment

### Objective

`Event` union থেকে strongly typed `handlers` map বানাও যেন প্রতিটা handler ঠিক payload টাইপ পায়।

### Example (Expected Behavior)

* wrong handler param type দিলে compile error

### Edge Case

* missing handler key (decide: allow partial or require full)

### Concepts Being Tested

* Mapped types over union

### Deliverables

* union event type + handlers type + example usage

---

## 20) Next Step Depends on Previous (advanced) — Interview Assignment

### Objective

Onboarding flow type-safe করো: `verifyEmail()` only allowed after `signup()`.

### Requirements

* Wrong order should not compile (type-level).
* If too hard, simplify: runtime check + typed state enum.

### Example (Expected Behavior)

```ts
flow.verifyEmail(); // ❌ if not signed up
flow.signup().verifyEmail(); // ✅
```

### Concepts Being Tested

* Generics
* State typing / phantom types

### Deliverables

* Minimal typed flow API + example of compile-time ordering rule

---
## 03) Optional and Readonly — Interview Assignments (Top 20)

---

## 1) Optional Profile Display — Interview Assignment

### Objective

`Profile { name; bio?; avatarUrl? }` বানাও এবং `renderProfile(p)` লিখো যাতে missing হলে fallback text দেখায়।

### Requirements

* `bio` missing হলে `"No bio"`
* `avatarUrl` missing হলে `"default-avatar.png"` (বা `"No avatar"`—choose one and document)
* `nullish coalescing (??)` ব্যবহার করো

### Example (Expected Behavior)

```ts
renderProfile({ name: "A" });
// bio -> "No bio"
```

### Edge Case

* `bio: ""` (empty string) vs `bio: undefined` difference explain (comment)

### Concepts Being Tested

* optional properties
* `??` vs `||` mindset

### Deliverables

* `Profile` type + `renderProfile()` + examples

---

## 2) Readonly SKU Update — Interview Assignment

### Objective

`Product { readonly sku; name; price }` বানাও এবং `updateProduct(p, patch)` এমন বানাও যাতে **sku change না হয়**, নতুন object return করে।

### Requirements

* return new product (no mutation)
* `patch`-এ `sku` দিলে compile-time এ reject হওয়া উচিত (best effort)

### Example (Expected Behavior)

```ts
updateProduct({ sku: "S1", name: "Pen", price: 10 }, { price: 12 });
// returns new product with same sku
```

### Edge Case

* `patch` includes `sku` should fail

### Concepts Being Tested

* readonly
* immutability
* patch typing (Omit/Partial)

### Deliverables

* Product type + update function + fail example

---

## 3) Optional Discount Apply — Interview Assignment

### Objective

`applyDiscount(price, discount?)` implement করো যেখানে `discount ?? 0` ব্যবহার হবে।

### Requirements

* `discount` undefined/null হলে 0 ধরবে
* `discount = 0` হলে price unchanged থাকবে (this is why `??`, not `||`)

### Example (Expected Behavior)

```ts
applyDiscount(100, undefined); // 100
applyDiscount(100, 0);         // 100
applyDiscount(100, 10);        // 90 (if percent) OR 90 (if flat) — define rule
```

### Concepts Being Tested

* `??` vs `||`

### Deliverables

* function + examples + rule comment

---

## 4) Readonly Array Add — Interview Assignment

### Objective

`readonly number[]` দিলে নতুন array return করো যেখানে extra value যোগ হবে।

### Requirements

* input mutate করা যাবে না
* return a new array

### Example (Expected Behavior)

```ts
addValue([1, 2] as readonly number[], 3); // [1,2,3]
```

### Concepts Being Tested

* readonly arrays
* immutability with spread

### Deliverables

* `addValue()` + example

---

## 5) Optional Chaining Nested — Interview Assignment

### Objective

`user.address?.city ?? "Unknown"` pattern দিয়ে utility function লিখো।

### Requirements

* address missing -> `"Unknown"`
* city missing -> `"Unknown"`

### Example (Expected Behavior)

```ts
getCity({ name: "A" }); // "Unknown"
```

### Edge Case

* city = "" should return "" (use `??` not `||`)

### Concepts Being Tested

* optional chaining `?.`
* nullish coalescing `??`

### Deliverables

* types + `getCity()` function

---

## 6) Partial Update Merge — Interview Assignment

### Objective

`merge<T>(base:T, patch:Partial<T>):T` implement করো without mutation.

### Requirements

* return `{...base, ...patch}`
* base unchanged

### Example (Expected Behavior)

```ts
merge({ name: "A", age: 1 }, { name: "B" }); // {name:"B", age:1}
```

### Edge Case

* Nested objects: shallow merge only (mention in comment)

### Concepts Being Tested

* `Partial<T>`
* immutability + spread

### Deliverables

* `merge<T>()` + note about shallow merge

---

## 7) Optional Param Currency — Interview Assignment

### Objective

`formatMoney(amount, currency?)` default `"INR"`.

### Requirements

* currency optional param with default value
* output format: `"INR 100"` (or `"₹100"`—choose and document)

### Example (Expected Behavior)

```ts
formatMoney(100);          // "INR 100"
formatMoney(100, "USD");   // "USD 100"
```

### Edge Case

* invalid currency string handling (optional): restrict union or runtime validate

### Concepts Being Tested

* default params
* optional parameters

### Deliverables

* function + examples

---

## 8) Readonly vs `Object.freeze` Demo — Interview Assignment

### Objective

Compile-time `readonly` vs runtime `Object.freeze()` difference দেখাও (code + short explanation)।

### Requirements

* `readonly` blocks TypeScript assignment but runtime can still mutate if you bypass types
* `Object.freeze` blocks runtime mutation (shallow)

### Example (Expected Behavior)

* show 2 snippets: one compile-time error, one runtime error (or no effect in non-strict)
* mention deep-freeze not automatic

### Concepts Being Tested

* compile-time vs runtime
* shallow freeze

### Deliverables

* demo code + 4–6 line explanation

---

## 9) Immutable Cart Qty Update — Interview Assignment

### Objective

Cart items array থেকে `sku` অনুযায়ী qty update করো immutably.

### Requirements

* return new array
* item found হলে only that item copied/updated
* not found -> return same reference (optional) or same values (document)

### Example (Expected Behavior)

```ts
updateQty([{ sku:"S1", qty:1 }], "S1", 2); // [{sku:"S1", qty:2}]
```

### Edge Case

* sku not found -> unchanged

### Concepts Being Tested

* immutability
* array map/update pattern

### Deliverables

* types + `updateQty()`

---

## 10) DeepReadonly vs Readonly — Interview Assignment

### Objective

Show that `Readonly<T>` deep lock করে না; DeepReadonly implement/ ব্যবহার দেখাও।

### Requirements

* Demonstrate: nested mutation allowed in Readonly, blocked in DeepReadonly
* Handle arrays too

### Example (Expected Behavior)

```ts
type R = Readonly<{ nested: { v: number } }>;   // nested.v mutable
type DR = DeepReadonly<{ nested: { v: number } }>; // nested.v readonly
```

### Concepts Being Tested

* mapped + conditional types
* recursion

### Deliverables

* demo + DeepReadonly (or reuse from Q01-17)

---

## 11) `exactOptionalPropertyTypes` Scenario — Interview Assignment

### Objective

Explain with code: `{ a?: string }` vs `{ a: string | undefined }` behavior difference.

### Requirements

* show assignment differences
* mention TS config `exactOptionalPropertyTypes` impacts behavior

### Example (Expected Behavior)

* `{a?:string}` cannot always accept `{a: undefined}` when config enabled (explain)

### Concepts Being Tested

* TS compiler option awareness

### Deliverables

* 2–3 code snippets + short explanation

---

## 12) Patch Unknown Key Reject (type-level) — Interview Assignment

### Objective

`updateUser(u, patch)` এমন করো যাতে `patch`-এ `User` ছাড়া অন্য keys দিলে compile error হয়।

### Requirements

* enforce `patch` keys are subset of `keyof User`

### Example (Expected Behavior)

```ts
updateUser(u, { name: "B" });     // ✅
updateUser(u, { role: "x" });     // ❌
```

### Edge Case

* generics inference limitation mention (comment)

### Concepts Being Tested

* `keyof` constraints
* generic function typing

### Deliverables

* update function signature + examples

---

## 13) ReadonlyMap Read — Interview Assignment

### Objective

Rates store করো `ReadonlyMap<string, number>` এ এবং key দিয়ে safe read করো fallback সহ।

### Requirements

* missing key -> `0`

### Example (Expected Behavior)

```ts
getRate(rates, "USD"); // 0 if missing
```

### Concepts Being Tested

* readonly collections
* safe map access

### Deliverables

* function + example map

---

## 14) `mergeDefaults<T>` — Interview Assignment

### Objective

`mergeDefaults(defaults:T, overrides?:Partial<T>):T` implement করো।

### Requirements

* overrides missing/undefined -> defaults return
* no mutation

### Example (Expected Behavior)

```ts
mergeDefaults({ a:1, b:2 });           // {a:1,b:2}
mergeDefaults({ a:1, b:2 }, { b:9 });  // {a:1,b:9}
```

### Concepts Being Tested

* generics
* optional parameter
* Partial

### Deliverables

* function + examples

---

## 15) DeepPartial Utility — Interview Assignment

### Objective

Nested patch এর জন্য `DeepPartial<T>` implement করো।

### Requirements

* object properties recursively optional
* arrays handled (element type DeepPartial)
* functions unchanged (optional rule—state your choice)

### Example (Expected Behavior)

```ts
type P = DeepPartial<{ a: { b: number }, arr: {x:number}[] }>;
/*
{ a?: { b?: number }, arr?: { x?: number }[] }
*/
```

### Concepts Being Tested

* conditional types
* recursion + arrays

### Deliverables

* `DeepPartial<T>` + example

---

## 16) Immutable Tree Update — Interview Assignment

### Objective

Nested tree structure এ `id` দিয়ে node label update করো without mutation.

### Requirements

* recursion
* return new tree
* unchanged branches keep reference (optional optimization)

### Example (Expected Behavior)

```ts
updateLabel(tree, "n2", "New"); // returns new tree
```

### Edge Case

* id missing -> return same tree

### Concepts Being Tested

* recursion
* immutability

### Deliverables

* Tree type + update function + examples

---

## 17) Readonly State Reducer — Interview Assignment

### Objective

Todo reducer লিখো: state readonly, actions union; add/toggle/remove => new state return।

### Requirements

* `State` readonly (at least array readonly)
* unknown action -> same state

### Example (Expected Behavior)

```ts
reduce(state, { type:"add", text:"x" });
```

### Concepts Being Tested

* union actions
* immutability patterns

### Deliverables

* action union + reducer

---

## 18) Branded ID — Interview Assignment

### Objective

`type UserId = string & { __brand: "UserId" }` বানাও এবং `makeUserId(s)` validation সহ implement করো।

### Requirements

* empty/whitespace -> throw
* return branded type

### Example (Expected Behavior)

```ts
const id = makeUserId("u1"); // UserId
makeUserId("");              // throws
```

### Concepts Being Tested

* branded types
* runtime validation

### Deliverables

* branded type + constructor function

---

## 19) Readonly Constructor Param — Interview Assignment

### Objective

Class বানাও যেখানে constructor param property দিয়ে `readonly id` set হবে।

### Requirements

* `constructor(readonly id: string, public name: string)` style
* reassign should fail

### Example (Expected Behavior)

```ts
const u = new UserModel("u1", "A");
u.id = "x"; // ❌
```

### Concepts Being Tested

* parameter properties
* readonly in classes

### Deliverables

* class + demo

---

## 20) Persistent List Update — Interview Assignment

### Objective

Given readonly list, `removeById(list, id)` returns new list without that item.

### Requirements

* no mutation
* id not found -> return same list (optional) or unchanged copy (document)

### Example (Expected Behavior)

```ts
removeById([{id:"1"},{id:"2"}] as const, "2"); // [{id:"1"}]
```

### Edge Case

* id not found -> same

### Concepts Being Tested

* immutability
* readonly arrays

### Deliverables

* function + examples
---
## 04) Generics — Interview Assignments (Top 20)

---

## 1) `identity<T>` — Interview Assignment

### Objective

`identity<T>(x: T): T` implement করো।

### Requirements

* input যেটা, output সেইটাই (same type + same value)

### Example (Expected Behavior)

```ts
identity(5);       // 5
identity("hello"); // "hello"
```

### Concepts Being Tested

* generics basics

### Deliverables

* `identity<T>` function + examples

---

## 2) `first<T>` — Interview Assignment

### Objective

Array থেকে first element return করো, না থাকলে `undefined`.

### Requirements

* signature: `first<T>(arr: T[]): T | undefined`

### Example (Expected Behavior)

```ts
first([1, 2, 3]); // 1
first([]);        // undefined
```

### Edge Case

* empty array

### Concepts Being Tested

* `T | undefined`

### Deliverables

* `first<T>` + examples

---

## 3) `wrap<T>` — Interview Assignment

### Objective

`wrap<T>(value: T)` -> `{ value: T }`

### Example (Expected Behavior)

```ts
wrap("a"); // { value: "a" }
```

### Concepts Being Tested

* generics

### Deliverables

* `wrap<T>` + examples

---

## 4) `pair<T, U>` — Interview Assignment

### Objective

Two values নিয়ে typed pair return করো (tuple or object—choose one).

### Requirements

* multiple type params use করতে হবে

### Example (Expected Behavior)

```ts
pair(1, "a"); // [1, "a"]
```

### Concepts Being Tested

* multiple generic params

### Deliverables

* `pair<T, U>` + examples

---

## 5) `mapArray<T, R>` — Interview Assignment

### Objective

Generic map function implement করো।

### Requirements

* signature: `mapArray<T, R>(arr: T[], fn: (x: T) => R): R[]`

### Example (Expected Behavior)

```ts
mapArray([1, 2], String); // ["1", "2"]
```

### Concepts Being Tested

* higher-order functions + generics

### Deliverables

* `mapArray<T, R>` + example

---

## 6) `ApiResponse<T>` + `mapResponse<T, R>` — Interview Assignment

### Objective

Generic response model বানাও এবং mapper লিখো যা ok হলে data map করবে, error হলে pass-through।

### Requirements

* `ApiResponse<T>` union-like shape allowed (recommended):

  * `{ ok: true; data: T } | { ok: false; error: string }`
* `mapResponse<T, R>(r, fn)` returns `ApiResponse<R>`

### Example (Expected Behavior)

```ts
mapResponse({ ok: true, data: 2 }, (n) => String(n)); // ok("2")
mapResponse({ ok: false, error: "bad" }, (n) => n);   // error pass-through
```

### Concepts Being Tested

* generics + union

### Deliverables

* `ApiResponse<T>` + `mapResponse()`

---

## 7) `Box<T>` class — Interview Assignment

### Objective

Generic class বানাও যা value ধরে রাখে, getter/setter typed থাকবে।

### Requirements

* `get(): T`
* `set(v: T): void`
* (optional) setter validation

### Example (Expected Behavior)

```ts
const b = new Box<number>(1);
b.set(2);     // ✅
b.set("x");   // ❌
```

### Concepts Being Tested

* generics + class

### Deliverables

* `Box<T>` + usage examples

---

## 8) Constraint `T extends { id: string }` — Interview Assignment

### Objective

`indexById(list)` implement করো যা `Record<string, T>` return করবে।

### Requirements

* `T` must have `id: string`
* duplicate id হলে “last wins” (document)

### Example (Expected Behavior)

```ts
indexById([{ id: "u1", name: "A" }]); // { u1: {id:"u1", name:"A"} }
```

### Concepts Being Tested

* generic constraints

### Deliverables

* `indexById<T extends {id:string}>()` + example

---

## 9) `pluck<T, K extends keyof T>` — Interview Assignment

### Objective

Object থেকে key দিয়ে value বের করো (typed)।

### Requirements

* signature: `pluck<T, K extends keyof T>(obj: T, key: K): T[K]`

### Example (Expected Behavior)

```ts
pluck({ name: "A", age: 1 }, "name"); // typed as string
```

### Concepts Being Tested

* `keyof`
* indexed access `T[K]`

### Deliverables

* `pluck()` + example

---

## 10) `sortByKey<T, K extends keyof T>` — Interview Assignment

### Objective

Array sort করো given key দিয়ে, কিন্তু key-এর value **only string/number** হলে।

### Requirements

* enforce comparable keys (best effort with constraints)
* return new sorted array (no mutation) OR document mutation

### Example (Expected Behavior)

```ts
sortByKey([{ price: 10 }, { price: 5 }], "price"); // price ascending
```

### Edge Case

* undefined values (define behavior: treat as last / throw / filter)

### Concepts Being Tested

* constraints + generics

### Deliverables

* `sortByKey()` + examples

---

## 11) Generic Default Type — Interview Assignment

### Objective

Default generic params সহ type বানাও: `type Result<T = string, E = Error> = ...`

### Requirements

* generic args না দিলে defaults apply হবে

### Example (Expected Behavior)

```ts
type R1 = Result;              // Result<string, Error>
type R2 = Result<number, string>;
```

### Concepts Being Tested

* default generic params

### Deliverables

* `Result<T=..., E=...>` + 2 examples

---

## 12) `parseJson<T>(s, guard)` — Interview Assignment

### Objective

JSON string parse করে `unknown` নাও, তারপর guard দিয়ে `T` validate করো।

### Requirements

* invalid JSON -> error/result
* guard false -> error/result
* you choose: return `Result<T,string>` or throw (document)

### Example (Expected Behavior)

```ts
parseJson('{"id":"u1"}', isUser); // ok(User) or User
parseJson("{bad", isUser);        // error
```

### Concepts Being Tested

* `unknown`
* runtime type guards

### Deliverables

* `parseJson<T>()` + a sample guard

---

## 13) `freeze<T>(obj): Readonly<T>` — Interview Assignment

### Objective

`freeze<T>(obj)` return type should be `Readonly<T>`; compile-time protection demo করো।

### Requirements

* implement using `Object.freeze` (shallow)
* show TS prevents assignment

### Example (Expected Behavior)

```ts
const u = freeze({ id: "u1", name: "A" });
u.name = "B"; // ❌ TS error
```

### Edge Case

* shallow only (nested still mutable unless DeepReadonly)

### Concepts Being Tested

* `Readonly<T>`

### Deliverables

* `freeze<T>` + demo

---

## 14) `timeout<T>(promise, ms)` — Interview Assignment

### Objective

Promise কে wrap করে `ms` পরে reject করবে যদি resolve না হয়।

### Requirements

* return type: `Promise<T>`
* cleanup timer (best effort)

### Example (Expected Behavior)

```ts
await timeout(fetchData(), 1000); // throws on timeout
```

### Concepts Being Tested

* Promise generics

### Deliverables

* `timeout<T>()` + example usage

---

## 15) `PromiseReturn<T>` — Interview Assignment

### Objective

`type PromiseReturn<T> = T extends Promise<infer X> ? X : T`

### Example (Expected Behavior)

```ts
type A = PromiseReturn<Promise<number>>; // number
type B = PromiseReturn<string>;          // string
```

### Optional Edge Case

* nested promises handle (optional): `PromiseReturn<Promise<Promise<number>>>`

### Concepts Being Tested

* conditional types + `infer`

### Deliverables

* `PromiseReturn<T>` + examples

---

## 16) `FuncReturn<T>` — Interview Assignment

### Objective

Function return type extract করো: `T extends (...a:any)=>infer R ? R : never`

### Example (Expected Behavior)

```ts
type R = FuncReturn<() => number>; // number
type N = FuncReturn<string>;       // never
```

### Concepts Being Tested

* `infer` + conditional types

### Deliverables

* `FuncReturn<T>` + examples

---

## 17) Typed Event Emitter — Interview Assignment

### Objective

Event map generic বানাও যেখানে `on(event, handler)` এবং `emit(event, payload)` fully typed থাকবে।

### Requirements

* `Events` map: `{ login: {userId:string}; logout: void; }` style
* wrong payload -> compile error

### Example (Expected Behavior)

```ts
emitter.emit("login", { userId: "u1" }); // ✅
emitter.emit("login", { id: "u1" });     // ❌
```

### Concepts Being Tested

* mapped types + generics

### Deliverables

* minimal emitter type + tiny runtime stub

---

## 18) Fluent Builder Accumulating Keys — Interview Assignment

### Objective

Builder chain typed: `set(key, value)` accumulates keys; `build()` returns final typed object.

### Requirements

* duplicate set overwrites type/value (latest wins)

### Example (Expected Behavior)

```ts
createBuilder().set("name", "A").build(); // { name: string }
```

### Concepts Being Tested

* advanced generics
* type accumulation

### Deliverables

* builder API + examples

---

## 19) Primitive Schema to Type — Interview Assignment

### Objective

Schema union থেকে TS type map করো:
`{ kind:"string" } | { kind:"number" }` → `string | number` (type-level mapping)

### Requirements

* conditional types mapping
* optional: arrays support `{ kind:"array"; of: Schema }`

### Example (Expected Behavior)

```ts
type S = { kind: "string" } | { kind: "number" };
type T = SchemaToType<S>; // string | number (depending on design)
```

### Concepts Being Tested

* conditional types

### Deliverables

* `SchemaToType<S>` + examples

---

## 20) `decode<T>(unknown, guard)` — Interview Assignment

### Objective

Safe decode pattern implement করো: `decode<User>(u, isUser)`।

### Requirements

* input `unknown`
* guard false হলে throw বা Result return (document)
* guard true হলে typed `T` return

### Example (Expected Behavior)

```ts
decode<User>(payload, isUser); // returns User or error
```

### Concepts Being Tested

* runtime validation + generics

### Deliverables

* `decode<T>()` + sample guard + examples
## 05) Enum vs String Literal Union — Interview Assignments (Top 20)

---

## 1) Status union — Interview Assignment

### Objective

`type Status = "draft" | "published"` বানাও এবং `canEdit(status)` true হবে শুধু `"draft"` হলে।

### Requirements

* `canEdit(status: Status): boolean`

### Example (Expected Behavior)

```ts
canEdit("published"); // false
canEdit("draft");     // true
```

### Concepts Being Tested

* string literal unions

### Deliverables

* `Status` + `canEdit()`

---

## 2) Same as enum — Interview Assignment

### Objective

`enum StatusEnum { Draft="draft", Published="published" }` বানাও এবং union type-এর সাথে usage compare করো।

### Requirements

* show: `StatusEnum.Draft`

### Example (Expected Behavior)

```ts
const s: StatusEnum = StatusEnum.Draft;
```

### Edge Case

* runtime output explain (enum exists at runtime; union doesn’t)

### Concepts Being Tested

* enums + runtime JS output

### Deliverables

* enum code + short comparison note

---

## 3) Switch label — Interview Assignment

### Objective

Status → label map করো `switch` দিয়ে।

### Requirements

* `draft -> "Draft"`, `published -> "Published"`
* exhaustive check (`assertNever`)

### Example (Expected Behavior)

```ts
toLabel("draft"); // "Draft"
```

### Concepts Being Tested

* switch + never exhaustive

### Deliverables

* `toLabel(status: Status)` + `assertNever`

---

## 4) Record label map — Interview Assignment

### Objective

`Record<Status, string>` দিয়ে label map বানাও।

### Requirements

* missing key দিলে compile error হওয়া উচিত

### Example (Expected Behavior)

```ts
labelMap[status];
```

### Concepts Being Tested

* `Record`

### Deliverables

* `labelMap: Record<Status,string>`

---

## 5) Prevent invalid string — Interview Assignment

### Objective

`"drafft"` assignment fail করা দেখাও।

### Requirements

* only valid `Status` assignable

### Example (Expected Behavior)

```ts
const s: Status = "drafft"; // ❌
```

### Edge Case

* external data parse needed (mention)

### Concepts Being Tested

* literal unions safety

### Deliverables

* failing snippet + short note

---

## 6) const object + `as const` — Interview Assignment

### Objective

`const STATUS = {...} as const` দিয়ে enum-like pattern বানাও এবং type derive করো।

### Requirements

* `type Status = typeof STATUS[keyof typeof STATUS]`

### Example (Expected Behavior)

```ts
STATUS.Draft; // "draft"
```

### Concepts Being Tested

* `as const`
* deriving union from values

### Deliverables

* STATUS const + derived `Status`

---

## 7) `parseStatus(input: unknown)` — Interview Assignment

### Objective

External value validate করে `Status` বানাও; invalid হলে throw বা Result return (choose + document)।

### Requirements

* trim whitespace
* optional: lowercase/uppercase normalize (document)

### Example (Expected Behavior)

```ts
parseStatus("draft"); // ok
parseStatus(" DRAFT "); // ok if normalized
parseStatus("x"); // error
```

### Concepts Being Tested

* runtime validation + types

### Deliverables

* `parseStatus()` + examples

---

## 8) Exhaustive transitions — Interview Assignment

### Objective

Only transition allowed: `draft -> published`.
`published -> draft` invalid.

### Requirements

* enforce rule in `transition(current, next)` (throw or Result)

### Example (Expected Behavior)

```ts
transition("draft", "published"); // ok
transition("published", "draft"); // error
```

### Concepts Being Tested

* state machine with unions

### Deliverables

* `transition()` + tests

---

## 9) Enum compile footprint explanation — Interview Assignment

### Objective

Numeric enum vs string enum runtime output brief explain করো (small code + comment)।

### Requirements

* show that enums emit JS

### Concepts Being Tested

* runtime JS output awareness

### Deliverables

* short snippet + 3–5 line explanation

---

## 10) Reverse mapping demo — Interview Assignment

### Objective

Numeric enum reverse mapping দেখাও: `Enum[0] -> "A"`.

### Requirements

* must be numeric enum (not string)

### Example (Expected Behavior)

```ts
enum E { A, B }
E[0]; // "A"
```

### Concepts Being Tested

* enums reverse mapping

### Deliverables

* demo code

---

## 11) Tree-shaking discussion (coding hint) — Interview Assignment

### Objective

Explain why string unions / `as const` objects often smaller than enums in libraries.

### Requirements

* short explanation, bundler note

### Concepts Being Tested

* bundling awareness

### Deliverables

* 4–8 line explanation + tiny example

---

## 12) Serialize/deserialize — Interview Assignment

### Objective

Status localStorage এ string হিসেবে store করো, restore করার সময় parser ব্যবহার করো।

### Requirements

* corrupted storage handle (fallback or error)

### Example (Expected Behavior)

```ts
saveStatus("draft");
loadStatus(); // Status
```

### Concepts Being Tested

* parsing + validation

### Deliverables

* `saveStatus`, `loadStatus`, uses `parseStatus`

---

## 13) Role permission map — Interview Assignment

### Objective

`type Role="ADMIN"|"USER"|"GUEST"` এবং `Record<Role,string[]>` permission map।

### Example (Expected Behavior)

```ts
permissions.ADMIN; // string[]
```

### Concepts Being Tested

* unions + Record

### Deliverables

* Role type + permissions map + getter function (optional)

---

## 14) Feature flag keys union — Interview Assignment

### Objective

Flags keys union বানাও এবং `isEnabled(flag)` typed করো।

### Requirements

* unknown flag should error at compile-time

### Example (Expected Behavior)

```ts
isEnabled("newUI");   // ✅
isEnabled("unknown"); // ❌
```

### Concepts Being Tested

* unions for keys

### Deliverables

* flag union + function

---

## 15) Enum-like object pattern — Interview Assignment

### Objective

PaymentMethod const object + derived type pattern বানাও।

### Example (Expected Behavior)

```ts
PaymentMethod.Card; // "card"
```

### Concepts Being Tested

* `as const` enum-like pattern

### Deliverables

* const object + derived union type

---

## 16) Strict parser with helpful error — Interview Assignment

### Objective

Parser error message এ allowed values list থাকবে।

### Example (Expected Behavior)

```ts
parseStatus("x"); // throws "Allowed: draft,published"
```

### Concepts Being Tested

* runtime validation UX

### Deliverables

* improved `parseStatus()` error formatting

---

## 17) Typed transition map — Interview Assignment

### Objective

`const transitions: Record<Status, Status[]>` দিয়ে allowed next states define করো।

### Example (Expected Behavior)

```ts
transitions["draft"]; // includes "published"
```

### Concepts Being Tested

* Record + state transitions

### Deliverables

* `transitions` + `canTransition(from,to)` helper

---

## 18) Template literal type events — Interview Assignment

### Objective

`type EventName = \`user:${"created"|"deleted"}``

### Example (Expected Behavior)

```ts
const e: EventName = "user:created"; // ✅
```

### Concepts Being Tested

* template literal types

### Deliverables

* EventName type + example

---

## 19) i18n labels mapping — Interview Assignment

### Objective

`Record<Status, { en: string; bn: string }>` labels map বানাও।

### Example (Expected Behavior)

```ts
labels[status].bn;
```

### Concepts Being Tested

* Record mapping

### Deliverables

* labels map + usage

---

## 20) Endpoint by status — Interview Assignment

### Objective

`getEndpoint(status: Status)` exhaustive switch দিয়ে implement করো।

### Example (Expected Behavior)

```ts
getEndpoint("draft"); // "/draft"
```

### Concepts Being Tested

* exhaustive switch

### Deliverables

* function + assertNever

---

---

## 06) Any / Unknown / Never — Interview Assignments (Top 20)

---

## 1) unknown to string — Interview Assignment

### Objective

`normalizeName(x: unknown): string`
string হলে trim করো, না হলে throw.

### Requirements

* empty after trim -> throw

### Example (Expected Behavior)

```ts
normalizeName(" A "); // "A"
normalizeName(12);    // throws
normalizeName("   "); // throws
```

### Concepts Being Tested

* unknown narrowing

### Deliverables

* function + examples

---

## 2) Replace `any` with `unknown` — Interview Assignment

### Objective

একটা function param `any` ছিল—`unknown` করো এবং safe checks যোগ করো (before/after দেখাও)।

### Requirements

* show unsafe version and safe version

### Concepts Being Tested

* safety improvements

### Deliverables

* 2 snippets (before/after) + short note

---

## 3) safeJsonParse returns unknown — Interview Assignment

### Objective

`safeJsonParse(s): unknown | null`
parse fail -> null.

### Example (Expected Behavior)

```ts
safeJsonParse('{"a":1}'); // object (unknown)
safeJsonParse("{bad");    // null
```

### Concepts Being Tested

* unknown-safe parsing

### Deliverables

* function

---

## 4) `isNumberArray` guard — Interview Assignment

### Objective

`isNumberArray(u): u is number[]` implement করো।

### Example (Expected Behavior)

```ts
isNumberArray([1, 2]);     // true
isNumberArray([1, "2"]);   // false
```

### Concepts Being Tested

* type predicate

### Deliverables

* guard function + examples

---

## 5) `fail(msg): never` — Interview Assignment

### Objective

Always throws এমন function লিখো।

### Requirements

* return type `never`

### Example (Expected Behavior)

```ts
fail("oops"); // throws
```

### Concepts Being Tested

* never

### Deliverables

* `fail()` function

---

## 6) `assertNever` helper — Interview Assignment

### Objective

`assertNever(x: never): never` লিখে exhaustive switch এ ব্যবহার করো।

### Example (Expected Behavior)

* new union member add করলে switch compile fail হবে

### Concepts Being Tested

* exhaustive checking

### Deliverables

* helper + switch example

---

## 7) catch error safely — Interview Assignment

### Objective

`catch (e)` এ `e` unknown—
`e instanceof Error` হলে message print, না হলে stringify.

### Example (Expected Behavior)

```ts
try { throw "bad"; } catch (e) { /* safe */ }
```

### Concepts Being Tested

* unknown in catch

### Deliverables

* try/catch snippet + safe printer

---

## 8) validate User shape at runtime — Interview Assignment

### Objective

`isUser(u: unknown): u is { id: string; name: string }`

### Example (Expected Behavior)

```ts
isUser({ id: "u1", name: "A" }); // true
isUser({ id: "u1" });            // false
```

### Concepts Being Tested

* runtime validation

### Deliverables

* guard + examples

---

## 9) decode API response unknown -> User — Interview Assignment

### Objective

`decodeUser(u: unknown): User` guard ব্যবহার করে decode করো; invalid হলে throw.

### Requirements

* reuse `isUser`

### Example (Expected Behavior)

```ts
decodeUser(payload); // User or throws
```

### Concepts Being Tested

* decode pattern

### Deliverables

* `User` type + `isUser` + `decodeUser`

---

## 10) Error union mapping — Interview Assignment

### Objective

Custom error union বানাও এবং HTTP-like status code map করো।

### Example (Expected Behavior)

```ts
toStatus({ type: "auth" }); // 401
```

### Edge Case

* unknown -> 500

### Concepts Being Tested

* unions + fallback

### Deliverables

* error union + mapper

---

## 11) Remove unsafe casting — Interview Assignment

### Objective

কোডে `as User` আছে—এটা remove করে guard-based safe code করো।

### Requirements

* show “unsafe” then “safe”

### Concepts Being Tested

* avoid `as` casts

### Deliverables

* before/after snippet

---

## 12) Conditional returns `never` on invalid — Interview Assignment

### Objective

`type NonString<T> = T extends string ? never : T`

### Example (Expected Behavior)

```ts
type A = NonString<string>;        // never
type B = NonString<number>;        // number
type C = NonString<string|number>; // number (distribution)
```

### Concepts Being Tested

* conditional types distribution

### Deliverables

* type + examples

---

## 13) Parse query params unknown — Interview Assignment

### Objective

`parseLimit(x: unknown): number` where limit must be 1..100.

### Requirements

* accept `"10"` or `10`
* reject 0, 101, NaN

### Example (Expected Behavior)

```ts
parseLimit("10"); // 10
parseLimit(0);    // error
```

### Concepts Being Tested

* runtime validation + narrowing

### Deliverables

* function + examples

---

## 14) Narrow unknown event payload by topic — Interview Assignment

### Objective

Topic string দেখে payload validate করো (topic→guard mapping)।

### Example (Expected Behavior)

```ts
decodeEvent("user.created", payload); // expects {id:string}
```

### Edge Case

* unknown topic -> error

### Concepts Being Tested

* mapping + validation

### Deliverables

* topic union + decoder

---

## 15) Mini validator combinators — Interview Assignment

### Objective

Basic validators বানাও: `isString`, `isNumber`, `isObject`, `hasKey` এবং compose করে shape validate দেখাও।

### Edge Case

* `typeof null === "object"` handle করো

### Concepts Being Tested

* runtime checks composition

### Deliverables

* 4 validators + one composed example

---

## 16) Result-based errors instead of throw — Interview Assignment

### Objective

Throw না করে `Result<T,E>` return করো parser/decoder থেকে।

### Example (Expected Behavior)

```ts
ok(value) / err(message)
```

### Concepts Being Tested

* result union

### Deliverables

* `Result` type + one parser

---

## 17) `assertDefined` — Interview Assignment

### Objective

`assertDefined(x): asserts x is NonNullable<T>` implement করো।

### Requirements

* null/undefined only fail; falsy values (0,"") pass

### Example (Expected Behavior)

```ts
assertDefined(x);
x.toString(); // safe after assert
```

### Concepts Being Tested

* assertion functions

### Deliverables

* `assertDefined` + example

---

## 18) Safe deep get — Interview Assignment

### Objective

`get(u: unknown, path: string[]): unknown` safe deep access (missing path -> undefined)।

### Edge Case

* arrays index as string (optional)

### Concepts Being Tested

* unknown-safe utilities

### Deliverables

* `get()` + examples

---

## 19) Unknown payload registry typed — Interview Assignment

### Objective

`topic -> parser` registry বানাও যাতে `parse(topic, payload)` return type inferred হয়।

### Requirements

* topic not found -> error/result

### Concepts Being Tested

* generics + mapping

### Deliverables

* registry + typed parse function

---

## 20) Never unreachable branch demonstration — Interview Assignment

### Objective

Union switch এ default unreachable ensure করো; নতুন member add করলে compile error দেখাও।

### Requirements

* use `assertNever`

### Concepts Being Tested

* never exhaustive

### Deliverables

* union + switch + assertNever demo
## 07) Null / Undefined / Strict Mode — Interview Assignments (Top 20)

---

## 1) Nullable username display — Interview Assignment

### Objective

`username: string | null` দিলে display value বের করো: null হলে `"Guest"`, কিন্তু empty string `""` হলে `""`-ই দেখাবে।

### Requirements

* fallback only for `null` (and optionally `undefined` if included)
* `""` should NOT become `"Guest"`

### Example (Expected Behavior)

```ts id="r4chgj"
displayName(null); // "Guest"
displayName("");   // ""
displayName("A");  // "A"
```

### Concepts Being Tested

* nullish vs falsy (`??` vs `||`)

### Deliverables

* function + examples

---

## 2) Optional param vs `string | undefined` — Interview Assignment

### Objective

2টা function বানাও এবং call sites এ difference দেখাও:

1. `f(x?: string)`
2. `g(x: string | undefined)`

### Requirements

* demonstrate which calls compile and why
* mention `exactOptionalPropertyTypes` effect (short note)

### Example (Expected Behavior)

```ts id="ak9pfx"
f();          // ✅
g(undefined); // ✅
g();          // ❌ (argument missing)
```

### Concepts Being Tested

* optional semantics
* `undefined` union vs optional parameter

### Deliverables

* 2 functions + compile-demo comments

---

## 3) `strictNullChecks` demo — Interview Assignment

### Objective

Show why `let s: string = null` fails in strict mode, and fix with union.

### Requirements

* show failing snippet
* fix: `string | null`

### Example (Expected Behavior)

```ts id="1i3ak9"
let s: string = null;        // ❌
let t: string | null = null; // ✅
```

### Concepts Being Tested

* TS strictness (`strictNullChecks`)

### Deliverables

* short snippet + 1–2 line explanation

---

## 4) Non-null assertion risk — Interview Assignment

### Objective

`user!.name` কেন risky সেটা দেখাও এবং safer guard-based version লিখো।

### Requirements

* replace `!` with `if (!user) ...` guard

### Example (Expected Behavior)

```ts id="g5h6r1"
getUserName(user); // safe output or fallback
```

### Edge Case

* user undefined/null

### Concepts Being Tested

* safer patterns over non-null assertion

### Deliverables

* unsafe snippet + safe replacement

---

## 5) Optional chaining safe access — Interview Assignment

### Objective

`config.api?.timeoutMs ?? 3000` pattern implement করো।

### Requirements

* missing -> 3000
* timeout=0 -> keep 0 (so use `??`, not `||`)

### Example (Expected Behavior)

```ts id="v2imn8"
getTimeout({}); // 3000
getTimeout({ api: { timeoutMs: 0 } }); // 0
```

### Concepts Being Tested

* `?.` + `??`

### Deliverables

* types + `getTimeout()`

---

## 6) `??` vs `||` — Interview Assignment

### Objective

0, `""`, `false` দিয়ে `??` এবং `||` difference prove করো।

### Requirements

* show outputs clearly

### Example (Expected Behavior)

```ts id="u8c7n3"
0 ?? 10;   // 0
0 || 10;   // 10
"" ?? "x"; // ""
"" || "x"; // "x"
false ?? true; // false
false || true; // true
```

### Concepts Being Tested

* operators + falsy vs nullish

### Deliverables

* demo snippet + 2–3 line explanation

---

## 7) Default param — Interview Assignment

### Objective

`function f(x: number = 10)` vs `function g(x?: number)` compare করো।

### Requirements

* show `f()` -> 10
* show `g()` -> undefined unless you handle inside
* discuss explicit `undefined` call

### Example (Expected Behavior)

```ts id="5o8u2y"
f();           // 10
f(undefined);  // 10
g();           // undefined
```

### Concepts Being Tested

* default params vs optional params

### Deliverables

* both functions + notes

---

## 8) Parse optional to number — Interview Assignment

### Objective

`parsePage(x: string | undefined): number` default 1. Invalid হলে 1 বা error—একটা choose করে document করো।

### Requirements

* undefined -> 1
* `"10"` -> 10
* invalid string -> chosen behavior

### Example (Expected Behavior)

```ts id="h3j5q1"
parsePage(undefined); // 1
parsePage("2");       // 2
parsePage("x");       // 1 or error
```

### Concepts Being Tested

* parsing + undefined handling

### Deliverables

* function + examples + behavior note

---

## 9) Filter undefined with predicate — Interview Assignment

### Objective

`(string | undefined)[]` -> `string[]` type-safely।

### Requirements

* keep `""` (empty string)
* only remove `undefined`

### Example (Expected Behavior)

```ts id="q9sh4a"
compact(["a", undefined, ""]); // ["a", ""]
```

### Concepts Being Tested

* type predicate (`x is string`)

### Deliverables

* predicate + `compact()` function

---

## 10) `Maybe<T>` helper — Interview Assignment

### Objective

`type Maybe<T> = T | null | undefined` এবং `unwrapMaybe(x, fallback)` implement করো।

### Requirements

* null/undefined -> fallback
* 0/""/false -> should NOT fallback

### Example (Expected Behavior)

```ts id="u9j0k1"
unwrapMaybe(null, 10); // 10
unwrapMaybe(0, 10);    // 0
```

### Concepts Being Tested

* nullish handling

### Deliverables

* type + function + examples

---

## 11) Form value normalize — Interview Assignment

### Objective

Input `string | null` -> output `string` (null হলে `""`)।

### Requirements

* optional: trim behavior define

### Example (Expected Behavior)

```ts id="08p4ze"
normalizeInput(null);     // ""
normalizeInput(" A ");    // " A " (or "A" if trimming)
```

### Concepts Being Tested

* normalization

### Deliverables

* function + note

---

## 12) Date format safe — Interview Assignment

### Objective

`Date | null` -> string; null -> `"N/A"`।

### Requirements

* invalid date handling (define): `"Invalid date"` or `"N/A"`

### Example (Expected Behavior)

```ts id="xg2b1m"
formatDate(null); // "N/A"
```

### Concepts Being Tested

* null handling + runtime checks

### Deliverables

* function + examples

---

## 13) Config required env vars — Interview Assignment

### Objective

`getEnv(name): string` throws if missing.

### Requirements

* decide: empty string allowed or treated as missing (document)

### Example (Expected Behavior)

```ts id="u2h2xq"
getEnv("API_KEY"); // string or throws
```

### Concepts Being Tested

* runtime strictness

### Deliverables

* function + behavior note

---

## 14) Strict function types (variance) — Interview Assignment

### Objective

Explain + show code why `(x: string) => void` not assignable to `(x: string | number) => void` in strict mode।

### Requirements

* short explanation
* show assignment error snippet

### Example (Expected Behavior)

```ts id="f2q2l7"
let wide: (x: string | number) => void;
const narrow: (x: string) => void = (x) => {};
wide = narrow; // ❌ in strictFunctionTypes (common in TS)
```

### Concepts Being Tested

* variance / function parameter compatibility

### Deliverables

* snippet + explanation

---

## 15) `exactOptionalPropertyTypes` behavior — Interview Assignment

### Objective

Show `{ a?: string }` vs `{ a: string | undefined }` assignment differences under exact optional setting.

### Requirements

* mention config dependent

### Example (Expected Behavior)

```ts id="p0x2v9"
type A = { a?: string };
type B = { a: string | undefined };
// demonstrate assignments + note about exactOptionalPropertyTypes
```

### Concepts Being Tested

* TS config awareness

### Deliverables

* snippet + 3–6 line explanation

---

## 16) `assertDefined` utility — Interview Assignment

### Objective

Assertion function to narrow null/undefined.

### Requirements

* falsy values pass; only null/undefined fail
* signature: `asserts x is NonNullable<T>`

### Example (Expected Behavior)

```ts id="2b1bbr"
assertDefined(x);
x.toString(); // safe
```

### Concepts Being Tested

* assertion functions

### Deliverables

* `assertDefined()` + example

---

## 17) `NonNullable` usage — Interview Assignment

### Objective

`type NN = NonNullable<string | null | undefined>` demonstrate result is `string`.

### Example (Expected Behavior)

```ts id="b7b4fi"
type NN = NonNullable<string | null | undefined>; // string
```

### Concepts Being Tested

* utility types

### Deliverables

* type alias + example

---

## 18) Optional keys mapping (advanced) — Interview Assignment

### Objective

Type-level: `OptionalKeys<T>` বের করো (optional keys union)।

### Requirements

* mapped + conditional types
* mention “tricky types” limitation (brief)

### Example (Expected Behavior)

```ts id="1t3bzx"
type X = OptionalKeys<{ a: string; b?: number; c?: undefined }>;
// "b" | "c"
```

### Concepts Being Tested

* mapped + conditional types

### Deliverables

* `OptionalKeys<T>` + example

---

## 19) Strict config schema runtime — Interview Assignment

### Objective

Config load করো যেখানে **কোন field undefined থাকবে না**; validate করে typed config return করো।

### Requirements

* missing -> throw/result
* nested required fields supported (at least 1 level)

### Example (Expected Behavior)

```ts id="8dn2r0"
loadConfig({ apiBaseUrl: "x" }); // ok
loadConfig({});                  // error
```

### Concepts Being Tested

* runtime validation + typed return

### Deliverables

* config type + loader + validation

---

## 20) Builder ensures required fields before build — Interview Assignment

### Objective

Typestate builder: `setApiBaseUrl()` call না করলে `build()` callable হবে না।

### Requirements

* minimal version acceptable
* compile-time blocking demo

### Example (Expected Behavior)

```ts id="v1b7o3"
createConfigBuilder().build();                 // ❌
createConfigBuilder().setApiBaseUrl("x").build(); // ✅
```

### Concepts Being Tested

* typestate pattern (generics)

### Deliverables

* builder types + example

---
## 11) Workflow + Exhaustive Checks — Interview Assignments (Top 20)  
Non-HMS: support tickets / content pipeline.

---

## 1) Ticket status union — Interview Assignment

### Objective  
`type Status = "open" | "in_progress" | "resolved"` এবং `canClose(status)` লিখো।

### Requirements  
- only `"resolved"` হলে close allowed (বা তোমার rule define করো—then document)

### Example (Expected Behavior)

```ts id="q11_01"
canClose("open");        // false
canClose("resolved");    // true
```

### Concepts Being Tested  
- unions

### Deliverables  
- `Status` + `canClose()`

---

## 2) Video pipeline statuses — Interview Assignment

### Objective  
`"uploaded" | "transcoding" | "published" | "failed"` → label function লিখো।

### Requirements  
- `failed -> "Failed"`  
- exhaustive switch + `assertNever`

### Example (Expected Behavior)

```ts id="q11_02"
toVideoLabel("failed"); // "Failed"
```

### Concepts Being Tested  
- switch exhaustiveness

### Deliverables  
- `VideoStatus` + label function

---

## 3) Exhaustive handler — Interview Assignment

### Objective  
All statuses handle করতে হবে; `default` এ `assertNever(x)`।

### Requirements  
- new status add করলে compile error trigger হবে (demo comment)

### Example (Expected Behavior)

```ts id="q11_03"
handleStatus(status); // compile-safe
```

### Concepts Being Tested  
- never + exhaustive checks

### Deliverables  
- `assertNever` + handler

---

## 4) Filter open tickets — Interview Assignment

### Objective  
Tickets list থেকে শুধু `"open"` status filter করো।

### Requirements  
- return new array

### Example (Expected Behavior)

```ts id="q11_04"
filterOpen([{status:"open"},{status:"resolved"}]); // only open
```

### Concepts Being Tested  
- filter + unions

### Deliverables  
- `Ticket` type + `filterOpen()`

---

## 5) Transition with actions — Interview Assignment

### Objective  
Action union: `"start"|"resolve"|"reopen"`; state transition rules enforce করো।

### Suggested Rules  
- open + start → in_progress  
- in_progress + resolve → resolved  
- resolved + reopen → open  
- other combos invalid

### Example (Expected Behavior)

```ts id="q11_05"
transition("open", "start"); // "in_progress"
transition("resolved", "start"); // error/result
```

### Concepts Being Tested  
- state machine + unions

### Deliverables  
- `Status`, `Action`, `transition()`

---

## 6) Resolved requires note — Interview Assignment

### Objective  
Resolved ticket এ `resolutionNote` must. Type-level বা runtime—যেকোনো একভাবে enforce করো।

### Requirements  
- note empty string invalid

### Example (Expected Behavior)

```ts id="q11_06"
const t: Ticket = { status:"resolved", resolutionNote:"Fixed" }; // ✅
const x: Ticket = { status:"resolved" }; // ❌ (type-level) OR runtime reject
```

### Concepts Being Tested  
- discriminated union payloads

### Deliverables  
- ticket model + validation

---

## 7) Process Result union — Interview Assignment

### Objective  
Workflow step returns `Result<T,E>`; error হলে bubble করে pipeline stop করো।

### Requirements  
- support single error or `errors: string[]` (choose + document)

### Example (Expected Behavior)

```ts id="q11_07"
runSteps(); // stops on first err
```

### Concepts Being Tested  
- Result<T,E> flow

### Deliverables  
- `Result` type + small pipeline

---

## 8) Typed actions drive transitions — Interview Assignment

### Objective  
Actions payload সহ model করো:  
`{ type:"assign"; agentId:string }`, `{ type:"resolve"; note:string }` etc.

### Requirements  
- assign করলে `assigneeId` set হবে  
- resolved ticket এ assign invalid

### Example (Expected Behavior)

```ts id="q11_08"
reduce(ticket, { type:"assign", agentId:"a1" }); // assignee set
```

### Concepts Being Tested  
- action unions + typed payloads

### Deliverables  
- action union + reducer/transition

---

## 9) Guard invalid transition — Interview Assignment

### Objective  
Invalid transition হলে throw না করে error object return করো।

### Requirements  
- `{ ok:false, error:"InvalidTransition" }` style

### Example (Expected Behavior)

```ts id="q11_09"
tryTransition("resolved", "start"); // {ok:false,...}
```

### Concepts Being Tested  
- result errors

### Deliverables  
- `tryTransition()` + error type

---

## 10) Timeline event log — Interview Assignment

### Objective  
Every transition এ timeline event append করো।

### Requirements  
- events include `type`, `at` (timestamp), `from`, `to`

### Example (Expected Behavior)

```ts id="q11_10"
ticket.timeline.length++; // on each transition
```

### Edge Case  
- monotonic time: `Date.now()` sufficient (note)

### Concepts Being Tested  
- event logging

### Deliverables  
- event model + update logic

---

## 11) Retry failed step — Interview Assignment

### Objective  
Pipeline state: `failed -> retry -> transcoding` with max retries 2.

### Requirements  
- after 2 retries, stay failed  
- success হলে retryCounter reset

### Example (Expected Behavior)

```ts id="q11_11"
retry(failedState); // may move to transcoding until max
```

### Concepts Being Tested  
- counters + state rules

### Deliverables  
- state model + retry logic

---

## 12) Must assign before in_progress — Interview Assignment

### Objective  
Ticket assigned না থাকলে `"start"` action invalid.

### Requirements  
- open unassigned + start -> error/result  
- optional: auto-assign (if you choose, document)

### Example (Expected Behavior)

```ts id="q11_12"
startTicket({status:"open", assigneeId: undefined}); // error/result
```

### Concepts Being Tested  
- invariants

### Deliverables  
- rule enforced in transition/reducer

---

## 13) SLA overdue flag — Interview Assignment

### Objective  
Open থাকা সময় N hours ছাড়ালে `overdue=true` compute করো।

### Requirements  
- use ms math (`Date.now()` - createdAtMs)  
- ignore timezone (ms-based)

### Example (Expected Behavior)

```ts id="q11_13"
isOverdue(ticket, 24); // boolean
```

### Concepts Being Tested  
- time logic

### Deliverables  
- `isOverdue()` + example

---

## 14) Batch process summary — Interview Assignment

### Objective  
Tickets count by status → `Record<Status, number>`.

### Requirements  
- empty list -> zeros for all statuses

### Example (Expected Behavior)

```ts id="q11_14"
summarize([]); // {open:0,in_progress:0,resolved:0}
```

### Concepts Being Tested  
- Record + initialization

### Deliverables  
- `summarizeByStatus()`

---

## 15) Typed transition map (advanced) — Interview Assignment

### Objective  
`const allowed: Record<Status, Status[]>` define করো এবং membership check করো।

### Example (Expected Behavior)

```ts id="q11_15"
allowed.open.includes("in_progress"); // true
```

### Concepts Being Tested  
- mapping + validation

### Deliverables  
- `allowed` map + `canMove(from,to)`

---

## 16) Reducer pattern — Interview Assignment

### Objective  
`(state, action) => newState` reducer বানাও workflow এর জন্য।

### Requirements  
- unknown action -> same state  
- no mutation

### Example (Expected Behavior)

```ts id="q11_16"
reduce(state, {type:"start"}); // new state
```

### Concepts Being Tested  
- reducer + unions

### Deliverables  
- reducer + action union

---

## 17) Side-effect event — Interview Assignment

### Objective  
Ticket resolved হলে side-effect event emit করো: `notification.send`.

### Requirements  
- return `{ state, events }` pattern (recommended)  
- payload includes ticketId + message

### Example (Expected Behavior)

```ts id="q11_17"
const { events } = reduce(...resolve...); // includes notification.send
```

### Concepts Being Tested  
- events + side-effect modeling

### Deliverables  
- event type + emission logic

---

## 18) Async workflow simulation — Interview Assignment

### Objective  
`await delay()` দিয়ে steps simulate করো; each step শেষে state update + log print।

### Requirements  
- failure at step 2 scenario handle

### Example (Expected Behavior)

```ts id="q11_18"
await runPipeline(); // prints progress logs
```

### Concepts Being Tested  
- async state machine

### Deliverables  
- `delay()` + `runPipeline()` demo

---

## 19) Result-based async pipeline — Interview Assignment

### Objective  
Async steps return `Result`; pipeline stops on first error.

### Requirements  
- `await step()` returns ok/err  
- err -> early exit

### Example (Expected Behavior)

```ts id="q11_19"
await run(); // ok(final) or err(firstFailure)
```

### Concepts Being Tested  
- functional error handling in async

### Deliverables  
- `Result` + 2–3 steps pipeline

---

## 20) In-code metrics counters — Interview Assignment

### Objective  
Transition counters maintain করো:  
`metrics["open->in_progress"]++`

### Requirements  
- typed transition key union OR runtime-safe string keys (choose)  
- unknown transitions handling define

### Example (Expected Behavior)

```ts id="q11_20"
metrics["open->in_progress"] += 1;
```

### Concepts Being Tested  
- observability basics

### Deliverables  
- metrics map + update points

---  
 ## 12) Tailwind UI Snippets (Non-HMS) — Interview Assignments (Top 20)

Rule: Use React/Angular/HTML as you like, but keep **state typed in TypeScript**. Tailwind only.

---

## 1) Product Card — Interview Assignment

### Objective

Product card UI বানাও।

### Requirements

* Props type: `{ readonly id: string; name: string; price: number; discount?: number; tags?: string[] }`
* discount missing হলে normal price দেখাবে
* discount=0 হলে discount apply হবে না (treat as no discount)

### Example (Expected Behavior)

```ts id="q12_01"
<ProductCard id="p1" name="Mouse" price={999} />
<ProductCard id="p2" name="Keyboard" price={1999} discount={10} tags={["hot"]} />
```

### Concepts Being Tested

* readonly + optional props
* rendering conditions

### Deliverables

* UI component + typed props + Tailwind classes

---

## 2) Login Form State — Interview Assignment

### Objective

Login form UI বানাও।

### Requirements

* State type: `{ email: string; password: string; remember: boolean }`
* submit disabled if invalid (email empty/invalid OR password < 6)
* trim email before validation

### Example (Expected Behavior)

```ts id="q12_02"
state.email = " a@b.com " -> validate as "a@b.com"
```

### Concepts Being Tested

* typed form state
* validation rules

### Deliverables

* form UI + typed state + disable logic

---

## 3) Loading Skeleton — Interview Assignment

### Objective

`isLoading: boolean` toggled skeleton UI।

### Requirements

* loading হলে skeleton, else content
* skeleton uses Tailwind animate-pulse blocks

### Example (Expected Behavior)

```ts id="q12_03"
{isLoading ? <Skeleton/> : <Content/>}
```

### Concepts Being Tested

* UI state toggle

### Deliverables

* skeleton component + demo container

---

## 4) Toast variants union — Interview Assignment

### Objective

Toast component বানাও with union variants.

### Requirements

* `type ToastType = "success" | "error" | "info"`
* `Record<ToastType, string>` দিয়ে class map typed
* show toast on button click

### Example (Expected Behavior)

```ts id="q12_04"
showToast("success", "Saved!");
```

### Concepts Being Tested

* unions + Record mapping

### Deliverables

* toast UI + typed variant map

---

## 5) Modal component typed callbacks — Interview Assignment

### Objective

Modal component with typed async confirm flow।

### Requirements

Props:

* `{ open: boolean; onClose: () => void; onConfirm: () => Promise<void> }`
* confirm click করলে loading state
* confirm fails -> show error text

### Example (Expected Behavior)

```ts id="q12_05"
<ConfirmModal open={open} onClose={...} onConfirm={async()=>{...}} />
```

### Concepts Being Tested

* async + props typing + UI state

### Deliverables

* modal UI + loading/error handling

---

## 6) Generic Table<T> — Interview Assignment

### Objective

Reusable generic table বানাও।

### Requirements

* Props: `rows: T[]`
* `columns: Array<{ key: keyof T; label: string }>`
* empty list হলে empty state message

### Example (Expected Behavior)

```ts id="q12_06"
<Table rows={users} columns={[{key:"name",label:"Name"}]} />
```

### Concepts Being Tested

* generics + keyof

### Deliverables

* generic table component

---

## 7) Pagination component — Interview Assignment

### Objective

Pagination UI with typed events।

### Requirements

* props: `{ page: number; pageSize: number; total: number; onPageChange: (p: number) => void }`
* next/prev disable at boundaries

### Example (Expected Behavior)

```ts id="q12_07"
onPageChange(page + 1);
```

### Concepts Being Tested

* typed callbacks + boundary logic

### Deliverables

* pagination UI

---

## 8) Debounced Search Bar — Interview Assignment

### Objective

Search input 300ms idle পরে async search triggers.

### Requirements

* state typed
* cancel stale responses (simple version ok: requestId compare)
* show loading + results list

### Example (Expected Behavior)

```ts id="q12_08"
type SearchState = { query: string; isLoading: boolean; results: string[]; error?: string }
```

### Concepts Being Tested

* async + stale response handling

### Deliverables

* search bar UI + debounce logic

---

## 9) Dropdown union options — Interview Assignment

### Objective

Sort dropdown with union type।

### Requirements

* `type Sort = "price_asc" | "price_desc" | "rating"`
* selecting sort reorders list
* invalid sort should not compile

### Example (Expected Behavior)

```ts id="q12_09"
setSort("price_asc"); // ✅
setSort("x");         // ❌
```

### Concepts Being Tested

* unions + typed state

### Deliverables

* dropdown UI + sorting

---

## 10) Badge variants map — Interview Assignment

### Objective

Badge component with variant union + class map.

### Requirements

* `type Badge = "new" | "sale" | "hot"`
* `Record<Badge, string>` for classes

### Example (Expected Behavior)

```ts id="q12_10"
<BadgeChip kind="sale" />
```

### Concepts Being Tested

* Record + unions

### Deliverables

* badge component

---

## 11) Confirm dialog returns `Promise<boolean>` — Interview Assignment

### Objective

`confirm(message)` returns `Promise<boolean>` using modal.

### Requirements

* resolve true/false on button click
* handle multiple confirms queued (basic approach ok: reject if already open OR queue array)

### Example (Expected Behavior)

```ts id="q12_11"
const ok = await confirm("Delete?");
```

### Concepts Being Tested

* Promise patterns + UI orchestration

### Deliverables

* confirm utility + modal UI

---

## 12) Stepper for workflow — Interview Assignment

### Objective

Stepper UI for status union; current step highlight.

### Requirements

* `type Step = "draft" | "review" | "published"`
* current step typed and must be valid
* render steps horizontally

### Example (Expected Behavior)

```ts id="q12_12"
<Stepper current="review" />
```

### Concepts Being Tested

* unions + mapping to UI

### Deliverables

* stepper component

---

## 13) Tabs typed key union — Interview Assignment

### Objective

Tabs UI with typed tab keys.

### Requirements

* `type TabKey = "overview" | "details" | "settings"`
* active tab state typed
* keyboard accessible (basic: buttons)

### Example (Expected Behavior)

```ts id="q12_13"
setActive("details");
```

### Concepts Being Tested

* union keys + state typing

### Deliverables

* tabs component

---

## 14) Empty State component — Interview Assignment

### Objective

Reusable empty state component with optional action.

### Requirements

Props: `{ title: string; description?: string; actionLabel?: string; onAction?: () => void }`

* if no action props, button not shown

### Example (Expected Behavior)

```ts id="q12_14"
<EmptyState title="No items" actionLabel="Add" onAction={...} />
```

### Concepts Being Tested

* optional props rendering

### Deliverables

* component + demo usage

---

## 15) Error UI state union — Interview Assignment

### Objective

Discriminated union based UI renderer।

### Requirements

* `UIState<T> = {kind:"loading"} | {kind:"ready";data:T} | {kind:"error";message:string}`
* render with exhaustive switch

### Example (Expected Behavior)

```ts id="q12_15"
renderState({kind:"error", message:"Oops"})
```

### Concepts Being Tested

* discriminated unions + exhaustive checks

### Deliverables

* UIState type + renderer component

---

## 16) File Upload UI state — Interview Assignment

### Objective

Upload UI with state machine + progress।

### Requirements

* states: `idle | uploading | success | error`
* show progress number (0..100)
* retry button on error

### Example (Expected Behavior)

```ts id="q12_16"
type UploadState =
  | { kind: "idle" }
  | { kind: "uploading"; progress: number }
  | { kind: "success"; url: string }
  | { kind: "error"; message: string };
```

### Concepts Being Tested

* unions + async UI state

### Deliverables

* UI + simulated upload

---

## 17) Progress bar for async job — Interview Assignment

### Objective

Simulated async job progress bar (0..100).

### Requirements

* progress update via interval/await loop
* cancel support (optional)

### Example (Expected Behavior)

```ts id="q12_17"
startJob(); // progress increases
```

### Concepts Being Tested

* async loops + state updates

### Deliverables

* progress bar component + job simulation

---

## 18) Sidebar menu typed routes — Interview Assignment

### Objective

Typed routes derived from `as const` and render sidebar.

### Requirements

* `const routes = [...] as const`
* derive `RouteKey` union from routes
* active route highlight

### Example (Expected Behavior)

```ts id="q12_18"
type RouteKey = typeof routes[number]["key"];
```

### Concepts Being Tested

* `as const` + derived types

### Deliverables

* sidebar UI + typed selection state

---

## 19) Form validation error map — Interview Assignment

### Objective

Typed field errors map show under inputs.

### Requirements

* `type Field = "email" | "password"`
* errors type: `Partial<Record<Field, string>>`
* show only existing errors

### Example (Expected Behavior)

```ts id="q12_19"
errors.email = "Invalid email";
```

### Concepts Being Tested

* Partial + Record + typed form validation

### Deliverables

* small form + error rendering

---

## 20) Theme switcher union — Interview Assignment

### Objective

Theme toggle UI and persist to localStorage with parser.

### Requirements

* `type Theme = "light" | "dark"`
* load theme from localStorage safely (corrupted -> default)
* toggle updates UI classes

### Example (Expected Behavior)

```ts id="q12_20"
setTheme("dark"); // persisted
```

### Concepts Being Tested

* unions + parsing + persistence

### Deliverables

* theme toggle component + `parseTheme()` + localStorage integration

---

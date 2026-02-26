 
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

## 07) null undefined strict mode
Top 20.

1) **Nullable username display** — [Answer](typescript-interview-answers.md#q07-01)
- Problem: `string | null`; display fallback "Guest".
- Example I/O: null -> Guest
- Edge cases: "" empty string should display "" (not Guest)
- Concepts: nullish vs falsy

2) **Optional param vs `string|undefined`** — [Answer](typescript-interview-answers.md#q07-02)
- Problem: create two functions and show difference in call sites.
- Example I/O: compile demo
- Edge cases: exactOptionalPropertyTypes
- Concepts: optional semantics

3) **strictNullChecks demo** — [Answer](typescript-interview-answers.md#q07-03)
- Problem: show why `let s:string = null` fails; fix with union.
- Example I/O: compile
- Edge cases: none
- Concepts: TS strictness

4) **Non-null assertion risk** — [Answer](typescript-interview-answers.md#q07-04)
- Problem: `user!.name` risk; replace with guard.
- Example I/O: safe output
- Edge cases: undefined user
- Concepts: safer patterns

5) **Optional chaining safe access** — [Answer](typescript-interview-answers.md#q07-05)
- Problem: read nested `config.api?.timeoutMs ?? 3000`.
- Example I/O: missing -> 3000
- Edge cases: timeout=0 should keep 0
- Concepts: `?.`, `??`

6) **`??` vs `||`** — [Answer](typescript-interview-answers.md#q07-06)
- Problem: demonstrate with 0 and empty string.
- Example I/O: 0 ?? 10 -> 0, 0 || 10 -> 10
- Edge cases: false boolean
- Concepts: operators

7) **Default param** — [Answer](typescript-interview-answers.md#q07-07)
- Problem: `function f(x:number=10)` compare with `x?:number`.
- Example I/O: f() -> 10
- Edge cases: explicit undefined
- Concepts: defaults

8) **parse optional to number** — [Answer](typescript-interview-answers.md#q07-08)
- Problem: `parsePage(x:string|undefined): number` default 1.
- Example I/O: undefined -> 1
- Edge cases: invalid -> 1 or error (choose)
- Concepts: parsing

9) **Filter undefined with predicate** — [Answer](typescript-interview-answers.md#q07-09)
- Problem: `(string|undefined)[]` to `string[]`.
- Example I/O: ["a",undefined] -> ["a"]
- Edge cases: "" stays
- Concepts: type predicate

10) **Maybe<T> helper** — [Answer](typescript-interview-answers.md#q07-10)
- Problem: `type Maybe<T>=T|null|undefined`; `unwrapMaybe(x, fallback)`
- Example I/O: null -> fallback
- Edge cases: 0 not fallback
- Concepts: nullish

11) **Form value normalize** — [Answer](typescript-interview-answers.md#q07-11)
- Problem: input `string|null` -> `string` (empty if null).
- Example I/O: null -> ""
- Edge cases: trim optional
- Concepts: normalization

12) **Date format safe** — [Answer](typescript-interview-answers.md#q07-12)
- Problem: `Date|null` -> string; null -> "N/A".
- Example I/O: null -> N/A
- Edge cases: invalid date
- Concepts: null handling

13) **Config required env vars** — [Answer](typescript-interview-answers.md#q07-13)
- Problem: `getEnv(name): string` throws if missing.
- Example I/O: missing -> error
- Edge cases: empty string treat as missing or allowed (define)
- Concepts: runtime strictness

14) **Strict function types (variance)** — [Answer](typescript-interview-answers.md#q07-14)
- Problem: show why `(x: string) => void` not assignable to `(x: string|number)=>void` in strict mode.
- Example I/O: explanation + code
- Edge cases: none
- Concepts: function variance

15) **exactOptionalPropertyTypes behavior** — [Answer](typescript-interview-answers.md#q07-15)
- Problem: show assignment differences.
- Example I/O: `{a?:string}` cannot set `a: undefined` under exact optional settings.
- Edge cases: config-dependent
- Concepts: TS config

16) **assertDefined utility** — [Answer](typescript-interview-answers.md#q07-16)
- Problem: assertion fn to narrow null/undefined.
- Example I/O: after assert, type is T
- Edge cases: falsy values
- Concepts: asserts

17) **NonNullable usage** — [Answer](typescript-interview-answers.md#q07-17)
- Problem: `type NN = NonNullable<string|null|undefined>`.
- Example I/O: NN is string
- Edge cases: unions
- Concepts: utility types

18) **Optional keys mapping** — [Answer](typescript-interview-answers.md#q07-18)
- Problem: get optional keys of type `T` (advanced).
- Example I/O: optional key union
- Edge cases: tricky types
- Concepts: mapped + conditional

19) **Strict config schema runtime** — [Answer](typescript-interview-answers.md#q07-19)
- Problem: load config object where no field remains undefined; validate and return typed config.
- Example I/O: missing -> error
- Edge cases: nested required fields
- Concepts: validation

20) **Builder ensures required fields before build** — [Answer](typescript-interview-answers.md#q07-20)
- Problem: step builder: setApiBaseUrl then build; build not callable before set.
- Example I/O: compile-time block
- Edge cases: keep minimal
- Concepts: typestate pattern

---

## 08) classes access modifiers getter setter
Top 20.

1) **BankAccount encapsulation** — [Answer](typescript-interview-answers.md#q08-01)
- Problem: private `balance`, methods `deposit`, `withdraw` (no negative).
- Example I/O: withdraw too much -> error/result
- Edge cases: deposit 0/negative
- Concepts: private, validation

2) **readonly accountId** — [Answer](typescript-interview-answers.md#q08-02)
- Problem: readonly id set in constructor; cannot modify later.
- Example I/O: assignment error
- Edge cases: none
- Concepts: readonly, constructor param properties

3) **Getter masked id** — [Answer](typescript-interview-answers.md#q08-03)
- Problem: getter returns masked id `****1234`.
- Example I/O: "ABC1234" -> "****1234"
- Edge cases: length <4
- Concepts: getter

4) **Setter pin validation** — [Answer](typescript-interview-answers.md#q08-04)
- Problem: setter for pin requires 4 digits.
- Example I/O: "123" -> error
- Edge cases: non-numeric
- Concepts: setter

5) **public/private access demo** — [Answer](typescript-interview-answers.md#q08-05)
- Problem: show compile error when accessing private property outside class.
- Example I/O: compile error
- Edge cases: none
- Concepts: access modifiers

6) **Simple inheritance** — [Answer](typescript-interview-answers.md#q08-06)
- Problem: `Vehicle` base with `move()`, `Car` extends adds `honk()`.
- Example I/O: call both
- Edge cases: override move
- Concepts: extends, override

7) **protected usage** — [Answer](typescript-interview-answers.md#q08-07)
- Problem: base has protected `log()` used only by child classes.
- Example I/O: outside call fails
- Edge cases: none
- Concepts: protected

8) **static counter** — [Answer](typescript-interview-answers.md#q08-08)
- Problem: count active sessions with static property.
- Example I/O: create 2 -> count 2
- Edge cases: destroy reduces count (implement)
- Concepts: static

9) **abstract gateway** — [Answer](typescript-interview-answers.md#q08-09)
- Problem: abstract `PaymentGateway` with `pay(amount)`; implement `MockGateway`.
- Example I/O: pay returns receipt
- Edge cases: negative amount
- Concepts: abstract

10) **implements Logger interface** — [Answer](typescript-interview-answers.md#q08-10)
- Problem: `Logger` interface and `ConsoleLogger` class implements it.
- Example I/O: logger.log("hi")
- Edge cases: levels union
- Concepts: implements

11) **Prevent invalid state (setter)** — [Answer](typescript-interview-answers.md#q08-11)
- Problem: `Product.price` cannot be negative; enforce in setter.
- Example I/O: set -1 -> error
- Edge cases: NaN
- Concepts: invariants

12) **Composition over inheritance** — [Answer](typescript-interview-answers.md#q08-12)
- Problem: `OrderService` uses `PriceCalculator` injected in constructor.
- Example I/O: calculate order total
- Edge cases: none
- Concepts: DI, testability

13) **Simple role check method** — [Answer](typescript-interview-answers.md#q08-13)
- Problem: `AdminPanel` method `deleteUser` only allowed if role is ADMIN.
- Example I/O: USER -> error
- Edge cases: none
- Concepts: guard

14) **Factory method ensures invariants** — [Answer](typescript-interview-answers.md#q08-14)
- Problem: private constructor; static `create()` validates input.
- Example I/O: invalid -> error
- Edge cases: empty strings
- Concepts: factory pattern

15) **Immutable class** — [Answer](typescript-interview-answers.md#q08-15)
- Problem: `Money` class; `add()` returns new instance, does not mutate.
- Example I/O: m1 unchanged
- Edge cases: rounding
- Concepts: immutability

16) **Builder with private constructor** — [Answer](typescript-interview-answers.md#q08-16)
- Problem: `OrderBuilder` collects items; `build()` returns Order.
- Example I/O: items empty -> error
- Edge cases: qty <=0
- Concepts: builder

17) **Custom error classes** — [Answer](typescript-interview-answers.md#q08-17)
- Problem: `InsufficientBalanceError`, map to message.
- Example I/O: catch and print
- Edge cases: unknown
- Concepts: error modeling

18) **Strategy pattern shipping** — [Answer](typescript-interview-answers.md#q08-18)
- Problem: `ShippingStrategy` interface; `Standard`, `Express` implement.
- Example I/O: cost differs
- Edge cases: weight negative
- Concepts: polymorphism

19) **Lazy computed getter cache** — [Answer](typescript-interview-answers.md#q08-19)
- Problem: expensive compute; compute once then cache.
- Example I/O: repeated calls no recompute
- Edge cases: invalidation
- Concepts: caching

20) **Testability: mock dependency** — [Answer](typescript-interview-answers.md#q08-20)
- Problem: service depends on `Clock` interface; in tests pass FakeClock.
- Example I/O: deterministic timestamps
- Edge cases: none
- Concepts: DI best practices

---

## 09) async await promise
Top 20.

1) **delay(ms)** — [Answer](typescript-interview-answers.md#q09-01)
- Problem: implement `delay(ms): Promise<void>`.
- Example I/O: await delay(100)
- Edge cases: ms < 0 -> treat 0
- Concepts: promises

2) **try/catch async error** — [Answer](typescript-interview-answers.md#q09-02)
- Problem: async function throws; handle and return fallback.
- Example I/O: fail -> "fallback"
- Edge cases: non-Error thrown
- Concepts: error handling

3) **Sequential calls** — [Answer](typescript-interview-answers.md#q09-03)
- Problem: fetch user then fetch orders(userId) sequentially.
- Example I/O: returns combined result
- Edge cases: first fails -> stop
- Concepts: await chain

4) **Parallel calls with Promise.all** — [Answer](typescript-interview-answers.md#q09-04)
- Problem: fetch A,B,C parallel and sum.
- Example I/O: 1+2+3=6
- Edge cases: if any fails -> reject
- Concepts: Promise.all

5) **Promise.race timeout** — [Answer](typescript-interview-answers.md#q09-05)
- Problem: wrap promise with timeout error after ms.
- Example I/O: long -> timeout
- Edge cases: cleanup mention
- Concepts: race

6) **Retry 3 times** — [Answer](typescript-interview-answers.md#q09-06)
- Problem: retry on failure with exponential backoff.
- Example I/O: succeeds on 2nd try
- Edge cases: always fail -> return error
- Concepts: loops + await

7) **finally loading** — [Answer](typescript-interview-answers.md#q09-07)
- Problem: `loading=true` before await, set false in finally.
- Example I/O: always false after
- Edge cases: none
- Concepts: finally

8) **Promise.allSettled report** — [Answer](typescript-interview-answers.md#q09-08)
- Problem: run tasks and return `{okCount, failCount}`.
- Example I/O: 2 ok 1 fail
- Edge cases: empty list
- Concepts: allSettled

9) **Concurrency limit (max 3)** — [Answer](typescript-interview-answers.md#q09-09)
- Problem: process 20 async jobs with limit 3 in parallel.
- Example I/O: completes all
- Edge cases: job failure handling strategy
- Concepts: queue, semaphore-like

10) **Cancel pattern simulation** — [Answer](typescript-interview-answers.md#q09-10)
- Problem: create cancel token; if cancelled, stop processing loop.
- Example I/O: cancel mid-way
- Edge cases: in-flight tasks
- Concepts: cancellation patterns

11) **FIFO async job queue** — [Answer](typescript-interview-answers.md#q09-11)
- Problem: queue with `enqueue(job)` and worker `start()` processes one by one.
- Example I/O: order preserved
- Edge cases: enqueue while running
- Concepts: producer-consumer

12) **Debounced async search** — [Answer](typescript-interview-answers.md#q09-12)
- Problem: debounce input changes and call async search only after 300ms idle.
- Example I/O: rapid typing -> one call
- Edge cases: last call only
- Concepts: debounce

13) **Async memoization cache** — [Answer](typescript-interview-answers.md#q09-13)
- Problem: cache results by key; concurrent same key should share in-flight promise.
- Example I/O: 2 calls -> 1 network
- Edge cases: failure should clear cache
- Concepts: inflight map

14) **Circuit breaker mini** — [Answer](typescript-interview-answers.md#q09-14)
- Problem: after 3 consecutive failures, open circuit for 5 seconds.
- Example I/O: calls blocked during open
- Edge cases: reset after time
- Concepts: resilience

15) **Return Result instead of throwing** — [Answer](typescript-interview-answers.md#q09-15)
- Problem: async function returns `Result<T,E>` not throw.
- Example I/O: ok/err
- Edge cases: mapping errors
- Concepts: typed error handling

16) **Async pipeline** — [Answer](typescript-interview-answers.md#q09-16)
- Problem: `pipeAsync(value, [fn1,fn2,fn3])`.
- Example I/O: transforms sequentially
- Edge cases: fn throws -> stop
- Concepts: composition

17) **Idempotent request dedupe** — [Answer](typescript-interview-answers.md#q09-17)
- Problem: `requestOnce(key, fn)` ensures only one in-flight per key.
- Example I/O: duplicates await same promise
- Edge cases: failure release
- Concepts: dedupe

18) **Backpressure simulation** — [Answer](typescript-interview-answers.md#q09-18)
- Problem: producer pushes jobs; if queue size > N, producer waits.
- Example I/O: no memory blow
- Edge cases: cancel
- Concepts: backpressure

19) **DLQ for failed jobs** — [Answer](typescript-interview-answers.md#q09-19)
- Problem: failed jobs pushed to `deadLetterQueue` with reason and timestamp.
- Example I/O: dlq length increments
- Edge cases: retry before dlq
- Concepts: error workflows

20) **CorrelationId tracing** — [Answer](typescript-interview-answers.md#q09-20)
- Problem: pass `correlationId` through async calls and include in logs.
- Example I/O: logs contain id
- Edge cases: missing id -> generate
- Concepts: observability

---

## 10) inventory literal unions
Non-HMS: warehouse/store inventory.

1) **Basic dispense guard** — [Answer](typescript-interview-answers.md#q10-01)
- Problem: `dispense(item, qty)` cannot reduce below 0.
- Example I/O: qty 5, dispense 6 -> error
- Edge cases: qty <=0 invalid
- Concepts: guards

2) **Category union** — [Answer](typescript-interview-answers.md#q10-02)
- Problem: `Category = "food"|"electronics"|"fashion"`; filter items by category.
- Example I/O: returns only electronics
- Edge cases: unknown category not allowed
- Concepts: literal unions

3) **StockStatus computed** — [Answer](typescript-interview-answers.md#q10-03)
- Problem: compute `"in_stock"|"low"|"out"` based on qty thresholds.
- Example I/O: 0 -> out
- Edge cases: negative qty invalid
- Concepts: unions, business rules

4) **Add item unique sku** — [Answer](typescript-interview-answers.md#q10-04)
- Problem: add only if sku not exists.
- Example I/O: duplicate -> error
- Edge cases: whitespace sku
- Concepts: map/indexing

5) **Search by keyword** — [Answer](typescript-interview-answers.md#q10-05)
- Problem: search by name contains keyword (case-insensitive).
- Example I/O: "phone" -> matching items
- Edge cases: empty keyword -> all
- Concepts: string ops

6) **Sort by qty desc** — [Answer](typescript-interview-answers.md#q10-06)
- Problem: return new sorted array without mutating original.
- Example I/O: sorted list
- Edge cases: equal qty keep stable (optional)
- Concepts: immutability

7) **Restock validate positive** — [Answer](typescript-interview-answers.md#q10-07)
- Problem: `restock(sku, addQty)` addQty must be >0.
- Example I/O: restock 10
- Edge cases: sku missing
- Concepts: validation

8) **Batch dispense (transaction)** — [Answer](typescript-interview-answers.md#q10-08)
- Problem: dispense multiple items; if any insufficient, none change.
- Example I/O: fails -> inventory unchanged
- Edge cases: duplicates in request
- Concepts: atomic update, copying

9) **Result-based operations** — [Answer](typescript-interview-answers.md#q10-09)
- Problem: all inventory ops return `Result<Success, InventoryError>`.
- Example I/O: ok/err
- Edge cases: none
- Concepts: typed errors

10) **Group by category** — [Answer](typescript-interview-answers.md#q10-10)
- Problem: group items into `Record<Category, StockItem[]>`.
- Example I/O: map categories
- Edge cases: empty categories
- Concepts: Record

11) **Reorder tasks** — [Answer](typescript-interview-answers.md#q10-11)
- Problem: if qty below reorderThreshold, create reorder task entries.
- Example I/O: low items produce tasks
- Edge cases: threshold per category
- Concepts: derived data

12) **Readonly sku in model** — [Answer](typescript-interview-answers.md#q10-12)
- Problem: ensure sku never changes on updates.
- Example I/O: patch sku -> compile error
- Edge cases: none
- Concepts: readonly

13) **Audit log** — [Answer](typescript-interview-answers.md#q10-13)
- Problem: append audit entries for add/restock/dispense with timestamp.
- Example I/O: audit length grows
- Edge cases: log size limit (optional)
- Concepts: logging

14) **Queue to prevent race (simulation)** — [Answer](typescript-interview-answers.md#q10-14)
- Problem: implement operation queue so dispense requests process sequentially.
- Example I/O: no negative due to race
- Edge cases: cancellation
- Concepts: concurrency patterns

15) **Lifecycle state machine** — [Answer](typescript-interview-answers.md#q10-15)
- Problem: item lifecycle `"active"|"discontinued"|"archived"` with allowed transitions.
- Example I/O: discontinued -> archived ok
- Edge cases: active -> archived not allowed
- Concepts: state machines

16) **Exhaustive switch on lifecycle** — [Answer](typescript-interview-answers.md#q10-16)
- Problem: switch all lifecycle states; default assertNever.
- Example I/O: compile safe
- Edge cases: add new state triggers compile fail
- Concepts: never

17) **Generic inventory store** — [Answer](typescript-interview-answers.md#q10-17)
- Problem: generic store `T extends { sku:string }` with add/get/update.
- Example I/O: works for different item types
- Edge cases: none
- Concepts: generics + constraints

18) **CSV import with validation** — [Answer](typescript-interview-answers.md#q10-18)
- Problem: parse CSV lines into items; invalid lines collected as errors.
- Example I/O: returns {items, errors}
- Edge cases: missing columns
- Concepts: parsing + Result

19) **Discount rules by category** — [Answer](typescript-interview-answers.md#q10-19)
- Problem: apply discounts based on category union.
- Example I/O: electronics 10% off
- Edge cases: rounding
- Concepts: unions + mapping

20) **Typed event emitter for inventory** — [Answer](typescript-interview-answers.md#q10-20)
- Problem: emit events `item_dispensed`, `item_restocked` with typed payloads.
- Example I/O: handlers get correct types
- Edge cases: none
- Concepts: event maps

---

## 11) workflow exhaustive checks
Non-HMS: support tickets / content pipeline.

1) **Ticket status union** — [Answer](typescript-interview-answers.md#q11-01)
- Problem: `Status="open"|"in_progress"|"resolved"`; `canClose(status)`.
- Example I/O: open -> false
- Edge cases: none
- Concepts: unions

2) **Video pipeline statuses** — [Answer](typescript-interview-answers.md#q11-02)
- Problem: `"uploaded"|"transcoding"|"published"|"failed"` label function.
- Example I/O: failed -> "Failed"
- Edge cases: exhaustive check
- Concepts: switch

3) **Exhaustive handler** — [Answer](typescript-interview-answers.md#q11-03)
- Problem: handle all statuses; default assertNever.
- Example I/O: compile safe
- Edge cases: add new status triggers compile fail
- Concepts: never

4) **Filter open tickets** — [Answer](typescript-interview-answers.md#q11-04)
- Problem: filter list to status "open".
- Example I/O: returns subset
- Edge cases: none
- Concepts: filter + unions

5) **Transition with actions** — [Answer](typescript-interview-answers.md#q11-05)
- Problem: action union `"start"|"resolve"|"reopen"`; transition rules.
- Example I/O: open+start -> in_progress
- Edge cases: resolved+start invalid
- Concepts: state machine

6) **Resolved requires note** — [Answer](typescript-interview-answers.md#q11-06)
- Problem: resolved state must include `resolutionNote`.
- Example I/O: cannot create resolved without note (type-level or runtime)
- Edge cases: empty note invalid
- Concepts: discriminated union payloads

7) **Process Result union** — [Answer](typescript-interview-answers.md#q11-07)
- Problem: workflow step returns ok/err; bubble errors.
- Example I/O: err -> stop
- Edge cases: multiple errors list
- Concepts: Result<T,E>

8) **Typed actions drive transitions** — [Answer](typescript-interview-answers.md#q11-08)
- Problem: model actions with payload e.g. `{type:"assign";agentId}`.
- Example I/O: assign sets assignee
- Edge cases: assign when resolved invalid
- Concepts: action union

9) **Guard invalid transition** — [Answer](typescript-interview-answers.md#q11-09)
- Problem: invalid transition returns error object not throw.
- Example I/O: {ok:false,error:"InvalidTransition"}
- Edge cases: none
- Concepts: result errors

10) **Timeline event log** — [Answer](typescript-interview-answers.md#q11-10)
- Problem: maintain timeline events array every transition.
- Example I/O: includes timestamps
- Edge cases: monotonic time
- Concepts: event logging

11) **Retry failed step** — [Answer](typescript-interview-answers.md#q11-11)
- Problem: failed -> retry -> transcoding; max retries 2.
- Example I/O: after 2, stays failed
- Edge cases: reset counter on success
- Concepts: counters + state

12) **Must assign before in_progress** — [Answer](typescript-interview-answers.md#q11-12)
- Problem: cannot start ticket unless assigned.
- Example I/O: open unassigned + start -> error
- Edge cases: auto assign optional
- Concepts: invariants

13) **SLA overdue flag** — [Answer](typescript-interview-answers.md#q11-13)
- Problem: if open more than N hours, overdue true.
- Example I/O: compute boolean
- Edge cases: timezone ignore, use ms
- Concepts: time logic

14) **Batch process summary** — [Answer](typescript-interview-answers.md#q11-14)
- Problem: count tickets by status -> `Record<Status, number>`.
- Example I/O: {open:2,...}
- Edge cases: empty list -> zeros
- Concepts: Record

15) **Typed transition map (advanced)** — [Answer](typescript-interview-answers.md#q11-15)
- Problem: `const allowed: Record<Status, Status[]>` and check membership.
- Example I/O: validates
- Edge cases: none
- Concepts: mapping

16) **Reducer pattern** — [Answer](typescript-interview-answers.md#q11-16)
- Problem: `(state, action) => newState` for workflow.
- Example I/O: action -> state
- Edge cases: unknown action -> state
- Concepts: reducer + unions

17) **Side-effect event** — [Answer](typescript-interview-answers.md#q11-17)
- Problem: when resolved, emit `notification.send`.
- Example I/O: event emitted with payload
- Edge cases: none
- Concepts: events

18) **Async workflow simulation** — [Answer](typescript-interview-answers.md#q11-18)
- Problem: simulate steps with `await delay`; state updates after each.
- Example I/O: printed logs
- Edge cases: failure at step 2
- Concepts: async state machine

19) **Result-based async pipeline** — [Answer](typescript-interview-answers.md#q11-19)
- Problem: async steps return Result; pipeline stops on first error.
- Example I/O: ok -> final, err -> early exit
- Edge cases: none
- Concepts: functional error handling

20) **In-code metrics counters** — [Answer](typescript-interview-answers.md#q11-20)
- Problem: maintain counters for each transition type.
- Example I/O: transitions["open->in_progress"]++
- Edge cases: unknown transitions
- Concepts: observability basics

---

## 12) tailwind ui snippets (non-hms)
These are UI coding prompts. Use React/Angular/HTML as you like, but keep state typed in TS.
Each item includes a TS typing requirement + UI requirement.

1) **Product Card** — [Answer](typescript-interview-answers.md#q12-01)
- Problem: build product card UI. Props: `{readonly id; name; price; discount?:number; tags?:string[]}`.
- Example I/O: missing discount shows normal price
- Edge cases: discount=0
- Concepts: optional, readonly

2) **Login Form State** — [Answer](typescript-interview-answers.md#q12-02)
- Problem: create login form UI. State type: `{email; password; remember:boolean}`.
- Example I/O: submit disabled if invalid
- Edge cases: trim email
- Concepts: types for form state

3) **Loading Skeleton** — [Answer](typescript-interview-answers.md#q12-03)
- Problem: create skeleton UI toggled by `isLoading:boolean`.
- Example I/O: show skeleton then content
- Edge cases: none
- Concepts: union UI states optional

4) **Toast variants union** — [Answer](typescript-interview-answers.md#q12-04)
- Problem: `type ToastType="success"|"error"|"info"`; style map typed.
- Example I/O: show success toast
- Edge cases: none
- Concepts: Record + unions

5) **Modal component typed callbacks** — [Answer](typescript-interview-answers.md#q12-05)
- Problem: modal props: `{open:boolean; onClose:()=>void; onConfirm:()=>Promise<void>}`.
- Example I/O: confirm shows loading
- Edge cases: confirm fails -> show error
- Concepts: async + props typing

6) **Generic Table<T>** — [Answer](typescript-interview-answers.md#q12-06)
- Problem: table component takes `rows:T[]` and `columns: Array<{key: keyof T; label:string}>`.
- Example I/O: renders any row type
- Edge cases: empty list shows empty state
- Concepts: generics + keyof

7) **Pagination component** — [Answer](typescript-interview-answers.md#q12-07)
- Problem: `page:number`, `pageSize:number`, `total:number`, `onPageChange(p)` typed.
- Example I/O: next/prev
- Edge cases: last page
- Concepts: typed events

8) **Debounced Search Bar** — [Answer](typescript-interview-answers.md#q12-08)
- Problem: search input triggers async search after 300ms idle.
- Example I/O: rapid typing -> 1 request
- Edge cases: cancel stale responses
- Concepts: async + state

9) **Dropdown union options** — [Answer](typescript-interview-answers.md#q12-09)
- Problem: `type Sort="price_asc"|"price_desc"|"rating"`; dropdown selects sort.
- Example I/O: list sorted
- Edge cases: none
- Concepts: unions

10) **Badge variants map** — [Answer](typescript-interview-answers.md#q12-10)
- Problem: `type Badge="new"|"sale"|"hot"`; map to class names via Record.
- Example I/O: badge displays
- Edge cases: none
- Concepts: Record

11) **Confirm dialog returns Promise<boolean>** — [Answer](typescript-interview-answers.md#q12-11)
- Problem: `confirm("Delete?")` returns Promise<boolean> using modal.
- Example I/O: resolve true/false
- Edge cases: multiple confirms queued
- Concepts: Promise patterns

12) **Stepper for workflow** — [Answer](typescript-interview-answers.md#q12-12)
- Problem: stepper UI for statuses union; highlight current.
- Example I/O: current step shown
- Edge cases: unknown status not allowed
- Concepts: unions + mapping

13) **Tabs typed key union** — [Answer](typescript-interview-answers.md#q12-13)
- Problem: `type TabKey="overview"|"details"|"settings"`; tab state typed.
- Example I/O: switch tab
- Edge cases: none
- Concepts: unions

14) **Empty State component** — [Answer](typescript-interview-answers.md#q12-14)
- Problem: show empty state with typed props `{title; description?; actionLabel?; onAction?}`.
- Example I/O: optional action
- Edge cases: no action
- Concepts: optional props

15) **Error UI state union** — [Answer](typescript-interview-answers.md#q12-15)
- Problem: `UIState = {kind:"loading"}|{kind:"ready";data:T}|{kind:"error";message:string}`.
- Example I/O: render each
- Edge cases: exhaustive check
- Concepts: discriminated union

16) **File Upload UI state** — [Answer](typescript-interview-answers.md#q12-16)
- Problem: states: idle/uploading/success/error; show progress number.
- Example I/O: uploading 40%
- Edge cases: retry
- Concepts: unions + async

17) **Progress bar for async job** — [Answer](typescript-interview-answers.md#q12-17)
- Problem: simulate job that updates progress 0..100.
- Example I/O: bar moves
- Edge cases: cancel
- Concepts: async loops

18) **Sidebar menu typed routes** — [Answer](typescript-interview-answers.md#q12-18)
- Problem: `const routes = [{key:"home", path:"/"}...] as const` derive type and render.
- Example I/O: select active
- Edge cases: none
- Concepts: as const, derived types

19) **Form validation error map** — [Answer](typescript-interview-answers.md#q12-19)
- Problem: `type Field="email"|"password"`; error map `Partial<Record<Field,string>>`.
- Example I/O: show field errors
- Edge cases: none
- Concepts: Partial + Record

20) **Theme switcher union** — [Answer](typescript-interview-answers.md#q12-20)
- Problem: `type Theme="light"|"dark"`; toggle UI and persist to localStorage with parser.
- Example I/O: reload retains theme
- Edge cases: corrupted storage
- Concepts: unions + parsing

---
End of file.
```
 
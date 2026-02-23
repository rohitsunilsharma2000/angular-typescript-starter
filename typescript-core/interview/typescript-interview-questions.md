 
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

1) **User Shape (interface + type)** — [Answer](typescript-interview-answers.md#q01-01)
- Problem: `User` model বানাও: `readonly id: string`, `name: string`, `email?: string`.
  Create it once with `interface`, once with `type`.
- Example I/O:
  - Input: `{ id:"u1", name:"A" }`
  - Output: valid compile
- Edge cases: email missing, id reassignment should fail
- Concepts: interface vs type, readonly, optional

2) **Admin Extends User** — [Answer](typescript-interview-answers.md#q01-02)
- Problem: `AdminUser` বানাও যা `User` extend করে `permissions: string[]` যোগ করবে।
- Example I/O: `permissions: ["read","write"]`
- Edge cases: permissions empty allowed
- Concepts: `extends`, structural typing

3) **Declaration Merging Demo** — [Answer](typescript-interview-answers.md#q01-03)
- Problem: `interface AppConfig` দুইবার declare করো যাতে final shape merge হয়।
- Example I/O:
  - First: `{ apiBaseUrl: string }`
  - Second: `{ timeoutMs: number }`
- Edge cases: name conflict types mismatch should error
- Concepts: interface merging

4) **Type Alias Union Model** — [Answer](typescript-interview-answers.md#q01-04)
- Problem: `type Account = PersonalAccount | BusinessAccount` with `kind` discriminant.
- Example I/O:
  - `{ kind:"personal", pan:"..." }`
  - `{ kind:"business", gst:"..." }`
- Edge cases: invalid kind should error
- Concepts: type alias, discriminated unions

5) **Excess Property Check** — [Answer](typescript-interview-answers.md#q01-05)
- Problem: `createUser(u: User)` লিখে object literal pass করলে extra key error দেখাও।
- Example I/O: `createUser({ id:"u1", name:"A", role:"x" })` should error
- Edge cases: if variable assigned first, may pass (explain)
- Concepts: excess property checks

6) **Function Type via Interface** — [Answer](typescript-interview-answers.md#q01-06)
- Problem: `interface Validator { (value: string): boolean }` বানাও এবং `isEmail` implement করো।
- Example I/O: `"a@b.com" -> true`
- Edge cases: empty string
- Concepts: callable interface

7) **Hybrid Function + Property** — [Answer](typescript-interview-answers.md#q01-07)
- Problem: `logger` এমন বানাও যেটা function হিসেবে call হবে এবং `.level` property থাকবে।
- Example I/O:
  - `logger("msg")`
  - `logger.level = "debug"`
- Edge cases: restrict level union
- Concepts: intersection types, callable objects

8) **Pick User Preview** — [Answer](typescript-interview-answers.md#q01-08)
- Problem: `type UserPreview = Pick<User,"id"|"name">` এবং `toPreview()` বানাও।
- Example I/O: `{id,name,email} -> {id,name}`
- Edge cases: missing email ok
- Concepts: Pick

9) **Omit Sensitive Fields** — [Answer](typescript-interview-answers.md#q01-09)
- Problem: `PublicUser = Omit<User,"email"> & { displayName: string }`
- Example I/O: user -> public user
- Edge cases: ensure email removed
- Concepts: Omit, intersection

10) **Readonly Enforcement** — [Answer](typescript-interview-answers.md#q01-10)
- Problem: `readonly id` change attempt লিখে compile error দেখাও; fix by creating new object.
- Example I/O: `u.id = "x"` fails
- Edge cases: none
- Concepts: immutability mindset

11) **Interface vs Type for Extensibility** — [Answer](typescript-interview-answers.md#q01-11)
- Problem: `interface Shape { area(): number }` and `type Shape2 = { area(): number }` and explain pros/cons.
- Example I/O: `class Circle implements Shape`
- Edge cases: none
- Concepts: implements, contracts

12) **Index Signature Flags** — [Answer](typescript-interview-answers.md#q01-12)
- Problem: Feature flags model: `Record<string, boolean>` and `isEnabled(flags, "newUI")`.
- Example I/O: `{ newUI:true } -> true`
- Edge cases: missing key -> false
- Concepts: Record, index signature

13) **Generic ApiResponse** — [Answer](typescript-interview-answers.md#q01-13)
- Problem: `interface ApiResponse<T> { ok: boolean; data?: T; error?: string }`
  `getOrThrow<T>(r)` implement.
- Example I/O: ok -> data, else throw
- Edge cases: ok true but data missing -> throw
- Concepts: generics, runtime checks

14) **Namespace-like Types** — [Answer](typescript-interview-answers.md#q01-14)
- Problem: `type Models = { User: ..., Product: ... }` then use `Models["User"]`.
- Example I/O: compile usage
- Edge cases: none
- Concepts: indexed access types

15) **Result<T,E> Union** — [Answer](typescript-interview-answers.md#q01-15)
- Problem: `type Result<T,E> = {ok:true;data:T} | {ok:false;error:E}` + `unwrap()`.
- Example I/O: `{ok:true,data:1} -> 1`
- Edge cases: error -> throw with message
- Concepts: discriminated union

16) **Exact Type Helper** — [Answer](typescript-interview-answers.md#q01-16)
- Problem: helper type `Exact<T, Shape>` to forbid extra keys in function arg.
- Example I/O: extra key should error
- Edge cases: explain limitation
- Concepts: advanced types

17) **DeepReadonly Utility** — [Answer](typescript-interview-answers.md#q01-17)
- Problem: `type DeepReadonly<T> = ...` nested object/array readonly.
- Example I/O: mutation should fail
- Edge cases: functions left unchanged
- Concepts: mapped types, conditional types

18) **Simplify Utility** — [Answer](typescript-interview-answers.md#q01-18)
- Problem: `type Simplify<T> = { [K in keyof T]: T[K] } & {}` for readable intersections.
- Example I/O: `Simplify<A & B>`
- Edge cases: none
- Concepts: mapped types

19) **Fluent Builder (typed)** — [Answer](typescript-interview-answers.md#q01-19)
- Problem: builder chain that collects fields then build returns full type.
- Example I/O:
  - `b.set("name","A").set("age",1).build()`
- Edge cases: build before required fields should fail (advanced)
- Concepts: generics + conditional typing

20) **Typed Routes Contract** — [Answer](typescript-interview-answers.md#q01-20)
- Problem: `Routes` map from path to `{req,res}` types; `callApi("/login", req)` typed.
- Example I/O: wrong req fields -> compile error
- Edge cases: none
- Concepts: indexed access, generics

---

## 02) union and intersection
Top 20.

1) **Payment Union** — [Answer](typescript-interview-answers.md#q02-01)
- Problem: `type Payment = "card"|"upi"|"cod"`; `isOnline(p)` true for card/upi.
- Example I/O: `"cod" -> false`
- Edge cases: none
- Concepts: unions

2) **LoginResult Union Objects** — [Answer](typescript-interview-answers.md#q02-02)
- Problem: `{ok:true;token}` | `{ok:false;message}`; `printLogin()`.
- Example I/O: prints token/message
- Edge cases: none
- Concepts: narrowing

3) **Intersection Profile** — [Answer](typescript-interview-answers.md#q02-03)
- Problem: `BasicUser & Address` -> `UserProfile`.
- Example I/O: combine two objects
- Edge cases: key overlap
- Concepts: intersections

4) **Narrowing with `in`** — [Answer](typescript-interview-answers.md#q02-04)
- Problem: union where only one has `token`; use `if ("token" in x)`.
- Example I/O: safe access
- Edge cases: property exists but undefined (explain)
- Concepts: `in` guard

5) **typeof Narrowing** — [Answer](typescript-interview-answers.md#q02-05)
- Problem: param `string | number` -> if number double else uppercase.
- Example I/O: `"ab"->"AB"`, `2->4`
- Edge cases: empty string
- Concepts: typeof guard

6) **Filter Union Array Safely** — [Answer](typescript-interview-answers.md#q02-06)
- Problem: `(number | string)[]` -> `number[]` using predicate.
- Example I/O: `[1,"a",2] -> [1,2]`
- Edge cases: NaN handling optional
- Concepts: type predicate

7) **Free vs Paid Video** — [Answer](typescript-interview-answers.md#q02-07)
- Problem: `{kind:"free"}` | `{kind:"paid";price:number}`; `getPriceOrZero()`.
- Example I/O: free -> 0
- Edge cases: negative price invalid (validate)
- Concepts: discriminant

8) **Event Union Handler** — [Answer](typescript-interview-answers.md#q02-08)
- Problem: `page_view/add_to_cart/purchase`; return log string.
- Example I/O: purchase -> "Purchased orderId=..."
- Edge cases: missing fields should not compile
- Concepts: discriminated union

9) **Error Union to Message** — [Answer](typescript-interview-answers.md#q02-09)
- Problem: `NetworkError | ValidationError | AuthError`; `toUserMessage(e)`.
- Example I/O: network -> "Try again later"
- Edge cases: unknown default
- Concepts: unions, switch

10) **Exhaustive Check with never** — [Answer](typescript-interview-answers.md#q02-10)
- Problem: in switch default: `assertNever(x)`.
- Example I/O: compile-time guard
- Edge cases: none
- Concepts: never

11) **Intersection of Capabilities** — [Answer](typescript-interview-answers.md#q02-11)
- Problem: `Trackable` + `Serializable`; create object that satisfies both.
- Example I/O: has `track()` and `toJSON()`
- Edge cases: method name conflicts
- Concepts: intersections

12) **Union Parameter vs Overload** — [Answer](typescript-interview-answers.md#q02-12)
- Problem: `formatDate(input: string | Date)`; parse string to Date safely.
- Example I/O: "2026-01-01" -> "01 Jan 2026"
- Edge cases: invalid string -> error/result
- Concepts: union params

13) **Type Predicate Guard** — [Answer](typescript-interview-answers.md#q02-13)
- Problem: `function isPaidVideo(v): v is {kind:"paid";price:number}`.
- Example I/O: used in filter
- Edge cases: v is unknown
- Concepts: type predicate

14) **Compose Search Filters (intersection)** — [Answer](typescript-interview-answers.md#q02-14)
- Problem: `Filter = ByPrice & ByCategory & ByRating`; apply all filters to products.
- Example I/O: list -> filtered list
- Edge cases: missing filter values
- Concepts: intersections + optional

15) **UnionToIntersection Utility** — [Answer](typescript-interview-answers.md#q02-15)
- Problem: Implement `UnionToIntersection<U>` type utility.
- Example I/O: `"a"| "b"` -> `"a"&"b"` concept test (type-level)
- Edge cases: distribute conditional
- Concepts: conditional types

16) **Simplify Intersection Output** — [Answer](typescript-interview-answers.md#q02-16)
- Problem: `Simplify<T>` to flatten display in IDE.
- Example I/O: `Simplify<A & B>`
- Edge cases: none
- Concepts: mapped types

17) **Order State Machine (union + transitions)** — [Answer](typescript-interview-answers.md#q02-17)
- Problem: `Status = "created"|"paid"|"packed"|"shipped"|"delivered"|"cancelled"`.
  `transition(status, action)` prevents invalid transitions.
- Example I/O: created + pay -> paid, delivered + cancel -> error
- Edge cases: cancel from some states only
- Concepts: unions, exhaustive checks

18) **ParseResult Union** — [Answer](typescript-interview-answers.md#q02-18)
- Problem: parse number from string returning `Result<number,string>`.
- Example I/O: "12.5" -> ok(12.5), "x" -> err("Invalid number")
- Edge cases: whitespace, NaN
- Concepts: result union

19) **Handler Map from Union** — [Answer](typescript-interview-answers.md#q02-19)
- Problem: `type Event = ...`; create `handlers` map typed so each handler gets correct payload type.
- Example I/O: wrong handler param -> compile error
- Edge cases: missing handler key
- Concepts: mapped types over union

20) **Next Step Depends on Previous (advanced)** — [Answer](typescript-interview-answers.md#q02-20)
- Problem: onboarding steps; only allow calling `verifyEmail()` after `signup()`.
- Example I/O: wrong order should not compile (type-level)
- Edge cases: simplify if too hard
- Concepts: generics, state typing

---

## 03) optional and readonly
Top 20.

1) **Optional Profile Display** — [Answer](typescript-interview-answers.md#q03-01)
- Problem: `Profile { name; bio?; avatarUrl? }`; `renderProfile(p)` uses fallback texts.
- Example I/O: missing bio -> "No bio"
- Edge cases: empty string vs undefined
- Concepts: optional, nullish

2) **Readonly SKU Update** — [Answer](typescript-interview-answers.md#q03-02)
- Problem: `Product { readonly sku; name; price }`; `updateProduct(p, patch)` returns new product without changing sku.
- Example I/O: attempt patch sku should fail
- Edge cases: patch includes sku
- Concepts: readonly, immutability

3) **Optional Discount Apply** — [Answer](typescript-interview-answers.md#q03-03)
- Problem: `applyDiscount(price, discount?)` uses `discount ?? 0`.
- Example I/O: (100, undefined)->100
- Edge cases: discount=0 should keep 100
- Concepts: `??` vs `||`

4) **Readonly Array Add** — [Answer](typescript-interview-answers.md#q03-04)
- Problem: given `readonly number[]`, return new array with extra value.
- Example I/O: [1,2]+3 -> [1,2,3]
- Edge cases: none
- Concepts: readonly arrays

5) **Optional Chaining Nested** — [Answer](typescript-interview-answers.md#q03-05)
- Problem: `user.address?.city ?? "Unknown"` utility function.
- Example I/O: no address -> "Unknown"
- Edge cases: city empty string
- Concepts: `?.`, `??`

6) **Partial Update Merge** — [Answer](typescript-interview-answers.md#q03-06)
- Problem: `merge<T>(base:T, patch:Partial<T>):T` without mutation.
- Example I/O: update name only
- Edge cases: nested objects shallow merge only (mention)
- Concepts: Partial, spread

7) **Optional Param Currency** — [Answer](typescript-interview-answers.md#q03-07)
- Problem: `formatMoney(amount, currency?)` default "INR".
- Example I/O: 100 -> "INR 100"
- Edge cases: invalid currency string
- Concepts: default params

8) **Readonly vs Object.freeze Demo** — [Answer](typescript-interview-answers.md#q03-08)
- Problem: show compile-time readonly vs runtime freeze difference with code + explanation.
- Example I/O: freeze prevents runtime mutation
- Edge cases: deep freeze not automatic
- Concepts: runtime vs compile-time

9) **Immutable Cart Qty Update** — [Answer](typescript-interview-answers.md#q03-09)
- Problem: cart items array; update qty by sku immutably.
- Example I/O: update -> new array
- Edge cases: sku not found -> return same array
- Concepts: immutability

10) **DeepReadonly vs Readonly** — [Answer](typescript-interview-answers.md#q03-10)
- Problem: demonstrate `Readonly<T>` does not deep lock nested; implement DeepReadonly.
- Example I/O: nested mutation should fail with DeepReadonly
- Edge cases: arrays
- Concepts: mapped + conditional

11) **exactOptionalPropertyTypes scenario** — [Answer](typescript-interview-answers.md#q03-11)
- Problem: explain with code `{a?:string}` vs `{a:string|undefined}` behavior.
- Example I/O: assignment differences
- Edge cases: toolchain dependent
- Concepts: TS config

12) **Patch Unknown Key Reject (type-level)** — [Answer](typescript-interview-answers.md#q03-12)
- Problem: `updateUser(u, patch)` should not allow keys outside User at compile time.
- Example I/O: patch `{role:"x"}` error
- Edge cases: generics inference
- Concepts: keyof constraints

13) **ReadonlyMap read** — [Answer](typescript-interview-answers.md#q03-13)
- Problem: store rates in `ReadonlyMap<string, number>` and read rate for key with fallback.
- Example I/O: missing -> 0
- Edge cases: none
- Concepts: readonly collections

14) **mergeDefaults<T>** — [Answer](typescript-interview-answers.md#q03-14)
- Problem: `mergeDefaults(defaults:T, overrides?:Partial<T>):T`.
- Example I/O: overrides missing -> defaults
- Edge cases: overrides undefined
- Concepts: generics + optional

15) **DeepPartial Utility** — [Answer](typescript-interview-answers.md#q03-15)
- Problem: implement `DeepPartial<T>` for nested patch.
- Example I/O: patch nested fields
- Edge cases: arrays
- Concepts: conditional types

16) **Immutable Tree Update** — [Answer](typescript-interview-answers.md#q03-16)
- Problem: update node label by id in nested tree without mutation.
- Example I/O: returns new tree
- Edge cases: id missing
- Concepts: recursion, immutability

17) **Readonly State Reducer** — [Answer](typescript-interview-answers.md#q03-17)
- Problem: todo reducer with readonly state; return new state for add/toggle/remove.
- Example I/O: action -> next state
- Edge cases: unknown action -> same state
- Concepts: union actions, immutability

18) **Branded ID** — [Answer](typescript-interview-answers.md#q03-18)
- Problem: `type UserId = string & { __brand:"UserId" }`; `makeUserId(s)` validation.
- Example I/O: "" -> throw
- Edge cases: trim
- Concepts: branded types

19) **Readonly constructor param** — [Answer](typescript-interview-answers.md#q03-19)
- Problem: class with `readonly id` set in constructor param property.
- Example I/O: cannot reassign
- Edge cases: none
- Concepts: parameter properties

20) **Persistent List Update** — [Answer](typescript-interview-answers.md#q03-20)
- Problem: given readonly list, remove item by id returning new list.
- Example I/O: list size decreases
- Edge cases: id not found -> same list
- Concepts: immutability

---

## 04) generics
Top 20.

1) **identity<T>** — [Answer](typescript-interview-answers.md#q04-01)
- Problem: `identity<T>(x:T):T`
- Example I/O: identity(5)->5
- Edge cases: none
- Concepts: generics basics

2) **first<T>** — [Answer](typescript-interview-answers.md#q04-02)
- Problem: return first element or undefined.
- Example I/O: [] -> undefined
- Edge cases: empty array
- Concepts: `T | undefined`

3) **wrap<T>** — [Answer](typescript-interview-answers.md#q04-03)
- Problem: return `{ value:T }`
- Example I/O: wrap("a") -> {value:"a"}
- Edge cases: none
- Concepts: generics

4) **pair<T,U>** — [Answer](typescript-interview-answers.md#q04-04)
- Problem: return tuple/object pair typed.
- Example I/O: pair(1,"a") -> [1,"a"]
- Edge cases: none
- Concepts: multiple type params

5) **mapArray<T,R>** — [Answer](typescript-interview-answers.md#q04-05)
- Problem: implement generic map.
- Example I/O: [1,2] -> ["1","2"]
- Edge cases: none
- Concepts: higher-order functions

6) **ApiResponse<T>** — [Answer](typescript-interview-answers.md#q04-06)
- Problem: generic response model and `mapResponse<T,R>()`
- Example I/O: ok(data) -> ok(mapped)
- Edge cases: error pass-through
- Concepts: generics + union

7) **Box<T> class** — [Answer](typescript-interview-answers.md#q04-07)
- Problem: class holds value with getter/setter typed.
- Example I/O: set number only
- Edge cases: validation in setter
- Concepts: generics + class

8) **Constraint `T extends {id:string}`** — [Answer](typescript-interview-answers.md#q04-08)
- Problem: `indexById(list)` returns `Record<string,T>`
- Example I/O: list -> map
- Edge cases: duplicate id last wins
- Concepts: constraints

9) **pluck<T,K extends keyof T>** — [Answer](typescript-interview-answers.md#q04-09)
- Problem: return `obj[key]`
- Example I/O: pluck(user,"name")->string
- Edge cases: none
- Concepts: keyof

10) **sortByKey<T, K extends keyof T>** — [Answer](typescript-interview-answers.md#q04-10)
- Problem: sort array by key (number/string only).
- Example I/O: products sort by price
- Edge cases: undefined values
- Concepts: constraints

11) **Generic default type** — [Answer](typescript-interview-answers.md#q04-11)
- Problem: `type Result<T=string,E=Error> = ...`
- Example I/O: Result without args defaults
- Edge cases: none
- Concepts: default generic params

12) **parseJson<T>(s, guard)** — [Answer](typescript-interview-answers.md#q04-12)
- Problem: parse JSON as unknown then guard to T.
- Example I/O: invalid -> err
- Edge cases: JSON parse error
- Concepts: unknown + guard

13) **freeze<T>(obj): Readonly<T>** — [Answer](typescript-interview-answers.md#q04-13)
- Problem: return typed readonly, and demonstrate compile-time protection.
- Example I/O: cannot assign
- Edge cases: shallow
- Concepts: Readonly

14) **timeout<T>(promise, ms)** — [Answer](typescript-interview-answers.md#q04-14)
- Problem: resolve or reject after ms.
- Example I/O: long task -> timeout error
- Edge cases: cleanup
- Concepts: Promise generics

15) **PromiseReturn<T>** — [Answer](typescript-interview-answers.md#q04-15)
- Problem: `type PromiseReturn<T> = T extends Promise<infer X> ? X : T`
- Example I/O: PromiseReturn<Promise<number>> = number
- Edge cases: nested promises optional
- Concepts: infer

16) **FuncReturn<T>** — [Answer](typescript-interview-answers.md#q04-16)
- Problem: return type extraction: `T extends (...a:any)=>infer R ? R : never`
- Example I/O: Fn -> return type
- Edge cases: non-function -> never
- Concepts: infer

17) **Typed Event Emitter** — [Answer](typescript-interview-answers.md#q04-17)
- Problem: event map generic with `on` and `emit` typed.
- Example I/O: emit("login",{userId:"u1"})
- Edge cases: wrong payload errors
- Concepts: mapped types + generics

18) **Fluent Builder Accumulating Keys** — [Answer](typescript-interview-answers.md#q04-18)
- Problem: builder sets keys and build returns object with those keys typed.
- Example I/O: set("name","A").build() -> {name:string}
- Edge cases: duplicate set overwrites
- Concepts: advanced generics

19) **Primitive Schema to Type** — [Answer](typescript-interview-answers.md#q04-19)
- Problem: schema `{ kind:"string" }|{kind:"number"}` maps to TS types.
- Example I/O: infer output type
- Edge cases: arrays
- Concepts: conditional types

20) **decode<T>(unknown, guard)** — [Answer](typescript-interview-answers.md#q04-20)
- Problem: safe decode pattern.
- Example I/O: decode<User>(u,isUser)
- Edge cases: guard false -> throw/result
- Concepts: runtime validation

---

## 05) enum vs string literal union
Top 20.

1) **Status union** — [Answer](typescript-interview-answers.md#q05-01)
- Problem: `type Status="draft"|"published"`; `canEdit(status)` true only for draft.
- Example I/O: "published" -> false
- Edge cases: none
- Concepts: string unions

2) **Same as enum** — [Answer](typescript-interview-answers.md#q05-02)
- Problem: Implement `enum StatusEnum { Draft="draft", Published="published" }` and compare usage.
- Example I/O: `StatusEnum.Draft`
- Edge cases: runtime output explain
- Concepts: enums

3) **Switch label** — [Answer](typescript-interview-answers.md#q05-03)
- Problem: map status to label via switch.
- Example I/O: draft -> "Draft"
- Edge cases: exhaustive check
- Concepts: switch + never

4) **Record label map** — [Answer](typescript-interview-answers.md#q05-04)
- Problem: `Record<Status,string>` label map.
- Example I/O: labelMap[status]
- Edge cases: missing key compile error
- Concepts: Record

5) **Prevent invalid string** — [Answer](typescript-interview-answers.md#q05-05)
- Problem: `"drafft"` assignment should fail.
- Example I/O: compile error
- Edge cases: external data parse required
- Concepts: literal unions

6) **const object + as const** — [Answer](typescript-interview-answers.md#q05-06)
- Problem: `const STATUS = {...} as const` derive type.
- Example I/O: `type Status = typeof STATUS[keyof typeof STATUS]`
- Edge cases: none
- Concepts: as const

7) **parseStatus(input: unknown)** — [Answer](typescript-interview-answers.md#q05-07)
- Problem: validate external value into Status else throw/result.
- Example I/O: "draft" -> ok
- Edge cases: uppercase, whitespace
- Concepts: validation

8) **Exhaustive transitions** — [Answer](typescript-interview-answers.md#q05-08)
- Problem: status transitions: draft->published only; enforce.
- Example I/O: published->draft error
- Edge cases: none
- Concepts: state machine

9) **Enum compile footprint explanation** — [Answer](typescript-interview-answers.md#q05-09)
- Problem: show numeric vs string enum output (brief).
- Example I/O: explain
- Edge cases: bundlers
- Concepts: runtime JS output

10) **Reverse mapping demo** — [Answer](typescript-interview-answers.md#q05-10)
- Problem: numeric enum reverse mapping example.
- Example I/O: Enum[0] -> "A"
- Edge cases: none
- Concepts: enums

11) **Tree-shaking discussion (coding hint)** — [Answer](typescript-interview-answers.md#q05-11)
- Problem: show why string unions often smaller than enums for libs.
- Example I/O: explain
- Edge cases: none
- Concepts: bundling

12) **Serialize/deserialize** — [Answer](typescript-interview-answers.md#q05-12)
- Problem: store Status in localStorage as string; restore with parser.
- Example I/O: read -> Status
- Edge cases: corrupted storage
- Concepts: parsing

13) **Role permission map** — [Answer](typescript-interview-answers.md#q05-13)
- Problem: `type Role="ADMIN"|"USER"|"GUEST"`; `Record<Role,string[]>`.
- Example I/O: get permissions
- Edge cases: none
- Concepts: Record

14) **Feature flag keys union** — [Answer](typescript-interview-answers.md#q05-14)
- Problem: flags keys union; typed `isEnabled(flag)` function.
- Example I/O: `isEnabled("newUI")`
- Edge cases: unknown flag should error
- Concepts: unions

15) **Enum-like object pattern** — [Answer](typescript-interview-answers.md#q05-15)
- Problem: create enum-like const object for PaymentMethod and derived type.
- Example I/O: PaymentMethod.Card -> "card"
- Edge cases: none
- Concepts: as const

16) **Strict parser with helpful error** — [Answer](typescript-interview-answers.md#q05-16)
- Problem: error message includes allowed values list.
- Example I/O: input "x" -> "Allowed: draft,published"
- Edge cases: none
- Concepts: runtime + types

17) **Typed transition map** — [Answer](typescript-interview-answers.md#q05-17)
- Problem: `const transitions: Record<Status, Status[]>` enforce allowed next states.
- Example I/O: transitions["draft"] includes published
- Edge cases: none
- Concepts: Record

18) **Template literal type events** — [Answer](typescript-interview-answers.md#q05-18)
- Problem: `type EventName = \`user:${"created"|"deleted"}\``
- Example I/O: "user:created" ok
- Edge cases: none
- Concepts: template literal types

19) **i18n labels mapping** — [Answer](typescript-interview-answers.md#q05-19)
- Problem: `Record<Status,{en:string;bn:string}>`
- Example I/O: labels[status].bn
- Edge cases: none
- Concepts: mapping

20) **Endpoint by status** — [Answer](typescript-interview-answers.md#q05-20)
- Problem: `getEndpoint(status:Status)` returns string; exhaustive switch.
- Example I/O: draft -> "/draft"
- Edge cases: none
- Concepts: switch exhaustive

---

## 06) any unknown never
Top 20.

1) **unknown to string** — [Answer](typescript-interview-answers.md#q06-01)
- Problem: `normalizeName(x:unknown):string` string হলে trim else throw.
- Example I/O: " A " -> "A"
- Edge cases: empty after trim -> throw
- Concepts: unknown narrowing

2) **Replace any with unknown** — [Answer](typescript-interview-answers.md#q06-02)
- Problem: given function param any; change to unknown and add safe checks.
- Example I/O: show before/after
- Edge cases: none
- Concepts: safety

3) **safeJsonParse returns unknown** — [Answer](typescript-interview-answers.md#q06-03)
- Problem: `safeJsonParse(s): unknown | null` parse fail -> null.
- Example I/O: invalid JSON -> null
- Edge cases: none
- Concepts: unknown

4) **isNumberArray guard** — [Answer](typescript-interview-answers.md#q06-04)
- Problem: `isNumberArray(u): u is number[]`.
- Example I/O: [1,2] true
- Edge cases: [1,"2"] false
- Concepts: type predicate

5) **fail(msg): never** — [Answer](typescript-interview-answers.md#q06-05)
- Problem: function always throws.
- Example I/O: throw Error
- Edge cases: none
- Concepts: never

6) **assertNever helper** — [Answer](typescript-interview-answers.md#q06-06)
- Problem: `assertNever(x:never):never` used in exhaustive switch.
- Example I/O: compile-time guarantee
- Edge cases: none
- Concepts: exhaustive

7) **catch error safely** — [Answer](typescript-interview-answers.md#q06-07)
- Problem: `catch(e)` is unknown; print message if e is Error else stringify.
- Example I/O: prints message
- Edge cases: thrown string
- Concepts: unknown in catch

8) **validate User shape at runtime** — [Answer](typescript-interview-answers.md#q06-08)
- Problem: `isUser(u:unknown): u is {id:string;name:string}`
- Example I/O: valid -> true
- Edge cases: missing keys
- Concepts: runtime validation

9) **decode API response unknown -> User** — [Answer](typescript-interview-answers.md#q06-09)
- Problem: `decodeUser(u:unknown):User` using guard.
- Example I/O: invalid -> throw
- Edge cases: nested types optional
- Concepts: decode pattern

10) **Error union mapping** — [Answer](typescript-interview-answers.md#q06-10)
- Problem: custom errors union; map to HTTP-like status codes.
- Example I/O: AuthError -> 401
- Edge cases: unknown -> 500
- Concepts: unions

11) **Remove unsafe casting** — [Answer](typescript-interview-answers.md#q06-11)
- Problem: code uses `as User`; replace with guards.
- Example I/O: compile safe
- Edge cases: none
- Concepts: avoid `as`

12) **Conditional returns never on invalid** — [Answer](typescript-interview-answers.md#q06-12)
- Problem: `type NonString<T> = T extends string ? never : T`
- Example I/O: NonString<string> -> never
- Edge cases: union distribution
- Concepts: conditional types

13) **Parse query params unknown** — [Answer](typescript-interview-answers.md#q06-13)
- Problem: `parseLimit(x:unknown):number` must be 1..100.
- Example I/O: "10" -> 10
- Edge cases: 0, 101 -> error
- Concepts: validation

14) **Narrow unknown event payload by topic** — [Answer](typescript-interview-answers.md#q06-14)
- Problem: topic string determines payload guard.
- Example I/O: "user.created" expects {id}
- Edge cases: unknown topic -> error
- Concepts: mapping

15) **Mini validator combinators** — [Answer](typescript-interview-answers.md#q06-15)
- Problem: implement `isString`, `isNumber`, `isObject`, `hasKey`.
- Example I/O: compose to validate shapes
- Edge cases: null is object in JS (handle)
- Concepts: runtime checks

16) **Result-based errors instead of throw** — [Answer](typescript-interview-answers.md#q06-16)
- Problem: return `Result<T,E>` from parser instead of throwing.
- Example I/O: ok/err
- Edge cases: none
- Concepts: result union

17) **assertDefined** — [Answer](typescript-interview-answers.md#q06-17)
- Problem: `assertDefined(x): asserts x is NonNullable<T>`
- Example I/O: after assert, x not null/undefined
- Edge cases: falsy values should pass if not null
- Concepts: assertion functions

18) **Safe deep get** — [Answer](typescript-interview-answers.md#q06-18)
- Problem: `get(u:unknown, path:string[]): unknown` handle missing path safely.
- Example I/O: missing -> undefined
- Edge cases: arrays index strings
- Concepts: unknown-safe utilities

19) **Unknown payload registry typed** — [Answer](typescript-interview-answers.md#q06-19)
- Problem: registry map `topic -> parser`; output type inferred.
- Example I/O: parse("user.created", payload) returns UserCreatedEvent typed
- Edge cases: topic not found
- Concepts: generics + mapping

20) **Never unreachable branch demonstration** — [Answer](typescript-interview-answers.md#q06-20)
- Problem: in union switch, ensure default unreachable; show compile error if new union member added and not handled.
- Example I/O: add new member -> compile fail
- Edge cases: none
- Concepts: never exhaustive

---

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
 
 
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

1) User Shape (interface + type)
- Problem: `User` model বানাও: `readonly id: string`, `name: string`, `email?: string`.
  Create it once with `interface`, once with `type`.
- Example I/O:
  - Input: `{ id:"u1", name:"A" }`
  - Output: valid compile
- Edge cases: email missing, id reassignment should fail
- Concepts: interface vs type, readonly, optional

2) Admin Extends User
- Problem: `AdminUser` বানাও যা `User` extend করে `permissions: string[]` যোগ করবে।
- Example I/O: `permissions: ["read","write"]`
- Edge cases: permissions empty allowed
- Concepts: `extends`, structural typing

3) Declaration Merging Demo
- Problem: `interface AppConfig` দুইবার declare করো যাতে final shape merge হয়।
- Example I/O:
  - First: `{ apiBaseUrl: string }`
  - Second: `{ timeoutMs: number }`
- Edge cases: name conflict types mismatch should error
- Concepts: interface merging

4) Type Alias Union Model
- Problem: `type Account = PersonalAccount | BusinessAccount` with `kind` discriminant.
- Example I/O:
  - `{ kind:"personal", pan:"..." }`
  - `{ kind:"business", gst:"..." }`
- Edge cases: invalid kind should error
- Concepts: type alias, discriminated unions

5) Excess Property Check
- Problem: `createUser(u: User)` লিখে object literal pass করলে extra key error দেখাও।
- Example I/O: `createUser({ id:"u1", name:"A", role:"x" })` should error
- Edge cases: if variable assigned first, may pass (explain)
- Concepts: excess property checks

6) Function Type via Interface
- Problem: `interface Validator { (value: string): boolean }` বানাও এবং `isEmail` implement করো।
- Example I/O: `"a@b.com" -> true`
- Edge cases: empty string
- Concepts: callable interface

7) Hybrid Function + Property
- Problem: `logger` এমন বানাও যেটা function হিসেবে call হবে এবং `.level` property থাকবে।
- Example I/O:
  - `logger("msg")`
  - `logger.level = "debug"`
- Edge cases: restrict level union
- Concepts: intersection types, callable objects

8) Pick User Preview
- Problem: `type UserPreview = Pick<User,"id"|"name">` এবং `toPreview()` বানাও।
- Example I/O: `{id,name,email} -> {id,name}`
- Edge cases: missing email ok
- Concepts: Pick

9) Omit Sensitive Fields
- Problem: `PublicUser = Omit<User,"email"> & { displayName: string }`
- Example I/O: user -> public user
- Edge cases: ensure email removed
- Concepts: Omit, intersection

10) Readonly Enforcement
- Problem: `readonly id` change attempt লিখে compile error দেখাও; fix by creating new object.
- Example I/O: `u.id = "x"` fails
- Edge cases: none
- Concepts: immutability mindset

11) Interface vs Type for Extensibility
- Problem: `interface Shape { area(): number }` and `type Shape2 = { area(): number }` and explain pros/cons.
- Example I/O: `class Circle implements Shape`
- Edge cases: none
- Concepts: implements, contracts

12) Index Signature Flags
- Problem: Feature flags model: `Record<string, boolean>` and `isEnabled(flags, "newUI")`.
- Example I/O: `{ newUI:true } -> true`
- Edge cases: missing key -> false
- Concepts: Record, index signature

13) Generic ApiResponse
- Problem: `interface ApiResponse<T> { ok: boolean; data?: T; error?: string }`
  `getOrThrow<T>(r)` implement.
- Example I/O: ok -> data, else throw
- Edge cases: ok true but data missing -> throw
- Concepts: generics, runtime checks

14) Namespace-like Types
- Problem: `type Models = { User: ..., Product: ... }` then use `Models["User"]`.
- Example I/O: compile usage
- Edge cases: none
- Concepts: indexed access types

15) Result<T,E> Union
- Problem: `type Result<T,E> = {ok:true;data:T} | {ok:false;error:E}` + `unwrap()`.
- Example I/O: `{ok:true,data:1} -> 1`
- Edge cases: error -> throw with message
- Concepts: discriminated union

16) Exact Type Helper
- Problem: helper type `Exact<T, Shape>` to forbid extra keys in function arg.
- Example I/O: extra key should error
- Edge cases: explain limitation
- Concepts: advanced types

17) DeepReadonly Utility
- Problem: `type DeepReadonly<T> = ...` nested object/array readonly.
- Example I/O: mutation should fail
- Edge cases: functions left unchanged
- Concepts: mapped types, conditional types

18) Simplify Utility
- Problem: `type Simplify<T> = { [K in keyof T]: T[K] } & {}` for readable intersections.
- Example I/O: `Simplify<A & B>`
- Edge cases: none
- Concepts: mapped types

19) Fluent Builder (typed)
- Problem: builder chain that collects fields then build returns full type.
- Example I/O:
  - `b.set("name","A").set("age",1).build()`
- Edge cases: build before required fields should fail (advanced)
- Concepts: generics + conditional typing

20) Typed Routes Contract
- Problem: `Routes` map from path to `{req,res}` types; `callApi("/login", req)` typed.
- Example I/O: wrong req fields -> compile error
- Edge cases: none
- Concepts: indexed access, generics

---

## 02) union and intersection
Top 20.

1) Payment Union
- Problem: `type Payment = "card"|"upi"|"cod"`; `isOnline(p)` true for card/upi.
- Example I/O: `"cod" -> false`
- Edge cases: none
- Concepts: unions

2) LoginResult Union Objects
- Problem: `{ok:true;token}` | `{ok:false;message}`; `printLogin()`.
- Example I/O: prints token/message
- Edge cases: none
- Concepts: narrowing

3) Intersection Profile
- Problem: `BasicUser & Address` -> `UserProfile`.
- Example I/O: combine two objects
- Edge cases: key overlap
- Concepts: intersections

4) Narrowing with `in`
- Problem: union where only one has `token`; use `if ("token" in x)`.
- Example I/O: safe access
- Edge cases: property exists but undefined (explain)
- Concepts: `in` guard

5) typeof Narrowing
- Problem: param `string | number` -> if number double else uppercase.
- Example I/O: `"ab"->"AB"`, `2->4`
- Edge cases: empty string
- Concepts: typeof guard

6) Filter Union Array Safely
- Problem: `(number | string)[]` -> `number[]` using predicate.
- Example I/O: `[1,"a",2] -> [1,2]`
- Edge cases: NaN handling optional
- Concepts: type predicate

7) Free vs Paid Video
- Problem: `{kind:"free"}` | `{kind:"paid";price:number}`; `getPriceOrZero()`.
- Example I/O: free -> 0
- Edge cases: negative price invalid (validate)
- Concepts: discriminant

8) Event Union Handler
- Problem: `page_view/add_to_cart/purchase`; return log string.
- Example I/O: purchase -> "Purchased orderId=..."
- Edge cases: missing fields should not compile
- Concepts: discriminated union

9) Error Union to Message
- Problem: `NetworkError | ValidationError | AuthError`; `toUserMessage(e)`.
- Example I/O: network -> "Try again later"
- Edge cases: unknown default
- Concepts: unions, switch

10) Exhaustive Check with never
- Problem: in switch default: `assertNever(x)`.
- Example I/O: compile-time guard
- Edge cases: none
- Concepts: never

11) Intersection of Capabilities
- Problem: `Trackable` + `Serializable`; create object that satisfies both.
- Example I/O: has `track()` and `toJSON()`
- Edge cases: method name conflicts
- Concepts: intersections

12) Union Parameter vs Overload
- Problem: `formatDate(input: string | Date)`; parse string to Date safely.
- Example I/O: "2026-01-01" -> "01 Jan 2026"
- Edge cases: invalid string -> error/result
- Concepts: union params

13) Type Predicate Guard
- Problem: `function isPaidVideo(v): v is {kind:"paid";price:number}`.
- Example I/O: used in filter
- Edge cases: v is unknown
- Concepts: type predicate

14) Compose Search Filters (intersection)
- Problem: `Filter = ByPrice & ByCategory & ByRating`; apply all filters to products.
- Example I/O: list -> filtered list
- Edge cases: missing filter values
- Concepts: intersections + optional

15) UnionToIntersection Utility
- Problem: Implement `UnionToIntersection<U>` type utility.
- Example I/O: `"a"| "b"` -> `"a"&"b"` concept test (type-level)
- Edge cases: distribute conditional
- Concepts: conditional types

16) Simplify Intersection Output
- Problem: `Simplify<T>` to flatten display in IDE.
- Example I/O: `Simplify<A & B>`
- Edge cases: none
- Concepts: mapped types

17) Order State Machine (union + transitions)
- Problem: `Status = "created"|"paid"|"packed"|"shipped"|"delivered"|"cancelled"`.
  `transition(status, action)` prevents invalid transitions.
- Example I/O: created + pay -> paid, delivered + cancel -> error
- Edge cases: cancel from some states only
- Concepts: unions, exhaustive checks

18) ParseResult Union
- Problem: parse number from string returning `Result<number,string>`.
- Example I/O: "12.5" -> ok(12.5), "x" -> err("Invalid number")
- Edge cases: whitespace, NaN
- Concepts: result union

19) Handler Map from Union
- Problem: `type Event = ...`; create `handlers` map typed so each handler gets correct payload type.
- Example I/O: wrong handler param -> compile error
- Edge cases: missing handler key
- Concepts: mapped types over union

20) Next Step Depends on Previous (advanced)
- Problem: onboarding steps; only allow calling `verifyEmail()` after `signup()`.
- Example I/O: wrong order should not compile (type-level)
- Edge cases: simplify if too hard
- Concepts: generics, state typing

---

## 03) optional and readonly
Top 20.

1) Optional Profile Display
- Problem: `Profile { name; bio?; avatarUrl? }`; `renderProfile(p)` uses fallback texts.
- Example I/O: missing bio -> "No bio"
- Edge cases: empty string vs undefined
- Concepts: optional, nullish

2) Readonly SKU Update
- Problem: `Product { readonly sku; name; price }`; `updateProduct(p, patch)` returns new product without changing sku.
- Example I/O: attempt patch sku should fail
- Edge cases: patch includes sku
- Concepts: readonly, immutability

3) Optional Discount Apply
- Problem: `applyDiscount(price, discount?)` uses `discount ?? 0`.
- Example I/O: (100, undefined)->100
- Edge cases: discount=0 should keep 100
- Concepts: `??` vs `||`

4) Readonly Array Add
- Problem: given `readonly number[]`, return new array with extra value.
- Example I/O: [1,2]+3 -> [1,2,3]
- Edge cases: none
- Concepts: readonly arrays

5) Optional Chaining Nested
- Problem: `user.address?.city ?? "Unknown"` utility function.
- Example I/O: no address -> "Unknown"
- Edge cases: city empty string
- Concepts: `?.`, `??`

6) Partial Update Merge
- Problem: `merge<T>(base:T, patch:Partial<T>):T` without mutation.
- Example I/O: update name only
- Edge cases: nested objects shallow merge only (mention)
- Concepts: Partial, spread

7) Optional Param Currency
- Problem: `formatMoney(amount, currency?)` default "INR".
- Example I/O: 100 -> "INR 100"
- Edge cases: invalid currency string
- Concepts: default params

8) Readonly vs Object.freeze Demo
- Problem: show compile-time readonly vs runtime freeze difference with code + explanation.
- Example I/O: freeze prevents runtime mutation
- Edge cases: deep freeze not automatic
- Concepts: runtime vs compile-time

9) Immutable Cart Qty Update
- Problem: cart items array; update qty by sku immutably.
- Example I/O: update -> new array
- Edge cases: sku not found -> return same array
- Concepts: immutability

10) DeepReadonly vs Readonly
- Problem: demonstrate `Readonly<T>` does not deep lock nested; implement DeepReadonly.
- Example I/O: nested mutation should fail with DeepReadonly
- Edge cases: arrays
- Concepts: mapped + conditional

11) exactOptionalPropertyTypes scenario
- Problem: explain with code `{a?:string}` vs `{a:string|undefined}` behavior.
- Example I/O: assignment differences
- Edge cases: toolchain dependent
- Concepts: TS config

12) Patch Unknown Key Reject (type-level)
- Problem: `updateUser(u, patch)` should not allow keys outside User at compile time.
- Example I/O: patch `{role:"x"}` error
- Edge cases: generics inference
- Concepts: keyof constraints

13) ReadonlyMap read
- Problem: store rates in `ReadonlyMap<string, number>` and read rate for key with fallback.
- Example I/O: missing -> 0
- Edge cases: none
- Concepts: readonly collections

14) mergeDefaults<T>
- Problem: `mergeDefaults(defaults:T, overrides?:Partial<T>):T`.
- Example I/O: overrides missing -> defaults
- Edge cases: overrides undefined
- Concepts: generics + optional

15) DeepPartial Utility
- Problem: implement `DeepPartial<T>` for nested patch.
- Example I/O: patch nested fields
- Edge cases: arrays
- Concepts: conditional types

16) Immutable Tree Update
- Problem: update node label by id in nested tree without mutation.
- Example I/O: returns new tree
- Edge cases: id missing
- Concepts: recursion, immutability

17) Readonly State Reducer
- Problem: todo reducer with readonly state; return new state for add/toggle/remove.
- Example I/O: action -> next state
- Edge cases: unknown action -> same state
- Concepts: union actions, immutability

18) Branded ID
- Problem: `type UserId = string & { __brand:"UserId" }`; `makeUserId(s)` validation.
- Example I/O: "" -> throw
- Edge cases: trim
- Concepts: branded types

19) Readonly constructor param
- Problem: class with `readonly id` set in constructor param property.
- Example I/O: cannot reassign
- Edge cases: none
- Concepts: parameter properties

20) Persistent List Update
- Problem: given readonly list, remove item by id returning new list.
- Example I/O: list size decreases
- Edge cases: id not found -> same list
- Concepts: immutability

---

## 04) generics
Top 20.

1) identity<T>
- Problem: `identity<T>(x:T):T`
- Example I/O: identity(5)->5
- Edge cases: none
- Concepts: generics basics

2) first<T>
- Problem: return first element or undefined.
- Example I/O: [] -> undefined
- Edge cases: empty array
- Concepts: `T | undefined`

3) wrap<T>
- Problem: return `{ value:T }`
- Example I/O: wrap("a") -> {value:"a"}
- Edge cases: none
- Concepts: generics

4) pair<T,U>
- Problem: return tuple/object pair typed.
- Example I/O: pair(1,"a") -> [1,"a"]
- Edge cases: none
- Concepts: multiple type params

5) mapArray<T,R>
- Problem: implement generic map.
- Example I/O: [1,2] -> ["1","2"]
- Edge cases: none
- Concepts: higher-order functions

6) ApiResponse<T>
- Problem: generic response model and `mapResponse<T,R>()`
- Example I/O: ok(data) -> ok(mapped)
- Edge cases: error pass-through
- Concepts: generics + union

7) Box<T> class
- Problem: class holds value with getter/setter typed.
- Example I/O: set number only
- Edge cases: validation in setter
- Concepts: generics + class

8) Constraint `T extends {id:string}`
- Problem: `indexById(list)` returns `Record<string,T>`
- Example I/O: list -> map
- Edge cases: duplicate id last wins
- Concepts: constraints

9) pluck<T,K extends keyof T>
- Problem: return `obj[key]`
- Example I/O: pluck(user,"name")->string
- Edge cases: none
- Concepts: keyof

10) sortByKey<T, K extends keyof T>
- Problem: sort array by key (number/string only).
- Example I/O: products sort by price
- Edge cases: undefined values
- Concepts: constraints

11) Generic default type
- Problem: `type Result<T=string,E=Error> = ...`
- Example I/O: Result without args defaults
- Edge cases: none
- Concepts: default generic params

12) parseJson<T>(s, guard)
- Problem: parse JSON as unknown then guard to T.
- Example I/O: invalid -> err
- Edge cases: JSON parse error
- Concepts: unknown + guard

13) freeze<T>(obj): Readonly<T>
- Problem: return typed readonly, and demonstrate compile-time protection.
- Example I/O: cannot assign
- Edge cases: shallow
- Concepts: Readonly

14) timeout<T>(promise, ms)
- Problem: resolve or reject after ms.
- Example I/O: long task -> timeout error
- Edge cases: cleanup
- Concepts: Promise generics

15) PromiseReturn<T>
- Problem: `type PromiseReturn<T> = T extends Promise<infer X> ? X : T`
- Example I/O: PromiseReturn<Promise<number>> = number
- Edge cases: nested promises optional
- Concepts: infer

16) FuncReturn<T>
- Problem: return type extraction: `T extends (...a:any)=>infer R ? R : never`
- Example I/O: Fn -> return type
- Edge cases: non-function -> never
- Concepts: infer

17) Typed Event Emitter
- Problem: event map generic with `on` and `emit` typed.
- Example I/O: emit("login",{userId:"u1"})
- Edge cases: wrong payload errors
- Concepts: mapped types + generics

18) Fluent Builder Accumulating Keys
- Problem: builder sets keys and build returns object with those keys typed.
- Example I/O: set("name","A").build() -> {name:string}
- Edge cases: duplicate set overwrites
- Concepts: advanced generics

19) Primitive Schema to Type
- Problem: schema `{ kind:"string" }|{kind:"number"}` maps to TS types.
- Example I/O: infer output type
- Edge cases: arrays
- Concepts: conditional types

20) decode<T>(unknown, guard)
- Problem: safe decode pattern.
- Example I/O: decode<User>(u,isUser)
- Edge cases: guard false -> throw/result
- Concepts: runtime validation

---

## 05) enum vs string literal union
Top 20.

1) Status union
- Problem: `type Status="draft"|"published"`; `canEdit(status)` true only for draft.
- Example I/O: "published" -> false
- Edge cases: none
- Concepts: string unions

2) Same as enum
- Problem: Implement `enum StatusEnum { Draft="draft", Published="published" }` and compare usage.
- Example I/O: `StatusEnum.Draft`
- Edge cases: runtime output explain
- Concepts: enums

3) Switch label
- Problem: map status to label via switch.
- Example I/O: draft -> "Draft"
- Edge cases: exhaustive check
- Concepts: switch + never

4) Record label map
- Problem: `Record<Status,string>` label map.
- Example I/O: labelMap[status]
- Edge cases: missing key compile error
- Concepts: Record

5) Prevent invalid string
- Problem: `"drafft"` assignment should fail.
- Example I/O: compile error
- Edge cases: external data parse required
- Concepts: literal unions

6) const object + as const
- Problem: `const STATUS = {...} as const` derive type.
- Example I/O: `type Status = typeof STATUS[keyof typeof STATUS]`
- Edge cases: none
- Concepts: as const

7) parseStatus(input: unknown)
- Problem: validate external value into Status else throw/result.
- Example I/O: "draft" -> ok
- Edge cases: uppercase, whitespace
- Concepts: validation

8) Exhaustive transitions
- Problem: status transitions: draft->published only; enforce.
- Example I/O: published->draft error
- Edge cases: none
- Concepts: state machine

9) Enum compile footprint explanation
- Problem: show numeric vs string enum output (brief).
- Example I/O: explain
- Edge cases: bundlers
- Concepts: runtime JS output

10) Reverse mapping demo
- Problem: numeric enum reverse mapping example.
- Example I/O: Enum[0] -> "A"
- Edge cases: none
- Concepts: enums

11) Tree-shaking discussion (coding hint)
- Problem: show why string unions often smaller than enums for libs.
- Example I/O: explain
- Edge cases: none
- Concepts: bundling

12) Serialize/deserialize
- Problem: store Status in localStorage as string; restore with parser.
- Example I/O: read -> Status
- Edge cases: corrupted storage
- Concepts: parsing

13) Role permission map
- Problem: `type Role="ADMIN"|"USER"|"GUEST"`; `Record<Role,string[]>`.
- Example I/O: get permissions
- Edge cases: none
- Concepts: Record

14) Feature flag keys union
- Problem: flags keys union; typed `isEnabled(flag)` function.
- Example I/O: `isEnabled("newUI")`
- Edge cases: unknown flag should error
- Concepts: unions

15) Enum-like object pattern
- Problem: create enum-like const object for PaymentMethod and derived type.
- Example I/O: PaymentMethod.Card -> "card"
- Edge cases: none
- Concepts: as const

16) Strict parser with helpful error
- Problem: error message includes allowed values list.
- Example I/O: input "x" -> "Allowed: draft,published"
- Edge cases: none
- Concepts: runtime + types

17) Typed transition map
- Problem: `const transitions: Record<Status, Status[]>` enforce allowed next states.
- Example I/O: transitions["draft"] includes published
- Edge cases: none
- Concepts: Record

18) Template literal type events
- Problem: `type EventName = \`user:${"created"|"deleted"}\``
- Example I/O: "user:created" ok
- Edge cases: none
- Concepts: template literal types

19) i18n labels mapping
- Problem: `Record<Status,{en:string;bn:string}>`
- Example I/O: labels[status].bn
- Edge cases: none
- Concepts: mapping

20) Endpoint by status
- Problem: `getEndpoint(status:Status)` returns string; exhaustive switch.
- Example I/O: draft -> "/draft"
- Edge cases: none
- Concepts: switch exhaustive

---

## 06) any unknown never
Top 20.

1) unknown to string
- Problem: `normalizeName(x:unknown):string` string হলে trim else throw.
- Example I/O: " A " -> "A"
- Edge cases: empty after trim -> throw
- Concepts: unknown narrowing

2) Replace any with unknown
- Problem: given function param any; change to unknown and add safe checks.
- Example I/O: show before/after
- Edge cases: none
- Concepts: safety

3) safeJsonParse returns unknown
- Problem: `safeJsonParse(s): unknown | null` parse fail -> null.
- Example I/O: invalid JSON -> null
- Edge cases: none
- Concepts: unknown

4) isNumberArray guard
- Problem: `isNumberArray(u): u is number[]`.
- Example I/O: [1,2] true
- Edge cases: [1,"2"] false
- Concepts: type predicate

5) fail(msg): never
- Problem: function always throws.
- Example I/O: throw Error
- Edge cases: none
- Concepts: never

6) assertNever helper
- Problem: `assertNever(x:never):never` used in exhaustive switch.
- Example I/O: compile-time guarantee
- Edge cases: none
- Concepts: exhaustive

7) catch error safely
- Problem: `catch(e)` is unknown; print message if e is Error else stringify.
- Example I/O: prints message
- Edge cases: thrown string
- Concepts: unknown in catch

8) validate User shape at runtime
- Problem: `isUser(u:unknown): u is {id:string;name:string}`
- Example I/O: valid -> true
- Edge cases: missing keys
- Concepts: runtime validation

9) decode API response unknown -> User
- Problem: `decodeUser(u:unknown):User` using guard.
- Example I/O: invalid -> throw
- Edge cases: nested types optional
- Concepts: decode pattern

10) Error union mapping
- Problem: custom errors union; map to HTTP-like status codes.
- Example I/O: AuthError -> 401
- Edge cases: unknown -> 500
- Concepts: unions

11) Remove unsafe casting
- Problem: code uses `as User`; replace with guards.
- Example I/O: compile safe
- Edge cases: none
- Concepts: avoid `as`

12) Conditional returns never on invalid
- Problem: `type NonString<T> = T extends string ? never : T`
- Example I/O: NonString<string> -> never
- Edge cases: union distribution
- Concepts: conditional types

13) Parse query params unknown
- Problem: `parseLimit(x:unknown):number` must be 1..100.
- Example I/O: "10" -> 10
- Edge cases: 0, 101 -> error
- Concepts: validation

14) Narrow unknown event payload by topic
- Problem: topic string determines payload guard.
- Example I/O: "user.created" expects {id}
- Edge cases: unknown topic -> error
- Concepts: mapping

15) Mini validator combinators
- Problem: implement `isString`, `isNumber`, `isObject`, `hasKey`.
- Example I/O: compose to validate shapes
- Edge cases: null is object in JS (handle)
- Concepts: runtime checks

16) Result-based errors instead of throw
- Problem: return `Result<T,E>` from parser instead of throwing.
- Example I/O: ok/err
- Edge cases: none
- Concepts: result union

17) assertDefined
- Problem: `assertDefined(x): asserts x is NonNullable<T>`
- Example I/O: after assert, x not null/undefined
- Edge cases: falsy values should pass if not null
- Concepts: assertion functions

18) Safe deep get
- Problem: `get(u:unknown, path:string[]): unknown` handle missing path safely.
- Example I/O: missing -> undefined
- Edge cases: arrays index strings
- Concepts: unknown-safe utilities

19) Unknown payload registry typed
- Problem: registry map `topic -> parser`; output type inferred.
- Example I/O: parse("user.created", payload) returns UserCreatedEvent typed
- Edge cases: topic not found
- Concepts: generics + mapping

20) Never unreachable branch demonstration
- Problem: in union switch, ensure default unreachable; show compile error if new union member added and not handled.
- Example I/O: add new member -> compile fail
- Edge cases: none
- Concepts: never exhaustive

---

## 07) null undefined strict mode
Top 20.

1) Nullable username display
- Problem: `string | null`; display fallback "Guest".
- Example I/O: null -> Guest
- Edge cases: "" empty string should display "" (not Guest)
- Concepts: nullish vs falsy

2) Optional param vs `string|undefined`
- Problem: create two functions and show difference in call sites.
- Example I/O: compile demo
- Edge cases: exactOptionalPropertyTypes
- Concepts: optional semantics

3) strictNullChecks demo
- Problem: show why `let s:string = null` fails; fix with union.
- Example I/O: compile
- Edge cases: none
- Concepts: TS strictness

4) Non-null assertion risk
- Problem: `user!.name` risk; replace with guard.
- Example I/O: safe output
- Edge cases: undefined user
- Concepts: safer patterns

5) Optional chaining safe access
- Problem: read nested `config.api?.timeoutMs ?? 3000`.
- Example I/O: missing -> 3000
- Edge cases: timeout=0 should keep 0
- Concepts: `?.`, `??`

6) `??` vs `||`
- Problem: demonstrate with 0 and empty string.
- Example I/O: 0 ?? 10 -> 0, 0 || 10 -> 10
- Edge cases: false boolean
- Concepts: operators

7) Default param
- Problem: `function f(x:number=10)` compare with `x?:number`.
- Example I/O: f() -> 10
- Edge cases: explicit undefined
- Concepts: defaults

8) parse optional to number
- Problem: `parsePage(x:string|undefined): number` default 1.
- Example I/O: undefined -> 1
- Edge cases: invalid -> 1 or error (choose)
- Concepts: parsing

9) Filter undefined with predicate
- Problem: `(string|undefined)[]` to `string[]`.
- Example I/O: ["a",undefined] -> ["a"]
- Edge cases: "" stays
- Concepts: type predicate

10) Maybe<T> helper
- Problem: `type Maybe<T>=T|null|undefined`; `unwrapMaybe(x, fallback)`
- Example I/O: null -> fallback
- Edge cases: 0 not fallback
- Concepts: nullish

11) Form value normalize
- Problem: input `string|null` -> `string` (empty if null).
- Example I/O: null -> ""
- Edge cases: trim optional
- Concepts: normalization

12) Date format safe
- Problem: `Date|null` -> string; null -> "N/A".
- Example I/O: null -> N/A
- Edge cases: invalid date
- Concepts: null handling

13) Config required env vars
- Problem: `getEnv(name): string` throws if missing.
- Example I/O: missing -> error
- Edge cases: empty string treat as missing or allowed (define)
- Concepts: runtime strictness

14) Strict function types (variance)
- Problem: show why `(x: string) => void` not assignable to `(x: string|number)=>void` in strict mode.
- Example I/O: explanation + code
- Edge cases: none
- Concepts: function variance

15) exactOptionalPropertyTypes behavior
- Problem: show assignment differences.
- Example I/O: `{a?:string}` cannot set `a: undefined` under exact optional settings.
- Edge cases: config-dependent
- Concepts: TS config

16) assertDefined utility
- Problem: assertion fn to narrow null/undefined.
- Example I/O: after assert, type is T
- Edge cases: falsy values
- Concepts: asserts

17) NonNullable usage
- Problem: `type NN = NonNullable<string|null|undefined>`.
- Example I/O: NN is string
- Edge cases: unions
- Concepts: utility types

18) Optional keys mapping
- Problem: get optional keys of type `T` (advanced).
- Example I/O: optional key union
- Edge cases: tricky types
- Concepts: mapped + conditional

19) Strict config schema runtime
- Problem: load config object where no field remains undefined; validate and return typed config.
- Example I/O: missing -> error
- Edge cases: nested required fields
- Concepts: validation

20) Builder ensures required fields before build
- Problem: step builder: setApiBaseUrl then build; build not callable before set.
- Example I/O: compile-time block
- Edge cases: keep minimal
- Concepts: typestate pattern

---

## 08) classes access modifiers getter setter
Top 20.

1) BankAccount encapsulation
- Problem: private `balance`, methods `deposit`, `withdraw` (no negative).
- Example I/O: withdraw too much -> error/result
- Edge cases: deposit 0/negative
- Concepts: private, validation

2) readonly accountId
- Problem: readonly id set in constructor; cannot modify later.
- Example I/O: assignment error
- Edge cases: none
- Concepts: readonly, constructor param properties

3) Getter masked id
- Problem: getter returns masked id `****1234`.
- Example I/O: "ABC1234" -> "****1234"
- Edge cases: length <4
- Concepts: getter

4) Setter pin validation
- Problem: setter for pin requires 4 digits.
- Example I/O: "123" -> error
- Edge cases: non-numeric
- Concepts: setter

5) public/private access demo
- Problem: show compile error when accessing private property outside class.
- Example I/O: compile error
- Edge cases: none
- Concepts: access modifiers

6) Simple inheritance
- Problem: `Vehicle` base with `move()`, `Car` extends adds `honk()`.
- Example I/O: call both
- Edge cases: override move
- Concepts: extends, override

7) protected usage
- Problem: base has protected `log()` used only by child classes.
- Example I/O: outside call fails
- Edge cases: none
- Concepts: protected

8) static counter
- Problem: count active sessions with static property.
- Example I/O: create 2 -> count 2
- Edge cases: destroy reduces count (implement)
- Concepts: static

9) abstract gateway
- Problem: abstract `PaymentGateway` with `pay(amount)`; implement `MockGateway`.
- Example I/O: pay returns receipt
- Edge cases: negative amount
- Concepts: abstract

10) implements Logger interface
- Problem: `Logger` interface and `ConsoleLogger` class implements it.
- Example I/O: logger.log("hi")
- Edge cases: levels union
- Concepts: implements

11) Prevent invalid state (setter)
- Problem: `Product.price` cannot be negative; enforce in setter.
- Example I/O: set -1 -> error
- Edge cases: NaN
- Concepts: invariants

12) Composition over inheritance
- Problem: `OrderService` uses `PriceCalculator` injected in constructor.
- Example I/O: calculate order total
- Edge cases: none
- Concepts: DI, testability

13) Simple role check method
- Problem: `AdminPanel` method `deleteUser` only allowed if role is ADMIN.
- Example I/O: USER -> error
- Edge cases: none
- Concepts: guard

14) Factory method ensures invariants
- Problem: private constructor; static `create()` validates input.
- Example I/O: invalid -> error
- Edge cases: empty strings
- Concepts: factory pattern

15) Immutable class
- Problem: `Money` class; `add()` returns new instance, does not mutate.
- Example I/O: m1 unchanged
- Edge cases: rounding
- Concepts: immutability

16) Builder with private constructor
- Problem: `OrderBuilder` collects items; `build()` returns Order.
- Example I/O: items empty -> error
- Edge cases: qty <=0
- Concepts: builder

17) Custom error classes
- Problem: `InsufficientBalanceError`, map to message.
- Example I/O: catch and print
- Edge cases: unknown
- Concepts: error modeling

18) Strategy pattern shipping
- Problem: `ShippingStrategy` interface; `Standard`, `Express` implement.
- Example I/O: cost differs
- Edge cases: weight negative
- Concepts: polymorphism

19) Lazy computed getter cache
- Problem: expensive compute; compute once then cache.
- Example I/O: repeated calls no recompute
- Edge cases: invalidation
- Concepts: caching

20) Testability: mock dependency
- Problem: service depends on `Clock` interface; in tests pass FakeClock.
- Example I/O: deterministic timestamps
- Edge cases: none
- Concepts: DI best practices

---

## 09) async await promise
Top 20.

1) delay(ms)
- Problem: implement `delay(ms): Promise<void>`.
- Example I/O: await delay(100)
- Edge cases: ms < 0 -> treat 0
- Concepts: promises

2) try/catch async error
- Problem: async function throws; handle and return fallback.
- Example I/O: fail -> "fallback"
- Edge cases: non-Error thrown
- Concepts: error handling

3) Sequential calls
- Problem: fetch user then fetch orders(userId) sequentially.
- Example I/O: returns combined result
- Edge cases: first fails -> stop
- Concepts: await chain

4) Parallel calls with Promise.all
- Problem: fetch A,B,C parallel and sum.
- Example I/O: 1+2+3=6
- Edge cases: if any fails -> reject
- Concepts: Promise.all

5) Promise.race timeout
- Problem: wrap promise with timeout error after ms.
- Example I/O: long -> timeout
- Edge cases: cleanup mention
- Concepts: race

6) Retry 3 times
- Problem: retry on failure with exponential backoff.
- Example I/O: succeeds on 2nd try
- Edge cases: always fail -> return error
- Concepts: loops + await

7) finally loading
- Problem: `loading=true` before await, set false in finally.
- Example I/O: always false after
- Edge cases: none
- Concepts: finally

8) Promise.allSettled report
- Problem: run tasks and return `{okCount, failCount}`.
- Example I/O: 2 ok 1 fail
- Edge cases: empty list
- Concepts: allSettled

9) Concurrency limit (max 3)
- Problem: process 20 async jobs with limit 3 in parallel.
- Example I/O: completes all
- Edge cases: job failure handling strategy
- Concepts: queue, semaphore-like

10) Cancel pattern simulation
- Problem: create cancel token; if cancelled, stop processing loop.
- Example I/O: cancel mid-way
- Edge cases: in-flight tasks
- Concepts: cancellation patterns

11) FIFO async job queue
- Problem: queue with `enqueue(job)` and worker `start()` processes one by one.
- Example I/O: order preserved
- Edge cases: enqueue while running
- Concepts: producer-consumer

12) Debounced async search
- Problem: debounce input changes and call async search only after 300ms idle.
- Example I/O: rapid typing -> one call
- Edge cases: last call only
- Concepts: debounce

13) Async memoization cache
- Problem: cache results by key; concurrent same key should share in-flight promise.
- Example I/O: 2 calls -> 1 network
- Edge cases: failure should clear cache
- Concepts: inflight map

14) Circuit breaker mini
- Problem: after 3 consecutive failures, open circuit for 5 seconds.
- Example I/O: calls blocked during open
- Edge cases: reset after time
- Concepts: resilience

15) Return Result instead of throwing
- Problem: async function returns `Result<T,E>` not throw.
- Example I/O: ok/err
- Edge cases: mapping errors
- Concepts: typed error handling

16) Async pipeline
- Problem: `pipeAsync(value, [fn1,fn2,fn3])`.
- Example I/O: transforms sequentially
- Edge cases: fn throws -> stop
- Concepts: composition

17) Idempotent request dedupe
- Problem: `requestOnce(key, fn)` ensures only one in-flight per key.
- Example I/O: duplicates await same promise
- Edge cases: failure release
- Concepts: dedupe

18) Backpressure simulation
- Problem: producer pushes jobs; if queue size > N, producer waits.
- Example I/O: no memory blow
- Edge cases: cancel
- Concepts: backpressure

19) DLQ for failed jobs
- Problem: failed jobs pushed to `deadLetterQueue` with reason and timestamp.
- Example I/O: dlq length increments
- Edge cases: retry before dlq
- Concepts: error workflows

20) CorrelationId tracing
- Problem: pass `correlationId` through async calls and include in logs.
- Example I/O: logs contain id
- Edge cases: missing id -> generate
- Concepts: observability

---

## 10) inventory literal unions
Non-HMS: warehouse/store inventory.

1) Basic dispense guard
- Problem: `dispense(item, qty)` cannot reduce below 0.
- Example I/O: qty 5, dispense 6 -> error
- Edge cases: qty <=0 invalid
- Concepts: guards

2) Category union
- Problem: `Category = "food"|"electronics"|"fashion"`; filter items by category.
- Example I/O: returns only electronics
- Edge cases: unknown category not allowed
- Concepts: literal unions

3) StockStatus computed
- Problem: compute `"in_stock"|"low"|"out"` based on qty thresholds.
- Example I/O: 0 -> out
- Edge cases: negative qty invalid
- Concepts: unions, business rules

4) Add item unique sku
- Problem: add only if sku not exists.
- Example I/O: duplicate -> error
- Edge cases: whitespace sku
- Concepts: map/indexing

5) Search by keyword
- Problem: search by name contains keyword (case-insensitive).
- Example I/O: "phone" -> matching items
- Edge cases: empty keyword -> all
- Concepts: string ops

6) Sort by qty desc
- Problem: return new sorted array without mutating original.
- Example I/O: sorted list
- Edge cases: equal qty keep stable (optional)
- Concepts: immutability

7) Restock validate positive
- Problem: `restock(sku, addQty)` addQty must be >0.
- Example I/O: restock 10
- Edge cases: sku missing
- Concepts: validation

8) Batch dispense (transaction)
- Problem: dispense multiple items; if any insufficient, none change.
- Example I/O: fails -> inventory unchanged
- Edge cases: duplicates in request
- Concepts: atomic update, copying

9) Result-based operations
- Problem: all inventory ops return `Result<Success, InventoryError>`.
- Example I/O: ok/err
- Edge cases: none
- Concepts: typed errors

10) Group by category
- Problem: group items into `Record<Category, StockItem[]>`.
- Example I/O: map categories
- Edge cases: empty categories
- Concepts: Record

11) Reorder tasks
- Problem: if qty below reorderThreshold, create reorder task entries.
- Example I/O: low items produce tasks
- Edge cases: threshold per category
- Concepts: derived data

12) Readonly sku in model
- Problem: ensure sku never changes on updates.
- Example I/O: patch sku -> compile error
- Edge cases: none
- Concepts: readonly

13) Audit log
- Problem: append audit entries for add/restock/dispense with timestamp.
- Example I/O: audit length grows
- Edge cases: log size limit (optional)
- Concepts: logging

14) Queue to prevent race (simulation)
- Problem: implement operation queue so dispense requests process sequentially.
- Example I/O: no negative due to race
- Edge cases: cancellation
- Concepts: concurrency patterns

15) Lifecycle state machine
- Problem: item lifecycle `"active"|"discontinued"|"archived"` with allowed transitions.
- Example I/O: discontinued -> archived ok
- Edge cases: active -> archived not allowed
- Concepts: state machines

16) Exhaustive switch on lifecycle
- Problem: switch all lifecycle states; default assertNever.
- Example I/O: compile safe
- Edge cases: add new state triggers compile fail
- Concepts: never

17) Generic inventory store
- Problem: generic store `T extends { sku:string }` with add/get/update.
- Example I/O: works for different item types
- Edge cases: none
- Concepts: generics + constraints

18) CSV import with validation
- Problem: parse CSV lines into items; invalid lines collected as errors.
- Example I/O: returns {items, errors}
- Edge cases: missing columns
- Concepts: parsing + Result

19) Discount rules by category
- Problem: apply discounts based on category union.
- Example I/O: electronics 10% off
- Edge cases: rounding
- Concepts: unions + mapping

20) Typed event emitter for inventory
- Problem: emit events `item_dispensed`, `item_restocked` with typed payloads.
- Example I/O: handlers get correct types
- Edge cases: none
- Concepts: event maps

---

## 11) workflow exhaustive checks
Non-HMS: support tickets / content pipeline.

1) Ticket status union
- Problem: `Status="open"|"in_progress"|"resolved"`; `canClose(status)`.
- Example I/O: open -> false
- Edge cases: none
- Concepts: unions

2) Video pipeline statuses
- Problem: `"uploaded"|"transcoding"|"published"|"failed"` label function.
- Example I/O: failed -> "Failed"
- Edge cases: exhaustive check
- Concepts: switch

3) Exhaustive handler
- Problem: handle all statuses; default assertNever.
- Example I/O: compile safe
- Edge cases: add new status triggers compile fail
- Concepts: never

4) Filter open tickets
- Problem: filter list to status "open".
- Example I/O: returns subset
- Edge cases: none
- Concepts: filter + unions

5) Transition with actions
- Problem: action union `"start"|"resolve"|"reopen"`; transition rules.
- Example I/O: open+start -> in_progress
- Edge cases: resolved+start invalid
- Concepts: state machine

6) Resolved requires note
- Problem: resolved state must include `resolutionNote`.
- Example I/O: cannot create resolved without note (type-level or runtime)
- Edge cases: empty note invalid
- Concepts: discriminated union payloads

7) Process Result union
- Problem: workflow step returns ok/err; bubble errors.
- Example I/O: err -> stop
- Edge cases: multiple errors list
- Concepts: Result<T,E>

8) Typed actions drive transitions
- Problem: model actions with payload e.g. `{type:"assign";agentId}`.
- Example I/O: assign sets assignee
- Edge cases: assign when resolved invalid
- Concepts: action union

9) Guard invalid transition
- Problem: invalid transition returns error object not throw.
- Example I/O: {ok:false,error:"InvalidTransition"}
- Edge cases: none
- Concepts: result errors

10) Timeline event log
- Problem: maintain timeline events array every transition.
- Example I/O: includes timestamps
- Edge cases: monotonic time
- Concepts: event logging

11) Retry failed step
- Problem: failed -> retry -> transcoding; max retries 2.
- Example I/O: after 2, stays failed
- Edge cases: reset counter on success
- Concepts: counters + state

12) Must assign before in_progress
- Problem: cannot start ticket unless assigned.
- Example I/O: open unassigned + start -> error
- Edge cases: auto assign optional
- Concepts: invariants

13) SLA overdue flag
- Problem: if open more than N hours, overdue true.
- Example I/O: compute boolean
- Edge cases: timezone ignore, use ms
- Concepts: time logic

14) Batch process summary
- Problem: count tickets by status -> `Record<Status, number>`.
- Example I/O: {open:2,...}
- Edge cases: empty list -> zeros
- Concepts: Record

15) Typed transition map (advanced)
- Problem: `const allowed: Record<Status, Status[]>` and check membership.
- Example I/O: validates
- Edge cases: none
- Concepts: mapping

16) Reducer pattern
- Problem: `(state, action) => newState` for workflow.
- Example I/O: action -> state
- Edge cases: unknown action -> state
- Concepts: reducer + unions

17) Side-effect event
- Problem: when resolved, emit `notification.send`.
- Example I/O: event emitted with payload
- Edge cases: none
- Concepts: events

18) Async workflow simulation
- Problem: simulate steps with `await delay`; state updates after each.
- Example I/O: printed logs
- Edge cases: failure at step 2
- Concepts: async state machine

19) Result-based async pipeline
- Problem: async steps return Result; pipeline stops on first error.
- Example I/O: ok -> final, err -> early exit
- Edge cases: none
- Concepts: functional error handling

20) In-code metrics counters
- Problem: maintain counters for each transition type.
- Example I/O: transitions["open->in_progress"]++
- Edge cases: unknown transitions
- Concepts: observability basics

---

## 12) tailwind ui snippets (non-hms)
These are UI coding prompts. Use React/Angular/HTML as you like, but keep state typed in TS.
Each item includes a TS typing requirement + UI requirement.

1) Product Card
- Problem: build product card UI. Props: `{readonly id; name; price; discount?:number; tags?:string[]}`.
- Example I/O: missing discount shows normal price
- Edge cases: discount=0
- Concepts: optional, readonly

2) Login Form State
- Problem: create login form UI. State type: `{email; password; remember:boolean}`.
- Example I/O: submit disabled if invalid
- Edge cases: trim email
- Concepts: types for form state

3) Loading Skeleton
- Problem: create skeleton UI toggled by `isLoading:boolean`.
- Example I/O: show skeleton then content
- Edge cases: none
- Concepts: union UI states optional

4) Toast variants union
- Problem: `type ToastType="success"|"error"|"info"`; style map typed.
- Example I/O: show success toast
- Edge cases: none
- Concepts: Record + unions

5) Modal component typed callbacks
- Problem: modal props: `{open:boolean; onClose:()=>void; onConfirm:()=>Promise<void>}`.
- Example I/O: confirm shows loading
- Edge cases: confirm fails -> show error
- Concepts: async + props typing

6) Generic Table<T>
- Problem: table component takes `rows:T[]` and `columns: Array<{key: keyof T; label:string}>`.
- Example I/O: renders any row type
- Edge cases: empty list shows empty state
- Concepts: generics + keyof

7) Pagination component
- Problem: `page:number`, `pageSize:number`, `total:number`, `onPageChange(p)` typed.
- Example I/O: next/prev
- Edge cases: last page
- Concepts: typed events

8) Debounced Search Bar
- Problem: search input triggers async search after 300ms idle.
- Example I/O: rapid typing -> 1 request
- Edge cases: cancel stale responses
- Concepts: async + state

9) Dropdown union options
- Problem: `type Sort="price_asc"|"price_desc"|"rating"`; dropdown selects sort.
- Example I/O: list sorted
- Edge cases: none
- Concepts: unions

10) Badge variants map
- Problem: `type Badge="new"|"sale"|"hot"`; map to class names via Record.
- Example I/O: badge displays
- Edge cases: none
- Concepts: Record

11) Confirm dialog returns Promise<boolean>
- Problem: `confirm("Delete?")` returns Promise<boolean> using modal.
- Example I/O: resolve true/false
- Edge cases: multiple confirms queued
- Concepts: Promise patterns

12) Stepper for workflow
- Problem: stepper UI for statuses union; highlight current.
- Example I/O: current step shown
- Edge cases: unknown status not allowed
- Concepts: unions + mapping

13) Tabs typed key union
- Problem: `type TabKey="overview"|"details"|"settings"`; tab state typed.
- Example I/O: switch tab
- Edge cases: none
- Concepts: unions

14) Empty State component
- Problem: show empty state with typed props `{title; description?; actionLabel?; onAction?}`.
- Example I/O: optional action
- Edge cases: no action
- Concepts: optional props

15) Error UI state union
- Problem: `UIState = {kind:"loading"}|{kind:"ready";data:T}|{kind:"error";message:string}`.
- Example I/O: render each
- Edge cases: exhaustive check
- Concepts: discriminated union

16) File Upload UI state
- Problem: states: idle/uploading/success/error; show progress number.
- Example I/O: uploading 40%
- Edge cases: retry
- Concepts: unions + async

17) Progress bar for async job
- Problem: simulate job that updates progress 0..100.
- Example I/O: bar moves
- Edge cases: cancel
- Concepts: async loops

18) Sidebar menu typed routes
- Problem: `const routes = [{key:"home", path:"/"}...] as const` derive type and render.
- Example I/O: select active
- Edge cases: none
- Concepts: as const, derived types

19) Form validation error map
- Problem: `type Field="email"|"password"`; error map `Partial<Record<Field,string>>`.
- Example I/O: show field errors
- Edge cases: none
- Concepts: Partial + Record

20) Theme switcher union
- Problem: `type Theme="light"|"dark"`; toggle UI and persist to localStorage with parser.
- Example I/O: reload retains theme
- Edge cases: corrupted storage
- Concepts: unions + parsing

---
End of file.
```
 
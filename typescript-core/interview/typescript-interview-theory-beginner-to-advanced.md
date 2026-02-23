## Interview Theory Questions (Beginner → Intermediate → Advanced)

Below are quick, easy-English answers for each common theory question. Keep them short when speaking; expand only if the interviewer probes.

---

## 01) Type vs Interface

### Beginner
- Q: How are `type` and `interface` different?  
  A: Both describe shapes. `interface` is mainly for objects/classes; `type` can also alias unions, primitives, etc.
- Q: Which is used more for object shapes?  
  A: Either works; teams pick one style. Interfaces are traditional for objects.
- Q: Why call an interface a “contract”?  
  A: It lists required members that any implementing object/class must provide.
- Q: What can `type` define besides objects?  
  A: Unions, intersections, primitives, function signatures, mapped types.

### Intermediate
- Q: How does `extends` work?  
  A: Interfaces extend interfaces; types build intersections (`A & B`) to combine shapes.
- Q: What is declaration merging? Why only interfaces?  
  A: Multiple `interface Foo` statements merge members. Types cannot be reopened, so they don’t merge.
- Q: Who can use `implements`?  
  A: Classes can `implements` interfaces (or type aliases that are object-like).
- Q: What’s the practical effect of not reopening a type alias?  
  A: The shape is fixed; you must create a new alias or intersection instead of adding fields later.

### Advanced
- Q: How to choose type vs interface in library typings?  
  A: Use interfaces for public object contracts that may need augmentation; use types for unions/utility-heavy shapes.
- Q: Does the choice change runtime behavior?  
  A: No—both vanish at runtime; only type-checking differs.
- Q: Risk of interface merging?  
  A: Accidental member collisions or unintended widening when consumers augment.
- Q: Why are types handy in complex union/intersection code?  
  A: Type aliases compose unions, mapped/conditional types more naturally than interfaces.

---

## 02) Union `|` and Intersection `&`

### Beginner
- Q: What are union and intersection types?  
  A: Union `A | B` means value can be A or B. Intersection `A & B` means it must satisfy both.
- Q: What does `A | B` guarantee?  
  A: Only the fields common to A and B are safe without narrowing.
- Q: What does `A & B` guarantee?  
  A: All fields from A and B are present.

### Intermediate
- Q: What is narrowing?  
  A: Checking runtime clues (typeof, `in`, discriminant) to shrink a union to a specific member.
- Q: What is a discriminated union?  
  A: A union where each member has a literal tag (e.g., `type`) so switch/case can narrow safely.
- Q: When is common property access safe/unsafe in unions?  
  A: Safe if the property exists on every member; unsafe otherwise until narrowed.
- Q: What happens if intersection members conflict?  
  A: Conflicting property types produce `never`/error; compatible types are intersected.

### Advanced
- Q: What is union distribution in conditional types?  
  A: Conditional types run per union member, then reunite the results.
- Q: Exhaustiveness checking idea?  
  A: Handle every union case (often with a `never` fall-through) so the compiler flags missing cases.
- Q: How do unions prevent impossible states?  
  A: They model exact allowed variants, making invalid combinations unrepresentable.
- Q: Role of unions in state machines?  
  A: Each state is a union member with its required data, enabling safe transitions and checks.

---

## 03) Optional `?`, `readonly`

### Beginner
- Q: What does `prop?: T` mean?  
  A: The property may be missing or `undefined`.
- Q: Why is `prop?: T` different from `prop: T | undefined`?  
  A: Optional means the key may be absent; `T | undefined` still requires the key.
- Q: Why use `readonly`?  
  A: To prevent reassignment after creation, protecting invariants.

### Intermediate
- Q: Is `readonly` shallow or deep?  
  A: Built-in `readonly` is shallow; nested objects stay mutable unless also marked readonly.
- Q: Difference between `ReadonlyArray<T>` and `readonly T[]`?  
  A: They’re equivalent in meaning; `ReadonlyArray` is the generic form.
- Q: When to use optional chaining `?.`?  
  A: When a link in the property chain might be null/undefined and you want `undefined` instead of a crash.

### Advanced
- Q: Is `readonly` compile-time only?  
  A: Yes; it enforces at compile time, not runtime. Cloning can bypass it.
- Q: Problems from overusing optional fields?  
  A: Harder validation, more undefined checks, looser contracts.
- Q: How does immutability help?  
  A: Predictable state, safer sharing, easier reasoning.
- Q: What is defensive copying?  
  A: Making copies before storing/returning to avoid external mutations; complements readonly.

---

## 04) Generics

### Beginner
- Q: Why generics?  
  A: Reuse logic while keeping type safety for different input/output types.
- Q: What does type parameter `T` represent?  
  A: A placeholder for a concrete type chosen by the caller or inferred.
- Q: What happens without generics?  
  A: You duplicate code or fall back to `any`, losing safety.

### Intermediate
- Q: Why use `T extends X`?  
  A: To constrain callers so `T` meets required shape/behavior.
- Q: What is inference?  
  A: The compiler deduces `T` from arguments/results instead of requiring you to write it.
- Q: Concept of `keyof`?  
  A: Produces the union of property names of a type.

### Advanced
- Q: Tradeoff of type safety vs flexibility?  
  A: Tighter constraints catch bugs but can reduce ease of use; looser ones may admit invalid states.
- Q: Why use a generic Result<T,E>?  
  A: Standardizes success/error payloads and avoids exceptions for flow control.
- Q: Variance intuition?  
  A: How subtyping behaves for inputs/outputs; functions are usually contravariant in params, covariant in returns.
- Q: Overload vs generic signature—when?  
  A: Use generics for one flexible rule; use overloads when distinct call shapes return different types.

---

## 05) Enum vs String Literal Union

### Beginner
- Q: What are enums and string literal unions?  
  A: Enums emit runtime objects; literal unions are type-only sets of strings.
- Q: Why are literal unions lightweight?  
  A: They vanish at compile time and add no runtime code.

### Intermediate
- Q: How do enums exist at runtime?  
  A: They compile to objects (unless `const enum`) with forward/reverse mappings.
- Q: Autocomplete/refactor safety differences?  
  A: Both give IntelliSense; enums add a namespaced object, unions rely on string literals.
- Q: What is `const enum`?  
  A: An enum erased to inlined values (no runtime object) but requiring isolatedModules care.

### Advanced
- Q: Tree-shaking/bundle impact?  
  A: Regular enums keep emitted objects, which may bloat bundles; unions do not.
- Q: Shared contracts across packages—tradeoffs?  
  A: Enums provide a single runtime source; unions avoid version conflicts but need manual alignment.
- Q: Risk sending enums over the wire?  
  A: Numeric enums can mismatch; strings are clearer. Version drift can break clients.
- Q: Migrating enum → union considerations?  
  A: Update runtime uses, adjust imports, ensure constants replace enum members.

---

## 06) `any`, `unknown`, `never`

### Beginner
- Q: Why is `any` dangerous?  
  A: It disables type checking, letting runtime bugs slip in.
- Q: Why is `unknown` safer?  
  A: You must narrow/validate before use, so unsafe operations are blocked.
- Q: What does `never` mean?  
  A: A value that never occurs (functions that throw/loop, unreachable code).

### Intermediate
- Q: What is a type guard?  
  A: A runtime check that tells the compiler a narrower type (e.g., `typeof x === "string"`).
- Q: How to use `unknown` values?  
  A: Narrow with guards or runtime validation first.
- Q: When does `never` appear naturally?  
  A: In exhaustive switches’ default branches or functions that throw/loop forever.

### Advanced
- Q: Role of `never` in exhaustiveness?  
  A: Assigning to `never` in the default case forces the compiler to ensure all union members were handled.
- Q: How does `any` harm soundness?  
  A: It allows assigning anything to anything, hiding type errors and spreading unsafety.
- Q: Benefit of `unknown` without runtime validation?  
  A: Limited; you still need checks. It mainly stops accidental unsafe use.
- Q: Throw vs Result union for errors?  
  A: Throws use exceptions for flow; Result unions keep control flow explicit and type-checked.

---

## 07) `null/undefined` + strict mode

### Beginner
- Q: Difference between `null` and `undefined`?  
  A: `undefined` = not provided; `null` = explicitly empty.
- Q: What changes with `strictNullChecks`?  
  A: You must handle `null`/`undefined` explicitly; they aren’t assignable to other types.

### Intermediate
- Q: `??` vs `||`?  
  A: `??` only falls back on null/undefined; `||` also treats falsy values like 0/"" as false.
- Q: Why is optional chaining safe?  
  A: It stops property access when a link is nullish, returning `undefined` instead of throwing.
- Q: What does non-null assertion `!` do?  
  A: Tells the compiler you’re sure a value isn’t nullish (no runtime check).

### Advanced
- Q: Why is overusing `!` risky?  
  A: You silence compiler warnings and can crash at runtime.
- Q: Null-handling philosophies?  
  A: Fail fast (throw early) vs silent fallback (default values); pick one consistently.
- Q: API design in strict mode?  
  A: Model nullability explicitly, avoid optional where not needed, document defaults.
- Q: Nullable modeling patterns?  
  A: Use unions with null, Maybe/Option types, or Result types to represent absence.

---

## 08) Classes, Access Modifiers, Getter/Setter

### Beginner
- Q: What is a class vs an object?  
  A: Class is a blueprint; objects are instances created from it.
- Q: Meaning of public/private/protected?  
  A: `public` accessible everywhere; `private` only inside the class; `protected` inside class and subclasses.
- Q: What is encapsulation?  
  A: Hiding internal state and exposing a safe API.

### Intermediate
- Q: Inheritance vs composition?  
  A: Inheritance reuses via “is-a”; composition reuses via “has-a”. Prefer composition when possible.
- Q: Why getters/setters?  
  A: To control reads/writes, validate, or compute values lazily.
- Q: `readonly` field vs private field?  
  A: `readonly` stops reassignment; `private` controls visibility. They solve different problems.

### Advanced
- Q: Why combine abstraction and encapsulation?  
  A: To model domains cleanly, keep invariants, and reduce coupling.
- Q: What is a class invariant?  
  A: A rule that must always hold for an instance (e.g., quantity >= 0).
- Q: Why is misuse of `protected` a smell?  
  A: It can leak internals to subclasses, making refactors risky.
- Q: DTO vs Domain model separation benefit?  
  A: DTOs are simple data carriers; domain models enforce rules. Separation keeps concerns clear.

---

## 09) async/await + Promise basics

### Beginner
- Q: What is a Promise? What do async functions return?  
  A: Promise represents a future value; async functions always return a Promise.
- Q: What does `await` do?  
  A: Pauses inside async function until the Promise settles, then gives the resolved value or throws on reject.
- Q: What do resolve/reject mean?  
  A: Resolve = success value; reject = failure reason.

### Intermediate
- Q: Sequential await vs parallel (Promise.all)?  
  A: Sequential waits one-by-one; parallel starts together and waits for all—faster when tasks are independent.
- Q: Why wrap async code in try/catch?  
  A: Awaited rejections throw; try/catch handles them like sync errors.
- Q: What is Promise chaining?  
  A: Returning a Promise in `.then` links operations so results flow to the next step.

### Advanced
- Q: What is backpressure/concurrency control?  
  A: Limiting simultaneous async work to avoid overload (e.g., queues, semaphore, mapLimit).
- Q: Why use cancellation patterns?  
  A: To stop unnecessary work when results are no longer needed.
- Q: How to avoid swallowed errors?  
  A: Always return/await Promises or handle rejections; use allSettled/logging.
- Q: What is idempotent request dedupe?  
  A: Reusing the same in-flight Promise for identical requests to prevent duplicate work.

---

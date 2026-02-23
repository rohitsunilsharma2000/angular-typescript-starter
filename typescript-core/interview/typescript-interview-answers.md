# TypeScript Interview Answers

- Anchors use `#qXX-YY` (topic, question). Each answer is concise sample code or explanation.
- Cross-link: questions file now links here per item.

## 01) type vs interface

### <a id="q01-01"></a>Q01-01 User Shape (interface + type)
```ts
interface User { readonly id: string; name: string; email?: string }
type UserAlias = { readonly id: string; name: string; email?: string };
```

### <a id="q01-02"></a>Q01-02 Admin Extends User
```ts
interface AdminUser extends User { permissions: string[] }
const admin: AdminUser = { id: "1", name: "Root", permissions: ["read","write"] };
```

### <a id="q01-03"></a>Q01-03 Declaration Merging Demo
```ts
interface AppConfig { apiBaseUrl: string }
interface AppConfig { timeoutMs: number }
const cfg: AppConfig = { apiBaseUrl: "/api", timeoutMs: 5000 };
```

### <a id="q01-04"></a>Q01-04 Type Alias Union Model
```ts
type Account = PersonalAccount | BusinessAccount;
type PersonalAccount = { kind: "personal"; pan: string };
type BusinessAccount = { kind: "business"; gst: string };
```

### <a id="q01-05"></a>Q01-05 Excess Property Check
```ts
function createUser(u: User) { return u; }
createUser({ id: "u1", name: "A" }); // ok
// createUser({ id:"u1", name:"A", role:"x" }); // TS error: excess property
```

### <a id="q01-06"></a>Q01-06 Function Type via Interface
```ts
interface Validator { (value: string): boolean }
const isEmail: Validator = v => /.+@.+\..+/.test(v);
```

### <a id="q01-07"></a>Q01-07 Hybrid Function + Property
```ts
interface LoggerFn { (msg: string): void; level: "debug"|"info"|"error" }
const logger = ((msg: string) => console.log(msg)) as LoggerFn;
logger.level = "debug";
```

### <a id="q01-08"></a>Q01-08 Pick User Preview
```ts
type UserPreview = Pick<User, "id" | "name">;
const toPreview = (u: User): UserPreview => ({ id: u.id, name: u.name });
```

### <a id="q01-09"></a>Q01-09 Omit Sensitive Fields
```ts
type PublicUser = Omit<User, "email"> & { displayName: string };
const toPublic = (u: User): PublicUser => ({ id: u.id, name: u.name, displayName: u.name });
```

### <a id="q01-10"></a>Q01-10 Readonly Enforcement
```ts
const u: User = { id: "1", name: "A" };
// u.id = "x"; // TS error
const updated = { ...u, id: "x" }; // new object if really needed
```

### <a id="q01-11"></a>Q01-11 Interface vs Type for Extensibility
```ts
interface Shape { area(): number }
type Shape2 = { area(): number };
class Circle implements Shape { constructor(private r: number) {} area() { return Math.PI * this.r ** 2; } }
// Note: interfaces can merge/implement; type aliases cannot merge.
```

### <a id="q01-12"></a>Q01-12 Index Signature Flags
```ts
type FeatureFlags = Record<string, boolean>;
const isEnabled = (flags: FeatureFlags, key: string) => flags[key] ?? false;
```

### <a id="q01-13"></a>Q01-13 Generic ApiResponse
```ts
interface ApiResponse<T> { ok: boolean; data?: T; error?: string }
function getOrThrow<T>(r: ApiResponse<T>): T {
  if (r.ok && r.data !== undefined) return r.data;
  throw new Error(r.error ?? "Unexpected response");
}
```

### <a id="q01-14"></a>Q01-14 Namespace-like Types
```ts
type Models = { User: User; Product: { id: string; price: number } };
type Product = Models["Product"];
```

### <a id="q01-15"></a>Q01-15 Result<T,E> Union
```ts
type Result<T, E> = { ok: true; data: T } | { ok: false; error: E };
const unwrap = <T, E>(r: Result<T, E>): T => r.ok ? r.data : (() => { throw r.error; })();
```

### <a id="q01-16"></a>Q01-16 Exact Type Helper (simple)
```ts
type Exact<T, Shape> = T & Record<Exclude<keyof T, keyof Shape>, never>;
function acceptsExact<T, S>(value: Exact<T, S>) { return value; }
```

### <a id="q01-17"></a>Q01-17 DeepReadonly Utility
```ts
type DeepReadonly<T> = T extends (...args: any) => any
  ? T
  : T extends Array<infer U>
    ? ReadonlyArray<DeepReadonly<U>>
    : T extends object
      ? { readonly [K in keyof T]: DeepReadonly<T[K]> }
      : T;
```

### <a id="q01-18"></a>Q01-18 Simplify Utility
```ts
type Simplify<T> = { [K in keyof T]: T[K] } & {};
```

### <a id="q01-19"></a>Q01-19 Fluent Builder (typed)
```ts
class Builder<T extends object> {
  constructor(private acc: T) {}
  set<K extends string, V>(key: K, value: V) { return new Builder(this.acc as T & Record<K, V>); }
  build() { return this.acc; }
}
const userBuilder = new Builder({} as const).set("name", "A").set("age", 1).build();
```

### <a id="q01-20"></a>Q01-20 Typed Routes Contract
```ts
type Routes = {
  "/login": { req: { email: string; password: string }; res: { token: string } };
  "/me": { req: void; res: { id: string; name: string } };
};
function callApi<Path extends keyof Routes>(path: Path, req: Routes[Path]["req"]): Promise<Routes[Path]["res"]> {
  // stub
  return Promise.resolve(null as any);
}
```

## 02) union and intersection

### <a id="q02-01"></a>Q02-01 Payment Union
```ts
type Payment = "card" | "upi" | "cod";
const isOnline = (p: Payment) => p !== "cod";
```

### <a id="q02-02"></a>Q02-02 LoginResult Union Objects
```ts
type LoginResult = { ok: true; token: string } | { ok: false; message: string };
function printLogin(r: LoginResult) { return r.ok ? r.token : r.message; }
```

### <a id="q02-03"></a>Q02-03 Intersection Profile
```ts
type BasicUser = { id: string; name: string };
type Address = { city: string; country: string };
type UserProfile = BasicUser & Address;
```

### <a id="q02-04"></a>Q02-04 Narrowing with "in"
```ts
type Session = { token: string; user: string } | { message: string };
function getToken(x: Session) { if ("token" in x) return x.token; return undefined; }
```

### <a id="q02-05"></a>Q02-05 typeof Narrowing
```ts
function normalize(v: string | number) { return typeof v === "number" ? v * 2 : v.toUpperCase(); }
```

### <a id="q02-06"></a>Q02-06 Filter Union Array Safely
```ts
function onlyNumbers(values: Array<number | string>): number[] {
  return values.filter((v): v is number => typeof v === "number");
}
```

### <a id="q02-07"></a>Q02-07 Free vs Paid Video
```ts
type Video = { kind: "free" } | { kind: "paid"; price: number };
const getPriceOrZero = (v: Video) => v.kind === "paid" ? Math.max(0, v.price) : 0;
```

### <a id="q02-08"></a>Q02-08 Event Union Handler
```ts
type Event =
  | { type: "page_view"; path: string }
  | { type: "add_to_cart"; sku: string; qty: number }
  | { type: "purchase"; orderId: string; total: number };
function handleEvent(e: Event) {
  switch (e.type) {
    case "page_view": return `Viewed ${e.path}`;
    case "add_to_cart": return `Added ${e.qty} of ${e.sku}`;
    case "purchase": return `Purchased orderId=${e.orderId}`;
  }
}
```

### <a id="q02-09"></a>Q02-09 Error Union to Message
```ts
type NetworkError = { kind: "network"; status: number };
type ValidationError = { kind: "validation"; field: string };
type AuthError = { kind: "auth" };
function toUserMessage(e: NetworkError | ValidationError | AuthError) {
  switch (e.kind) {
    case "network": return "Please try again later.";
    case "validation": return `Invalid ${e.field}`;
    case "auth": return "Please login.";
  }
}
```

### <a id="q02-10"></a>Q02-10 Exhaustive Check with never
```ts
function assertNever(x: never): never { throw new Error(`Unexpected value ${x}`); }
```

### <a id="q02-11"></a>Q02-11 Intersection of Capabilities
```ts
type Trackable = { track(): void };
type Serializable = { toJSON(): string };
const resource: Trackable & Serializable = {
  track() {},
  toJSON() { return "{}"; },
};
```

### <a id="q02-12"></a>Q02-12 Union Parameter vs Overload
```ts
function formatDate(input: string | Date) {
  const d = typeof input === "string" ? new Date(input) : input;
  if (Number.isNaN(d.getTime())) throw new Error("Invalid date");
  return d.toDateString();
}
```

### <a id="q02-13"></a>Q02-13 Type Predicate Guard
```ts
function isPaidVideo(v: { kind: string; price?: number }): v is { kind: "paid"; price: number } {
  return v.kind === "paid" && typeof v.price === "number";
}
```

### <a id="q02-14"></a>Q02-14 Compose Search Filters (intersection)
```ts
type ByPrice = { maxPrice?: number };
type ByCategory = { category?: string };
type ByRating = { minRating?: number };
type Filter = ByPrice & ByCategory & ByRating;
function applyFilters(items: Product[], f: Filter) {
  return items.filter(p => (
    (f.maxPrice === undefined || p.price <= f.maxPrice) &&
    (f.category === undefined || p.category === f.category) &&
    (f.minRating === undefined || p.rating >= f.minRating)
  ));
}
```

### <a id="q02-15"></a>Q02-15 UnionToIntersection Utility
```ts
type UnionToIntersection<U> = (U extends any ? (k: U) => void : never) extends (k: infer I) => void ? I : never;
```

### <a id="q02-16"></a>Q02-16 Simplify Intersection Output
```ts
type Simplify<T> = { [K in keyof T]: T[K] } & {};
```

### <a id="q02-17"></a>Q02-17 Order State Machine
```ts
type Status = "created"|"paid"|"packed"|"shipped"|"delivered"|"cancelled";
const transitions: Record<Status, Status[]> = {
  created: ["paid", "cancelled"],
  paid: ["packed", "cancelled"],
  packed: ["shipped", "cancelled"],
  shipped: ["delivered"],
  delivered: [],
  cancelled: [],
};
const transition = (s: Status, next: Status) => transitions[s].includes(next) ? next : (() => { throw new Error("Invalid transition"); })();
```

### <a id="q02-18"></a>Q02-18 ParseResult Union
```ts
type ParseResult = { ok: true; data: number } | { ok: false; error: string };
function parseNumber(input: string): ParseResult {
  const n = Number(input.trim());
  return Number.isFinite(n) ? { ok: true, data: n } : { ok: false, error: "Invalid number" };
}
```

### <a id="q02-19"></a>Q02-19 Handler Map from Union
```ts
type EventMap = { login: { userId: string }; logout: { userId: string }; purchase: { total: number } };
type EventKey = keyof EventMap;
const handlers: { [K in EventKey]: (payload: EventMap[K]) => void } = {
  login: p => console.log(p.userId),
  logout: p => console.log("bye", p.userId),
  purchase: p => console.log(p.total),
};
```

### <a id="q02-20"></a>Q02-20 Next Step Depends on Previous (simplified)
```ts
class Onboarding<S> {
  constructor(private state: S) {}
  signup(data: { email: string }) { return new Onboarding<S & { signedUp: true }>(this.state as any); }
  verifyEmail() { if ((this.state as any).signedUp) return new Onboarding<S & { verified: true }>(this.state as any); throw new Error("signup first"); }
}
```

## 03) optional and readonly

### <a id="q03-01"></a>Q03-01 Optional Profile Display
```ts
type Profile = { name: string; bio?: string; avatarUrl?: string };
const renderProfile = (p: Profile) => ({
  name: p.name,
  bio: p.bio ?? "No bio",
  avatar: p.avatarUrl ?? "https://placehold.co/80",
});
```

### <a id="q03-02"></a>Q03-02 Readonly SKU Update
```ts
type Product = { readonly sku: string; name: string; price: number };
function updateProduct(p: Product, patch: Partial<Product>): Product {
  const { sku, ...rest } = patch;
  return { ...p, ...rest };
}
```

### <a id="q03-03"></a>Q03-03 Optional Discount Apply
```ts
const applyDiscount = (price: number, discount?: number) => price - price * (discount ?? 0) / 100;
```

### <a id="q03-04"></a>Q03-04 Readonly Array Add
```ts
const addValue = (arr: readonly number[], v: number): number[] => [...arr, v];
```

### <a id="q03-05"></a>Q03-05 Optional Chaining Nested
```ts
const getCity = (user: { address?: { city?: string } }) => user.address?.city ?? "Unknown";
```

### <a id="q03-06"></a>Q03-06 Partial Update Merge
```ts
function merge<T>(base: T, patch: Partial<T>): T { return { ...base, ...patch }; }
```

### <a id="q03-07"></a>Q03-07 Optional Param Currency
```ts
const formatMoney = (amount: number, currency = "INR") => `${currency} ${amount}`;
```

### <a id="q03-08"></a>Q03-08 Readonly vs Object.freeze Demo
```ts
const compileReadonly: Readonly<Product> = { sku: "1", name: "A", price: 10 };
const runtimeFrozen = Object.freeze({ sku: "1", name: "A", price: 10 });
// compileReadonly.price = 20; // TS error
// runtimeFrozen.price = 20; // runtime throws in strict mode
```

### <a id="q03-09"></a>Q03-09 Immutable Cart Qty Update
```ts
type CartItem = { sku: string; qty: number };
const updateQty = (cart: readonly CartItem[], sku: string, qty: number): CartItem[] =>
  cart.map(item => item.sku === sku ? { ...item, qty } : item);
```

### <a id="q03-10"></a>Q03-10 DeepReadonly vs Readonly
```ts
type DeepReadonly<T> = T extends (...a: any) => any ? T :
  T extends object ? { readonly [K in keyof T]: DeepReadonly<T[K]> } : T;
type Shallow = Readonly<{ nested: { n: number } }>;
type Deep = DeepReadonly<{ nested: { n: number } }>;
```

### <a id="q03-11"></a>Q03-11 exactOptionalPropertyTypes scenario
```ts
// With exactOptionalPropertyTypes: {a?: string} means property may be missing; setting {a: undefined} is different.
```

### <a id="q03-12"></a>Q03-12 Patch Unknown Key Reject (type-level)
```ts
type ExactKeys<T, K extends keyof T> = { [P in K]: T[P] } & Record<Exclude<keyof any, K>, never>;
function updateUser(u: User, patch: Partial<User>) { return { ...u, ...patch }; }
```

### <a id="q03-13"></a>Q03-13 ReadonlyMap read
```ts
const rates: ReadonlyMap<string, number> = new Map([["USD", 1], ["INR", 83]]);
const getRate = (code: string) => rates.get(code) ?? 0;
```

### <a id="q03-14"></a>Q03-14 mergeDefaults<T>
```ts
const mergeDefaults = <T>(defaults: T, overrides?: Partial<T>): T => ({ ...defaults, ...(overrides ?? {}) });
```

### <a id="q03-15"></a>Q03-15 DeepPartial Utility
```ts
type DeepPartial<T> = T extends object ? { [K in keyof T]?: DeepPartial<T[K]> } : T;
```

### <a id="q03-16"></a>Q03-16 Immutable Tree Update
```ts
type Node = { id: string; label: string; children?: Node[] };
const updateLabel = (node: Node, id: string, label: string): Node => ({
  ...node,
  children: node.children?.map(c => c.id === id ? { ...c, label } : updateLabel(c, id, label))
});
```

### <a id="q03-17"></a>Q03-17 Readonly State Reducer
```ts
type Todo = { id: string; text: string; done: boolean };
type Action = { type: "add"; text: string } | { type: "toggle"; id: string } | { type: "remove"; id: string };
const reducer = (state: readonly Todo[], action: Action): readonly Todo[] => {
  switch (action.type) {
    case "add": return [...state, { id: crypto.randomUUID(), text: action.text, done: false }];
    case "toggle": return state.map(t => t.id === action.id ? { ...t, done: !t.done } : t);
    case "remove": return state.filter(t => t.id !== action.id);
    default: return state;
  }
};
```

### <a id="q03-18"></a>Q03-18 Branded ID
```ts
type UserId = string & { __brand: "UserId" };
const makeUserId = (s: string): UserId => {
  if (!s.trim()) throw new Error("id required");
  return s as UserId;
};
```

### <a id="q03-19"></a>Q03-19 Readonly constructor param
```ts
class Book { constructor(public readonly id: string, public title: string) {} }
```

### <a id="q03-20"></a>Q03-20 Persistent List Update
```ts
const removeById = (list: readonly { id: string }[], id: string) => list.filter(x => x.id !== id);
```

## 04) generics

### <a id="q04-01"></a>Q04-01 identity<T>
```ts
const identity = <T>(x: T): T => x;
```

### <a id="q04-02"></a>Q04-02 first<T>
```ts
const first = <T>(arr: T[]): T | undefined => arr[0];
```

### <a id="q04-03"></a>Q04-03 wrap<T>
```ts
const wrap = <T>(value: T) => ({ value });
```

### <a id="q04-04"></a>Q04-04 pair<T,U>
```ts
const pair = <T, U>(a: T, b: U): [T, U] => [a, b];
```

### <a id="q04-05"></a>Q04-05 mapArray<T,R>
```ts
const mapArray = <T, R>(arr: T[], fn: (v: T) => R): R[] => arr.map(fn);
```

### <a id="q04-06"></a>Q04-06 ApiResponse<T>
```ts
type ApiResponse<T> = { ok: true; data: T } | { ok: false; error: string };
const mapResponse = <T, R>(r: ApiResponse<T>, fn: (t: T) => R): ApiResponse<R> => r.ok ? { ok: true, data: fn(r.data) } : r;
```

### <a id="q04-07"></a>Q04-07 Box<T> class
```ts
class Box<T> {
  constructor(private value: T) {}
  get(): T { return this.value; }
  set(v: T) { this.value = v; }
}
```

### <a id="q04-08"></a>Q04-08 Constraint T extends {id:string}
```ts
const indexById = <T extends { id: string }>(list: T[]): Record<string, T> =>
  list.reduce((acc, item) => ({ ...acc, [item.id]: item }), {} as Record<string, T>);
```

### <a id="q04-09"></a>Q04-09 pluck<T,K>
```ts
const pluck = <T, K extends keyof T>(obj: T, key: K): T[K] => obj[key];
```

### <a id="q04-10"></a>Q04-10 sortByKey<T,K>
```ts
function sortByKey<T, K extends keyof T>(arr: T[], key: K): T[] {
  return [...arr].sort((a, b) => {
    const va = a[key]; const vb = b[key];
    if (va === vb) return 0;
    return (va as any) > (vb as any) ? 1 : -1;
  });
}
```

### <a id="q04-11"></a>Q04-11 Generic default type
```ts
type Result<T = string, E = Error> = { ok: true; data: T } | { ok: false; error: E };
```

### <a id="q04-12"></a>Q04-12 parseJson<T>
```ts
function parseJson<T>(s: string, guard: (u: unknown) => u is T): Result<T, string> {
  try {
    const val = JSON.parse(s);
    return guard(val) ? { ok: true, data: val } : { ok: false, error: "Invalid shape" };
  } catch (e) {
    return { ok: false, error: "Invalid JSON" };
  }
}
```

### <a id="q04-13"></a>Q04-13 freeze<T>
```ts
const freeze = <T>(obj: T): Readonly<T> => Object.freeze(obj);
```

### <a id="q04-14"></a>Q04-14 timeout<T>
```ts
function timeout<T>(p: Promise<T>, ms: number): Promise<T> {
  return Promise.race([
    p,
    new Promise<never>((_, rej) => setTimeout(() => rej(new Error("Timeout")), ms)),
  ]);
}
```

### <a id="q04-15"></a>Q04-15 PromiseReturn<T>
```ts
type PromiseReturn<T> = T extends Promise<infer U> ? U : T;
```

### <a id="q04-16"></a>Q04-16 FuncReturn<T>
```ts
type FuncReturn<T> = T extends (...a: any[]) => infer R ? R : never;
```

### <a id="q04-17"></a>Q04-17 Typed Event Emitter
```ts
type EventMap = { login: { userId: string }; logout: void };
class Emitter<Events extends Record<string, any>> {
  private listeners: { [K in keyof Events]?: Array<(payload: Events[K]) => void> } = {};
  on<K extends keyof Events>(event: K, cb: (payload: Events[K]) => void) {
    (this.listeners[event] ||= []).push(cb);
  }
  emit<K extends keyof Events>(event: K, payload: Events[K]) {
    this.listeners[event]?.forEach(cb => cb(payload));
  }
}
```

### <a id="q04-18"></a>Q04-18 Fluent Builder Accumulating Keys
```ts
class AccBuilder<T extends object> {
  constructor(private acc: T) {}
  set<K extends string, V>(k: K, v: V) { return new AccBuilder<T & Record<K, V>>({ ...this.acc, [k]: v } as any); }
  build() { return this.acc; }
}
```

### <a id="q04-19"></a>Q04-19 Primitive Schema to Type
```ts
type Schema = { kind: "string" } | { kind: "number" };
type SchemaToType<S extends Schema> = S extends { kind: "string" } ? string : number;
```

### <a id="q04-20"></a>Q04-20 decode<T>
```ts
function decode<T>(value: unknown, guard: (v: unknown) => v is T): T {
  if (guard(value)) return value;
  throw new Error("Invalid value");
}
```

## 05) enum vs string literal union

### <a id="q05-01"></a>Q05-01 Status union
```ts
type Status = "draft" | "published";
const canEdit = (s: Status) => s === "draft";
```

### <a id="q05-02"></a>Q05-02 Same as enum
```ts
enum StatusEnum { Draft = "draft", Published = "published" }
```

### <a id="q05-03"></a>Q05-03 Switch label
```ts
const label = (s: Status) => ({ draft: "Draft", published: "Published" }[s]);
```

### <a id="q05-04"></a>Q05-04 Record label map
```ts
const labelMap: Record<Status, string> = { draft: "Draft", published: "Published" };
```

### <a id="q05-05"></a>Q05-05 Prevent invalid string
```ts
const status: Status = "draft"; // "drafft" would be a TS error
```

### <a id="q05-06"></a>Q05-06 const object + as const
```ts
const STATUS = { draft: "draft", published: "published" } as const;
type StatusFromConst = typeof STATUS[keyof typeof STATUS];
```

### <a id="q05-07"></a>Q05-07 parseStatus
```ts
function parseStatus(input: unknown): Status {
  if (input === "draft" || input === "published") return input;
  throw new Error("Invalid status");
}
```

### <a id="q05-08"></a>Q05-08 Exhaustive transitions
```ts
const next = (s: Status) => s === "draft" ? "published" : (() => { throw new Error("Cannot revert"); })();
```

### <a id="q05-09"></a>Q05-09 Enum compile footprint explanation
```ts
// String enums emit an object at runtime; unions erase to strings.
```

### <a id="q05-10"></a>Q05-10 Reverse mapping demo
```ts
enum Numeric { A, B }
const nameOfA = Numeric[0]; // "A"
```

### <a id="q05-11"></a>Q05-11 Tree-shaking discussion
```ts
// Literal unions are tree-shakeable; enums keep emitted object unless const enum.
```

### <a id="q05-12"></a>Q05-12 Serialize/deserialize
```ts
localStorage.setItem("status", "draft");
const statusFromStorage = parseStatus(localStorage.getItem("status"));
```

### <a id="q05-13"></a>Q05-13 assertDefined utility
```ts
function assertDefined<T>(value: T): asserts value is NonNullable<T> {
  if (value === null || value === undefined) throw new Error("Missing value");
}
```

### <a id="q05-14"></a>Q05-14 NonNullable usage
```ts
type NN = NonNullable<string | null | undefined>; // string
```

### <a id="q05-15"></a>Q05-15 Optional keys mapping
```ts
type OptionalKeys<T> = { [K in keyof T]-?: undefined extends T[K] ? K : never }[keyof T];
```

### <a id="q05-16"></a>Q05-16 Strict config schema runtime
```ts
function loadConfig(raw: unknown): { api: string; token: string } {
  if (
    typeof raw === "object" && raw !== null &&
    "api" in raw && "token" in raw &&
    typeof (raw as any).api === "string" && typeof (raw as any).token === "string"
  ) return raw as any;
  throw new Error("Invalid config");
}
```

### <a id="q05-17"></a>Q05-17 Builder ensures required fields before build
```ts
class ConfigBuilder<S extends { api?: string; token?: string }> {
  constructor(private value: S) {}
  setApi(api: string) { return new ConfigBuilder<S & { api: string }>({ ...this.value, api } as any); }
  setToken(token: string) { return new ConfigBuilder<S & { token: string }>({ ...this.value, token } as any); }
  build(this: ConfigBuilder<{ api: string; token: string }>) { return this.value; }
}
```

### <a id="q05-18"></a>Q05-18 Template literal type events
```ts
type EventName = `user:${"created" | "deleted"}`;
const onEvent = (name: EventName) => name;
```

### <a id="q05-19"></a>Q05-19 i18n labels mapping
```ts
type StatusLabel = Record<Status, { en: string; bn: string }>;
const labels: StatusLabel = { draft: { en: "Draft", bn: "খসড়া" }, published: { en: "Published", bn: "প্রকাশিত" } };
```

### <a id="q05-20"></a>Q05-20 Endpoint by status
```ts
const endpoint = (status: Status) => {
  switch (status) {
    case "draft": return "/draft";
    case "published": return "/published";
    default: assertNever(status);
  }
};
```

## 06) any unknown never

### <a id="q06-01"></a>Q06-01 unknown to string
```ts
function normalizeName(x: unknown): string {
  if (typeof x === "string") {
    const trimmed = x.trim();
    if (!trimmed) throw new Error("empty name");
    return trimmed;
  }
  throw new Error("name must be string");
}
```

### <a id="q06-02"></a>Q06-02 Replace any with unknown
```ts
function logValue(v: unknown) {
  if (typeof v === "string") console.log(v.toUpperCase());
  else if (typeof v === "number") console.log(v.toFixed(2));
}
```

### <a id="q06-03"></a>Q06-03 safeJsonParse returns unknown
```ts
const safeJsonParse = (s: string): unknown | null => {
  try { return JSON.parse(s); } catch { return null; }
};
```

### <a id="q06-04"></a>Q06-04 isNumberArray guard
```ts
const isNumberArray = (u: unknown): u is number[] => Array.isArray(u) && u.every(n => typeof n === "number");
```

### <a id="q06-05"></a>Q06-05 fail(msg): never
```ts
function fail(msg: string): never { throw new Error(msg); }
```

### <a id="q06-06"></a>Q06-06 assertNever helper
```ts
function assertNever(x: never): never { throw new Error(`Unexpected: ${x}`); }
```

### <a id="q06-07"></a>Q06-07 catch error safely
```ts
try { /* ... */ }
catch (e: unknown) {
  if (e instanceof Error) console.error(e.message);
  else console.error(String(e));
}
```

### <a id="q06-08"></a>Q06-08 validate User shape at runtime
```ts
type UserShape = { id: string; name: string };
const isUser = (u: unknown): u is UserShape =>
  typeof u === "object" && u !== null && typeof (u as any).id === "string" && typeof (u as any).name === "string";
```

### <a id="q06-09"></a>Q06-09 decode API response unknown -> User
```ts
const decodeUser = (u: unknown): UserShape => {
  if (isUser(u)) return u;
  throw new Error("Invalid user");
};
```

### <a id="q06-10"></a>Q06-10 Error union mapping
```ts
type AuthError = { kind: "auth" };
type NotFoundError = { kind: "not_found" };
type AppError = AuthError | NotFoundError | { kind: "unknown" };
const toStatus = (e: AppError) => ({ auth: 401, not_found: 404, unknown: 500 }[e.kind]);
```

### <a id="q06-11"></a>Q06-11 Remove unsafe casting
```ts
function safeProcess(u: unknown) {
  if (!isUser(u)) throw new Error("Not a user");
  return u.name.toUpperCase();
}
```

### <a id="q06-12"></a>Q06-12 Conditional returns never on invalid
```ts
type NonString<T> = T extends string ? never : T;
```

### <a id="q06-13"></a>Q06-13 Parse query params unknown
```ts
const parseLimit = (x: unknown): number => {
  const n = typeof x === "string" ? Number(x) : typeof x === "number" ? x : NaN;
  if (!Number.isInteger(n) || n < 1 || n > 100) throw new Error("limit 1..100");
  return n;
};
```

### <a id="q06-14"></a>Q06-14 Narrow unknown event payload by topic
```ts
type Topics = "user.created" | "order.placed";
function validatePayload(topic: Topics, payload: unknown) {
  if (topic === "user.created" && isUser(payload)) return payload;
  if (topic === "order.placed" && typeof payload === "object" && payload !== null && "orderId" in payload) return payload;
  throw new Error("Invalid payload");
}
```

### <a id="q06-15"></a>Q06-15 Mini validator combinators
```ts
const isString = (u: unknown): u is string => typeof u === "string";
const isNumber = (u: unknown): u is number => typeof u === "number" && !Number.isNaN(u);
const isObject = (u: unknown): u is Record<string, unknown> => typeof u === "object" && u !== null && !Array.isArray(u);
const hasKey = <K extends string>(k: K) => (u: unknown): u is Record<K, unknown> => isObject(u) && k in u;
```

### <a id="q06-16"></a>Q06-16 Result-based errors instead of throw
```ts
type Result<T, E = string> = { ok: true; data: T } | { ok: false; error: E };
const parseNumberSafe = (s: string): Result<number> => {
  const n = Number(s);
  return Number.isFinite(n) ? { ok: true, data: n } : { ok: false, error: "NaN" };
};
```

### <a id="q06-17"></a>Q06-17 assertDefined
```ts
function assertDefined<T>(value: T): asserts value is NonNullable<T> {
  if (value === null || value === undefined) throw new Error("Value required");
}
```

### <a id="q06-18"></a>Q06-18 Safe deep get
```ts
const get = (u: unknown, path: string[]): unknown => {
  let curr: any = u;
  for (const key of path) {
    if (curr && typeof curr === "object" && key in curr) curr = curr[key];
    else return undefined;
  }
  return curr;
};
```

### <a id="q06-19"></a>Q06-19 Unknown payload registry typed
```ts
type Registry = { "user.created": UserShape; "order.placed": { orderId: string } };
function parseTopic<T extends keyof Registry>(topic: T, payload: unknown): Registry[T] {
  if (topic === "user.created" && isUser(payload)) return payload;
  if (topic === "order.placed" && typeof payload === "object" && payload !== null && typeof (payload as any).orderId === "string") return payload as any;
  throw new Error("Bad payload");
}
```

### <a id="q06-20"></a>Q06-20 Never unreachable branch demonstration
```ts
type Mode = "a" | "b";
function handle(mode: Mode) {
  switch (mode) {
    case "a": return "A";
    case "b": return "B";
    default: assertNever(mode); // new member forces compile error
  }
}
```

## 07) null undefined strict mode

### <a id="q07-01"></a>Q07-01 Nullable username display
```ts
const displayName = (name: string | null) => name ?? "Guest";
```

### <a id="q07-02"></a>Q07-02 Optional param vs string|undefined
```ts
function greetOptional(name?: string) { return name ?? "Guest"; }
function greetUnion(name: string | undefined) { return name ?? "Guest"; }
```

### <a id="q07-03"></a>Q07-03 strictNullChecks demo
```ts
let s: string; // must assign before use when strictNullChecks on
s = "hi"; // s = null; // TS error
let maybe: string | null = null; // allowed
```

### <a id="q07-04"></a>Q07-04 Non-null assertion risk
```ts
const user: { name?: string } | undefined = undefined;
const safe = user?.name ?? "Unknown"; // avoid user!.name
```

### <a id="q07-05"></a>Q07-05 Optional chaining safe access
```ts
const timeout = (config?: { api?: { timeoutMs?: number } }) => config?.api?.timeoutMs ?? 3000;
```

### <a id="q07-06"></a>Q07-06 ?? vs ||
```ts
0 ?? 10; // 0
0 || 10; // 10
```

### <a id="q07-07"></a>Q07-07 Default param
```ts
function f(x = 10) { return x; }
function g(x?: number) { return x ?? 10; }
```

### <a id="q07-08"></a>Q07-08 parse optional to number
```ts
const parsePage = (x: string | undefined): number => {
  const n = x ? Number(x) : 1;
  return Number.isInteger(n) && n > 0 ? n : 1;
};
```

### <a id="q07-09"></a>Q07-09 Filter undefined with predicate
```ts
const compact = (vals: Array<string | undefined>): string[] => vals.filter((v): v is string => v !== undefined);
```

### <a id="q07-10"></a>Q07-10 Maybe<T> helper
```ts
type Maybe<T> = T | null | undefined;
const unwrapMaybe = <T>(v: Maybe<T>, fallback: T) => v ?? fallback;
```

### <a id="q07-11"></a>Q07-11 Form value normalize
```ts
const normalizeInput = (v: string | null) => v ?? "";
```

### <a id="q07-12"></a>Q07-12 Date format safe
```ts
const formatDate = (d: Date | null) => d ? d.toISOString() : "N/A";
```

### <a id="q07-13"></a>Q07-13 Config required env vars
```ts
const getEnv = (name: string) => {
  const val = process.env[name];
  if (!val) throw new Error(`${name} missing`);
  return val;
};
```

### <a id="q07-14"></a>Q07-14 Strict function types (variance)
```ts
let a: (x: string) => void;
let b: (x: string | number) => void;
// a = b; // error under strictFunctionTypes (b expects wider input)
```

### <a id="q07-15"></a>Q07-15 exactOptionalPropertyTypes behavior
```ts
type Foo = { a?: string };
const foo1: Foo = {};      // ok
// const foo2: Foo = { a: undefined }; // error when exactOptionalPropertyTypes=true
```

### <a id="q07-16"></a>Q07-16 assertDefined utility
```ts
function assertDefined<T>(v: T): asserts v is NonNullable<T> { if (v == null) throw new Error("required"); }
```

### <a id="q07-17"></a>Q07-17 NonNullable usage
```ts
type NN = NonNullable<string | null | undefined>; // string
```

### <a id="q07-18"></a>Q07-18 Optional keys mapping
```ts
type OptionalKeys<T> = { [K in keyof T]-?: undefined extends T[K] ? K : never }[keyof T];
```

### <a id="q07-19"></a>Q07-19 Strict config schema runtime
```ts
const loadConfigStrict = (u: unknown): { apiBaseUrl: string; token: string } => {
  if (typeof u === "object" && u !== null && typeof (u as any).apiBaseUrl === "string" && typeof (u as any).token === "string") return u as any;
  throw new Error("Invalid config");
};
```

### <a id="q07-20"></a>Q07-20 Builder ensures required fields before build
```ts
class ApiConfigBuilder<S extends { url?: string }> {
  constructor(private value: S) {}
  setUrl(url: string) { return new ApiConfigBuilder<S & { url: string }>({ ...this.value, url } as any); }
  build(this: ApiConfigBuilder<{ url: string }>) { return this.value; }
}
```

## 08) classes access modifiers getter setter

### <a id="q08-01"></a>Q08-01 BankAccount encapsulation
```ts
class BankAccount {
  private balance = 0;
  constructor(private readonly id: string) {}
  deposit(amount: number) { if (amount <= 0) throw new Error("Invalid"); this.balance += amount; }
  withdraw(amount: number) { if (amount > this.balance) throw new Error("Insufficient"); this.balance -= amount; }
  getBalance() { return this.balance; }
}
```

### <a id="q08-02"></a>Q08-02 readonly accountId
```ts
class Account { constructor(public readonly id: string, public owner: string) {} }
```

### <a id="q08-03"></a>Q08-03 Getter masked id
```ts
class MaskedId {
  constructor(private id: string) {}
  get masked() { return this.id.slice(-4).padStart(this.id.length, "*"); }
}
```

### <a id="q08-04"></a>Q08-04 Setter pin validation
```ts
class PinHolder {
  private _pin = "0000";
  set pin(value: string) { if (!/^\d{4}$/.test(value)) throw new Error("Pin must be 4 digits"); this._pin = value; }
  get pin() { return this._pin; }
}
```

### <a id="q08-05"></a>Q08-05 public/private access demo
```ts
class Demo { private secret = 42; }
// new Demo().secret; // TS error
```

### <a id="q08-06"></a>Q08-06 Simple inheritance
```ts
class Vehicle { move() { return "moving"; } }
class Car extends Vehicle { honk() { return "beep"; } }
```

### <a id="q08-07"></a>Q08-07 protected usage
```ts
class Base {
  protected log(msg: string) { console.log(msg); }
}
class Child extends Base { action() { this.log("child"); } }
```

### <a id="q08-08"></a>Q08-08 static counter
```ts
class Session {
  static count = 0;
  constructor() { Session.count++; }
  close() { Session.count--; }
}
```

### <a id="q08-09"></a>Q08-09 abstract gateway
```ts
abstract class PaymentGateway { abstract pay(amount: number): Promise<string>; }
class MockGateway extends PaymentGateway { pay(amount: number) { if (amount < 0) throw new Error("Invalid"); return Promise.resolve("paid" + amount); } }
```

### <a id="q08-10"></a>Q08-10 implements Logger interface
```ts
interface Logger { log(msg: string): void }
class ConsoleLogger implements Logger { log(msg: string) { console.log(msg); } }
```

### <a id="q08-11"></a>Q08-11 Prevent invalid state (setter)
```ts
class Product { constructor(private _price: number) {}
  get price() { return this._price; }
  set price(p: number) { if (p < 0 || Number.isNaN(p)) throw new Error("Invalid"); this._price = p; }
}
```

### <a id="q08-12"></a>Q08-12 Composition over inheritance
```ts
class PriceCalculator { total(items: { price: number; qty: number }[]) { return items.reduce((s, i) => s + i.price * i.qty, 0); } }
class OrderService {
  constructor(private calc: PriceCalculator) {}
  checkout(items: { price: number; qty: number }[]) { return this.calc.total(items); }
}
```

### <a id="q08-13"></a>Q08-13 Simple role check method
```ts
type Role = "ADMIN" | "USER";
class AdminPanel {
  constructor(private role: Role) {}
  deleteUser(id: string) { if (this.role !== "ADMIN") throw new Error("Forbidden"); return `Deleted ${id}`; }
}
```

### <a id="q08-14"></a>Q08-14 Factory method ensures invariants
```ts
class UserEntity {
  private constructor(public readonly id: string, public readonly name: string) {}
  static create(id: string, name: string) {
    if (!id || !name) throw new Error("invalid");
    return new UserEntity(id, name);
  }
}
```

### <a id="q08-15"></a>Q08-15 Immutable class
```ts
class Money {
  constructor(private readonly amount: number, private readonly currency: string) {}
  add(other: Money) { if (other.currency !== this.currency) throw new Error("Currency mismatch"); return new Money(this.amount + other.amount, this.currency); }
  value() { return `${this.currency} ${this.amount}`; }
}
```

### <a id="q08-16"></a>Q08-16 Builder with private constructor
```ts
class Order {
  private constructor(public readonly items: { sku: string; qty: number }[]) {}
  static builder() { return new OrderBuilder([]); }
}
class OrderBuilder {
  constructor(private items: { sku: string; qty: number }[]) {}
  add(sku: string, qty: number) { if (qty <= 0) throw new Error("qty>0"); return new OrderBuilder([...this.items, { sku, qty }]); }
  build() { if (!this.items.length) throw new Error("empty"); return new Order(this.items); }
}
```

### <a id="q08-17"></a>Q08-17 Custom error classes
```ts
class InsufficientBalanceError extends Error { constructor() { super("Insufficient balance"); } }
```

### <a id="q08-18"></a>Q08-18 Strategy pattern shipping
```ts
interface ShippingStrategy { cost(weight: number): number }
class Standard implements ShippingStrategy { cost(w: number) { return w * 1.1; } }
class Express implements ShippingStrategy { cost(w: number) { return 10 + w * 1.5; } }
const price = (strategy: ShippingStrategy, weight: number) => strategy.cost(weight);
```

### <a id="q08-19"></a>Q08-19 Lazy computed getter cache
```ts
class Cached {
  private _value?: number;
  get value() { return this._value ??= this.expensive(); }
  private expensive() { return Math.random(); }
}
```

### <a id="q08-20"></a>Q08-20 Testability: mock dependency
```ts
interface Clock { now(): number }
class RealClock implements Clock { now() { return Date.now(); } }
class Service { constructor(private clock: Clock) {} getTimestamp() { return this.clock.now(); } }
class FakeClock implements Clock { constructor(private n: number) {} now() { return this.n; } }
```

## 09) async await promise

### <a id="q09-01"></a>Q09-01 delay(ms)
```ts
const delay = (ms: number) => new Promise<void>(res => setTimeout(res, Math.max(0, ms)));
```

### <a id="q09-02"></a>Q09-02 try/catch async error
```ts
async function safeCall<T>(fn: () => Promise<T>, fallback: T): Promise<T> {
  try { return await fn(); } catch { return fallback; }
}
```

### <a id="q09-03"></a>Q09-03 Sequential calls
```ts
async function fetchUserWithOrders(fetchUser: () => Promise<{id:string}>, fetchOrders: (id:string)=>Promise<any[]>) {
  const user = await fetchUser();
  const orders = await fetchOrders(user.id);
  return { user, orders };
}
```

### <a id="q09-04"></a>Q09-04 Parallel calls with Promise.all
```ts
async function sumAll(promises: Promise<number>[]) {
  const results = await Promise.all(promises);
  return results.reduce((a, b) => a + b, 0);
}
```

### <a id="q09-05"></a>Q09-05 Promise.race timeout
```ts
const withTimeout = <T>(p: Promise<T>, ms: number) => Promise.race([
  p,
  new Promise<never>((_, rej) => setTimeout(() => rej(new Error("Timeout")), ms)),
]);
```

### <a id="q09-06"></a>Q09-06 Retry 3 times
```ts
async function retry<T>(fn: () => Promise<T>, attempts = 3) {
  let delayMs = 200;
  for (let i = 1; i <= attempts; i++) {
    try { return await fn(); }
    catch (e) { if (i === attempts) throw e; await delay(delayMs); delayMs *= 2; }
  }
}
```

### <a id="q09-07"></a>Q09-07 finally loading
```ts
async function loadWithFlag<T>(fn: () => Promise<T>) {
  let loading = true;
  try { return await fn(); }
  finally { loading = false; }
}
```

### <a id="q09-08"></a>Q09-08 Promise.allSettled report
```ts
async function report(tasks: Promise<any>[]) {
  const settled = await Promise.allSettled(tasks);
  return settled.reduce((acc, r) => {
    r.status === "fulfilled" ? acc.ok++ : acc.fail++;
    return acc;
  }, { ok: 0, fail: 0 });
}
```

### <a id="q09-09"></a>Q09-09 Concurrency limit (max 3)
```ts
async function runLimited<T>(jobs: (() => Promise<T>)[], limit = 3) {
  const results: T[] = [];
  let i = 0;
  const workers = Array.from({ length: Math.min(limit, jobs.length) }, async () => {
    while (i < jobs.length) {
      const jobIndex = i++;
      results[jobIndex] = await jobs[jobIndex]();
    }
  });
  await Promise.all(workers);
  return results;
}
```

### <a id="q09-10"></a>Q09-10 Cancel pattern simulation
```ts
const makeCancelToken = () => {
  let cancelled = false;
  return {
    cancel() { cancelled = true; },
    isCancelled() { return cancelled; },
  };
};
```

### <a id="q09-11"></a>Q09-11 FIFO async job queue
```ts
class AsyncQueue<T> {
  private queue: (() => Promise<T>)[] = [];
  enqueue(job: () => Promise<T>) { this.queue.push(job); }
  async start() { const results: T[] = []; for (const job of this.queue) results.push(await job()); return results; }
}
```

### <a id="q09-12"></a>Q09-12 Debounced async search
```ts
function debounceAsync<T extends (...a: any[]) => Promise<any>>(fn: T, ms = 300) {
  let timer: any; let lastReject: ((reason?: any) => void) | null = null;
  return (...args: Parameters<T>): Promise<ReturnType<T>> => {
    if (lastReject) lastReject(new Error("cancelled"));
    clearTimeout(timer);
    return new Promise((resolve, reject) => {
      lastReject = reject;
      timer = setTimeout(() => fn(...args).then(resolve, reject), ms);
    });
  };
}
```

### <a id="q09-13"></a>Q09-13 Async memoization cache
```ts
function memoAsync<T>(fn: (key: string) => Promise<T>) {
  const cache = new Map<string, Promise<T>>();
  return (key: string) => {
    if (!cache.has(key)) cache.set(key, fn(key).catch(err => { cache.delete(key); throw err; }));
    return cache.get(key)!;
  };
}
```

### <a id="q09-14"></a>Q09-14 Circuit breaker mini
```ts
function circuit<T>(fn: () => Promise<T>, maxFailures = 3, openMs = 5000) {
  let failures = 0; let openUntil = 0;
  return async () => {
    const now = Date.now();
    if (now < openUntil) throw new Error("Circuit open");
    try {
      const result = await fn();
      failures = 0; return result;
    } catch (e) {
      failures++;
      if (failures >= maxFailures) openUntil = now + openMs;
      throw e;
    }
  };
}
```

### <a id="q09-15"></a>Q09-15 Return Result instead of throwing
```ts
type AsyncResult<T> = Promise<{ ok: true; data: T } | { ok: false; error: unknown }>;
const tryAsync = async <T>(fn: () => Promise<T>): AsyncResult<T> => {
  try { return { ok: true, data: await fn() }; }
  catch (error) { return { ok: false, error }; }
};
```

### <a id="q09-16"></a>Q09-16 Async pipeline
```ts
async function pipeAsync<T>(value: T, steps: Array<(v: any) => Promise<any>>): Promise<any> {
  let acc: any = value;
  for (const step of steps) acc = await step(acc);
  return acc;
}
```

### <a id="q09-17"></a>Q09-17 Idempotent request dedupe
```ts
function requestOnce<T>(key: string, fn: () => Promise<T>) {
  const inflight = new Map<string, Promise<T>>();
  return async () => {
    if (!inflight.has(key)) inflight.set(key, fn().finally(() => inflight.delete(key)));
    return inflight.get(key)!;
  };
}
```

### <a id="q09-18"></a>Q09-18 Backpressure simulation
```ts
async function processWithBackpressure<T>(jobs: T[], handler: (job: T) => Promise<void>, maxQueue = 5) {
  const queue: Promise<void>[] = [];
  for (const job of jobs) {
    const task = handler(job);
    queue.push(task);
    if (queue.length >= maxQueue) await queue.shift();
  }
  await Promise.all(queue);
}
```

### <a id="q09-19"></a>Q09-19 DLQ for failed jobs
```ts
type DeadLetter<T> = { job: T; reason: unknown; at: number };
async function runWithDLQ<T>(jobs: T[], handler: (job: T) => Promise<void>) {
  const dead: DeadLetter<T>[] = [];
  for (const job of jobs) {
    try { await handler(job); }
    catch (e) { dead.push({ job, reason: e, at: Date.now() }); }
  }
  return dead;
}
```

### <a id="q09-20"></a>Q09-20 CorrelationId tracing
```ts
async function withCorrelation<T>(id: string, fn: (ctx: { correlationId: string }) => Promise<T>) {
  return fn({ correlationId: id });
}
```

## 10) inventory literal unions

### <a id="q10-01"></a>Q10-01 Basic dispense guard
```ts
type StockItem = { sku: string; qty: number };
function dispense(item: StockItem, qty: number) {
  if (qty <= 0) throw new Error("Invalid qty");
  if (item.qty < qty) throw new Error("Insufficient");
  return { ...item, qty: item.qty - qty };
}
```

### <a id="q10-02"></a>Q10-02 Category union
```ts
type Category = "food" | "electronics" | "fashion";
const filterByCategory = (items: StockItem[] & { category?: Category }[], cat: Category) =>
  items.filter(i => i.category === cat);
```

### <a id="q10-03"></a>Q10-03 StockStatus computed
```ts
type StockStatus = "in_stock" | "low" | "out";
const statusFromQty = (qty: number): StockStatus => qty <= 0 ? "out" : qty < 5 ? "low" : "in_stock";
```

### <a id="q10-04"></a>Q10-04 Add item unique sku
```ts
const addItem = (items: StockItem[], item: StockItem) => {
  if (items.some(i => i.sku === item.sku)) throw new Error("duplicate sku");
  return [...items, item];
};
```

### <a id="q10-05"></a>Q10-05 Search by keyword
```ts
const search = (items: { name: string }[], keyword: string) => keyword
  ? items.filter(i => i.name.toLowerCase().includes(keyword.toLowerCase()))
  : items;
```

### <a id="q10-06"></a>Q10-06 Sort by qty desc
```ts
const sortByQtyDesc = (items: StockItem[]) => [...items].sort((a, b) => b.qty - a.qty);
```

### <a id="q10-07"></a>Q10-07 Restock validate positive
```ts
const restock = (items: StockItem[], sku: string, addQty: number) => {
  if (addQty <= 0) throw new Error("addQty>0");
  return items.map(i => i.sku === sku ? { ...i, qty: i.qty + addQty } : i);
};
```

### <a id="q10-08"></a>Q10-08 Batch dispense (transaction)
```ts
type Dispense = { sku: string; qty: number };
const batchDispense = (items: StockItem[], reqs: Dispense[]) => {
  for (const r of reqs) {
    const item = items.find(i => i.sku === r.sku);
    if (!item || item.qty < r.qty) throw new Error("insufficient");
  }
  return items.map(i => {
    const r = reqs.find(x => x.sku === i.sku);
    return r ? { ...i, qty: i.qty - r.qty } : i;
  });
};
```

### <a id="q10-09"></a>Q10-09 Result-based operations
```ts
type Result<T> = { ok: true; data: T } | { ok: false; error: string };
const addOp = (items: StockItem[], item: StockItem): Result<StockItem[]> =>
  items.some(i => i.sku === item.sku) ? { ok: false, error: "duplicate" } : { ok: true, data: [...items, item] };
```

### <a id="q10-10"></a>Q10-10 Group by category
```ts
const groupByCategory = (items: { category: Category }[]) => items.reduce<Record<Category, typeof items>>( (acc, item) => {
  acc[item.category] = acc[item.category] ?? [];
  acc[item.category].push(item);
  return acc;
}, { food: [], electronics: [], fashion: [] });
```

### <a id="q10-11"></a>Q10-11 Reorder tasks
```ts
const reorderTasks = (items: { sku: string; qty: number; reorderThreshold: number }[]) =>
  items.filter(i => i.qty < i.reorderThreshold).map(i => ({ sku: i.sku, needed: i.reorderThreshold - i.qty }));
```

### <a id="q10-12"></a>Q10-12 Readonly sku in model
```ts
type Item = { readonly sku: string; qty: number };
```

### <a id="q10-13"></a>Q10-13 Audit log
```ts
type Audit = { action: string; sku: string; at: number };
const logAction = (logs: Audit[], action: string, sku: string): Audit[] => [...logs, { action, sku, at: Date.now() }];
```

### <a id="q10-14"></a>Q10-14 Queue to prevent race
```ts
class OperationQueue {
  private chain = Promise.resolve();
  enqueue<T>(task: () => Promise<T>) { return this.chain = this.chain.then(task, task); }
}
```

### <a id="q10-15"></a>Q10-15 Lifecycle state machine
```ts
type Life = "active" | "discontinued" | "archived";
const lifeTransitions: Record<Life, Life[]> = {
  active: ["discontinued"],
  discontinued: ["archived"],
  archived: [],
};
const transitionLife = (s: Life, next: Life) => {
  if (!lifeTransitions[s].includes(next)) throw new Error("invalid");
  return next;
};
```

### <a id="q10-16"></a>Q10-16 Exhaustive switch on lifecycle
```ts
function assertNever(x: never): never { throw new Error(String(x)); }
function lifeLabel(l: Life) {
  switch (l) {
    case "active": return "Active";
    case "discontinued": return "Discontinued";
    case "archived": return "Archived";
    default: assertNever(l);
  }
}
```

### <a id="q10-17"></a>Q10-17 Generic inventory store
```ts
class Store<T extends { sku: string }> {
  private items = new Map<string, T>();
  add(item: T) { if (this.items.has(item.sku)) throw new Error("duplicate"); this.items.set(item.sku, item); }
  get(sku: string) { return this.items.get(sku); }
  update(sku: string, patch: Partial<T>) { const item = this.get(sku); if (!item) return; this.items.set(sku, { ...item, ...patch }); }
}
```

### <a id="q10-18"></a>Q10-18 CSV import with validation
```ts
function importCsv(lines: string[]): { items: StockItem[]; errors: string[] } {
  const items: StockItem[] = []; const errors: string[] = [];
  lines.forEach((line, idx) => {
    const [sku, qtyStr] = line.split(",");
    const qty = Number(qtyStr);
    if (!sku || !Number.isFinite(qty)) errors.push(`Row ${idx + 1} invalid`);
    else items.push({ sku, qty });
  });
  return { items, errors };
}
```

### <a id="q10-19"></a>Q10-19 Discount rules by category
```ts
const discount = (cat: Category, price: number) => {
  const rate: Record<Category, number> = { electronics: 0.1, fashion: 0.15, food: 0 };
  return price * (1 - rate[cat]);
};
```

### <a id="q10-20"></a>Q10-20 Typed event emitter for inventory
```ts
type InventoryEvents = { item_dispensed: { sku: string; qty: number }; item_restocked: { sku: string; qty: number } };
class InventoryEmitter extends Emitter<InventoryEvents> {}
```

## 11) workflow exhaustive checks

### <a id="q11-01"></a>Q11-01 Ticket status union
```ts
type Status = "open" | "in_progress" | "resolved";
const canClose = (s: Status) => s === "resolved";
```

### <a id="q11-02"></a>Q11-02 Video pipeline statuses
```ts
type VideoStatus = "uploaded" | "transcoding" | "published" | "failed";
const labelVideo = (s: VideoStatus) => ({ uploaded: "Uploaded", transcoding: "Transcoding", published: "Published", failed: "Failed" }[s]);
```

### <a id="q11-03"></a>Q11-03 Exhaustive handler
```ts
function handleStatus(s: Status) {
  switch (s) {
    case "open": return "open";
    case "in_progress": return "working";
    case "resolved": return "done";
  }
}
```

### <a id="q11-04"></a>Q11-04 Filter open tickets
```ts
const filterOpen = (tickets: { status: Status }[]) => tickets.filter(t => t.status === "open");
```

### <a id="q11-05"></a>Q11-05 Transition with actions
```ts
type Action = "start" | "resolve" | "reopen";
const transition = (s: Status, action: Action): Status => {
  switch (s) {
    case "open": if (action === "start") return "in_progress"; break;
    case "in_progress": if (action === "resolve") return "resolved"; break;
    case "resolved": if (action === "reopen") return "open"; break;
  }
  throw new Error("Invalid transition");
};
```

### <a id="q11-06"></a>Q11-06 Resolved requires note
```ts
type Ticket = { status: "open" | "in_progress" } | { status: "resolved"; resolutionNote: string };
```

### <a id="q11-07"></a>Q11-07 Process Result union
```ts
type StepResult<T> = { ok: true; value: T } | { ok: false; error: string };
```

### <a id="q11-08"></a>Q11-08 Typed actions drive transitions
```ts
type TicketAction = { type: "assign"; agentId: string } | { type: "close" };
function reduce(state: { assignee?: string; status: Status }, action: TicketAction) {
  switch (action.type) {
    case "assign": return { ...state, assignee: action.agentId };
    case "close": if (state.status !== "resolved") throw new Error("Only resolved can close"); return state;
  }
}
```

### <a id="q11-09"></a>Q11-09 Guard invalid transition
```ts
const safeTransition = (s: Status, action: Action): Result<Status> => {
  try { return { ok: true, data: transition(s, action) }; }
  catch (e: any) { return { ok: false, error: e.message } as any; }
};
```

### <a id="q11-10"></a>Q11-10 Timeline event log
```ts
type TimelineEvent = { at: number; status: Status };
const addEvent = (events: TimelineEvent[], status: Status) => [...events, { at: Date.now(), status }];
```

### <a id="q11-11"></a>Q11-11 Retry failed step
```ts
const nextAfterRetry = (status: VideoStatus, retries: number) => {
  if (status === "failed" && retries < 2) return "transcoding";
  return status;
};
```

### <a id="q11-12"></a>Q11-12 Must assign before in_progress
```ts
function startTicket(ticket: { status: Status; assignee?: string }) {
  if (!ticket.assignee) throw new Error("Assign first");
  if (ticket.status !== "open") throw new Error("Only open can start");
  return { ...ticket, status: "in_progress" as Status };
}
```

### <a id="q11-13"></a>Q11-13 SLA overdue flag
```ts
const isOverdue = (createdAtMs: number, nowMs: number, thresholdHours: number) => (nowMs - createdAtMs) > thresholdHours * 3600_000;
```

### <a id="q11-14"></a>Q11-14 Batch process summary
```ts
const countByStatus = (tickets: { status: Status }[]): Record<Status, number> => ({
  open: tickets.filter(t => t.status === "open").length,
  in_progress: tickets.filter(t => t.status === "in_progress").length,
  resolved: tickets.filter(t => t.status === "resolved").length,
});
```

### <a id="q11-15"></a>Q11-15 Typed transition map (advanced)
```ts
const allowed: Record<Status, Status[]> = { open: ["in_progress"], in_progress: ["resolved"], resolved: ["open"] };
```

### <a id="q11-16"></a>Q11-16 Reducer pattern
```ts
type TicketAction2 = { type: "start" } | { type: "resolve" } | { type: "reopen" };
const reducer = (state: Status, action: TicketAction2): Status => transition(state, action.type as Action);
```

### <a id="q11-17"></a>Q11-17 Side-effect event
```ts
function resolveWithNotification(ticketId: string, notify: (id: string) => void) { notify(ticketId); return "notified"; }
```

### <a id="q11-18"></a>Q11-18 Async workflow simulation
```ts
async function process(video: VideoStatus) {
  const steps: VideoStatus[] = ["uploaded", "transcoding", "published"];
  for (const step of steps) { await delay(100); console.log(step); }
  return "published" as VideoStatus;
}
```

### <a id="q11-19"></a>Q11-19 Result-based async pipeline
```ts
const runSteps = async (steps: Array<() => Promise<Result<void>>>) => {
  for (const step of steps) {
    const r = await step();
    if (!r.ok) return r;
  }
  return { ok: true, data: undefined } as Result<void>;
};
```

### <a id="q11-20"></a>Q11-20 In-code metrics counters
```ts
const transitionsCount: Record<string, number> = {};
const recordTransition = (from: Status, to: Status) => transitionsCount[`${from}->${to}`] = (transitionsCount[`${from}->${to}`] ?? 0) + 1;
```

## 12) tailwind ui snippets (non-hms)

### <a id="q12-01"></a>Q12-01 Product Card (React + Tailwind)
```tsx
import React from "react";
type Props = { readonly id: string; name: string; price: number; discount?: number; tags?: string[] };
export const ProductCard: React.FC<Props> = ({ name, price, discount = 0, tags }) => {
  const final = price * (1 - discount / 100);
  return (
    <div className="rounded-xl border bg-white shadow p-4">
      <div className="flex items-baseline gap-2">
        <h3 className="text-lg font-semibold">{name}</h3>
        {discount > 0 && <span className="text-xs text-green-600">-{discount}%</span>}
      </div>
      <div className="text-2xl font-bold">₹{final}</div>
      {tags?.length && <div className="mt-2 flex gap-2 text-xs text-slate-500">{tags.map(t => <span key={t}>#{t}</span>)}</div>}
    </div>
  );
};
```

### <a id="q12-02"></a>Q12-02 Login Form State
```tsx
type FormState = { email: string; password: string; remember: boolean };
```

### <a id="q12-03"></a>Q12-03 Loading Skeleton
```tsx
const Skeleton: React.FC<{ isLoading: boolean }> = ({ isLoading, children }) => isLoading ? (
  <div className="animate-pulse space-y-2">
    <div className="h-4 bg-slate-200 rounded" />
    <div className="h-4 bg-slate-200 rounded w-5/6" />
  </div>
) : <>{children}</>;
```

### <a id="q12-04"></a>Q12-04 Toast variants union
```ts
type ToastType = "success" | "error" | "info";
const toastClass: Record<ToastType, string> = {
  success: "bg-green-600 text-white",
  error: "bg-red-600 text-white",
  info: "bg-blue-600 text-white",
};
```

### <a id="q12-05"></a>Q12-05 Modal component typed callbacks
```tsx
type ModalProps = { open: boolean; onClose: () => void; onConfirm: () => Promise<void> };
```

### <a id="q12-06"></a>Q12-06 Generic Table<T>
```tsx
type Column<T> = { key: keyof T; label: string };
function Table<T>({ rows, columns }: { rows: T[]; columns: Column<T>[] }) {
  return (
    <table className="min-w-full text-sm">
      <thead><tr>{columns.map(c => <th key={String(c.key)}>{c.label}</th>)}</tr></thead>
      <tbody>
        {rows.map((row, i) => (
          <tr key={i}>{columns.map(c => <td key={String(c.key)}>{String(row[c.key])}</td>)}</tr>
        ))}
      </tbody>
    </table>
  );
}
```

### <a id="q12-07"></a>Q12-07 Pagination component
```tsx
type PaginationProps = { page: number; pageSize: number; total: number; onPageChange: (p: number) => void };
```

### <a id="q12-08"></a>Q12-08 Debounced Search Bar
```tsx
const DebouncedSearch: React.FC<{ onSearch: (q: string) => void }> = ({ onSearch }) => {
  const [value, setValue] = React.useState("");
  React.useEffect(() => { const id = setTimeout(() => onSearch(value), 300); return () => clearTimeout(id); }, [value]);
  return <input className="input" value={value} onChange={e => setValue(e.target.value)} />;
};
```

### <a id="q12-09"></a>Q12-09 Dropdown union options
```tsx
type Sort = "price_asc" | "price_desc" | "rating";
```

### <a id="q12-10"></a>Q12-10 Badge variants map
```ts
type Badge = "new" | "sale" | "hot";
const badgeClass: Record<Badge, string> = { new: "bg-blue-100 text-blue-700", sale: "bg-amber-100 text-amber-700", hot: "bg-rose-100 text-rose-700" };
```

### <a id="q12-11"></a>Q12-11 Confirm dialog returns Promise<boolean>
```ts
const confirmAsync = (message: string) => new Promise<boolean>(resolve => resolve(window.confirm(message)));
```

### <a id="q12-12"></a>Q12-12 Stepper for workflow
```tsx
type Step = "uploaded" | "processing" | "done";
const steps: Step[] = ["uploaded", "processing", "done"];
```

### <a id="q12-13"></a>Q12-13 Tabs typed key union
```tsx
type TabKey = "overview" | "details" | "settings";
```

### <a id="q12-14"></a>Q12-14 Empty State component
```tsx
const EmptyState: React.FC<{ title: string; description?: string; actionLabel?: string; onAction?: () => void }> = ({ title, description, actionLabel, onAction }) => (
  <div className="rounded-lg border p-6 text-center">
    <h3 className="text-lg font-semibold">{title}</h3>
    {description && <p className="text-slate-500">{description}</p>}
    {actionLabel && onAction && <button className="btn" onClick={onAction}>{actionLabel}</button>}
  </div>
);
```

### <a id="q12-15"></a>Q12-15 Error UI state union
```ts
type UIState<T> = { kind: "loading" } | { kind: "ready"; data: T } | { kind: "error"; message: string };
```

### <a id="q12-16"></a>Q12-16 File Upload UI state
```ts
type UploadState = { status: "idle" } | { status: "uploading"; progress: number } | { status: "success" } | { status: "error"; message: string };
```

### <a id="q12-17"></a>Q12-17 Progress bar for async job
```tsx
const ProgressBar: React.FC<{ progress: number }> = ({ progress }) => (
  <div className="h-2 rounded bg-slate-200"><div className="h-full rounded bg-indigo-500" style={{ width: `${progress}%` }} /></div>
);
```

### <a id="q12-18"></a>Q12-18 Sidebar menu typed routes
```ts
const routes = [{ key: "home", path: "/" }, { key: "settings", path: "/settings" }] as const;
type RouteKey = typeof routes[number]["key"];
```

### <a id="q12-19"></a>Q12-19 Form validation error map
```ts
type Field = "email" | "password";
type Errors = Partial<Record<Field, string>>;
```

### <a id="q12-20"></a>Q12-20 Theme switcher union
```ts
type Theme = "light" | "dark";
const persistTheme = (t: Theme) => localStorage.setItem("theme", t);
const loadTheme = (): Theme => (localStorage.getItem("theme") === "dark" ? "dark" : "light");
```

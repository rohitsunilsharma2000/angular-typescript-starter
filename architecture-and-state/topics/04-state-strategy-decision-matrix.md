# 04) State strategy decision matrix

লেম্যানদের জন্য সহজ ভাষায়: কোন পরিস্থিতিতে কোন state strategy বেছে নেবেন (local component state, feature-level store, global event bus)। নিচের ডেমো চালালে তিনটিই এক মিনিটে বুঝে যাবেন।

## Things to learn (Bengali + beginner)
1) **Scope নির্ভর পছন্দ**: UI-এর ভিতরেই থাকলে local; একই feature-এর একাধিক কম্পোনেন্ট শেয়ার করলে service/store; cross-feature signal লাগলে event bus (বা NgRx ইত্যাদি) কিন্তু কম ব্যবহার করুন।
2) **প্রাইস ট্যাগ**: local = সহজ, zero boilerplate; service store = সামান্য infra; global bus = coupling ঝুঁকি, logging/contract দরকার।
3) **Side-effect লোকেশন**: API call/side-effect service/store লেয়ারে; pure render local UI-তে।
4) **Test surface**: local = pure function টেস্ট; store = state transition টেস্ট; bus = contract টেস্ট (ইভেন্ট নাম/পেলোড)।
5) **Migration পথ**: local থেকে শুরু, শেয়ারিং দরকার হলে service store এ তুলুন; খুব কমই global bus লাগবে।

## Decision matrix (চিটশিট)
- Local component state → Scope: single component; Sync/async: ok; Sharing: না; Boilerplate: সবচেয়ে কম; Risk: low.
- Feature service/store → Scope: একই feature-এর একাধিক কম্প; Async: হ্যাঁ; Sharing: হ্যাঁ; Boilerplate: মাঝারি; Risk: medium (leak না করলে)।
- Global event bus → Scope: cross-feature; Async: হ্যাঁ; Sharing: বড়; Boilerplate: কম কিন্তু discipline বেশি; Risk: coupling + debug pain, তাই sparingly।

## Hands-on (step-by-step + commands)
1) রেডি ডেমো চালান:
   ```bash
   cd architecture-and-state/demos/state-strategy-matrix-demo
   npm install
   npm run demo       # তিন ধরনের state আউটপুট দেখুন
   npm run typecheck  # টাইপ ও কন্ট্রাক্ট ঠিক আছে কিনা
   ```
2) কী দেখবেন (npm run demo):
   - Local অংশে count দুইবার বাড়ে।
   - Service store অংশে লিসেনার দুইবার আপডেট দেখে।
   - Event bus অংশে একই ইভেন্ট দুইটা ফিচার শোনে।
3) ব্রেক/ফিক্স এক্সপেরিমেন্ট:
   - `src/main.ts` থেকে `unsubscribe()` লাইনটা সরিয়ে `npm run demo` চালান → বারবার লিসেনার ফায়ার হবে (লিক ঝুঁকি)।
   - `bus.emit` এ ইভেন্ট নাম পাল্টে `user:login` করুন → কোনো আউটপুট পাবেন না (contract mismatch) → নাম ফিরিয়ে ঠিক করুন।
4) নিজে বানাতে চাইলে: `mkdir -p src/state` করে নিচের "Demos" সেকশনের ফাইলগুলো কপি করুন।

### Done when
- তিনটি আউটপুট সেকশন দেখেছেন এবং কেন কোনটা বেছে নেবেন তা বলতে পারেন।
- break/fix করে দেখেছেন লিক ও contract mismatch কেমন হয়।

## Demos (copy-paste)
`architecture-and-state/demos/state-strategy-matrix-demo/src/` থেকে মূল কোড:
```ts
// state/local-component.ts
export class LocalCounterComponent {
  private count = 0;
  increment() { this.count++; }
  render(): string { return `Local count: ${this.count}`; }
}
```
```ts
// state/service-store.ts
export type Listener<T> = (state: T) => void;
export class ServiceStore<T> {
  private state: T;
  private listeners: Listener<T>[] = [];
  constructor(initial: T) { this.state = initial; }
  get snapshot(): T { return this.state; }
  update(mutator: (state: T) => T): void {
    this.state = mutator(this.state);
    this.listeners.forEach(l => l(this.state));
  }
  subscribe(listener: Listener<T>): () => void {
    this.listeners.push(listener);
    return () => { this.listeners = this.listeners.filter(l => l !== listener); };
  }
}
```
```ts
// state/global-event-bus.ts
export type EventHandler = (payload: unknown) => void;
export class EventBus {
  private handlers: Record<string, EventHandler[]> = {};
  on(event: string, handler: EventHandler): () => void {
    this.handlers[event] = this.handlers[event] ?? [];
    this.handlers[event].push(handler);
    return () => { this.handlers[event] = this.handlers[event].filter(h => h !== handler); };
  }
  emit(event: string, payload: unknown): void {
    (this.handlers[event] ?? []).forEach(h => h(payload));
  }
}
```
```ts
// main.ts
import { LocalCounterComponent } from './state/local-component';
import { ServiceStore } from './state/service-store';
import { EventBus } from './state/global-event-bus';

async function run() {
  console.log('--- Local component state (isolated) ---');
  const local = new LocalCounterComponent();
  local.increment();
  local.increment();
  console.log(local.render());

  console.log('\n--- Service store (feature-shared) ---');
  const store = new ServiceStore<{ count: number }>({ count: 0 });
  const unsubscribe = store.subscribe(s => console.log('Listener saw count:', s.count));
  store.update(s => ({ count: s.count + 1 }));
  store.update(s => ({ count: s.count + 1 }));
  unsubscribe();

  console.log('\n--- Global event bus (cross-feature) ---');
  const bus = new EventBus();
  bus.on('user:logged-in', payload => console.log('Feature A heard:', payload));
  bus.on('user:logged-in', payload => console.log('Feature B heard:', payload));
  bus.emit('user:logged-in', { id: 'u1', name: 'Ayman' });
}

run();
```

## Ready-to-run demo (repo bundle)
- Path: `architecture-and-state/demos/state-strategy-matrix-demo`
- Commands:
  ```bash
  cd architecture-and-state/demos/state-strategy-matrix-demo
  npm install
  npm run demo       # তিন স্ট্র্যাটেজির আউটপুট
  npm run typecheck  # টাইপ/কন্ট্রাক্ট ঠিক আছে কিনা
  ```
- Expected output (সংক্ষিপ্ত):
  ```
  --- Local component state (isolated) ---
  Local count: 2

  --- Service store (feature-shared) ---
  Listener saw count: 1
  Listener saw count: 2

  --- Global event bus (cross-feature) ---
  Feature A heard: { id: 'u1', name: 'Ayman' }
  Feature B heard: { id: 'u1', name: 'Ayman' }
  ```
- Test ideas:
  - unsubscribe সরিয়ে মেমরি/ডুপ্লিকেট ইস্যু কল্পনা করুন।
  - ইভেন্ট নাম mismatch করে দেখুন কন্ট্রাক্ট না মানলে সিগন্যাল হারিয়ে যায়।

## Common mistakes
- ছোট UI’র জন্যই গ্লোবাল বাস ব্যবহার করা → coupling বাড়ে।
- Service store বানিয়ে রেখেও সব কাজ কম্পোনেন্টে করে ফেলা।
- Event নাম/পেলোড টাইপ ডকুমেন্ট না করা।

## Interview points
- Scope-based strategy নির্বাচন করতে পারেন (local vs feature vs global)।
- কেন over-globalization ডিবাগ কঠিন করে তা ব্যাখ্যা করুন।
- Migration গল্প: local → service store → (rarely) global bus/NgRx।

## Quick practice
- `npm run demo` চালিয়ে লাইনগুলো মিলে কিনা দেখুন।
- Break/fix এক্সপেরিমেন্ট করে শিখুন unsubscribe ও ইভেন্ট কনট্রাক্টের গুরুত্ব।
- নিজের ফিচারের জন্য কোন স্ট্র্যাটেজি যথেষ্ট তা এক লাইনে লিখে নিন।

# ECMAScript Core (ES1 → ES2024) — বাংলা + হাসপাতাল উদাহরণ

বিগিনার/ইন্টারভিউ ফোকাসড ট্র্যাক যাতে ES বেসিক থেকে আধুনিক ফিচার (ES2024 পর্যন্ত) শেখা যায়। সব উদাহরণ হাসপাতাল ম্যানেজমেন্ট (patient, bed, pharmacy, billing) প্রসঙ্গ থেকে, আর কোড ইংরেজিতে।

## কীভাবে পড়বেন
1) `topics` ফোল্ডার ক্রমানুসারে পড়ুন (ছোট নোট + কোড + Try it)।  
2) `demos/hospital-es-demo` রান করুন: `node app.js` (Node 18+).  
3) প্রতিদিন 30–60 মিনিট + Try it সমাধান লিখে GitHub গিস্টে রাখুন।

## ২ সপ্তাহের স্টাডি প্ল্যান
- **Day 1–2:** JS বেসিক, var/let/const, টাইপ ও truthy/falsy, মৌলিক ডিবাগ।  
- **Day 3:** Scope, hoisting, closures (EMR access example).  
- **Day 4:** Objects, prototypes vs class, `this` binding (patient model).  
- **Day 5:** Arrays, iterables, iterator protocol (rounds list).  
- **Day 6:** Functions → Promises → async/await (bed allocation API).  
- **Day 7:** Modules (import/export), bundling basics.  
- **Day 8:** Error handling, logging, optional catch binding.  
- **Day 9:** Map/Set/WeakMap for caches/tokens.  
- **Day 10:** Modern syntax: destructuring, rest/spread, optional chaining, nullish coalescing.  
- **Day 11:** Async patterns: `Promise.all/allSettled/any`, race conditions in alerts.  
- **Day 12:** Performance & memory: lazy data, resizable buffers.  
- **Day 13:** Testing/tooling: tsconfig targets, polyfills, lint.  
- **Day 14:** Mini project build + mock interview Q&A।

## Most-asked interview প্রশ্ন (ECMAScript কনটেক্সটে)
- var vs let vs const + hoisting?
- `this` context কিভাবে কাজ করে? arrow vs function?
- Prototype vs class—internals?
- Promise states? async/await error propagation?
- Optional chaining vs nullish coalescing?
- Array helpers: map/filter/reduce vs toSorted/toSpliced?
- `Promise.all` vs `allSettled` vs `any`?
- Map বনাম Object কখন ব্যবহার করবেন?
- Event loop / microtask queue ছোট ব্যাখ্যা?

## Syllabus (10–15 টপিক, ১ লাইনের outcome)
- ES history → কেন সংস্করণ ধরে পড়বেন।  
- Scope, hoisting, closures → common bug ধরা।  
- Objects/prototypes/classes → patient/admission মডেল করা।  
- Arrays/iterables/iterators → রাউন্ড লিস্ট ও স্ট্রিমড ল্যাব ডেটা।  
- Functions, Promise, async/await → bed/lab API call flow।  
- Modules (import/export) → ফিচার ভিত্তিক ফাইল সংগঠন।  
- Error handling/logging → optional catch binding, structured logs।  
- Map/Set/WeakMap → bed cache, session token leak এড়ানো।  
- Modern syntax → destructuring, rest/spread, optional chaining, nullish।  
- Async patterns → all/allSettled/any, cancellation ভাবনা।  
- Immutable array helpers → `toSorted`, `toSpliced`, `findLast` দিয়ে নিরাপদ আপডেট।  
- Performance/memory → resizable buffers for imaging, lazy iterables।  
- Tooling → tsconfig.target নির্বাচন, polyfill চেকলিস্ট।  
- Testing → smoke tests with Node/Vitest (optional)।  
- Mini project → “ER Intake & Lab Tracker”।

## Folder ম্যাপ
- `topics/01-ecmascript-basics.md` (var/let/const, truthy/falsy)
- `topics/02-scope-hoisting-closures.md`
- `topics/03-objects-prototypes-classes.md`
- `topics/04-arrays-iterables-iterators.md`
- `topics/05-functions-promises-async-await.md`
- `topics/06-modules-import-export.md`
- `topics/07-error-handling-logging.md`
- `topics/08-collection-utils-map-set.md`
- `topics/09-modern-syntax-optional-nullish.md`
- `topics/10-async-patterns-all-any-settled.md`
- `topics/11-immutable-array-helpers.md`
- `topics/12-performance-memory.md`
- `topics/13-tooling-testing-tsconfig.md`
- `topics/14-mini-project-guide.md`
- `demos/hospital-es-demo/app.js` (runnable Node demo)

## Mini project আইডিয়া (hospital workflow)
**ER Intake & Lab Tracker**: ইনকামিং patient queue + bed allocator (Map), lab stream (async generator), vitals sort (`toSorted`), alert race (`Promise.any`), optional chaining/nullish for partial payloads. Build twice with `tsconfig.target=ES2015` এবং `ES2020+` দেখাতে কোন polyfill লাগে।

## Quick setup
- Node.js 18+  
- npm install (only if you add deps); ডিফল্ট ডেমো pure Node API-তে চলে।  
- Want TypeScript? add `tsconfig.json` with `target: "ES2020"` (adjust per feature)।  
- Browser demo? নিশ্চিত করুন polyfill লিস্ট লিখে রাখছেন (optional chaining, `Promise.any`, `toSorted` etc.).  

## YouTube title ideas
`youtube-titles.md` এ beginner → advanced ভিডিও টাইটেল সাজেশন আছে (ICU beds, async/await, Map/Set, Promise.any, resizable buffers, mini project walkthrough)।  

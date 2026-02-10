# 01) Angular architecture ও SPA ধারণা

- Angular = component-based SPA ফ্রেমওয়ার্ক (HTML + TS + RxJS)।
- Core ব্লক: Component, Template, Dependency Injection, Router, Forms, HttpClient.
- Change Detection: zone.js + Ivy; OnPush থাকলে input/observable/promise এ change হলে update।
- Standalone যুগ: module না বানিয়েও component bootstrap করা যায় (Angular 15+)।

**হাসপাতাল উদাহরণ**: Hospital dashboard SPA → Navbar (Patients, Beds, Pharmacy), Router দিয়ে view বদলায়, state service দিয়ে shared data।

**Interview Q**
- Angular vs React-এর প্রধান architectural পার্থক্য?
- Standalone component কেন এসেছে?

**Quick check**
- Browser console এ `ng.version.full` (devtools console if Angular app loaded) দেখুন; Ivy default কিনা নিশ্চিত করুন।

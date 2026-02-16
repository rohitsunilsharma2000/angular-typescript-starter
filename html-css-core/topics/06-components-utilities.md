# 06) Components & utilities — buttons, badges, utilities for vitals

**কি শিখবেন**
- ছোট কম্পোনেন্ট স্টাইল (button, badge) এবং utility ক্লাস (flex, gap, text)।
- Reuse বনাম override সিদ্ধান্ত; specificity কম রাখা।
- State style: hover/focus/disabled নিরাপদ motion সহ।

**Code**
```html
<style>
.u-flex { display: flex; }
.u-gap-sm { gap: 0.5rem; }
.u-center { align-items: center; }
.u-wrap { flex-wrap: wrap; }

.btn {
  border: none;
  border-radius: 8px;
  padding: 0.6rem 0.9rem;
  font-weight: 600;
  cursor: pointer;
  transition: transform 120ms ease, box-shadow 120ms ease;
}
.btn--primary { background: #2563eb; color: #fff; }
.btn--ghost   { background: transparent; color: #2563eb; border: 1px solid currentColor; }
.btn:focus-visible { outline: 2px solid #0ea5e9; outline-offset: 2px; }
.btn:active { transform: translateY(1px); }
@media (prefers-reduced-motion: reduce) {
  .btn { transition: none; }
}

.badge {
  display: inline-flex;
  align-items: center;
  gap: 0.35rem;
  border-radius: 999px;
  padding: 0.3rem 0.6rem;
  font-size: 0.85rem;
  background: #e0f2fe;
  color: #075985;
}
</style>

<div class="u-flex u-gap-sm u-wrap u-center">
  <button class="btn btn--primary">Admit</button>
  <button class="btn btn--ghost" disabled>Discharge</button>
  <span class="badge">SpO2 96%</span>
  <span class="badge">HR 88</span>
</div>
```

**Interview takeaways**
- Utilities দ্রুত লেআউট সাজায়; কম্পোনেন্ট ক্লাস মূল ভিজ্যুয়াল সিস্টেম ধরে রাখে—মিশ্রণই বাস্তবসম্মত।
- `prefers-reduced-motion` দিয়ে মাইক্রো-মোশন safe করা ইন্টারভিউতে plus point।
- Focus-visible ব্যবহার করুন—keyboard users এর জন্য স্পষ্ট ফোকাস রিং দিন।

**আরো উদাহরণ (beginner → advanced)**
1) Pill badge
```html
<span style="display:inline-flex;align-items:center;padding:4px 10px;border-radius:999px;background:#eef2ff;color:#3730a3;">ICU</span>
```
2) Utility stack
```html
<div style="display:flex;gap:8px;flex-wrap:wrap;"><button>Admit</button><button>Cancel</button></div>
```
3) Icon button
```html
<button aria-label="Refresh beds" style="border:1px solid #cbd5e1;padding:8px;border-radius:8px">&#x21bb;</button>
```
4) Disabled button
```html
<button disabled style="opacity:0.55;cursor:not-allowed;">Disabled</button>
```
5) Stack + cluster
```html
<div style="display:flex;flex-direction:column;gap:12px;"><div style="display:flex;gap:12px;flex-wrap:wrap;align-items:center;"><span>HR 88</span><span>BP 120/80</span></div><div><button>Save</button></div></div>
```
6) Focus-visible utility
```html
<style>.focus-ring:focus-visible{outline:2px solid #2563eb;outline-offset:2px}</style><button class="focus-ring">Focusable</button>
```
7) Toggle switch
```html
<label style="display:inline-flex;align-items:center;gap:8px;"><input type="checkbox"> Enable alerts</label>
```
8) Skeleton loader
```html
<style>.skeleton{width:160px;height:14px;border-radius:6px;background:linear-gradient(90deg,#e5e7eb 25%,#f3f4f6 37%,#e5e7eb 63%);background-size:400% 100%;animation:shimmer 1.2s infinite}@keyframes shimmer{100%{background-position:-100% 0}}</style><div class="skeleton"></div>
```
9) Tooltip data-tip
```html
<style>[data-tip]{position:relative}[data-tip]::after{content:attr(data-tip);position:absolute;inset:auto auto 110% 50%;transform:translateX(-50%);background:#111827;color:#fff;padding:4px 8px;border-radius:6px;opacity:0;pointer-events:none;transition:opacity .15s}[data-tip]:hover::after,[data-tip]:focus-visible::after{opacity:1}</style><button data-tip="Edit patient">✎</button>
```
10) Modal dialog
```html
<dialog id="assign"><p>Assign bed?</p><button onclick="assign.close()">Close</button></dialog><button onclick="assign.showModal()">Open</button>
```

**Try it**
- Badge এ status ভ্যারিয়েন্ট যোগ করুন (`badge--warn`, `badge--ok`) রঙ ভ্যারিয়েবল দিয়ে।
- Button-এর disabled স্টেটে opacity + cursor স্টাইল যোগ করুন।
- Utilities দিয়ে vitals কার্ড লেআউট বানান (row wrap + gap)।  

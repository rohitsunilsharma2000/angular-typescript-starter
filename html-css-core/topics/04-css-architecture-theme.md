# 04) CSS architecture & theming — tokens for wards/pharmacy

**কি শিখবেন**
- CSS variables দিয়ে রঙ/spacing/token সেট; light/dark toggle বেসিক।
- BEM নামকরণ বনাম utility-first (সংক্ষিপ্ত সিদ্ধান্ত মানদণ্ড)।
- Component স্কোপিং ও ক্যাসকেড এড়ানোর কৌশল।

**Code**
```html
<style>
:root {
  --bg: #0f172a;
  --card: #ffffff;
  --text: #0f172a;
  --accent: #2563eb;
  --radius: 10px;
  --space: 0.75rem;
}
[data-theme="dark"] {
  --bg: #0b1220;
  --card: #111827;
  --text: #e5e7eb;
  --accent: #38bdf8;
}
body { margin: 0; font-family: system-ui; background: var(--bg); color: var(--text); }
.card { background: var(--card); border-radius: var(--radius); padding: var(--space); }
.btn { display: inline-block; background: var(--accent); color: #fff; padding: 0.5rem 0.9rem; border-radius: 6px; text-decoration: none; }

.bed-card { /* BEM-ish block */
  border: 1px solid #cbd5e1;
  margin-bottom: var(--space);
}
.bed-card__title { font-weight: 700; }
.bed-card--critical { border-color: #ef4444; }
</style>

<button id="toggle" class="btn">Toggle theme</button>

<article class="card bed-card bed-card--critical">
  <div class="bed-card__title">ICU-1</div>
  <div class="bed-card__body">Patient: P-11 | SpO2: 92%</div>
</article>

<script>
const toggle = document.querySelector('#toggle');
toggle.addEventListener('click', () => {
  document.documentElement.dataset.theme =
    document.documentElement.dataset.theme === 'dark' ? 'light' : 'dark';
});
</script>
```

**Interview takeaways**
- CSS variables runtime-এ টগলযোগ্য; JS/ডাটা-অ্যাট্রিবিউট দিয়ে থিম সুইচ করা যায়।
- BEM নামকরণ specificity কম ও পড়তে সহজ; utility-first (যেমন `flex`, `gap-4`) দ্রুত কিন্তু স্প্রেডেড—প্রসঙ্গে বেছে নিন।
- Global leakage রোধে: লোয়ার specificity, কম্পোনেন্ট রুট ক্লাস, এবং reset/normalize ব্যবহার করুন।

**আরো উদাহরণ (beginner → advanced)**
1) Root token
```html
<style>:root{--accent:#2563eb}.link{color:var(--accent)}</style><a class="link">Primary link</a>
```
2) BEM buttons
```html
<style>.btn{padding:8px 12px;border-radius:8px}.btn--primary{background:#2563eb;color:#fff}.btn--danger{background:#ef4444;color:#fff}</style><button class="btn btn--primary">Save</button>
```
3) Utilities
```html
<style>.u-flex{display:flex}.u-gap-sm{gap:8px}.u-center{align-items:center}</style><div class="u-flex u-gap-sm u-center"><span>HR 88</span><span>BP 120/80</span></div>
```
4) Data-theme switch
```html
<style>[data-theme="dark"]{--card:#111827;--text:#e2e8f0}.card{background:var(--card);color:var(--text);padding:12px;border-radius:8px}</style><article class="card" data-theme="dark">Dark card</article>
```
5) Scoped token
```html
<style>.pharmacy{--accent:#f97316}.pharmacy .btn--primary{background:var(--accent)}</style><div class="pharmacy"><button class="btn--primary">Order</button></div>
```
6) :where reset
```html
<style>:where(h1,h2,h3,p){margin:0}.card p{margin-block:8px}</style><div class="card"><h3>Title</h3><p>Body</p></div>
```
7) Component fallback token
```html
<style>:root{--surface:#f8fafc}.card{--card-bg:var(--surface,#fff);background:var(--card-bg);padding:12px;border-radius:8px}</style><div class="card">Tokenized</div>
```
8) Cascade layers
```html
<style>@layer utilities{.u-mb-2{margin-bottom:8px}}</style><p class="u-mb-2">Layered util</p>
```
9) Space scale
```html
<style>:root{--space-1:4px;--space-2:8px}.stack{display:grid;gap:var(--space-2)}</style><div class="stack"><div>Row1</div><div>Row2</div></div>
```
10) Auto dark fallback
```html
<style>@media (prefers-color-scheme: dark){:root{--surface:#0f172a;--text:#e2e8f0}}.card{background:var(--surface,#fff);color:var(--text,#0f172a)}</style><div class="card">Auto dark</div>
```

**Try it**
- Pharmacy সতর্কতা কার্ডে `bed-card--warning` ভ্যারিয়েন্ট বানান (`border-color: amber`), থিমেও কাজ করতে হবে।
- CSS variables দিয়ে spacing scale (`--s-1`, `--s-2`) বানিয়ে `.card` মার্জিন/প্যাডিং সেখানে রেফার করুন।
- prefers-reduced-motion থাকলে থিম টগল ট্রানজিশন এড়ান।  

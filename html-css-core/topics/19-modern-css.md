# 19) Modern CSS features — variables, math, aspect-ratio

**কি শিখবেন**
- CSS variables, `calc()/min()/max()/clamp()`।
- `aspect-ratio`, `object-fit`, scroll-behavior/snap, container queries (সংক্ষেপে)।

**Code**
```css
:root {
  --space: 1rem;
  --accent: #2563eb;
}
.card { padding: var(--space); border: 1px solid #cbd5e1; }
.hero { padding: calc(var(--space) * 2); }
.hero img { width: 100%; aspect-ratio: 4 / 3; object-fit: cover; }

html { scroll-behavior: smooth; }
```

**Interview takeaways**
- Variables runtime-এ টগল করা যায়; JS ছাড়াই থিমিং/spacing স্কেল।
- `clamp()` responsive টাইপ/spacing; `min/max` প্রান্তিক সীমা সেট করে।
- `aspect-ratio` CSS native, iframe/video/card সব জায়গায় কাজে লাগে।
- Scroll snap/behavior মাইক্রো-UX কিন্তু `prefers-reduced-motion` বিবেচনা করুন।

**আরো উদাহরণ (beginner → advanced)**
1) Clamp spacing
```html
<div style="padding:clamp(1rem,2vw,2rem);background:#f8fafc;">Section</div>
```
2) min()/max()
```html
<div style="width:min(320px,28vw);border:1px solid #cbd5e1;">Sidebar</div>
```
3) Object-fit cover
```html
<img src="ward.jpg" alt="" style="width:100%;height:160px;object-fit:cover;">
```
4) Scroll snap
```html
<div style="scroll-snap-type:x mandatory;display:flex;overflow:auto;gap:12px;"><article style="scroll-snap-align:start;min-width:280px;">Card</article><article style="scroll-snap-align:start;min-width:280px;">Card</article></div>
```
5) Container query
```html
<style>@container (min-width:600px){.card{display:grid;grid-template-columns:1fr 1fr}}</style><div style="container-type:inline-size;"><div class="card" style="border:1px solid #cbd5e1;">CQ card</div></div>
```
6) accent-color
```html
<input type="checkbox" style="accent-color:#22c55e">
```
7) :has parent
```html
<div class="field" style="border:1px solid #cbd5e1;padding:8px;"><input required></div><style>.field:has(input:invalid){border-color:#ef4444}</style>
```
8) color-mix
```html
<div style="background:color-mix(in srgb,#0ea5e9 70%,white);padding:12px;border-radius:8px;">Mix</div>
```
9) View transitions
```html
<style>::view-transition-old(root),::view-transition-new(root){animation-duration:180ms}</style>
```
10) @supports guard
```html
<style>@supports(backdrop-filter:blur(10px)){.glass{backdrop-filter:blur(10px)}}.glass{padding:12px;border:1px solid #cbd5e1}</style><div class="glass">Glass</div>
```

**Try it**
- Hero image aspect-ratio 16/9 বনাম 4/3 তুলনা করুন।
- Sidebar width `min()` ও `max()` দিয়ে আলাদা সীমা দিন।
- Scroll snap দিয়ে pharmacy কার্ড ক্যারোসেল বানান; reduce-motion থাকলে snap বন্ধ করুন।  

# 17) Responsive design — মোবাইল-প্রথম চিন্তা

**কি শিখবেন**
- Media queries, breakpoints; mobile-first।
- Fluid layouts, responsive টাইপ/ইমেজ, `clamp()`।

**Code**
```css
body { margin:0; padding: 1rem; }
.page { width: min(1100px, 92vw); margin: 0 auto; }
h1 { font-size: clamp(1.6rem, 2vw + 1rem, 2.4rem); }

@media (min-width: 768px) {
  .layout { display:grid; grid-template-columns: 240px 1fr; gap: 1rem; }
}
```

**Interview takeaways**
- Mobile-first: base styles small screens, তারপর min-width breakpoints।
- `clamp()` টাইপ/spacing smooth স্কেল দেয়; বড় ফাঁক ছাড়া।
- Responsive images: `max-width:100%; height:auto;` অথবা `srcset/sizes`।

**আরো উদাহরণ (beginner → advanced)**
1) Nav column on small
```html
<style>@media (max-width:600px){nav{flex-direction:column}}</style><nav style="display:flex;gap:8px;"><a>Home</a><a>Beds</a></nav>
```
2) Fluid container
```html
<div style="width:min(1200px,95vw);margin:0 auto;border:1px dashed #cbd5e1;">Container</div>
```
3) Table to cards
```html
<style>@media (max-width:640px){table,thead,tbody,tr,td{display:block}td::before{content:attr(data-label);font-weight:700}}</style><table><tbody><tr><td data-label="Bed">ICU</td><td data-label="Status">OK</td></tr></tbody></table>
```
4) Responsive img
```html
<img src="x.jpg" alt="Ward" style="max-width:100%;height:auto;">
```
5) Clamp section padding
```html
<div style="padding:clamp(1rem,2vw,2rem);background:#f8fafc;">Section</div>
```
6) Orientation rule
```html
<style>@media (orientation:landscape){.hero{min-height:60vh}}</style><section class="hero" style="background:#e0f2fe;">Hero</section>
```
7) Responsive gap
```html
<div style="display:grid;gap:clamp(10px,2vw,24px);grid-template-columns:repeat(auto-fit,minmax(200px,1fr));"><div>1</div><div>2</div></div>
```
8) Safe-area inset
```html
<header style="padding-top:max(16px,env(safe-area-inset-top));background:#0f172a;color:#fff;">Header</header>
```
9) Container query
```html
<style>@container (min-width:500px){.card{display:grid;grid-template-columns:1fr 1fr}}</style><div style="container-type:inline-size;"><div class="card" style="border:1px solid #cbd5e1;">Responsive card</div></div>
```
10) Reduce motion mobile
```html
<style>@media (prefers-reduced-motion: reduce){*{scroll-behavior:auto}}</style><a href="#target">Jump</a><div id="target">Target</div>
```

**Try it**
- Intake form দুই কলাম (>=768px) থেকে এক কলাম (মোবাইল) করুন।
- Hero টেক্সট `clamp` দিয়ে স্কেল করুন; breakpoints কমান।
- একটি ছবিতে `srcset` যোগ করে নেটওয়ার্ক tab-এ লোড হওয়া সাইজ দেখুন।  

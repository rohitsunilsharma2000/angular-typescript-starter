# 20) Advanced layout & effects — clip, filter, blend

**কি শিখবেন**
- CSS shapes/clip-path, filters/backdrop-filter, blend modes, custom scrollbar ইঙ্গিত।

**Code**
```css
.hero-mask {
  clip-path: polygon(0 0, 100% 0, 100% 85%, 0 100%);
  background: linear-gradient(135deg, #2563eb, #38bdf8);
  color: #fff;
  padding: 2rem;
}

.glass {
  backdrop-filter: blur(8px);
  background: rgba(255,255,255,0.08);
  border: 1px solid rgba(255,255,255,0.2);
}
```

**Interview takeaways**
- clip-path দিয়ে হিরো শেপ/ডায়াগোনাল কাট—SVG দরকার নেই।
- backdrop-filter শুধু সাপোর্টেড ব্রাউজারে; ফ্যালব্যাক রঙ রাখুন।
- blend-mode ইমেজ + গ্রেডিয়েন্ট মিক্সে কাজে লাগে; অ্যাক্সেসিবিলিটি (contrast) খেয়াল।

**আরো উদাহরণ (beginner → advanced)**
1) Drop-shadow filter
```html
<img src="thumb.jpg" alt="" style="filter:drop-shadow(0 10px 20px rgba(0,0,0,0.12));">
```
2) Frosted card
```html
<div style="backdrop-filter:blur(6px);background:rgba(15,23,42,0.3);padding:16px;color:#fff;">Frost</div>
```
3) Multiply blend
```html
<img src="hero.jpg" alt="" style="mix-blend-mode:multiply;width:100%;">
```
4) Custom scrollbar
```html
<style>*::-webkit-scrollbar{height:8px}*::-webkit-scrollbar-thumb{background:#94a3b8;border-radius:999px}</style><div style="overflow:auto;white-space:nowrap;width:220px;">Scroll horizontally to see thumb</div>
```
5) Clip-path avatar
```html
<img src="avatar.jpg" alt="" style="clip-path:circle(50%);width:80px;">
```
6) Gradient border
```html
<div style="position:relative;border:2px solid transparent;background:linear-gradient(#fff,#fff) padding-box,linear-gradient(120deg,#06b6d4,#6366f1) border-box;padding:12px;border-radius:12px;">Border</div>
```
7) Conic gauge
```html
<div style="width:140px;aspect-ratio:1;border-radius:50%;background:conic-gradient(#22c55e 0 72%,#e2e8f0 0);"></div>
```
8) Masked fade
```html
<div style="-webkit-mask-image:linear-gradient(180deg,transparent,#000 10%,#000 90%,transparent);height:140px;overflow:auto;">Long scroll text...</div>
```
9) Grayscale toggle
```html
<img src="ward.jpg" alt="" style="filter:grayscale(1);">
```
10) Blur fallback
```html
<style>@supports not (backdrop-filter:blur(6px)){.frost{background:rgba(15,23,42,0.7)}}</style><div class="frost" style="backdrop-filter:blur(6px);padding:14px;color:#fff;">Fallback</div>
```

**Try it**
- Hero সেকশনে diagonal clip-path যোগ করুন; মোবাইলে ফ্যালব্যাক হিসেবে square রাখুন।
- Dashboard কার্ডে subtle blur overlay দিন; prefers-reduced-transparency থাকলে ফ্যালব্যাক দিন।
- Scrollbar স্টাইল করুন কিন্তু contrast বজায় রাখুন।  

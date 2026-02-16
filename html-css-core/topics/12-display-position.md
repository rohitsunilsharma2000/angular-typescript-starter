# 12) Display & positioning — block থেকে sticky পর্যন্ত

**কি শিখবেন**
- Display মান: block, inline, inline-block, none।
- Position: static, relative, absolute, fixed, sticky; stacking context ও z-index।

**Code**
```html
<header class="top">CityCare</header>
<div class="card">Content</div>
```
```css
.top { position: sticky; top: 0; background: #0f172a; color: #fff; z-index: 10; }
.card { display: inline-block; padding: 12px; border: 1px solid #cbd5e1; }
```

**Interview takeaways**
- inline এলিমেন্ট width/height সেট করতে পারে না; inline-block পারে কিন্তু লাইন-ফ্লো ধরে।
- Relative নিজস্ব জায়গা রেখে offset হয়; absolute নিকটতম positioned ancestor ধরে।
- Sticky স্ক্রল-সেন্সিটিভ; parent overflow hidden হলে কাজ নাও করতে পারে।
- z-index শুধুমাত্র positioned এলিমেন্টে কার্যকর; stacking context প্রায়শই transform/opacity দিয়ে নতুন হয়।

**আরো উদাহরণ (beginner → advanced)**
1) Inline vs block
```html
<span style="display:block">Block span</span><span>Inline span</span>
```
2) Relative + absolute badge
```html
<div style="position:relative;padding:10px 14px;border:1px solid #cbd5e1;">P-11<span style="position:absolute;top:-4px;right:-4px;width:10px;height:10px;background:#ef4444;border-radius:50%;"></span></div>
```
3) Fixed bar
```html
<div style="position:fixed;bottom:16px;right:16px;background:#2563eb;color:#fff;padding:10px;">Status</div>
```
4) Sticky subheader
```html
<div style="height:160px;overflow:auto;"><h3 style="position:sticky;top:0;background:#fff;">Subhead</h3><p>Long content...</p></div>
```
5) Stacking context
```html
<div style="position:relative;z-index:1;background:#fff;">Modal shell</div>
```
6) Inline-block gap fix
```html
<nav><a style="display:inline-block;margin-right:-4px;">Home</a><a style="display:inline-block;">Beds</a></nav>
```
7) Absolute center
```html
<div style="position:relative;height:200px;background:#e2e8f0;"><div style="position:absolute;inset:0;display:grid;place-items:center;">Centered</div></div>
```
8) display: contents
```html
<div style="display:grid;grid-template-columns:1fr 1fr;"><div style="display:contents"><div>Row1</div><div>Row2</div></div></div>
```
9) Overlay with pointer-events none
```html
<div style="position:fixed;inset:0;background:rgba(15,23,42,.4);pointer-events:none;"></div>
```
10) Sticky in scroll area
```html
<div style="max-height:200px;overflow:auto;border:1px solid #cbd5e1;"><h3 style="position:sticky;top:0;background:#fff;">Beds</h3><p>...</p></div>
```

**Try it**
- Bed list টেবিলের হেডার sticky করুন; scroll করলে উপরে থাকে কিনা দেখুন।
- Overlay modal absolute/fixed পার্থক্য পরীক্ষা করুন (body scroll lock?).
- z-index issue রেপ্রোডিউস করুন: parent-এ `transform: translateZ(0)` দিয়ে stacking context তৈরি করুন।  

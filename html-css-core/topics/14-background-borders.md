# 14) Backgrounds & borders — ভিজুয়াল হাইলাইট

**কি শিখবেন**
- background-color/image/size/position; গ্রেডিয়েন্ট।
- Border styles/radius; box-shadow, text-shadow।

**Code**
```html
<div class="card">
  <h3>Pharmacy</h3>
  <p>AMOX stock: 40</p>
</div>
```
```css
.card {
  background: linear-gradient(135deg, #e0f2fe, #fff);
  border: 1px solid #cbd5e1;
  border-radius: 12px;
  box-shadow: 0 10px 24px rgba(15, 23, 42, 0.12);
  padding: 1rem;
}
```

**Interview takeaways**
- `background-size: cover` হিরোতে; `contain` লোগো/আইকনে।
- Shadows বেশি হলে পারফরম্যান্সে প্রভাব; subtle রাখুন।
- Gradients CSS-এ রাখুন—ইমেজ ডাউনলোড দরকার নেই।

**আরো উদাহরণ (beginner → advanced)**
1) Solid bg
```html
<span style="background:#fef3c7;padding:6px 10px;border-radius:6px;">Warning</span>
```
2) Cover image
```html
<div style="height:200px;background:url('icu.jpg') center/cover no-repeat;"></div>
```
3) Repeating pattern
```html
<div style="background-image:linear-gradient(#e2e8f0 1px,transparent 1px);background-size:24px 24px;height:120px;"></div>
```
4) Radial gradient
```html
<div style="width:140px;height:140px;background:radial-gradient(circle at 20% 20%,#38bdf8,#0ea5e9);border-radius:12px;"></div>
```
5) Text shadow
```html
<h1 style="text-shadow:0 1px 2px rgba(0,0,0,0.12);">Beds</h1>
```
6) Per-corner radius
```html
<div style="border:1px solid #cbd5e1;border-radius:16px 16px 4px 4px;padding:12px;">Card</div>
```
7) Focus outline
```html
<button style="outline:3px solid transparent;" onfocus="this.style.outline='3px solid #22c55e';this.style.outlineOffset='3px'">Focus me</button>
```
8) Inset shadow
```html
<button style="padding:10px 14px;" onmousedown="this.style.boxShadow='inset 0 2px 6px rgba(0,0,0,0.2)'">Press</button>
```
9) Gradient text
```html
<div style="background:linear-gradient(90deg,#06b6d4,#6366f1);-webkit-background-clip:text;color:transparent;">Gradient text</div>
```
10) Border image
```html
<div style="border:12px solid transparent;border-image:url('frame.png') 30 round;padding:12px;">Framed</div>
```

**Try it**
- Hero-তে background-blend-mode ব্যবহার করে গ্রেডিয়েন্ট + ছবি মিক্স করুন।
- Card-এর border-radius পরিবর্তন করে 4px বনাম 16px তুলনা করুন।
- Shadow পারফরম্যান্স প্রভাব দেখতে DevTools → Rendering → Paint flashing অন করুন।  

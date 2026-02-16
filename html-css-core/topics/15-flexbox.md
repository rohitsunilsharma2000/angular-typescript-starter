# 15) Flexbox — অবশ্যই আয়ত্ত করুন

**কি শিখবেন**
- Main axis বনাম cross axis; direction/wrap।
- justify-content, align-items/content; grow/shrink/basis; gap।

**Code**
```html
<div class="toolbar">
  <button class="btn">Admit</button>
  <button class="btn">Discharge</button>
  <span class="badge">Beds: 12/17</span>
</div>
```
```css
.toolbar {
  display: flex;
  gap: 10px;
  align-items: center;
  flex-wrap: wrap;
}
.btn { flex: 0 0 auto; }
.badge { margin-left: auto; } /* pushes to end */
```

**Interview takeaways**
- Direction বদলালে main/cross এক্সিস বদলে যায়; justify main, align cross।
- `flex: 1` মানে `flex: 1 1 0` → grow এবং shrink দুটোই on, basis 0।
- Gap flex-এ এখন নেটিভ; মার্জিন হ্যাকের প্রয়োজন কম।

**আরো উদাহরণ (beginner → advanced)**
1) Centering
```html
<div style="display:flex;align-items:center;justify-content:center;height:120px;background:#f8fafc;">Center</div>
```
2) Wrap cards
```html
<div style="display:flex;flex-wrap:wrap;gap:12px;"><article style="flex:1 1 200px;border:1px solid #cbd5e1;">ICU</article><article style="flex:1 1 200px;border:1px solid #cbd5e1;">Ward</article></div>
```
3) Column stack
```html
<div style="display:flex;flex-direction:column;gap:8px;"><div>Row1</div><div>Row2</div></div>
```
4) Grow vs shrink
```html
<div style="display:flex;gap:8px;"><input style="flex:1 1 0;" placeholder="Search"><button style="flex-shrink:0;">Go</button></div>
```
5) Align-self
```html
<div style="display:flex;align-items:flex-start;gap:8px;"><div>Short</div><div style="align-self:stretch;background:#e2e8f0;">Stretch</div></div>
```
6) Order
```html
<div style="display:flex;gap:8px;"><button style="order:-1;">Primary</button><button>Secondary</button></div>
```
7) Fixed + fluid
```html
<div style="display:flex;gap:12px;"><aside style="flex:0 0 220px;background:#e2e8f0;">Side</aside><main style="flex:1 1 auto;">Main</main></div>
```
8) Space-between nav
```html
<nav style="display:flex;justify-content:space-between;align-items:center;"><span>Logo</span><button>Login</button></nav>
```
9) Align-content
```html
<div style="display:flex;flex-wrap:wrap;align-content:start;gap:12px;height:200px;border:1px dashed #cbd5e1;"><div style="width:80px;height:60px;background:#e0f2fe;"></div><div style="width:80px;height:60px;background:#fee2e2;"></div></div>
```
10) Sticky footer
```html
<body style="min-height:100vh;display:flex;flex-direction:column;"><main style="flex:1">Content</main><footer style="margin-top:auto;background:#e2e8f0;padding:12px;">Footer</footer></body>
```

**Try it**
- Toolbar-এ একটি সার্চ ইনপুট `flex:1` দিয়ে বাকি বোতাম স্থির রাখুন।
- Card row-তে wrap on/off করে viewport resize করে পার্থক্য দেখুন।
- `align-content` কীভাবে কাজ করে দেখতে multi-row flex wrap গ্রিড বানান।  

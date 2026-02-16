# 16) CSS Grid — 2D লেআউটের জন্য সেরা

**কি শিখবেন**
- Rows/columns, `grid-template-columns/rows`, `fr` ইউনিট, gap।
- grid-column/row span, auto-fit/auto-fill, implicit বনাম explicit grid।

**Code**
```html
<section class="grid">
  <article class="card">ICU</article>
  <article class="card">ER</article>
  <article class="card">Ward</article>
  <article class="card span">Pharmacy</article>
</section>
```
```css
.grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
}
.span { grid-column: span 2; }
```

**Interview takeaways**
- `fr` space-sharing সহজ করে; minmax + auto-fit responsive কার্ডে সেরা।
- Explicit grid (template) বনাম implicit (auto placed) — implicit rows তৈরি হয় স্বয়ংক্রিয়ভাবে।
- grid-area নামকরণ করে টেমপ্লেট স্ট্রিং লেখা যায়; বড় লেআউটে পরিষ্কার।

**আরো উদাহরণ (beginner → advanced)**
1) Fixed tracks
```html
<div style="display:grid;grid-template-columns:200px 1fr;gap:12px;"><aside>Nav</aside><main>Main</main></div>
```
2) Named areas
```html
<div style="display:grid;grid-template-areas:'h h' 's m' 'f f';grid-template-columns:240px 1fr;gap:12px;"><header style="grid-area:h">H</header><aside style="grid-area:s">S</aside><main style="grid-area:m">M</main><footer style="grid-area:f">F</footer></div>
```
3) Justify/align items
```html
<div style="display:grid;grid-template-columns:1fr 1fr;justify-items:stretch;align-items:start;gap:12px;"><div style="border:1px solid #cbd5e1;">A</div><div style="border:1px solid #cbd5e1;">B</div></div>
```
4) Implicit rows
```html
<div style="display:grid;grid-auto-rows:minmax(120px,auto);gap:12px;"><div style="border:1px solid #cbd5e1;">Row</div></div>
```
5) auto-fill vs auto-fit
```html
<div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(180px,1fr));gap:12px;"><div>1</div><div>2</div></div>
```
6) Gap shorthand
```html
<div style="display:grid;gap:12px 20px;"><div>A</div><div>B</div><div>C</div></div>
```
7) Full-bleed hero
```html
<div style="display:grid;grid-template-columns:1fr minmax(0,960px) 1fr;"><section style="grid-column:1/-1;background:#e0f2fe;padding:20px;">Hero</section><p style="grid-column:2;">Body</p></div>
```
8) Track repeat
```html
<div style="display:grid;grid-template-columns:repeat(3,minmax(0,1fr));gap:12px;"><div>1</div><div>2</div><div>3</div></div>
```
9) Justify/align content
```html
<div style="display:grid;justify-content:center;align-content:start;height:200px;border:1px dashed #cbd5e1;"><div style="width:120px;height:80px;background:#e0f2fe;"></div></div>
```
10) Subgrid (if supported)
```html
<div style="display:grid;grid-template-columns:1fr 1fr;gap:8px;" class="list"><div style="display:grid;grid-template-columns:subgrid;grid-column:span 2;">Item spans</div></div>
```

**Try it**
- Bed dashboardে header/sidebar/main/footer grid areas তৈরি করুন।
- এক কার্ডকে দুই কলাম স্প্যান করান; মোবাইলে এক কলামে নামিয়ে দিন।
- `grid-auto-flow: dense` দিয়ে ফাঁক পূরণ পরীক্ষা করুন।  

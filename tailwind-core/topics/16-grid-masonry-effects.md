# 16) Grid masonry-ish & media cards

Using CSS `grid-auto-flow:dense` + arbitrary aspect ratios for media cards (CDN safe).

```html
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-slate-50 text-slate-900 p-6">
  <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 grid-flow-dense">
    <article class="rounded-xl overflow-hidden shadow-sm border border-slate-200 bg-white">
      <img class="w-full aspect-[4/3] object-cover" src="https://images.unsplash.com/photo-1583912266690-2f38ff6f934b?auto=format&fit=crop&w=600&q=80" alt="ICU">
      <div class="p-3 font-semibold">ICU</div>
    </article>
    <article class="rounded-xl overflow-hidden shadow-sm border border-slate-200 bg-white col-span-2 row-span-2">
      <img class="w-full aspect-[16/9] object-cover" src="https://images.unsplash.com/photo-1580281658629-74c0de26c7b0?auto=format&fit=crop&w=900&q=80" alt="Ward">
      <div class="p-3 font-semibold">Ward (wide)</div>
    </article>
    <article class="rounded-xl overflow-hidden shadow-sm border border-slate-200 bg-white">
      <div class="aspect-square bg-gradient-to-br from-blue-500 to-indigo-600"></div>
      <div class="p-3 font-semibold">Gradient tile</div>
    </article>
  </div>
</body>
</html>
```

**Examples (beginner → advanced)**
1) Aspect utility
```html
<img class="w-full aspect-[1/1] object-cover" src="..." alt="square">
```
2) Dense flow
```html
<div class="grid grid-cols-3 gap-3 grid-flow-dense">
  <div class="col-span-2 bg-slate-200 h-24"></div>
  <div class="bg-slate-100 h-24"></div>
</div>
```
3) Span rows
```html
<div class="row-span-2 bg-white shadow rounded h-full">Tall card</div>
```
4) Gradient media
```html
<div class="aspect-[3/2] bg-gradient-to-r from-emerald-400 to-blue-500 rounded-xl"></div>
```
5) Responsive columns
```html
<div class="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-5 gap-4"> ... </div>
```

# 05) Grid layout (auto-fit, spans)

```html
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-slate-50 text-slate-900 p-6">
  <section class="max-w-5xl mx-auto space-y-4">
    <h2 class="text-xl font-semibold">Beds overview</h2>
    <div class="grid gap-4 grid-cols-1 sm:grid-cols-2 lg:grid-cols-3">
      <article class="p-4 rounded-xl border border-slate-200 bg-white shadow-sm">ICU-1</article>
      <article class="p-4 rounded-xl border border-slate-200 bg-white shadow-sm">ICU-2</article>
      <article class="p-4 rounded-xl border border-slate-200 bg-white shadow-sm lg:col-span-2">Ward A (spans)</article>
    </div>
  </section>
</body>
</html>
```

- `grid-cols-1 sm:grid-cols-2 lg:grid-cols-3` for responsive columns.
- `col-span-2` / `lg:col-span-2` to stretch items.

**Examples (beginner → advanced)**
1) Two-column grid
```html
<div class="grid grid-cols-2 gap-4">
  <div class="bg-slate-100 p-3 rounded">A</div>
  <div class="bg-slate-100 p-3 rounded">B</div>
</div>
```
2) Responsive auto-fit
```html
<div class="grid gap-3 grid-cols-1 sm:grid-cols-2 lg:grid-cols-4">
  <div class="p-3 bg-white shadow rounded">Card</div>
  <div class="p-3 bg-white shadow rounded">Card</div>
</div>
```
3) Span columns
```html
<div class="grid grid-cols-3 gap-4">
  <div class="col-span-2 bg-emerald-100 p-3 rounded">Wide</div>
  <div class="bg-emerald-50 p-3 rounded">Narrow</div>
</div>
```
4) Equal heights via grid
```html
<div class="grid grid-cols-3 gap-4 auto-rows-fr">
  <div class="p-3 bg-white shadow rounded">Short</div>
  <div class="p-3 bg-white shadow rounded">Very long content<br>line 2<br>line 3</div>
  <div class="p-3 bg-white shadow rounded">Medium</div>
</div>
```
5) Grid template areas (utility)
```html
<div class="grid gap-4 grid-cols-4 grid-rows-[auto_1fr_auto]">
  <header class="col-span-4 bg-slate-900 text-white p-3 rounded">Header</header>
  <aside class="col-span-1 bg-slate-100 p-3 rounded">Side</aside>
  <main class="col-span-3 bg-white p-3 rounded shadow">Main</main>
  <footer class="col-span-4 bg-slate-100 p-3 rounded">Footer</footer>
</div>
```

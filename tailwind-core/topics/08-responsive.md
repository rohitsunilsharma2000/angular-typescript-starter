# 08) Responsive basics (mobile-first)

```html
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-slate-50 text-slate-900 p-6 space-y-6">
  <header class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 bg-white border border-slate-200 rounded-xl px-4 py-3 shadow-sm">
    <div>
      <h1 class="text-xl font-semibold">Patients</h1>
      <p class="text-sm text-slate-600">Responsive toolbar switches layout.</p>
    </div>
    <div class="flex gap-2">
      <input class="rounded-lg border border-slate-300 px-3 py-2 text-sm w-full sm:w-64" placeholder="Search patients" />
      <button class="px-3 py-2 rounded-md bg-blue-600 text-white text-sm">New</button>
    </div>
  </header>

  <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
    <article class="p-4 rounded-xl border border-slate-200 bg-white shadow-sm">Card 1</article>
    <article class="p-4 rounded-xl border border-slate-200 bg-white shadow-sm">Card 2</article>
    <article class="p-4 rounded-xl border border-slate-200 bg-white shadow-sm">Card 3</article>
  </div>

  <div class="hidden md:block p-4 rounded-lg border border-dashed border-slate-300 text-sm text-slate-600">Visible on md+ only</div>
  <div class="md:hidden p-4 rounded-lg border border-dashed border-slate-300 text-sm text-slate-600">Visible on mobile only</div>
</body>
</html>
```

- Breakpoints: `sm` 640px, `md` 768px, `lg` 1024px.

**Examples (beginner → advanced)**
1) Mobile-first stack
```html
<div class="flex flex-col sm:flex-row gap-3">
  <button class="px-3 py-2 bg-blue-600 text-white rounded-md">Primary</button>
  <button class="px-3 py-2 border border-slate-200 rounded-md">Secondary</button>
</div>
```
2) Responsive grid
```html
<div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
  <div class="p-3 bg-white shadow rounded">Card</div>
  <div class="p-3 bg-white shadow rounded">Card</div>
</div>
```
3) Show/hide
```html
<div class="md:hidden p-2 bg-amber-100 rounded">Mobile notice</div>
<div class="hidden md:block p-2 bg-emerald-100 rounded">Desktop notice</div>
```
4) Responsive container padding
```html
<div class="px-4 sm:px-8 lg:px-16 py-6 bg-slate-100 rounded-xl">Adaptive padding</div>
```
5) Clamp width hero
```html
<section class="w-full max-w-5xl mx-auto px-4 sm:px-8 py-10 bg-white shadow rounded-xl">Hero content</section>
```

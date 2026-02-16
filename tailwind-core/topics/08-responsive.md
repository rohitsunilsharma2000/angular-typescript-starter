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

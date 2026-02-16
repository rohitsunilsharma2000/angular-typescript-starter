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

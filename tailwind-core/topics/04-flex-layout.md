# 04) Flex layout (rows, wrap, align)

```html
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-slate-50 text-slate-900 p-6 space-y-6">
  <nav class="flex items-center gap-4 bg-white border border-slate-200 rounded-xl px-4 py-3 shadow-sm">
    <span class="font-semibold">CityCare</span>
    <div class="flex gap-2 text-sm text-slate-600">
      <a class="hover:text-slate-900" href="#">Beds</a>
      <a class="hover:text-slate-900" href="#">Patients</a>
      <a class="hover:text-slate-900" href="#">Pharmacy</a>
    </div>
    <div class="ml-auto flex gap-2">
      <button class="px-3 py-2 rounded-md bg-blue-600 text-white hover:bg-blue-700 text-sm">New Admit</button>
      <button class="px-3 py-2 rounded-md border border-slate-200 text-sm">Export</button>
    </div>
  </nav>

  <div class="flex flex-wrap gap-4">
    <article class="flex-1 min-w-[220px] p-4 rounded-xl border border-slate-200 bg-white shadow-sm">
      <div class="text-sm text-slate-500">ICU-1</div>
      <div class="text-2xl font-semibold">Occupied</div>
    </article>
    <article class="flex-1 min-w-[220px] p-4 rounded-xl border border-slate-200 bg-white shadow-sm">
      <div class="text-sm text-slate-500">ICU-2</div>
      <div class="text-2xl font-semibold">Empty</div>
    </article>
  </div>
</body>
</html>
```

- `flex`, `items-center`, `justify-between`, `gap-4`, `flex-wrap`, `min-w-[220px]` are enough for most navs/toolbars/cards.

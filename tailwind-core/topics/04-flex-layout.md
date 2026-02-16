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

**Examples (beginner → advanced)**
1) Center content
```html
<div class="flex items-center justify-center h-24 bg-slate-100 rounded-lg">Center</div>
```
2) Toolbar with gap
```html
<div class="flex items-center gap-3 bg-white px-4 py-2 rounded-md shadow">
  <span>Logo</span><input class="border px-3 py-2 rounded-md flex-1" placeholder="Search"><button class="px-3 py-2 bg-blue-600 text-white rounded-md">Go</button>
</div>
```
3) Wrap chips
```html
<div class="flex flex-wrap gap-2">
  <span class="px-2 py-1 bg-slate-200 rounded-full text-sm">ICU</span>
  <span class="px-2 py-1 bg-slate-200 rounded-full text-sm">Ward</span>
</div>
```
4) Fixed + fluid
```html
<div class="flex gap-4">
  <aside class="w-56 bg-slate-100 p-3 rounded-lg">Sidebar</aside>
  <main class="flex-1 p-3 bg-white rounded-lg shadow">Main</main>
</div>
```
5) Justify/align variations
```html
<div class="flex justify-between items-center bg-slate-900 text-white px-4 py-3 rounded-lg">
  <span>Left</span><span>Right</span>
</div>
```

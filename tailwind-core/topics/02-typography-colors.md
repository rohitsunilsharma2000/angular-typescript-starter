# 02) Typography & colors

```html
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-slate-50 text-slate-900 p-6 space-y-4">
  <h1 class="text-3xl font-bold tracking-tight">ICU Dashboard</h1>
  <p class="text-base text-slate-600 leading-relaxed">Monitor beds, pharmacy, and vitals in one place.</p>
  <p class="text-sm text-slate-500">Small helper text.</p>
  <div class="space-x-2">
    <span class="px-2 py-1 rounded-full bg-emerald-100 text-emerald-700 text-sm">Stable</span>
    <span class="px-2 py-1 rounded-full bg-amber-100 text-amber-700 text-sm">Needs Attention</span>
  </div>
</body>
</html>
```

**Tips**
- `leading-relaxed`, `tracking-tight`, `font-semibold` for hierarchy.
- Colors: prefer semantic pairs (bg-emerald-100 + text-emerald-700).

**Examples (beginner → advanced)**
1) Body copy
```html
<p class="text-base leading-relaxed text-slate-700">Readable paragraph text.</p>
```
2) Heading scale
```html
<h1 class="text-3xl font-bold tracking-tight">Page Title</h1>
<h2 class="text-xl font-semibold text-slate-700">Section</h2>
```
3) Semantic badge
```html
<span class="inline-flex items-center px-2 py-1 rounded-full bg-emerald-100 text-emerald-700 text-sm">Stable</span>
```
4) Muted on dark
```html
<div class="bg-slate-900 text-slate-50 p-4 rounded-lg">
  <p class="text-sm text-slate-300">Dark foreground contrast</p>
</div>
```
5) Accent links
```html
<a class="text-blue-600 hover:text-blue-700 font-medium underline decoration-2 underline-offset-4">Docs</a>
```

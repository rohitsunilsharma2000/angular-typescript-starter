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

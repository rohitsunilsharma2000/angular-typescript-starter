# 03) Spacing & sizing

```html
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-white text-slate-900 p-6">
  <div class="max-w-4xl mx-auto space-y-6">
    <section class="p-6 bg-slate-50 rounded-xl border border-slate-200 space-y-4">
      <h2 class="text-xl font-semibold">Spacing scale</h2>
      <div class="flex items-center gap-3">
        <div class="w-10 h-10 bg-blue-100 rounded"></div>
        <div class="w-16 h-16 bg-blue-200 rounded"></div>
        <div class="w-24 h-24 bg-blue-300 rounded"></div>
      </div>
      <p class="text-sm text-slate-600">Use `p-6`, `m-4`, `gap-3`, `w-24`, `max-w-4xl` etc.</p>
    </section>

    <section class="p-6 bg-slate-50 rounded-xl border border-slate-200 space-y-4">
      <h2 class="text-xl font-semibold">Container & center</h2>
      <div class="container mx-auto px-4">
        <div class="bg-emerald-100 text-emerald-900 p-4 rounded-lg">Container auto width + padding</div>
      </div>
    </section>
  </div>
</body>
</html>
```

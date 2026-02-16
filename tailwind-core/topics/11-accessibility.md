# 11) Accessibility helpers

```html
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-slate-50 text-slate-900 p-6 space-y-4">
  <a href="#main" class="sr-only focus:not-sr-only focus:absolute focus:left-4 focus:top-4 focus:px-3 focus:py-2 focus:bg-white focus:border focus:border-slate-300 focus:rounded-md">Skip to main</a>

  <main id="main" class="space-y-4">
    <button class="px-3 py-2 rounded-md border border-slate-300 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:ring-offset-2">Focusable</button>

    <div class="flex gap-3">
      <label class="inline-flex items-center gap-2 text-sm">
        <input type="checkbox" class="rounded border-slate-300 text-blue-600 focus:ring-blue-500"> Alert me
      </label>
      <label class="inline-flex items-center gap-2 text-sm">
        <input type="checkbox" class="rounded border-slate-300 text-blue-600 focus:ring-blue-500" checked> Subscribe
      </label>
    </div>

    <div class="p-4 rounded-lg bg-white border border-slate-200 shadow-sm">
      <h2 class="text-lg font-semibold">Reduced motion</h2>
      <p class="text-sm text-slate-600">Honor OS setting: disable heavy animations.</p>
    </div>
  </main>
</body>
</html>
```

- Use `sr-only` and `focus:` utilities for keyboard users.
- `focus:ring-offset-2` helps rings stand out on colored backgrounds.

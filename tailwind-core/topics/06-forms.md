# 06) Forms & validation

```html
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-slate-50 text-slate-900 p-6">
  <form class="max-w-lg mx-auto space-y-4 bg-white p-6 rounded-xl border border-slate-200 shadow-sm">
    <div>
      <label class="block text-sm font-medium text-slate-700 mb-1" for="pid">Patient ID</label>
      <input id="pid" class="w-full rounded-lg border border-slate-300 px-3 py-2 focus:border-blue-500 focus:ring-2 focus:ring-blue-200" placeholder="P-101" required />
      <p class="mt-1 text-xs text-slate-500">Format: P-###</p>
    </div>
    <div class="grid grid-cols-1 sm:grid-cols-2 gap-3">
      <div>
        <label class="block text-sm font-medium text-slate-700 mb-1">Email</label>
        <input type="email" class="w-full rounded-lg border border-slate-300 px-3 py-2 focus:border-blue-500 focus:ring-2 focus:ring-blue-200" required />
      </div>
      <div>
        <label class="block text-sm font-medium text-slate-700 mb-1">Phone</label>
        <input type="tel" class="w-full rounded-lg border border-slate-300 px-3 py-2 focus:border-blue-500 focus:ring-2 focus:ring-blue-200" pattern="\d{10}" required />
      </div>
    </div>
    <div class="flex items-center gap-2 text-sm">
      <input id="consent" type="checkbox" class="rounded border-slate-300 text-blue-600 focus:ring-blue-500" required />
      <label for="consent">Consent signed</label>
    </div>
    <button class="w-full rounded-lg bg-blue-600 text-white py-2 font-semibold hover:bg-blue-700">Submit</button>
  </form>
</body>
</html>
```

- Focus ring: `focus:ring-2 focus:ring-blue-200`.
- Checkbox styling: `text-blue-600` controls the check color.

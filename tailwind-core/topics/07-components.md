# 07) Components (buttons, badges, cards, nav, modal)

```html
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-slate-50 text-slate-900 p-6 space-y-6">
  <!-- Buttons -->
  <div class="space-x-2">
    <button class="px-4 py-2 rounded-md bg-blue-600 text-white hover:bg-blue-700">Primary</button>
    <button class="px-4 py-2 rounded-md border border-slate-200 hover:bg-slate-100">Ghost</button>
  </div>

  <!-- Badges -->
  <div class="space-x-2">
    <span class="inline-flex items-center gap-1 px-2 py-1 rounded-full bg-emerald-100 text-emerald-700 text-sm">Stable</span>
    <span class="inline-flex items-center gap-1 px-2 py-1 rounded-full bg-amber-100 text-amber-700 text-sm">Pending</span>
  </div>

  <!-- Card list -->
  <div class="grid gap-4 sm:grid-cols-2">
    <article class="p-4 rounded-xl border border-slate-200 bg-white shadow-sm space-y-2">
      <div class="flex items-center justify-between">
        <h3 class="font-semibold">ICU-1</h3>
        <span class="px-2 py-1 rounded-full bg-red-100 text-red-700 text-xs">Occupied</span>
      </div>
      <p class="text-sm text-slate-600">Patient: P-11 | SpO2: 92%</p>
      <button class="text-sm text-blue-600 hover:text-blue-700">View</button>
    </article>
    <article class="p-4 rounded-xl border border-slate-200 bg-white shadow-sm space-y-2">
      <div class="flex items-center justify-between">
        <h3 class="font-semibold">ICU-2</h3>
        <span class="px-2 py-1 rounded-full bg-green-100 text-green-700 text-xs">Empty</span>
      </div>
      <p class="text-sm text-slate-600">Ready for admission</p>
      <button class="text-sm text-blue-600 hover:text-blue-700">View</button>
    </article>
  </div>

  <!-- Modal (native dialog) -->
  <dialog id="assign" class="backdrop:bg-slate-900/50 rounded-xl border border-slate-200 p-6 max-w-sm">
    <h3 class="text-lg font-semibold mb-2">Assign bed?</h3>
    <p class="text-sm text-slate-600 mb-4">Confirm assignment to ICU-2.</p>
    <div class="flex justify-end gap-2">
      <button onclick="assign.close()" class="px-3 py-2 rounded-md border border-slate-200">Cancel</button>
      <button class="px-3 py-2 rounded-md bg-blue-600 text-white" onclick="assign.close()">Confirm</button>
    </div>
  </dialog>
  <button class="px-3 py-2 rounded-md bg-slate-900 text-white" onclick="assign.showModal()">Open modal</button>
</body>
</html>
```

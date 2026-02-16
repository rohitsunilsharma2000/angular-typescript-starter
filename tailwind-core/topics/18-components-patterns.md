# 18) Components & patterns (buttons → skeletons)

Buttons, forms, cards/tables, nav/sidebar/breadcrumbs, modal/dialog, dropdown/tooltip, tabs/accordion, toast, pagination, skeleton.

```html
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-slate-50 text-slate-900 p-6 space-y-6">
  <h1 class="text-2xl font-bold">Patterns</h1>
</body>
</html>
```

**Examples (beginner → advanced)**
1) Buttons (variant + disabled)
```html
<button class="px-4 py-2 rounded-md bg-blue-600 text-white hover:bg-blue-700">Primary</button>
<button class="px-4 py-2 rounded-md border border-slate-200 text-slate-700 disabled:opacity-50" disabled>Disabled</button>
<button class="px-4 py-2 rounded-md bg-blue-600 text-white flex items-center gap-2">
  <span class="h-4 w-4 border-2 border-white border-t-transparent rounded-full animate-spin"></span>Saving
</button>
```
2) Form control with error
```html
<label class="block text-sm font-medium mb-1">Email</label>
<input class="w-full rounded-lg border border-slate-300 px-3 py-2 invalid:border-red-500 invalid:text-red-600" type="email" required>
<p class="mt-1 text-sm text-red-600">Enter a valid email.</p>
```
3) Card list
```html
<div class="grid sm:grid-cols-2 gap-4">
  <article class="p-4 rounded-xl border border-slate-200 bg-white shadow-sm space-y-2">
    <div class="flex items-center justify-between">
      <h3 class="font-semibold">ICU-1</h3>
      <span class="px-2 py-1 text-xs rounded-full bg-red-100 text-red-700">Occupied</span>
    </div>
    <p class="text-sm text-slate-600">Patient P-11 | SpO2 92%</p>
  </article>
</div>
```
4) Table
```html
<table class="min-w-full divide-y divide-slate-200 text-sm">
  <thead class="bg-slate-100 text-slate-600"><tr><th class="px-3 py-2 text-left">Bed</th><th class="px-3 py-2 text-left">Status</th></tr></thead>
  <tbody class="divide-y divide-slate-200 bg-white">
    <tr><td class="px-3 py-2">ICU-1</td><td class="px-3 py-2 text-red-600">Occupied</td></tr>
  </tbody>
</table>
```
5) Navbar + sidebar
```html
<nav class="flex items-center gap-4 px-4 py-3 bg-white shadow-sm border-b border-slate-200">
  <span class="font-semibold">CityCare</span>
  <div class="flex gap-3 text-sm text-slate-600">
    <a class="hover:text-slate-900" href="#">Beds</a>
    <a class="hover:text-slate-900" href="#">Patients</a>
  </div>
</nav>
<aside class="w-60 bg-slate-900 text-slate-100 p-4 space-y-2">
  <a class="block px-3 py-2 rounded hover:bg-slate-800" href="#">Dashboard</a>
</aside>
```
6) Modal (dialog)
```html
<dialog id="modal" class="backdrop:bg-slate-900/50 p-6 rounded-xl border border-slate-200 max-w-sm">
  <p class="text-lg font-semibold mb-3">Confirm?</p>
  <div class="flex justify-end gap-2">
    <button onclick="modal.close()" class="px-3 py-2 rounded-md border">Cancel</button>
    <button class="px-3 py-2 rounded-md bg-blue-600 text-white" onclick="modal.close()">Confirm</button>
  </div>
</dialog>
<button class="px-3 py-2 bg-slate-900 text-white rounded-md" onclick="modal.showModal()">Open modal</button>
```
7) Dropdown / tooltip
```html
<div class="relative inline-block">
  <button class="px-3 py-2 bg-white border rounded-md shadow" aria-haspopup="true">Menu ▾</button>
  <div class="absolute mt-2 w-40 rounded-md border bg-white shadow-lg p-2 text-sm">Item 1</div>
</div>
<button class="relative px-3 py-2 border rounded-md" data-tip="Info">?
  <span class="absolute -top-8 left-1/2 -translate-x-1/2 px-2 py-1 text-xs rounded bg-slate-900 text-white opacity-0 hover:opacity-100">Tooltip</span>
</button>
```
8) Tabs
```html
<div class="flex gap-2 text-sm">
  <button class="px-3 py-2 rounded-md bg-slate-900 text-white">Beds</button>
  <button class="px-3 py-2 rounded-md text-slate-600">Patients</button>
</div>
```
9) Toast/alert
```html
<div class="fixed bottom-4 right-4 flex items-start gap-2 p-4 rounded-lg bg-emerald-600 text-white shadow-lg">
  <span class="font-semibold">Saved</span>
  <button class="text-sm underline" onclick="this.parentElement.remove()">Dismiss</button>
</div>
```
10) Skeleton loader
```html
<div class="space-y-3 p-4 rounded-xl border border-slate-200 bg-white">
  <div class="h-4 rounded bg-slate-200 animate-pulse"></div>
  <div class="h-4 rounded bg-slate-200 animate-pulse w-1/2"></div>
</div>
```

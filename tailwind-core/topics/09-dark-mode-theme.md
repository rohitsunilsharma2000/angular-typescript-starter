# 09) Dark mode & theming (class-based)

```html
<!doctype html>
<html lang="en" class="dark"> <!-- remove 'dark' to default light; toggle via JS -->
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <script src="https://cdn.tailwindcss.com"></script>
  <script>
    tailwind.config = {
      darkMode: 'class',
      theme: {
        extend: {
          colors: { brand: '#2563eb' }
        }
      }
    }
  </script>
</head>
<body class="bg-slate-50 text-slate-900 dark:bg-slate-900 dark:text-slate-50 p-6 space-y-4">
  <button id="toggle" class="px-3 py-2 rounded-md bg-brand text-white">Toggle theme</button>
  <article class="p-4 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 shadow-sm">
    <h2 class="text-xl font-semibold">Dark-ready card</h2>
    <p class="text-sm text-slate-600 dark:text-slate-300">Colors flip via `dark:` prefix.</p>
  </article>
  <script>
    const root = document.documentElement;
    document.getElementById('toggle').onclick = () => root.classList.toggle('dark');
  </script>
</body>
</html>
```

- `dark:` prefix mirrors the light class.
- With CDN you can still set `tailwind.config` inline for colors.

**Examples (beginner → advanced)**
1) Dark text inversion
```html
<p class="text-slate-700 dark:text-slate-200">Adaptive text</p>
```
2) Dual card
```html
<article class="p-4 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800">
  <h3 class="font-semibold">Card</h3>
  <p class="text-sm text-slate-600 dark:text-slate-300">Works in both themes.</p>
</article>
```
3) Button swap
```html
<button class="px-3 py-2 rounded-md bg-blue-600 text-white dark:bg-blue-500 dark:text-slate-900">Toggle</button>
```
4) Themed badge
```html
<span class="px-2 py-1 rounded-full bg-emerald-100 text-emerald-700 dark:bg-emerald-900 dark:text-emerald-200 text-sm">Stable</span>
```
5) JS toggle snippet
```html
<button id="dm" class="px-3 py-2 border rounded-md">Switch</button>
<script>dm.onclick=()=>document.documentElement.classList.toggle('dark');</script>
```

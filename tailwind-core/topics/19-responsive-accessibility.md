# 19) Responsive + Accessibility patterns

Mobile-first, dark mode, focus states, ARIA-friendly components, reduced motion.

```html
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-slate-50 text-slate-900 p-6 space-y-6">
  <a href="#main" class="sr-only focus:not-sr-only focus:absolute focus:left-4 focus:top-4 focus:bg-white focus:px-3 focus:py-2 focus:border focus:rounded">Skip to main</a>
  <main id="main" class="space-y-4">
    <header class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 bg-white border border-slate-200 rounded-xl px-4 py-3 shadow-sm">
      <div>
        <h1 class="text-xl font-semibold">Responsive & A11y</h1>
        <p class="text-sm text-slate-600">Mobile-first + focus-visible</p>
      </div>
      <div class="flex gap-2">
        <input class="rounded-lg border border-slate-300 px-3 py-2 text-sm w-full sm:w-64 focus:ring-2 focus:ring-blue-200" placeholder="Search" />
        <button class="px-3 py-2 rounded-md bg-blue-600 text-white text-sm">Action</button>
      </div>
    </header>
  </main>
</body>
</html>
```

**Examples (beginner → advanced)**
1) Mobile-first stack
```html
<div class="flex flex-col sm:flex-row gap-2">
  <button class="w-full sm:w-auto px-3 py-2 bg-blue-600 text-white rounded-md">Primary</button>
  <button class="w-full sm:w-auto px-3 py-2 border rounded-md">Secondary</button>
</div>
```
2) Dark mode toggle
```html
<button id="darkToggle" class="px-3 py-2 rounded-md border">Toggle dark</button>
<script>darkToggle.onclick=()=>document.documentElement.classList.toggle('dark');</script>
```
3) Focus-visible rings
```html
<a href="#" class="px-3 py-2 rounded-md border border-slate-300 focus:outline-none focus-visible:ring-2 focus-visible:ring-blue-400">Focusable link</a>
```
4) ARIA menu pattern
```html
<button aria-expanded="false" class="px-3 py-2 rounded-md border aria-[expanded=true]:bg-blue-50">Menu</button>
```
5) Reduced motion guard
```html
<div class="motion-reduce:animate-none animate-bounce w-4 h-4 bg-emerald-500 rounded-full"></div>
```

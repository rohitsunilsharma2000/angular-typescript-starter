# 17) Tailwind Core — utility-first toolkit

Covers spacing, colors, typography, breakpoints, flex/grid, positioning, sizing, spacing system, borders/shadows, backgrounds, prose.

```html
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <script src="https://cdn.tailwindcss.com"></script>
  <script>tailwind.config={theme:{extend:{}}}</script>
</head>
<body class="bg-slate-50 text-slate-900 p-6 space-y-6">
  <div class="container mx-auto px-4">
    <h1 class="text-3xl font-bold tracking-tight">Core utilities</h1>
  </div>
</body>
</html>
```

**Examples (beginner → advanced)**
1) Spacing + color + type
```html
<p class="p-4 m-4 bg-blue-50 text-blue-800 rounded-lg text-lg">Padding + margin + color</p>
```
2) Responsive prefixes
```html
<div class="p-4 bg-white rounded-lg shadow sm:bg-emerald-50 md:bg-amber-50 lg:bg-blue-50">sm/md/lg background changes</div>
```
3) Flex alignment
```html
<div class="flex items-center justify-between gap-3 p-3 bg-white rounded shadow">
  <span class="font-semibold">Toolbar</span>
  <button class="px-3 py-2 bg-blue-600 text-white rounded-md">Action</button>
</div>
```
4) Grid columns + spans
```html
<div class="grid grid-cols-2 sm:grid-cols-3 gap-4">
  <div class="col-span-2 bg-slate-100 p-3 rounded">Span 2</div>
  <div class="bg-slate-100 p-3 rounded">1</div>
</div>
```
5) Positioning
```html
<div class="relative h-24 bg-slate-900 text-white rounded-lg">
  <span class="absolute right-3 top-3 px-2 py-1 bg-amber-500 rounded">Badge</span>
</div>
```
6) Sizing + aspect
```html
<div class="w-40 h-24 bg-emerald-100 rounded"></div>
<div class="w-full max-w-md aspect-[16/9] bg-slate-200 rounded"></div>
```
7) Spacing system utilities
```html
<ul class="space-y-2 divide-y divide-slate-200">
  <li class="pt-2 first:pt-0">Row 1</li>
  <li class="pt-2">Row 2</li>
</ul>
```
8) Borders/radius/shadow
```html
<div class="p-4 border border-slate-200 rounded-2xl shadow-lg">Card</div>
```
9) Background gradients/images
```html
<section class="p-6 rounded-2xl text-white bg-gradient-to-r from-indigo-500 to-blue-500">Gradient hero</section>
```
10) Typography plugin (prose)
```html
<article class="prose prose-slate max-w-none">
  <h2>Prose title</h2>
  <p>Tailwind Typography plugin works with CDN.</p>
</article>
<script>tailwind.config={plugins:[tailwindcss.typography?tailwindcss.typography:()=>{}]}</script>
```

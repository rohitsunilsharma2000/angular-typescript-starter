# 01) Tailwind Play CDN — zero build

Copy this into `index.html` and run `live-server`.
```html
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <script src="https://cdn.tailwindcss.com"></script>
  <title>Tailwind CDN Starter</title>
</head>
<body class="bg-slate-50 text-slate-900">
  <div class="p-6 space-y-4">
    <h1 class="text-2xl font-bold">CityCare</h1>
    <p class="text-slate-600">Tailwind via CDN, no build step.</p>
    <button class="px-4 py-2 rounded-md bg-blue-600 text-white hover:bg-blue-700">Admit</button>
  </div>
</body>
</html>
```

**Inline theme config (optional, still no build):**
```html
<script>
  tailwind.config = {
    theme: {
      extend: {
        colors: {
          brand: '#2563eb',
          'brand-muted': '#dbeafe'
        }
      }
    }
  }
</script>
```
Use those tokens: `class="text-brand bg-brand-muted"`.

**Notes**
- CDN is great for demos/prototyping. For prod, move to CLI + purge.
- Avoid dynamic class names (string concatenation) with CDN; write full classes.

**Examples (beginner → advanced)**
1) Beginner — bare minimum
```html
<script src="https://cdn.tailwindcss.com"></script>
<div class="p-4 text-lg font-semibold text-blue-600">Hello Tailwind</div>
```
2) Beginner — inline container
```html
<div class="container mx-auto px-4 py-6 bg-slate-50 rounded-xl">Centered content</div>
```
3) Intermediate — inline config colors
```html
<script>
  tailwind.config = { theme:{ extend:{ colors:{ brand:'#2563eb' }}}}
</script>
<button class="px-4 py-2 rounded-md bg-brand text-white">Brand CTA</button>
```
4) Intermediate — import Google font + apply
```html
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600&display=swap">
<style>html{font-family:'Inter',system-ui;}</style>
<p class="text-slate-700">Inter applied globally.</p>
```
5) Advanced — small layout scaffold
```html
<div class="min-h-screen bg-slate-100">
  <header class="h-14 px-4 flex items-center bg-white shadow">Topbar</header>
  <main class="p-6 space-y-4">
    <section class="p-4 bg-white rounded-xl shadow">Card</section>
  </main>
</div>
```

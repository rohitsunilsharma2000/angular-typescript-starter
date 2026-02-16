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

# 13) Advanced theming (CSS variables + arbitrary values)

Works on CDN: define CSS vars on root or parent, then consume via Tailwind's arbitrary values.

```html
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <script src="https://cdn.tailwindcss.com"></script>
  <style>
    :root {
      --brand: 34 197 94; /* emerald-500 */
      --surface: #0f172a;
      --text: #e2e8f0;
    }
  </style>
</head>
<body class="bg-slate-50 text-slate-900 p-6 space-y-4">
  <article class="p-4 rounded-xl border border-slate-200 bg-[var(--surface)] text-[var(--text)] shadow-lg">
    <h2 class="text-lg font-semibold">Variable-driven card</h2>
    <p class="text-sm text-[color:rgb(var(--brand))]">Accent via CSS vars + arbitrary color.</p>
  </article>
</body>
</html>
```

**Examples (beginner → advanced)**
1) Arbitrary color from var
```html
<div class="p-3 rounded-lg bg-[rgb(var(--brand))]/10 text-[rgb(var(--brand))]">Brand hint</div>
```
2) Gradient using HSL var
```html
<div class="p-4 rounded-xl bg-[linear-gradient(120deg,hsl(var(--brand))/0.2,white)]">Soft gradient</div>
```
3) Card with per-section tokens
```html
<div class="[--card:#0f172a] [--text:#e2e8f0] p-4 rounded-xl bg-[var(--card)] text-[var(--text)]">Scoped theme</div>
```
4) Clamp via arbitrary value
```html
<h1 class="text-[clamp(1.5rem,2vw+1rem,2.4rem)] font-bold">Fluid heading</h1>
```
5) Dark surface + light text combo
```html
<section class="p-6 rounded-2xl bg-[#0b1224] text-slate-100 shadow-xl">Night panel</section>
```

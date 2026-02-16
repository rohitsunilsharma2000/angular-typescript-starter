# 20) Advanced Tailwind — config, @apply, variants, performance, debugging

```html
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <script src="https://cdn.tailwindcss.com"></script>
  <script>
    tailwind.config = {
      theme: {
        extend: {
          colors: { brand: '#2563eb' },
          fontFamily: { display: ['Inter', 'system-ui'] },
          spacing: { '4.5': '1.125rem' }
        }
      }
    }
  </script>
</head>
<body class="bg-slate-50 text-slate-900 p-6 space-y-4">
  <div class="p-4 rounded-xl bg-white shadow-sm border border-slate-200">
    <h2 class="text-lg font-semibold">Advanced config demo</h2>
    <p class="text-sm text-slate-600">Custom colors, fonts, spacing via inline config.</p>
  </div>
</body>
</html>
```

**Examples (beginner → advanced)**
1) Theme extend color + spacing
```html
<script>tailwind.config={theme:{extend:{colors:{brand:'#2563eb'},spacing:{18:'4.5rem'}}}}</script>
<div class="bg-brand text-white px-4 py-2 rounded-md">Brand</div>
```
2) @apply (only in build, shown for reference)
```html
<!-- In a CSS file when using build: -->
<style>
.btn { @apply inline-flex items-center gap-2 px-3 py-2 rounded-md bg-blue-600 text-white hover:bg-blue-700; }
</style>
```
3) Variant composition (group/peer)
```html
<article class="group p-4 border rounded-xl bg-white shadow-sm">
  <h3 class="font-semibold group-hover:text-blue-600">Group hover</h3>
  <p class="text-sm text-slate-600">Child reacts to parent hover.</p>
</article>
<label class="flex items-center gap-2">
  <input type="checkbox" class="peer hidden">
  <span class="px-3 py-2 rounded-md border border-slate-200 peer-checked:bg-blue-600 peer-checked:text-white">Peer checked</span>
</label>
```
4) Animations & transitions
```html
<div class="p-4 rounded-xl bg-white shadow transition hover:-translate-y-1 hover:shadow-lg">Hover lift</div>
<div class="w-3 h-3 rounded-full bg-emerald-500 animate-ping motion-reduce:animate-none"></div>
```
5) Performance & debugging checklist (CDN context)
```html
<ul class="list-disc pl-5 text-sm text-slate-700 space-y-1">
  <li>Prefer concise, static class lists; avoid string concatenation.</li>
  <li>Use DevTools → Computed to see final styles if a class "isn't applying".</li>
  <li>Remember specificity: later classes win; check `dark:`/`sm:` ordering.</li>
  <li>For production, move to build + `content` purge to trim size.</li>
</ul>
```

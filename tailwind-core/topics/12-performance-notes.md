# 12) Performance & when to leave CDN

CDN is perfect for prototypes; for production move to a build so unused classes get purged.

```html
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-slate-50 text-slate-900 p-6 space-y-4">
  <div class="p-4 bg-white border border-slate-200 rounded-xl shadow-sm">
    <h2 class="text-lg font-semibold">CDN caveats</h2>
    <ul class="list-disc pl-5 text-sm text-slate-700 space-y-1">
      <li>Bundle size not tree-shaken; fine for small pages, not for apps.</li>
      <li>Avoid dynamic class names (string concat) — CDN can’t see them.</li>
      <li>Prefer utility-first; keep class lists predictable.</li>
    </ul>
  </div>
  <div class="p-4 bg-white border border-slate-200 rounded-xl shadow-sm">
    <h3 class="font-semibold mb-2">Ready to ship?</h3>
    <ol class="list-decimal pl-5 text-sm text-slate-700 space-y-1">
      <li>Switch to `tailwindcss` CLI or Vite/Angular builder.</li>
      <li>Configure `content` globs for purge.</li>
      <li>Add `@tailwind base; @tailwind components; @tailwind utilities;` in a CSS entry file.</li>
    </ol>
  </div>
</body>
</html>
```

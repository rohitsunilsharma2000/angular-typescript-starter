# 21) Tailwind utility reference (Play CDN) — quick examples

Grouped cheat-sheet with runnable snippets; copy blocks into an HTML file that already loads `https://cdn.tailwindcss.com`.

## Getting started
- **Installation (CDN)**: `<script src="https://cdn.tailwindcss.com"></script>`
- **Editor setup**: enable Tailwind IntelliSense; set filetype to HTML/JS/TSX; turn on Emmet (VS Code).
- **Compatibility**: modern evergreen browsers; prefers-reduced-motion respected; dark mode via class/media.
- **Upgrade guide**: check `https://tailwindcss.com/docs/upgrade-guide` for breaking changes; CDN uses latest stable.

## Core concepts (examples)
1) Styling with utilities
```html
<div class="p-4 bg-blue-50 text-blue-800 rounded-lg">Utility-styled box</div>
```
2) Hover/focus states
```html
<button class="px-3 py-2 bg-slate-900 text-white rounded-md hover:bg-slate-800 focus-visible:ring-2 focus-visible:ring-blue-400">Hover/focus</button>
```
3) Responsive prefixes
```html
<div class="p-4 bg-white sm:bg-emerald-50 md:bg-amber-50 lg:bg-blue-50">sm/md/lg change</div>
```
4) Dark mode
```html
<div class="p-4 bg-white text-slate-900 dark:bg-slate-900 dark:text-slate-50">Dual theme</div>
```
5) Theme variables (inline config)
```html
<script>tailwind.config={theme:{extend:{colors:{brand:'#2563eb'}}}}</script>
<p class="text-brand">Brand text</p>
```
6) Colors
```html
<span class="px-2 py-1 rounded-full bg-emerald-100 text-emerald-700 text-sm">Success</span>
```
7) Custom styles
```html
<style>.card{ @apply p-4 rounded-xl bg-white shadow; }</style> <!-- build step needed -->
```
8) Detecting classes (purge)
```html
<!-- For build: content: ['./src/**/*.{html,ts,tsx}'] to keep used classes -->
```
9) Functions/directives
```css
/* build context */
@tailwind base; @tailwind components; @tailwind utilities;
@layer components { .btn { @apply px-4 py-2 bg-blue-600 text-white rounded-md; } }
```

## Base styles
- **Preflight**: included automatically; resets form and typography defaults.

## Layout (selected utilities)
```html
<div class="grid grid-cols-2 gap-4">grid</div>
<div class="flex items-center justify-between">flex</div>
<div class="aspect-[16/9] bg-slate-200">aspect</div>
<div class="overflow-auto max-h-40">overflow</div>
<div class="sticky top-0 bg-white">sticky</div>
<div class="relative"><span class="absolute right-2 top-2">abs</span></div>
<div class="isolate bg-white/60 backdrop-blur">isolation</div>
```

## Flexbox & Grid
```html
<div class="flex flex-wrap gap-3">
  <div class="flex-1 min-w-[160px] bg-slate-100 p-3">grow</div>
  <div class="w-32 bg-slate-200 p-3">fixed</div>
</div>
<div class="grid grid-cols-3 gap-2 auto-rows-fr">
  <div class="col-span-2 row-span-1 bg-emerald-100 p-2">span</div>
  <div class="bg-emerald-50 p-2">cell</div>
</div>
```

## Spacing & Sizing
```html
<div class="p-4 m-4 bg-slate-100">padding + margin</div>
<div class="space-y-2 divide-y divide-slate-200"><p class="pt-2">space/divide</p><p class="pt-2">row</p></div>
<div class="w-40 h-24 bg-blue-100"></div>
<div class="w-full max-w-lg aspect-[4/3] bg-slate-200"></div>
```

## Typography
```html
<h1 class="text-3xl font-bold tracking-tight">Heading</h1>
<p class="text-sm text-slate-600 leading-relaxed">Body copy.</p>
<p class="text-xs uppercase tracking-widest text-slate-500">Label</p>
<p class="font-variant-numeric tabular-nums">120/80</p>
<p class="line-clamp-2 text-slate-700">Long text clipped after two lines.</p>
```

## Backgrounds & Borders & Effects
```html
<div class="p-4 rounded-2xl bg-gradient-to-r from-indigo-500 to-blue-500 text-white">Gradient</div>
<div class="p-4 border border-dashed border-slate-300 rounded-xl">Border</div>
<div class="p-4 rounded-xl shadow-lg bg-white">Shadow</div>
<div class="p-4 bg-cover bg-center text-white" style="background-image:url('https://images.unsplash.com/photo-1583912266690-2f38ff6f934b?auto=format&fit=crop&w=800&q=80')">BG image</div>
```

## Filters & Backdrop
```html
<img class="w-32 rounded-lg filter sepia" src="https://via.placeholder.com/150" alt="">
<div class="p-6 rounded-2xl bg-white/60 backdrop-blur shadow">Glass card</div>
```

## Tables
```html
<table class="min-w-full text-sm divide-y divide-slate-200">
  <thead class="bg-slate-100 text-slate-600"><tr><th class="px-3 py-2 text-left">Bed</th><th class="px-3 py-2 text-left">Status</th></tr></thead>
  <tbody class="divide-y divide-slate-200 bg-white"><tr><td class="px-3 py-2">ICU-1</td><td class="px-3 py-2 text-red-600">Occupied</td></tr></tbody>
</table>
```

## Transitions & Animation
```html
<button class="px-4 py-2 bg-blue-600 text-white rounded-md transition hover:-translate-y-0.5 hover:shadow-lg">Hover</button>
<div class="w-3 h-3 rounded-full bg-emerald-500 animate-ping motion-reduce:animate-none"></div>
```

## Transforms
```html
<div class="p-4 bg-white rounded shadow transition hover:rotate-3 hover:scale-105 origin-center">Rotate/scale</div>
```

## Interactivity
```html
<input type="checkbox" class="accent-blue-600 rounded border-slate-300">
<div class="cursor-pointer select-none">Pointer + select none</div>
<div class="scroll-smooth"><a href="#top">Scroll smooth</a></div>
```

## SVG
```html
<svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path d="M5 12h14"/></svg>
```

## Accessibility
```html
<button class="px-3 py-2 rounded-md border focus-visible:ring-2 focus-visible:ring-blue-400 focus-visible:ring-offset-2">Focus ring</button>
<div class="sr-only">Screen-reader only text</div>
<button class="px-3 py-2 rounded-md border forced-color-adjust:auto">HC safe</button>
```

## Debugging checklist
- Class not applying? Check typos, breakpoint order (`sm:` before `md:`), dark mode class on `<html>`, and specificity (last class wins).
- Use DevTools → Computed tab to see which rule wins.
- CDN loads latest Tailwind; clear cache if classes seem missing.

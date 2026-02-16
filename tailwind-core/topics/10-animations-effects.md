# 10) Animations & effects

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
          keyframes: {
            pulsecard: { '0%,100%': { transform: 'translateY(0)' }, '50%': { transform: 'translateY(-4px)' } }
          },
          animation: {
            pulsecard: 'pulsecard 1.4s ease-in-out infinite'
          },
          boxShadow: {
            card: '0 10px 30px rgba(15,23,42,0.12)'
          }
        }
      }
    }
  </script>
</head>
<body class="bg-slate-50 text-slate-900 p-6 space-y-6">
  <button class="px-4 py-2 rounded-md bg-blue-600 text-white transition hover:-translate-y-0.5 hover:shadow-lg">Hover lift</button>

  <article class="p-4 rounded-xl bg-white border border-slate-200 shadow-card animate-pulsecard max-w-sm">
    <h3 class="font-semibold">Loading</h3>
    <div class="mt-2 h-2 w-32 bg-slate-200 rounded-full"></div>
  </article>

  <div class="p-6 rounded-2xl border border-slate-200 bg-white/80 backdrop-blur">
    Glass effect card
  </div>
</body>
</html>
```

- Transitions: `transition`, `hover:-translate-y-0.5`, `hover:shadow-lg`.
- Custom keyframes/animation defined inline with `tailwind.config` (works with CDN).

**Examples (beginner → advanced)**
1) Simple hover
```html
<button class="px-4 py-2 bg-blue-600 text-white rounded-md transition hover:bg-blue-700">Hover</button>
```
2) Scale tap
```html
<button class="px-3 py-2 bg-slate-900 text-white rounded-md active:scale-95 transition">Tap</button>
```
3) Pulse badge
```html
<span class="inline-flex items-center px-2 py-1 rounded-full bg-emerald-100 text-emerald-700 animate-pulse">Live</span>
```
4) Custom keyframe use
```html
<div class="p-4 rounded-xl bg-white shadow-card animate-pulsecard">Loading card</div>
```
5) Glass card
```html
<div class="p-6 rounded-2xl border border-white/30 bg-white/70 backdrop-blur shadow-lg">Glassmorphism</div>
```

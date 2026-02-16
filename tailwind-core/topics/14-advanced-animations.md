# 14) Advanced animations (custom keyframes, stagger, prefers-reduced-motion)

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
            rise: { '0%': { opacity:0, transform:'translateY(8px)' }, '100%': { opacity:1, transform:'none' } },
            shimmer: { '100%': { transform:'translateX(100%)' } }
          },
          animation: {
            rise: 'rise 250ms ease-out forwards',
            shimmer: 'shimmer 1.2s ease-in-out infinite'
          }
        }
      }
    }
  </script>
</head>
<body class="bg-slate-50 text-slate-900 p-6 space-y-6">
  <div class="space-y-2">
    <div class="h-3 rounded bg-slate-200 overflow-hidden relative before:absolute before:inset-0 before:bg-gradient-to-r before:from-transparent before:via-white/60 before:to-transparent before:animate-shimmer"></div>
    <div class="h-3 rounded bg-slate-200"></div>
  </div>
  <ul class="space-y-2" id="list"></ul>
  <script>
    const list=document.getElementById('list');
    ['ICU','Ward','Pharmacy'].forEach((txt,i)=>{
      const li=document.createElement('li');
      li.className='p-3 rounded-lg bg-white shadow-sm border border-slate-200 animate-rise motion-reduce:animate-none';
      li.style.animationDelay=`${i*120}ms`;
      li.textContent=txt;
      list.appendChild(li);
    });
  </script>
</body>
</html>
```

**Examples (beginner → advanced)**
1) Hover tilt
```html
<div class="p-4 bg-white rounded-xl shadow transition hover:-translate-y-1 hover:shadow-lg">Hover me</div>
```
2) Active press
```html
<button class="px-3 py-2 bg-slate-900 text-white rounded-md transition active:scale-95">Press</button>
```
3) Skeleton shimmer
```html
<div class="relative overflow-hidden h-3 rounded bg-slate-200">
  <span class="absolute inset-0 bg-gradient-to-r from-transparent via-white/60 to-transparent animate-[shimmer_1.3s_infinite]"></span>
</div>
```
4) Staggered list (JS + classes)
```html
<li class="animate-rise" style="animation-delay:120ms">Item</li>
```
5) Respect reduced motion
```html
<div class="motion-reduce:animate-none animate-ping w-3 h-3 rounded-full bg-emerald-500"></div>
```

# 15) State & data attribute variants (menu, tabs, collapses)

Tailwind supports attribute selectors via arbitrary variants. Use `data-[state=open]:bg-slate-100` etc. Works without a build.

```html
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-slate-50 text-slate-900 p-6 space-y-4">
  <button id="menuBtn" class="px-3 py-2 rounded-md border border-slate-200 bg-white data-[open=true]:bg-blue-50 data-[open=true]:border-blue-200">
    Toggle menu
  </button>
  <div id="menu" class="hidden flex-col gap-2 p-3 rounded-lg border border-slate-200 bg-white shadow-sm">
    <a class="text-sm text-slate-700 hover:text-blue-600" href="#">Beds</a>
    <a class="text-sm text-slate-700 hover:text-blue-600" href="#">Patients</a>
  </div>
  <script>
    const btn=document.getElementById('menuBtn');
    const menu=document.getElementById('menu');
    btn.onclick=()=>{
      const open=btn.dataset.open==='true';
      btn.dataset.open=open?'false':'true';
      menu.classList.toggle('hidden');
    };
  </script>
</body>
</html>
```

**Examples (beginner → advanced)**
1) data-open highlight
```html
<button class="px-3 py-2 rounded-md border data-[open=true]:bg-emerald-50" data-open="true">Open state</button>
```
2) aria-expanded sync
```html
<button aria-expanded="false" class="px-3 py-2 rounded-md border aria-[expanded=true]:bg-blue-50">Accordion</button>
```
3) Checkbox peer styling
```html
<label class="flex items-center gap-2">
  <input type="checkbox" class="peer hidden">
  <span class="px-3 py-2 rounded-md border border-slate-200 peer-checked:bg-blue-600 peer-checked:text-white">Peer variant</span>
</label>
```
4) Group hover card
```html
<article class="group p-4 border rounded-xl bg-white shadow-sm">
  <h3 class="font-semibold group-hover:text-blue-600">Hover title</h3>
  <p class="text-sm text-slate-600 group-hover:text-slate-800">Details</p>
</article>
```
5) Pressed state with active
```html
<button class="px-3 py-2 rounded-md bg-slate-900 text-white active:translate-y-0.5 active:bg-slate-800">Press</button>
```

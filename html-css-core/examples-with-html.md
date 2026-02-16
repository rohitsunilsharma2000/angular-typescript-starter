# HTML & CSS Core — Copy‑paste Examples (HTML included)

Every snippet below is standalone: drop it into an `.html` file and open in a browser. Examples progress beginner → advanced per topic.

## 01 — HTML semantics & ARIA
1) Minimal landmarks
```html
<header>CityCare</header><main><h1>Patients</h1></main><footer>&copy; 2026</footer>
```
2) Label + hint
```html
<label for="pname">Name</label>
<input id="pname" aria-describedby="pname-hint">
<small id="pname-hint">Full legal name</small>
```
3) Definition list
```html
<dl><dt>Heart Rate</dt><dd>88</dd><dt>SpO2</dt><dd>96%</dd></dl>
```
4) Table with caption
```html
<table><caption>Bed Status</caption><thead><tr><th>Bed</th><th>Status</th></tr></thead><tbody><tr><td>ICU-1</td><td>Occupied</td></tr><tr><td>ICU-2</td><td>Empty</td></tr></tbody></table>
```
5) Icon button with aria-label
```html
<button aria-label="Open pharmacy panel"><svg aria-hidden="true" width="16" height="16"></svg></button>
```
6) Landmark order
```html
<header>Top</header><main><section aria-labelledby="labs"><h2 id="labs">Labs</h2></section></main><footer>Contacts</footer>
```
7) Breadcrumb
```html
<nav aria-label="Breadcrumb"><ol><li><a href="/">Home</a></li><li><a href="/patients">Patients</a></li><li aria-current="page">P-101</li></ol></nav>
```
8) Live error
```html
<div role="alert" aria-live="assertive">Please fill insurance ID.</div>
```
9) Search aria-label
```html
<input type="search" aria-label="Search patients" placeholder="Search">
```
10) Status live update
```html
<p id="sync" role="status" aria-live="polite">Syncing...</p><script>setTimeout(()=>sync.textContent='Updated from server',1200);</script>
```

## 02 — Forms, validation & ARIA
1) Required with placeholder
```html
<input type="text" required placeholder="P-101">
```
2) Fieldset + legend
```html
<fieldset><legend>Consent</legend><label><input type="radio" name="consent" value="yes" required> Yes</label><label><input type="radio" name="consent" value="no"> No</label></fieldset>
```
3) Inline error
```html
<input id="phone" pattern="\d{10}" required><p id="err" role="alert" hidden>10-digit only</p><script>phone.oninput=()=>err.hidden=phone.checkValidity();</script>
```
4) Date min/max
```html
<input type="date" min="2026-02-01" max="2026-12-31">
```
5) Form to JSON
```html
<form id="f"><input name="pid"><button>Save</button></form><script>f.onsubmit=e=>{e.preventDefault();console.log(JSON.stringify(Object.fromEntries(new FormData(f))))};</script>
```
6) List invalid fields
```html
<form id="g"><input name="email" type="email" required><button>Submit</button></form><script>g.onsubmit=e=>{e.preventDefault();const bad=[...g.elements].filter(el=>el.willValidate&&!el.checkValidity());alert('Fix: '+bad.map(b=>b.name).join(', '));};</script>
```
7) Input mask hint
```html
<input type="tel" pattern="\d{3}-\d{3}-\d{4}" title="123-456-7890" required>
```
8) Custom validity
```html
<input id="pid"><script>pid.oninput=()=>pid.setCustomValidity(pid.value.startsWith('P-')?'':'Prefix P- required');</script>
```
9) aria-invalid toggle
```html
<input id="room" required><script>room.oninput=()=>room.toggleAttribute('aria-invalid',!room.checkValidity());</script>
```
10) Disable submit while loading
```html
<form id="s"><button id="submit">Submit</button></form><script>s.onsubmit=async e=>{e.preventDefault();submit.disabled=true;submit.textContent='Saving...';await new Promise(r=>setTimeout(r,800));submit.disabled=false;submit.textContent='Submit';};</script>
```

## 03 — CSS layout: flex & grid
1) Flex nav
```html
<nav style="display:flex;gap:8px;"><a>Home</a><a>Beds</a><a>Pharmacy</a></nav>
```
2) Flex wrap cards
```html
<div style="display:flex;gap:12px;flex-wrap:wrap;"><div style="flex:1 1 160px;border:1px solid #ccc;">ICU-1</div><div style="flex:1 1 160px;border:1px solid #ccc;">ICU-2</div></div>
```
3) Grid shell areas
```html
<div style="display:grid;grid-template-areas:'h h''s m''f f';grid-template-columns:220px 1fr;min-height:100vh;"><header style="grid-area:h">Header</header><aside style="grid-area:s">Sidebar</aside><main style="grid-area:m">Main</main><footer style="grid-area:f">Footer</footer></div>
```
4) Auto-fit cards
```html
<div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(180px,1fr));gap:12px;"><article class="card">Bed A</article><article class="card">Bed B</article></div>
```
5) Sticky header layout
```html
<style>.layout{display:grid;grid-template-rows:64px 1fr;height:100vh}.top{position:sticky;top:0;background:#0f172a;color:#fff;padding:12px}.scroll{overflow:auto;padding:12px}</style><div class="layout"><div class="top">Sticky Ops Bar</div><div class="scroll">...long bed list...</div></div>
```
6) Equal-height grid cards
```html
<style>.cards{display:grid;grid-template-columns:repeat(auto-fit,minmax(200px,1fr));grid-auto-rows:1fr;gap:12px;} .cards article{padding:12px;border:1px solid #d0d7de;}</style><div class="cards"><article>ICU-1</article><article>ICU-2</article></div>
```
7) Flex action bar
```html
<div style="display:flex;gap:8px;justify-content:space-between;align-items:center;"><span>Patients</span><button>New</button></div>
```
8) Grid named lines
```html
<div style="display:grid;grid-template-columns:[sidebar]240px [content]1fr [end];gap:16px;"><aside>Nav</aside><main>Main</main></div>
```
9) Clamp page width
```html
<div style="width:clamp(320px,90vw,1100px);margin:0 auto;border:1px dashed #ccc;">Content</div>
```
10) Dense grid
```html
<style>.feed{display:grid;grid-template-columns:repeat(auto-fit,minmax(180px,1fr));grid-auto-flow:dense;gap:10px;} .wide{grid-column:span 2;}</style><div class="feed"><article class="wide">ICU</article><article>Ward</article><article>OPD</article></div>
```

## 04 — CSS architecture & theme
1) Root token
```html
<style>:root{--accent:#2563eb}.link{color:var(--accent)}</style><a class="link">Primary link</a>
```
2) BEM buttons
```html
<style>.btn{padding:8px 12px;border-radius:8px}.btn--primary{background:#2563eb;color:#fff}.btn--danger{background:#ef4444;color:#fff}</style><button class="btn btn--primary">Save</button>
```
3) Utilities
```html
<style>.u-flex{display:flex}.u-gap-sm{gap:8px}.u-center{align-items:center}</style><div class="u-flex u-gap-sm u-center"><span>HR 88</span><span>BP 120/80</span></div>
```
4) Data-theme switch
```html
<style>[data-theme="dark"]{--card:#111827;--text:#e2e8f0}.card{background:var(--card);color:var(--text);padding:12px;border-radius:8px}</style><article class="card" data-theme="dark">Dark card</article>
```
5) Scoped token
```html
<style>.pharmacy{--accent:#f97316}.pharmacy .btn--primary{background:var(--accent)}</style><div class="pharmacy"><button class="btn--primary">Order</button></div>
```
6) :where reset
```html
<style>:where(h1,h2,h3,p){margin:0}.card p{margin-block:8px}</style><div class="card"><h3>Title</h3><p>Body</p></div>
```
7) Component fallback token
```html
<style>:root{--surface:#f8fafc}.card{--card-bg:var(--surface,#fff);background:var(--card-bg);padding:12px;border-radius:8px}</style><div class="card">Tokenized</div>
```
8) Cascade layers
```html
<style>@layer utilities{.u-mb-2{margin-bottom:8px}}</style><p class="u-mb-2">Layered util</p>
```
9) Space scale
```html
<style>:root{--space-1:4px;--space-2:8px}.stack{display:grid;gap:var(--space-2)}</style><div class="stack"><div>Row1</div><div>Row2</div></div>
```
10) Auto dark fallback
```html
<style>@media (prefers-color-scheme: dark){:root{--surface:#0f172a;--text:#e2e8f0}}.card{background:var(--surface,#fff);color:var(--text,#0f172a)}</style><div class="card">Auto dark</div>
```

## 05 — Responsive media
1) Media query padding
```html
<style>@media (max-width:600px){body{padding:12px}}</style><p>Resize to see padding change.</p>
```
2) Fluid heading
```html
<style>h1{font-size:clamp(1.6rem,2vw+1rem,2.4rem)}</style><h1>Fluid</h1>
```
3) Auto-fit grid
```html
<style>.cards{display:grid;grid-template-columns:repeat(auto-fit,minmax(200px,1fr));gap:1rem}</style><div class="cards"><article>ICU</article><article>Ward</article></div>
```
4) Aspect-ratio thumb
```html
<img src="ward.jpg" alt="Ward" style="width:100%;aspect-ratio:4/3;object-fit:cover;border-radius:8px;">
```
5) Container width clamp
```html
<div style="width:min(1100px,92vw);margin:0 auto;border:1px dashed #ccc;">Content</div>
```
6) Picture sources
```html
<picture><source media="(min-width:900px)" srcset="ward-large.jpg"><img src="ward-small.jpg" alt="Ward" style="width:100%;aspect-ratio:16/9;"></picture>
```
7) Srcset + sizes
```html
<img src="bed-640.jpg" srcset="bed-320.jpg 320w, bed-640.jpg 640w, bed-960.jpg 960w" sizes="(max-width:600px) 90vw, 400px" alt="ICU bed">
```
8) Responsive embed
```html
<div class="video" style="aspect-ratio:16/9;width:100%;"><iframe src="https://example.com" title="Demo" style="width:100%;height:100%;border:0;"></iframe></div>
```
9) Clamp spacing
```html
<div style="padding:clamp(16px,4vw,48px);background:#f8fafc;">Section</div>
```
10) Prefers-reduced-data
```html
<style>@media (prefers-reduced-data: reduce){.hero{background-image:none}}</style><div class="hero" style="height:200px;background:#e0f2fe;">Hero</div>
```

## 06 — Components & utilities
1) Pill badge
```html
<span style="display:inline-flex;align-items:center;padding:4px 10px;border-radius:999px;background:#eef2ff;color:#3730a3;">ICU</span>
```
2) Utility stack
```html
<div style="display:flex;gap:8px;flex-wrap:wrap;"><button>Admit</button><button>Cancel</button></div>
```
3) Icon button
```html
<button aria-label="Refresh beds" style="border:1px solid #cbd5e1;padding:8px;border-radius:8px">&#x21bb;</button>
```
4) Disabled button
```html
<button disabled style="opacity:0.55;cursor:not-allowed;">Disabled</button>
```
5) Stack + cluster
```html
<div style="display:flex;flex-direction:column;gap:12px;"><div style="display:flex;gap:12px;flex-wrap:wrap;align-items:center;"><span>HR 88</span><span>BP 120/80</span></div><div><button>Save</button></div></div>
```
6) Focus-visible utility
```html
<style>.focus-ring:focus-visible{outline:2px solid #2563eb;outline-offset:2px}</style><button class="focus-ring">Focusable</button>
```
7) Toggle switch
```html
<label style="display:inline-flex;align-items:center;gap:8px;"><input type="checkbox"> Enable alerts</label>
```
8) Skeleton loader
```html
<style>.skeleton{width:160px;height:14px;border-radius:6px;background:linear-gradient(90deg,#e5e7eb 25%,#f3f4f6 37%,#e5e7eb 63%);background-size:400% 100%;animation:shimmer 1.2s infinite}@keyframes shimmer{100%{background-position:-100% 0}}</style><div class="skeleton"></div>
```
9) Tooltip data-tip
```html
<style>[data-tip]{position:relative}[data-tip]::after{content:attr(data-tip);position:absolute;inset:auto auto 110% 50%;transform:translateX(-50%);background:#111827;color:#fff;padding:4px 8px;border-radius:6px;opacity:0;pointer-events:none;transition:opacity .15s}[data-tip]:hover::after,[data-tip]:focus-visible::after{opacity:1}</style><button data-tip="Edit patient">✎</button>
```
10) Modal dialog
```html
<dialog id="assign"><p>Assign bed?</p><button onclick="assign.close()">Close</button></dialog><button onclick="assign.showModal()">Open</button>
```

## 07 — Performance & tooling
1) Defer JS
```html
<script src="/app.js" defer></script>
```
2) Preload font
```html
<link rel="preload" as="font" href="/fonts/Inter.var.woff2" type="font/woff2" crossorigin>
```
3) Lazy iframe
```html
<iframe src="beds.html" loading="lazy" title="Beds"></iframe>
```
4) Imagemin hint (run)
```html
<!-- Run: npx imagemin assets/* --out-dir=public/img -->
```
5) Inline critical CSS
```html
<style>body{margin:0;font-family:system-ui}.topbar{position:sticky;top:0}</style><link rel="stylesheet" href="/css/main.css" media="print" onload="this.media='all'">
```
6) Preconnect CDN
```html
<link rel="preconnect" href="https://cdn.example.com" crossorigin>
```
7) content-visibility
```html
<style>.patient-card{content-visibility:auto;contain-intrinsic-size:300px}</style><div class="patient-card">Patient</div>
```
8) will-change
```html
<div style="will-change:transform">Drawer</div>
```
9) Cache header (express)
```html
<!-- server: app.use('/assets', express.static('assets', { maxAge: '7d', etag: true })); -->
```
10) Source-map-explorer
```html
<!-- Run: npx source-map-explorer dist/main.*.js -->
```

## 08 — Testing & debugging
1) Contrast check
```html
<!-- Chrome DevTools color picker shows contrast ratio; aim AA 4.5:1 -->
```
2) Tab walk
```html
<!-- Press Tab through form; ensure focus ring visible -->
```
3) Lighthouse CLI
```html
<!-- Run: npx lighthouse http://localhost:5500 --only-categories=accessibility --quiet -->
```
4) Axe with Playwright
```html
<!-- JS: import AxeBuilder from '@axe-core/playwright'; await new AxeBuilder({page}).analyze(); -->
```
5) Visual diff
```html
<!-- Playwright: await expect(page).toHaveScreenshot('beds.png',{fullPage:true}); -->
```
6) CSS outline debug
```html
<style>*{outline:1px solid rgba(255,0,0,0.08);}</style><div>Outline debug</div>
```
7) Grid/flex overlay note
```html
<!-- DevTools Layout panel → toggle Grid/Flex overlays -->
```
8) Network throttle launch
```html
<!-- chromium --remote-debugging-port=9222 ... --force-fieldtrials="Throttling/enable" -->
```
9) Performance mark/measure
```html
<script>performance.mark('s');performance.mark('e');performance.measure('render','s','e');console.log(performance.getEntriesByName('render')[0].duration);</script>
```
10) Reduced motion in Playwright
```html
<!-- await page.emulateMedia({reducedMotion:'reduce'}); -->
```

## 09 — CSS basics
1) Inline style
```html
<p style="color:tomato;">Inline</p>
```
2) Internal style
```html
<style>.badge{background:#e0f2fe;padding:4px 8px}</style><span class="badge">New</span>
```
3) External links
```html
<link rel="stylesheet" href="base.css"><link rel="stylesheet" href="theme.css">
```
4) HSL color
```html
<style>.alert{background:hsl(12 80% 54%)}.alert:hover{background:hsl(12 80% 48%)}</style><div class="alert">Warning</div>
```
5) Viewport hero
```html
<div style="min-height:70vh;padding:4vw;background:#f8fafc;">Hero</div>
```
6) rem sizing
```html
<style>body{font-size:16px}button{padding:.75rem 1.25rem}</style><button>Click</button>
```
7) calc width
```html
<div style="width:calc(30% - 12px);background:#e2e8f0;">Sidebar</div>
```
8) border-box reset
```html
<style>*,*::before,*::after{box-sizing:border-box}</style><div style="width:200px;padding:20px;border:2px solid #000;">Box</div>
```
9) CSS variable starter
```html
<style>:root{--brand:#0ea5e9}.link{color:var(--brand)}</style><a class="link">Brand link</a>
```
10) Shorthand vs longhand
```html
<div style="margin:16px;padding:12px 16px 20px 16px;border:1px solid #cbd5e1;">Card</div>
```

## 10 — Selectors & specificity
1) Grouping
```html
<style>h1,h2,h3{font-family:"Inter"}</style><h2>Title</h2>
```
2) Adjacent sibling
```html
<style>label+input{margin-top:4px}</style><label>Phone</label><input>
```
3) General sibling
```html
<style>.alert~.hint{opacity:.7}</style><div class="alert">Alert</div><p class="hint">Hint</p>
```
4) nth-child
```html
<style>.beds li:nth-child(odd){background:#f8fafc}</style><ul class="beds"><li>1</li><li>2</li><li>3</li></ul>
```
5) Specificity ladder
```html
<style>.card p{color:#0f172a}.card .highlight{color:#2563eb}#main .card .highlight{color:#dc2626}</style><div id="main" class="card"><p class="highlight">Hi</p></div>
```
6) Attribute selector
```html
<style>input[readonly]{background:#f3f4f6}</style><input readonly value="Lock">
```
7) :not
```html
<style>.nav a:not(.active){color:#475569}</style><nav class="nav"><a class="active">Home</a><a>Bed</a></nav>
```
8) :is
```html
<style>:is(h1,h2,h3){margin-bottom:.4em}</style><h3>Heading</h3>
```
9) :where zero spec
```html
<style>.card :where(h3,p){margin:0}</style><div class="card"><h3>H3</h3><p>Body</p></div>
```
10) :has parent
```html
<style>.field:has(input:invalid){border:1px solid #ef4444}</style><div class="field"><input required></div>
```

## 11 — Box model
1) Content-box width
```html
<div style="width:200px;padding:20px;border:2px solid #000;">Content-box</div>
```
2) Border-box reset
```html
<style>*{box-sizing:border-box}</style><div style="width:200px;padding:20px;border:2px solid #000;">Border-box</div>
```
3) Margin collapse demo
```html
<style>p{margin:16px 0}.wrapper{margin:0}</style><div class="wrapper"><p>First</p></div>
```
4) Padding hit-area
```html
<button style="padding:10px 14px;">Tap</button>
```
5) Outline vs border
```html
<input style="padding:8px;border:1px solid #cbd5e1;" onfocus="this.style.outline='2px solid #2563eb';this.style.outlineOffset='2px'">
```
6) Min/max width
```html
<div style="width:100%;max-width:420px;border:1px solid #cbd5e1;">Card</div>
```
7) Negative margin
```html
<div style="margin:-8px -16px 0;padding:12px 16px;background:#fee2e2;">Banner</div>
```
8) Box shadow
```html
<div style="padding:16px;box-shadow:0 10px 30px rgba(0,0,0,0.1);">Panel</div>
```
9) Overflow contain
```html
<div style="overflow:hidden;border-radius:999px;width:120px;"><img src="avatar.jpg" alt="" style="width:140px;"></div>
```
10) Inline-block gap
```html
<span style="display:inline-block;margin-right:-4px;">Tag A</span><span style="display:inline-block;">Tag B</span>
```

## 12 — Display & positioning
1) Inline vs block
```html
<span style="display:block">Block span</span><span>Inline span</span>
```
2) Relative + absolute badge
```html
<div style="position:relative;padding:10px 14px;border:1px solid #cbd5e1;">P-11<span style="position:absolute;top:-4px;right:-4px;width:10px;height:10px;background:#ef4444;border-radius:50%;"></span></div>
```
3) Fixed bar
```html
<div style="position:fixed;bottom:16px;right:16px;background:#2563eb;color:#fff;padding:10px;">Status</div>
```
4) Sticky subheader
```html
<div style="height:160px;overflow:auto;"><h3 style="position:sticky;top:0;background:#fff;">Subhead</h3><p>Long content...</p></div>
```
5) Stacking context
```html
<div style="position:relative;z-index:1;background:#fff;">Modal shell</div>
```
6) Inline-block gap fix
```html
<nav><a style="display:inline-block;margin-right:-4px;">Home</a><a style="display:inline-block;">Beds</a></nav>
```
7) Absolute center
```html
<div style="position:relative;height:200px;background:#e2e8f0;"><div style="position:absolute;inset:0;display:grid;place-items:center;">Centered</div></div>
```
8) display: contents
```html
<div style="display:grid;grid-template-columns:1fr 1fr;"><div style="display:contents"><div>Row1</div><div>Row2</div></div></div>
```
9) Overlay with pointer-events none
```html
<div style="position:fixed;inset:0;background:rgba(15,23,42,.4);pointer-events:none;"></div>
```
10) Sticky in scroll area
```html
<div style="max-height:200px;overflow:auto;border:1px solid #cbd5e1;"><h3 style="position:sticky;top:0;background:#fff;">Beds</h3><p>...</p></div>
```

## 13 — Typography
1) System stack
```html
<style>body{font-family:-apple-system,BlinkMacSystemFont,"Segoe UI",sans-serif}</style><p>System text</p>
```
2) Fluid heading
```html
<style>h1{font-size:clamp(1.6rem,2vw+1rem,2.4rem)}</style><h1>ICU Dashboard</h1>
```
3) Small caps
```html
<p style="text-transform:uppercase;letter-spacing:0.08em;">Vitals</p>
```
4) Underline control
```html
<style>a{text-decoration:none}a:hover{text-decoration:underline;text-decoration-thickness:2px}</style><a href="#">Link</a>
```
5) Tabular numbers
```html
<div style="font-variant-numeric:tabular-nums;">120/80</div>
```
6) Hyphenation
```html
<p style="hyphens:auto;">Pneumonoultramicroscopicsilicovolcanoconiosis</p>
```
7) Text wrap balance
```html
<h1 style="text-wrap:balance;">Monitor patients beds and pharmacy</h1>
```
8) Fluid letter-spacing
```html
<h2 style="letter-spacing:clamp(0.02em,0.4vw,0.08em);">CityCare</h2>
```
9) Variable font weight axis
```html
<p style="font-variation-settings:'wght' 750;">Heavy text</p>
```
10) Ellipsis
```html
<div style="white-space:nowrap;overflow:hidden;text-overflow:ellipsis;max-width:220px;">Very long patient name that will cut</div>
```

## 14 — Backgrounds & borders
1) Solid bg
```html
<span style="background:#fef3c7;padding:6px 10px;border-radius:6px;">Warning</span>
```
2) Cover image
```html
<div style="height:200px;background:url('icu.jpg') center/cover no-repeat;"></div>
```
3) Repeating pattern
```html
<div style="background-image:linear-gradient(#e2e8f0 1px,transparent 1px);background-size:24px 24px;height:120px;"></div>
```
4) Radial gradient
```html
<div style="width:140px;height:140px;background:radial-gradient(circle at 20% 20%,#38bdf8,#0ea5e9);border-radius:12px;"></div>
```
5) Text shadow
```html
<h1 style="text-shadow:0 1px 2px rgba(0,0,0,0.12);">Beds</h1>
```
6) Per-corner radius
```html
<div style="border:1px solid #cbd5e1;border-radius:16px 16px 4px 4px;padding:12px;">Card</div>
```
7) Focus outline
```html
<button style="outline:3px solid transparent;" onfocus="this.style.outline='3px solid #22c55e';this.style.outlineOffset='3px'">Focus me</button>
```
8) Inset shadow
```html
<button style="padding:10px 14px;" onmousedown="this.style.boxShadow='inset 0 2px 6px rgba(0,0,0,0.2)'">Press</button>
```
9) Gradient text
```html
<div style="background:linear-gradient(90deg,#06b6d4,#6366f1);-webkit-background-clip:text;color:transparent;">Gradient text</div>
```
10) Border image
```html
<div style="border:12px solid transparent;border-image:url('frame.png') 30 round;padding:12px;">Framed</div>
```

## 15 — Flexbox
1) Centering
```html
<div style="display:flex;align-items:center;justify-content:center;height:120px;background:#f8fafc;">Center</div>
```
2) Wrap cards
```html
<div style="display:flex;flex-wrap:wrap;gap:12px;"><article style="flex:1 1 200px;border:1px solid #cbd5e1;">ICU</article><article style="flex:1 1 200px;border:1px solid #cbd5e1;">Ward</article></div>
```
3) Column stack
```html
<div style="display:flex;flex-direction:column;gap:8px;"><div>Row1</div><div>Row2</div></div>
```
4) Grow vs shrink
```html
<div style="display:flex;gap:8px;"><input style="flex:1 1 0;" placeholder="Search"><button style="flex-shrink:0;">Go</button></div>
```
5) Align-self
```html
<div style="display:flex;align-items:flex-start;gap:8px;"><div>Short</div><div style="align-self:stretch;background:#e2e8f0;">Stretch</div></div>
```
6) Order
```html
<div style="display:flex;gap:8px;"><button style="order:-1;">Primary</button><button>Secondary</button></div>
```
7) Fixed + fluid
```html
<div style="display:flex;gap:12px;"><aside style="flex:0 0 220px;background:#e2e8f0;">Side</aside><main style="flex:1 1 auto;">Main</main></div>
```
8) Space-between nav
```html
<nav style="display:flex;justify-content:space-between;align-items:center;"><span>Logo</span><button>Login</button></nav>
```
9) Align-content
```html
<div style="display:flex;flex-wrap:wrap;align-content:start;gap:12px;height:200px;border:1px dashed #cbd5e1;"><div style="width:80px;height:60px;background:#e0f2fe;"></div><div style="width:80px;height:60px;background:#fee2e2;"></div></div>
```
10) Sticky footer
```html
<body style="min-height:100vh;display:flex;flex-direction:column;"><main style="flex:1">Content</main><footer style="margin-top:auto;background:#e2e8f0;padding:12px;">Footer</footer></body>
```

## 16 — CSS Grid
1) Fixed tracks
```html
<div style="display:grid;grid-template-columns:200px 1fr;gap:12px;"><aside>Nav</aside><main>Main</main></div>
```
2) Named areas
```html
<div style="display:grid;grid-template-areas:'h h' 's m' 'f f';grid-template-columns:240px 1fr;gap:12px;"><header style="grid-area:h">H</header><aside style="grid-area:s">S</aside><main style="grid-area:m">M</main><footer style="grid-area:f">F</footer></div>
```
3) Justify/align items
```html
<div style="display:grid;grid-template-columns:1fr 1fr;justify-items:stretch;align-items:start;gap:12px;"><div style="border:1px solid #cbd5e1;">A</div><div style="border:1px solid #cbd5e1;">B</div></div>
```
4) Implicit rows
```html
<div style="display:grid;grid-auto-rows:minmax(120px,auto);gap:12px;"><div style="border:1px solid #cbd5e1;">Row</div></div>
```
5) auto-fill vs auto-fit
```html
<div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(180px,1fr));gap:12px;"><div>1</div><div>2</div></div>
```
6) Gap shorthand
```html
<div style="display:grid;gap:12px 20px;"><div>A</div><div>B</div><div>C</div></div>
```
7) Full-bleed hero
```html
<div style="display:grid;grid-template-columns:1fr minmax(0,960px) 1fr;"><section style="grid-column:1/-1;background:#e0f2fe;padding:20px;">Hero</section><p style="grid-column:2;">Body</p></div>
```
8) Track repeat
```html
<div style="display:grid;grid-template-columns:repeat(3,minmax(0,1fr));gap:12px;"><div>1</div><div>2</div><div>3</div></div>
```
9) Justify/align content
```html
<div style="display:grid;justify-content:center;align-content:start;height:200px;border:1px dashed #cbd5e1;"><div style="width:120px;height:80px;background:#e0f2fe;"></div></div>
```
10) Subgrid (if supported)
```html
<div style="display:grid;grid-template-columns:1fr 1fr;gap:8px;" class="list"><div style="display:grid;grid-template-columns:subgrid;grid-column:span 2;">Item spans</div></div>
```

## 17 — Responsive design
1) Nav column on small
```html
<style>@media (max-width:600px){nav{flex-direction:column}}</style><nav style="display:flex;gap:8px;"><a>Home</a><a>Beds</a></nav>
```
2) Fluid container
```html
<div style="width:min(1200px,95vw);margin:0 auto;border:1px dashed #cbd5e1;">Container</div>
```
3) Table to cards
```html
<style>@media (max-width:640px){table,thead,tbody,tr,td{display:block}td::before{content:attr(data-label);font-weight:700}}</style><table><tbody><tr><td data-label="Bed">ICU</td><td data-label="Status">OK</td></tr></tbody></table>
```
4) Responsive img
```html
<img src="x.jpg" alt="Ward" style="max-width:100%;height:auto;">
```
5) Clamp section padding
```html
<div style="padding:clamp(1rem,2vw,2rem);background:#f8fafc;">Section</div>
```
6) Orientation rule
```html
<style>@media (orientation:landscape){.hero{min-height:60vh}}</style><section class="hero" style="background:#e0f2fe;">Hero</section>
```
7) Responsive gap
```html
<div style="display:grid;gap:clamp(10px,2vw,24px);grid-template-columns:repeat(auto-fit,minmax(200px,1fr));"><div>1</div><div>2</div></div>
```
8) Safe-area inset
```html
<header style="padding-top:max(16px,env(safe-area-inset-top));background:#0f172a;color:#fff;">Header</header>
```
9) Container query
```html
<style>@container (min-width:500px){.card{display:grid;grid-template-columns:1fr 1fr}}</style><div style="container-type:inline-size;"><div class="card" style="border:1px solid #cbd5e1;">Responsive card</div></div>
```
10) Reduce motion mobile
```html
<style>@media (prefers-reduced-motion: reduce){*{scroll-behavior:auto}}</style><a href="#target">Jump</a><div id="target">Target</div>
```

## 18 — Transitions & animations
1) Hover shadow
```html
<style>.card{transition:box-shadow 150ms ease}.card:hover{box-shadow:0 10px 20px rgba(0,0,0,0.12)}</style><div class="card" style="padding:16px;border:1px solid #cbd5e1;">Card</div>
```
2) Active scale
```html
<button style="transition:transform 80ms;" onmousedown="this.style.transform='scale(0.98)'" onmouseup="this.style.transform=''">Tap</button>
```
3) Pulse keyframes
```html
<style>@keyframes pulse{0%{transform:scale(1)}50%{transform:scale(1.05)}100%{transform:scale(1)}}.alert{animation:pulse 1.5s ease-in-out infinite}</style><div class="alert" style="padding:10px;background:#fee2e2;">Alert</div>
```
4) Skeleton shimmer
```html
<style>.skeleton{background:linear-gradient(90deg,#e5e7eb 25%,#f8fafc 37%,#e5e7eb 63%);background-size:400% 100%;animation:shimmer 1.4s ease infinite;height:14px;border-radius:6px}@keyframes shimmer{0%{background-position:100% 0}100%{background-position:-100% 0}}</style><div class="skeleton"></div>
```
5) Reduce motion guard
```html
<style>@media (prefers-reduced-motion: reduce){*{animation-duration:.001ms!important;animation-iteration-count:1!important}}</style><div>Safe</div>
```
6) Multi-property transition
```html
<style>.card{transition:transform 160ms ease,box-shadow 160ms ease}.card:hover{transform:translateY(-2px);box-shadow:0 12px 30px rgba(0,0,0,0.14)}</style><div class="card" style="padding:12px;border:1px solid #cbd5e1;">Hover me</div>
```
7) Custom easing
```html
<style>.fab{transition:transform 220ms cubic-bezier(0.22,1,0.36,1)}.fab:hover{transform:translateY(-6px)}</style><button class="fab" style="padding:12px 16px;border-radius:12px;background:#2563eb;color:#fff;">Fab</button>
```
8) Delayed tooltip fade
```html
<style>.tip{opacity:0;transition:opacity 120ms ease 120ms}.btn:hover .tip{opacity:1}</style><button class="btn">Hover<span class="tip" style="margin-left:6px;">Info</span></button>
```
9) Animation fill
```html
<style>@keyframes slide-in{from{transform:translateY(12px);opacity:0}to{transform:none;opacity:1}}.toast{animation:slide-in 180ms ease forwards}</style><div class="toast" style="padding:10px;background:#e0f2fe;">Toast</div>
```
10) Steps animation
```html
<style>@keyframes fill-bar{to{width:100%}}.progress{width:0;height:6px;background:#22c55e;animation:steps(5) fill-bar 2s infinite}</style><div style="width:160px;border:1px solid #cbd5e1;"><div class="progress"></div></div>
```

## 19 — Modern CSS
1) Clamp spacing
```html
<div style="padding:clamp(1rem,2vw,2rem);background:#f8fafc;">Section</div>
```
2) min()/max()
```html
<div style="width:min(320px,28vw);border:1px solid #cbd5e1;">Sidebar</div>
```
3) Object-fit cover
```html
<img src="ward.jpg" alt="" style="width:100%;height:160px;object-fit:cover;">
```
4) Scroll snap
```html
<div style="scroll-snap-type:x mandatory;display:flex;overflow:auto;gap:12px;"><article style="scroll-snap-align:start;min-width:280px;">Card</article><article style="scroll-snap-align:start;min-width:280px;">Card</article></div>
```
5) Container query
```html
<style>@container (min-width:600px){.card{display:grid;grid-template-columns:1fr 1fr}}</style><div style="container-type:inline-size;"><div class="card" style="border:1px solid #cbd5e1;">CQ card</div></div>
```
6) accent-color
```html
<input type="checkbox" style="accent-color:#22c55e">
```
7) :has parent
```html
<div class="field" style="border:1px solid #cbd5e1;padding:8px;"><input required></div><style>.field:has(input:invalid){border-color:#ef4444}</style>
```
8) color-mix
```html
<div style="background:color-mix(in srgb,#0ea5e9 70%,white);padding:12px;border-radius:8px;">Mix</div>
```
9) View transitions
```html
<style>::view-transition-old(root),::view-transition-new(root){animation-duration:180ms}</style>
```
10) @supports guard
```html
<style>@supports(backdrop-filter:blur(10px)){.glass{backdrop-filter:blur(10px)}}.glass{padding:12px;border:1px solid #cbd5e1}</style><div class="glass">Glass</div>
```

## 20 — Advanced effects
1) Drop-shadow filter
```html
<img src="thumb.jpg" alt="" style="filter:drop-shadow(0 10px 20px rgba(0,0,0,0.12));">
```
2) Frosted card
```html
<div style="backdrop-filter:blur(6px);background:rgba(15,23,42,0.3);padding:16px;color:#fff;">Frost</div>
```
3) Multiply blend
```html
<img src="hero.jpg" alt="" style="mix-blend-mode:multiply;width:100%;">
```
4) Custom scrollbar
```html
<style>*::-webkit-scrollbar{height:8px}*::-webkit-scrollbar-thumb{background:#94a3b8;border-radius:999px}</style><div style="overflow:auto;white-space:nowrap;width:220px;">Scroll horizontally to see thumb</div>
```
5) Clip-path avatar
```html
<img src="avatar.jpg" alt="" style="clip-path:circle(50%);width:80px;">
```
6) Gradient border
```html
<div style="position:relative;border:2px solid transparent;background:linear-gradient(#fff,#fff) padding-box,linear-gradient(120deg,#06b6d4,#6366f1) border-box;padding:12px;border-radius:12px;">Border</div>
```
7) Conic gauge
```html
<div style="width:140px;aspect-ratio:1;border-radius:50%;background:conic-gradient(#22c55e 0 72%,#e2e8f0 0);"></div>
```
8) Masked fade
```html
<div style="-webkit-mask-image:linear-gradient(180deg,transparent,#000 10%,#000 90%,transparent);height:140px;overflow:auto;">Long scroll text...</div>
```
9) Grayscale toggle
```html
<img src="ward.jpg" alt="" style="filter:grayscale(1);">
```
10) Blur fallback
```html
<style>@supports not (backdrop-filter:blur(6px)){.frost{background:rgba(15,23,42,0.7)}}</style><div class="frost" style="backdrop-filter:blur(6px);padding:14px;color:#fff;">Fallback</div>
```

## 21 — Accessibility in CSS
1) Focus ring
```html
<style>button:focus-visible,a:focus-visible{box-shadow:0 0 0 3px rgba(37,99,235,0.35)}</style><button>Focus</button>
```
2) Hover/focus parity
```html
<style>.card{border:1px solid #cbd5e1}.card:hover,.card:focus-within{border-color:#2563eb}</style><div class="card" tabindex="0">Card</div>
```
3) Reduced motion
```html
<style>@media (prefers-reduced-motion: reduce){.parallax{background-attachment:initial}}</style><div class="parallax" style="height:120px;background:#e0f2fe;">Parallax</div>
```
4) Forced colors
```html
<style>@media (forced-colors: active){*{border-color:ButtonText!important}}</style><div style="border:1px solid currentColor;padding:8px;">FC</div>
```
5) Skip link
```html
<a class="skip" href="#main" style="position:absolute;left:-999px;">Skip to main</a><main id="main">Main</main><style>.skip:focus{left:12px;top:12px;background:#fff;padding:8px}</style>
```
6) :target highlight
```html
<style>:target{outline:3px solid #22c55e;outline-offset:4px}</style><a href="#labs">Go labs</a><h2 id="labs">Labs</h2>
```
7) sr-only
```html
<style>.sr-only{position:absolute;width:1px;height:1px;padding:0;margin:-1px;overflow:hidden;clip:rect(0,0,0,0);}</style><span class="sr-only">Hidden text</span>
```
8) Error styling
```html
<style>.field[aria-invalid="true"]{border-color:#ef4444}.error{color:#b91c1c;margin-top:4px}</style><div class="field" aria-invalid="true" style="border:1px solid #cbd5e1;padding:6px;">Field</div><p class="error">Required</p>
```
9) Reduced transparency
```html
<style>@media (prefers-reduced-transparency: reduce){.glass{background:#0f172a;backdrop-filter:none}}</style><div class="glass" style="backdrop-filter:blur(8px);padding:10px;color:#fff;">Glass</div>
```
10) Larger tap targets
```html
<style>@media (max-width:640px){button,a{min-height:44px;padding:12px 14px}}</style><button>Tap me</button>
```

## 22 — Performance best practices
1) Avoid deep selector
```html
<style>.card__title{font-weight:600}</style><div class="card"><h3 class="card__title">Title</h3></div>
```
2) Utility reuse
```html
<style>.u-gap-sm{gap:8px}.u-center{display:flex;align-items:center;justify-content:center}</style><div class="u-center u-gap-sm">Centered</div>
```
3) Purge config hint
```html
<!-- purgecss.config.js: module.exports = { content:['./**/*.html'], css:['./dist/styles.css'] }; -->
```
4) Critical inline
```html
<style>header{position:sticky;top:0;background:#fff;}</style>
```
5) Media split
```html
<link rel="stylesheet" href="print.css" media="print">
```
6) :where low specificity
```html
<style>:where(h1,h2,h3){margin:0}</style><h2>No margin</h2>
```
7) Sprite icon
```html
<style>.icon{width:16px;height:16px;background:url('/img/sprite.svg') no-repeat;background-size:200px 200px}.icon--alert{background-position:-20px -40px}</style><span class="icon icon--alert" aria-hidden="true"></span>
```
8) Containment
```html
<div style="contain:content;padding:8px;border:1px solid #cbd5e1;">Widget</div>
```
9) font-display swap
```html
<style>@font-face{font-family:'Inter';src:url('/Inter.woff2') format('woff2');font-display:swap}</style>
```
10) Shadow token
```html
<style>:root{--shadow-sm:0 4px 10px rgba(15,23,42,0.08)}.card{box-shadow:var(--shadow-sm)}</style><div class="card" style="padding:10px;border:1px solid #cbd5e1;">Card</div>
```

## 23 — Preprocessors (SCSS shown as HTML comment when needed)
1) Variable
```html
<!-- SCSS --> $space:12px; .btn{padding:$space;}
```
2) Nested hover
```html
<!-- SCSS --> .btn{color:white;&:hover{background:darken(#2563eb,5%);}}
```
3) Mixin
```html
<!-- SCSS --> @mixin rounded($r){border-radius:$r;} .pill{@include rounded(999px);}
```
4) Partials import
```html
<!-- SCSS --> @use 'tokens'; @use 'buttons';
```
5) Function rem
```html
<!-- SCSS --> @function rem($px){@return $px/16*1rem;} .title{font-size:rem(28);}
```
6) Placeholder extend
```html
<!-- SCSS --> %card{border-radius:12px;padding:16px;} .note{@extend %card;background:#fef3c7;}
```
7) Map + map-get
```html
<!-- SCSS --> $status:(ok:#22c55e,warn:#f59e0b,danger:#ef4444); .badge--warn{background:map-get($status,warn);}
```
8) Loop spacers
```html
<!-- SCSS --> @each $i in 1,2,3 { .mt-#{$i}{margin-top:$i*4px;} }
```
9) Conditional mixin
```html
<!-- SCSS --> @mixin shadow($on:true){@if $on {box-shadow:0 8px 20px rgba(0,0,0,.08);} } .card{@include shadow(false);}
```
10) Forward namespace
```html
<!-- SCSS --> @forward 'tokens' as tok-*; .card{border-radius:tok-$radius;}
```

## 24 — CSS frameworks
1) Spacing utility
```html
<div class="p-4 space-y-2">...</div>
```
2) Flex utilities
```html
<div class="flex items-center gap-2"><button class="btn-primary">Save</button></div>
```
3) Tailwind theme config
```html
<!-- tailwind.config.js: module.exports={theme:{extend:{colors:{accent:'#2563eb'}}}} -->
```
4) Component extraction
```html
<div class="card shadow-sm border rounded-lg p-4">Card</div>
```
5) Purge/JIT
```html
<!-- tailwind.config.js: module.exports={content:['./index.html'],mode:'jit'} -->
```
6) @apply button
```html
<style>.btn{@apply inline-flex items-center gap-2 px-3 py-2 rounded-md text-white bg-blue-600 hover:bg-blue-700;}</style><button class="btn">Admit</button>
```
7) Custom container
```html
<!-- tailwind.config.js: module.exports={theme:{container:{center:true,padding:'1rem',screens:{lg:'1100px'}}}} -->
```
8) Forms plugin
```html
<!-- tailwind.config.js: module.exports={plugins:[require('@tailwindcss/forms')]} -->
```
9) clsx merge
```html
<!-- React: className={clsx('btn', disabled && 'opacity-50 cursor-not-allowed')} -->
```
10) DaisyUI theme
```html
<html data-theme="corporate"><button class="btn btn-primary">Admit</button></html>
```

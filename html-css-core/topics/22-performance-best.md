# 22) Performance & best practices — CSS হালকা রাখুন

**কি শিখবেন**
- Specificity wars এড়ানো, BEM/utility ব্যবহার।
- CSS সাইজ কমানো: purge/unused, minify, critical CSS।
- Architecture: SMACSS/ITCSS হিন্ট।

**Code**
```css
/* BEM + utilities mix */
.card { border: 1px solid #cbd5e1; padding: 1rem; border-radius: 10px; }
.card--alert { border-color: #ef4444; }
.u-flex { display: flex; gap: 0.75rem; }

/* Critical inline (above-the-fold) + async rest */
<style>body{margin:0;font-family:system-ui}</style>
<link rel="preload" as="style" href="/css/main.css">
<link rel="stylesheet" href="/css/main.css" media="print" onload="this.media='all'">
```

**Interview takeaways**
- Low specificity → সহজ override → কম !important।
- CSS bundle কমাতে: unused purge (e.g., PurgeCSS), split critical vs rest, minify।
- Naming conventions (BEM) ও ITCSS স্তর (settings/tools/generic/elements/objects/components/trumps) structure দেয়।

**আরো উদাহরণ (beginner → advanced)**
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

**Try it**
- main.css থেকে অপ্রয়োজনীয় ক্লাস কেটে PurgeCSS চালিয়ে সাইজ তুলুন।
- DevTools Coverage ট্যাবে unused CSS শতাংশ দেখুন।
- BEM রুলে ৩টি কম্পোনেন্ট নাম লিখে specificity ladder কম রাখুন।  

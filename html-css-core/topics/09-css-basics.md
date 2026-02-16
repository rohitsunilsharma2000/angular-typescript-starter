# 09) CSS Basics — syntax, selectors শুরু

**কি শিখবেন**
- CSS কীভাবে কাজ করে (cascade → specificity → inheritance)।
- Inline, internal, external CSS পার্থক্য ও ব্যবহার।
- বেসিক সিনট্যাক্স, সিলেক্টর (element/class/id), রঙ ও ইউনিট।

**Code (external CSS উদাহরণ)**
```html
<!-- index.html -->
<link rel="stylesheet" href="styles.css">
<h1 class="title" id="page-title">CityCare Beds</h1>
```
```css
/* styles.css */
body { font-family: system-ui; }
h1 { color: #2563eb; }       /* element selector */
.title { text-transform: uppercase; } /* class */
#page-title { letter-spacing: 1px; }  /* id */
```

**Interview takeaways**
- Cascade order: browser default < external < internal < inline < !important (avoid).
- External CSS cache-friendly; inline scope ছোট কিন্তু maintainability কম।
- রঙ: hex সংক্ষিপ্ত (#2563eb), rgb(), hsl() → hsl এ hue/sat/light সহজে টিউন।
- ইউনিট: `rem` root-relative; `em` parent-relative; viewport units (`vh/vw`) hero/section-এ কাজে লাগে।

**আরো উদাহরণ (beginner → advanced)**
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

**Try it**
- একই এলিমেন্টে element + class + id রুল লিখে specificity ফল দেখুন।
- `rem` ও `em` দিয়ে বাটন সাইজ করুন; প্যারেন্ট ফন্ট-সাইজ বদলে পার্থক্য লক্ষ্য করুন।
- External CSS-এ মন্তব্য (`/* ... */`) ব্যবহার করে সেকশন আলাদা করুন।  

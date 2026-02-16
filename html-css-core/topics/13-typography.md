# 13) Typography — পড়া সহজ করা

**কি শিখবেন**
- font-family stack, size/weight, line-height, letter-spacing।
- Text align/transform/decoration; web fonts দ্রুত লোড।

**Code**
```html
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap">
<h1 class="title">ICU Dashboard</h1>
<p class="lede">Monitor patients, beds, and pharmacy stock.</p>
```
```css
body { font-family: "Inter", system-ui, -apple-system, sans-serif; line-height: 1.6; }
.title { font-size: 2rem; font-weight: 700; letter-spacing: 0.5px; text-transform: uppercase; }
.lede { font-size: 1rem; color: #475569; }
```

**Interview takeaways**
- Line-height 1.4–1.7 পড়া আরামদায়ক; unitless হলে শিশু এলিমেন্টে উত্তরাধিকার পায়।
- Web font `display=swap` FOIT কমায়; ভ্যারিয়েবল ফন্ট ফাইল সংখ্যা কমায়।
- Typography tokens (size/weight/line-height) নির্ধারণ করে consistency বজায় রাখুন।

**আরো উদাহরণ (beginner → advanced)**
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

**Try it**
- Lede paragraph-এর line-height 1.8 করে দেখুন পড়া কেমন লাগে।
- Body ফন্ট Inter থেকে system-ui-তে বদলে FOIT পার্থক্য দেখুন (Chrome DevTools offline)।
- Numeric data টেবিলে tabular numbers ব্যবহার করুন।  

# 21) Accessibility in CSS — ফোকাস ও মোশন কেয়ার

**কি শিখবেন**
- Focus styles কাস্টমাইজ; prefers-reduced-motion/contrast সম্মান।
- রঙ contrast বেসিক; high-contrast মোড বিবেচনা।

**Code**
```css
:focus-visible {
  outline: 2px solid #0ea5e9;
  outline-offset: 3px;
}

@media (prefers-reduced-motion: reduce) {
  * { scroll-behavior: auto !important; animation: none !important; transition: none !important; }
}

@media (prefers-contrast: more) {
  :root { --accent: #0b63ce; }
  .btn { border: 2px solid currentColor; }
}
```

**Interview takeaways**
- Focus-visible ব্যবহারে মাউস ইউজারদের অপ্রয়োজনীয় outline এড়ায় কিন্তু কিবোর্ড ইউজারদের রক্ষা করে।
- prefers-reduced-motion ও prefers-contrast সম্মান করলে a11y সচেতনতা প্রদর্শিত হয়।
- Contrast ratio AA (4.5:1) লক্ষ্য রাখুন; DevTools/axe দিয়ে মাপুন।

**আরো উদাহরণ (beginner → advanced)**
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

**Try it**
- Intake form-এ skip link যোগ করুন (main-এ জাম্প করে), focus-visible স্টাইল দিন।
- prefers-reduced-motion অন করলে hero animation বন্ধ হচ্ছে কিনা পরীক্ষা করুন।
- Contrast checker দিয়ে badge টেক্সটের contrast যাচাই করুন।  

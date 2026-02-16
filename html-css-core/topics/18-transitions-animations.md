# 18) Transitions & animations — মুভমেন্ট, কিন্তু নিরাপদ

**কি শিখবেন**
- transition + transform (scale/rotate/translate)।
- keyframes animation ও পারফরম্যান্স টিপস।

**Code**
```css
.btn {
  background: #2563eb;
  color: #fff;
  border-radius: 10px;
  padding: 0.7rem 1rem;
  transition: transform 120ms ease, box-shadow 120ms ease;
}
.btn:hover { transform: translateY(-2px); box-shadow: 0 10px 24px rgba(37,99,235,0.25); }
@media (prefers-reduced-motion: reduce) {
  .btn { transition: none; }
}
```

**Interview takeaways**
- Transform/opacity GPU-ফ্রেন্ডলি; layout-affecting props (width/height/left) এড়িয়ে চলুন।
- `prefers-reduced-motion` সম্মান করা a11y সিগন্যাল।
- অল্প ডিউরেশন (120–200ms) UI তে দ্রুত ফিডব্যাক দেয়।

**আরো উদাহরণ (beginner → advanced)**
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

**Try it**
- Admit বাটনে hover lift + focus outline রাখুন; reduced-motion এ lift বন্ধ করুন।
- Skeleton loader যোগ করুন bed list লোডিং সময়।
- একটি modal খুললে fade+scale অ্যানিমেশন দিন, কিন্তু prefers-reduced-motion এ ইনস্ট্যান্ট করুন।  

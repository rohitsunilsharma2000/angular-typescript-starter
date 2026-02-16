# 10) Selectors & specificity — নির্বাচন শিখুন

**কি শিখবেন**
- Class, id, grouping, universal, descendant/child/sibling, attribute selectors।
- Pseudo-classes (`:hover`, `:focus`, `:nth-child`) ও pseudo-elements (`::before`, `::after`)।
- Specificity নিয়ম: inline > id > class/attr/pseudo > element > universal।

**Code**
```html
<ul class="beds">
  <li class="bed bed--icu" data-status="occupied">ICU-1</li>
  <li class="bed bed--ward" data-status="empty">Ward-2</li>
</ul>
```
```css
.bed { padding: 8px; }
.bed--icu { color: #ef4444; }                 /* class */
.beds li[data-status="empty"] { color: #16a34a; } /* attribute */
.beds li:first-child { font-weight: 700; }    /* pseudo-class */
.bed::before { content: "🛏 "; }               /* pseudo-element */
```

**Interview takeaways**
- Descendant (`.a .b`) vs child (`.a > .b`) specificity সমান কিন্তু matching scope আলাদা।
- Attribute selectors শক্তিশালী; data-* দিয়ে JS হিন্ট ও স্টাইল একসাথে।
- Pseudo-element দু’টাই (::before/::after) inline content যোগ করতে সেরা উপায়।
- Specificity ladder মনে রাখুন; !important এড়িয়ে চলুন।

**আরো উদাহরণ (beginner → advanced)**
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

**Try it**
- Pharmacy টেবিলে attribute selector `td[data-low="true"]` লাল করুন।
- `:not()` ব্যবহার করে “empty” বাদে সব bed bold করুন।
- একই এলিমেন্টে সংঘর্ষ তৈরি করুন এবং DevTools-এ “Styles” প্যানেলে winning rule দেখুন।  

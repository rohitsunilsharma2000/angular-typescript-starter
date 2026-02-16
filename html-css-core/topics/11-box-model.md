# 11) Box model — padding/border/margin বোঝা

**কি শিখবেন**
- Content → padding → border → margin স্তর।
- `box-sizing: border-box` কেন ডিফল্ট ভালো।
- Margin collapse কবে হয় এবং কীভাবে এড়াবেন।

**Code**
```html
<div class="card">ICU-1</div>
```
```css
* { box-sizing: border-box; }
.card {
  width: 200px;
  padding: 12px;
  border: 2px solid #cbd5e1;
  margin: 16px;
}
```

**Interview takeaways**
- `box-sizing: border-box` এ width প্যাডিং+বর্ডার সহ হয়—layout কম চমক।
- Margin collapse শুধুমাত্র উল্লম্ব ব্লক মার্জিনে (parent/child ও sibling) ঘটে; padding বা border থাকলে ঠেকানো যায়।
- Debug: DevTools box model ভিউ দেখুন; layout shift ধরতে সাহায্য করে।

**আরো উদাহরণ (beginner → advanced)**
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

**Try it**
- একই width-এ content-box বনাম border-box তুলনা করুন।
- Margin collapse থামাতে parent-এ `padding:1px` বা `overflow:hidden` সেট করে দেখুন।
- Card grid-এ `gap` বনাম `margin` ব্যবহার করে spacing ডিবাগ করুন।  

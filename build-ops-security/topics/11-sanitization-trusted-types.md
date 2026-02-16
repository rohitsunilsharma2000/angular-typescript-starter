# 11) Sanitization & trusted types

Hospital rich text (notes) বা external HTML embed এ XSS ঝুঁকি; Angular sanitization ও Trusted Types ব্যবহার করুন।

## Why this matters (real-world)
- XSS হলে patient ডেটা চুরি সম্ভব।
- নিরাপদ bypass ছাড়া sanitize জরুরি।

## Concepts
### Beginner
- Angular built-in sanitization (bindings auto-sanitize)।
- DOM bypassSecurityTrust* কেন এড়াতে হবে।
### Intermediate
- DomSanitizer safeValue; sanitizer.pipe।
- Trusted Types ধারণা; policy নাম।
### Advanced
- CSP + Trusted Types combo; only allow vetted policy; sanitizer service abstraction।

## Copy-paste Example
```ts
// safe-html.pipe.ts
import { Pipe, PipeTransform } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
@Pipe({ name: 'safeHtml', standalone: true })
export class SafeHtmlPipe implements PipeTransform {
  constructor(private sanitizer: DomSanitizer) {}
  transform(value: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(value); // use only on vetted content
  }
}
```
```html
<!-- patient-notes.component.html -->
<div [innerHTML]="note | safeHtml"></div>
```
```md
Trusted Types starter (documented only):
- Enable CSP header: `Content-Security-Policy: trusted-types default;` (rollout carefully)
- Create policy in app init: `window.trustedTypes.createPolicy('hmsPolicy', { createHTML: (s) => s });`
```

## Try it
- Beginner: unsafe <script> সহ string bind করে দেখুন Angular block করে।
- Advanced: safeHtml pipe ব্যবহার সীমিত করুন (only vetted server output), লিন্ট নিয়ম যোগ করুন bypassSecurityTrust* নিষিদ্ধ করতে।

## Common mistakes
- যে কোন HTML এ bypassSecurityTrustHtml ব্যবহার করা।
- Trusted Types policy না বানিয়ে header সেট।

## Interview points
- Angular sanitization ডিফল্ট; bypass সতর্ক; Trusted Types concept।

## Done when…
- Sanitization স্ট্রাটেজি নথিভুক্ত; bypass ব্যবহারে সীমা।
- Trusted Types rollout পরিকল্পনা উল্লেখ।

# 09) Lazy images + CLS control

Patients grid/doctor cards-এ ছবি থাকলে layout shift এড়াতে dimension ঠিক রাখুন এবং lazy-load করুন।

## Why this matters (real-world)
- CLS কমে; মোবাইলে কম ঝাঁকুনি।
- ব্যান্ডউইডথ বাঁচে (non-critical image lazy)।
- ইন্টারভিউ: “How to avoid CLS with images?”

## Concepts
### Beginner
- `loading="lazy"`, width/height বা aspect-ratio সেট।
### Intermediate
- `fetchpriority="high"` critical hero image; `decoding="async"`; responsive `srcset`।
### Advanced
- Placeholder blur/skeleton; intersection observer fallback; preconnect to CDN।

## Copy-paste Example
```html
<!-- patient-card.component.html -->
<article class="card">
  <img
    [src]="photoUrl"
    [attr.loading]="lazy ? 'lazy' : 'eager'"
    [attr.decoding]="'async'"
    [attr.fetchpriority]="hero ? 'high' : 'auto'"
    width="160" height="160"
    style="aspect-ratio:1/1; object-fit:cover;"
    alt="Patient photo" />
  <div>
    <h3>{{ name }}</h3>
    <p>{{ ward }}</p>
  </div>
</article>
```
```css
.card { width: 220px; min-height: 240px; display: grid; gap: 8px; background: #fff; padding: 12px; border-radius: 10px; }
```
```ts
// patient-card.component.ts
import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
@Component({
  standalone: true,
  selector: 'hms-patient-card',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './patient-card.component.html',
  styleUrls: ['./patient-card.component.css']
})
export class PatientCardComponent {
  @Input() name = '';
  @Input() ward = '';
  @Input() photoUrl = '';
  @Input() hero = false;
  @Input() lazy = true;
}
```

## Try it
- Beginner: loading="lazy" করে Lighthouse CLS ফল দেখুন।
- Advanced: hero card-এ fetchpriority="high" সেট করে LCP তুলনা করুন।

## Common mistakes
- width/height বাদ দেওয়া → CLS।
- `loading="lazy"` সব ইমেজে দিয়ে above-the-fold ছবিও দেরি করা।

## Interview points
- CLS এড়াতে dimensions + aspect-ratio; lazy vs eager tradeoff।

## Done when…
- সব non-critical ইমেজ lazy; critical hero eager+fetchpriority।
- Dimensions/aspect-ratio সেট।
- CLS < 0.1 lab।

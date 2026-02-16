# 10) Virtual scroll + trackBy

Hospital bed list বা billing history 10k রো হলে DOM ভারী। CDK virtual scroll + trackBy দিয়ে হালকা রাখুন।

## Why this matters (real-world)
- বড় তালিকায় স্ক্রল স্মুথ থাকে।
- DOM nodes কম → মেমরি ও রেন্ডার খরচ কম।
- ইন্টারভিউ: “How handle 10k rows?”

## Concepts
### Beginner
- CDK virtual scroll সেটআপ; itemSize নির্ধারণ।
- trackBy id।
### Intermediate
- Dynamic height এ autosize; sticky headers সতর্কতা।
- Filtering + virtualization pitfalls।
### Advanced
- Recycling + scroll restoration; viewport check for analytics (visible items)।

## Copy-paste Example
```ts
// app/features/beds/beds-virtual.component.ts
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CdkVirtualScrollViewport, ScrollingModule } from '@angular/cdk/scrolling';

type Bed = { id: string; ward: string; occupied: boolean };
@Component({
  standalone: true,
  selector: 'hms-beds-virtual',
  imports: [CommonModule, ScrollingModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <cdk-virtual-scroll-viewport itemSize="56" class="h-96 w-full border rounded">
      <div *cdkVirtualFor="let bed of beds; trackBy: trackById" class="flex justify-between px-3 py-2 border-b">
        <span>{{ bed.id }} ({{ bed.ward }})</span>
        <span [class.text-green-600]="!bed.occupied" [class.text-red-600]="bed.occupied">
          {{ bed.occupied ? 'Occupied' : 'Free' }}
        </span>
      </div>
    </cdk-virtual-scroll-viewport>
  `
})
export class BedsVirtualComponent {
  beds: Bed[] = Array.from({ length: 5000 }).map((_, i) => ({
    id: 'B' + i,
    ward: i % 2 === 0 ? 'ICU' : 'GEN',
    occupied: i % 3 === 0,
  }));
  trackById = (_: number, bed: Bed) => bed.id;
}
```

## Try it
- Beginner: itemSize ভুল মান দিলে জাম্প কেমন হয় দেখুন; সঠিক মান সেট করুন।
- Advanced: filter যোগ করুন; ফিল্টার করলে viewport reset করুন যাতে jump না হয়।

## Common mistakes
- itemSize না দিয়ে variable height আইটেমে ভুল scroll range।
- trackBy বাদ দিলে recycling কার্যকর না।

## Interview points
- CDK virtual scroll vs pagination tradeoff; itemSize importance।

## Done when…
- Virtual scroll চালু; smooth scroll।
- trackBy আছে; itemSize ঠিক।
- Large list loadেও ফ্রেম ড্রপ নেই (DevTools FPS)।

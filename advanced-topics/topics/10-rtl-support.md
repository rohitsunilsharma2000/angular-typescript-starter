# 10) RTL support

আরবি/হিব্রু ক্লিনিকের জন্য HMS UI RTL হওয়া দরকার; Layout/spacing/flex দিক উল্টাতে পরিকল্পনা করুন।

## Why this matters (real-world)
- RTL বাজারে একই UI চালাতে হবে।
- RTL না করলে ফর্ম/টেবিল পড়া কঠিন।

## Concepts
### Beginner
- `dir="rtl"` attribute; logical properties (`margin-inline-start`).
### Intermediate
- Angular i18n build per locale with `--localize`; RTL stylesheet; direction service।
### Advanced
- Component-level RTL tests; Storybook RTL decorator; icon mirroring।

## Copy-paste Example
```ts
// rtl-toggle.service.ts
import { Injectable } from '@angular/core';
@Injectable({ providedIn: 'root' })
export class RtlService {
  enable() { document.documentElement.setAttribute('dir', 'rtl'); }
  disable() { document.documentElement.removeAttribute('dir'); }
}
```
```css
/* use logical properties */
.card { padding-inline: 12px; }
```
```ts
// rtl story decorator (storybook)
export const withRtl = (Story: any) => ({ props: {}, template: `<div dir="rtl"><story /></div>` });
```

## Try it
- Beginner: dir="rtl" সেট করে patient card spacing দেখুন।
- Advanced: Storybook এ RTL decorator যোগ করে visual diff নিন।

## Common mistakes
- margin-left/right hardcode করা।
- Icons mirror না করা (arrow)।

## Interview points
- Logical properties; dir attribute; RTL testing strategy।

## Done when…
- RTL toggle কাজ করে।
- Logical CSS ব্যবহৃত; hardcoded LTR margin নেই।
- RTL story/test আছে।

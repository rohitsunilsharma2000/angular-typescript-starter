# 08) i18n basics

HMS অ্যাপ বাংলা/ইংরেজি দু ভাষায় চলবে; Angular i18n বিল্ট-ইন ব্যবহার করুন।

## Why this matters (real-world)
- বহুভাষায় নার্স/অ্যাডমিন ব্যবহার করতে পারবে।
- ভুল অনুবাদে মেডিক্যাল ভুল কমানো যায়।

## Concepts
### Beginner
- i18n attribute, extraction (`ng extract-i18n`), translation file।
### Intermediate
- Build per locale; locale data import; date/number pipe locale-aware।
### Advanced
- Runtime locale switch (lazy locale data); plural/gender handled in ICU (next topic)।

## Copy-paste Example
```html
<!-- patient-card.component.html -->
<h2 i18n="@@patientName">Patient Name</h2>
<p>{{ patientName }}</p>
<button i18n="@@viewDetails">View details</button>
```
```bash
# extract
ng extract-i18n --output-path src/locale
```
```ts
// main.ts locale data
import { registerLocaleData } from '@angular/common';
import localeBn from '@angular/common/locales/bn';
registerLocaleData(localeBn);
```
```json
<!-- src/locale/messages.bn.xlf excerpt -->
<trans-unit id="patientName" datatype="html">
  <target>রোগীর নাম</target>
</trans-unit>
```

## Try it
- Beginner: একটি কম্পোনেন্টে i18n অ্যাট্রিবিউট দিয়ে extraction চালান।
- Advanced: bn locale build করে UI দেখুন; date pipe লোকালাইজ হয় কিনা।

## Common mistakes
- Hardcoded টেক্সট; meaning/context না দেওয়া।
- Locale data register না করা।

## Interview points
- Angular built-in i18n flow; extraction; locale data।

## Done when…
- i18n markers যোগ; messages ফাইল জেনারেট।
- অন্তত দুই লোকাল build/test করা হয়েছে।

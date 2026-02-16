# 09) Locale + ICU messages

Appointments শিডিউল: ICU plural message, gendered pronoun—ICU syntax দিয়ে সঠিক অনুবাদ।

## Why this matters (real-world)
- ভুল plural/gender UX বিভ্রান্তিকর।
- বাংলা/ইংরেজি উভয় অনুবাদ সামঞ্জস্যপূর্ণ।

## Concepts
### Beginner
- ICU plural: `i18n="@@apptCount"` with `{count, plural, one {...} other {...}}`।
### Intermediate
- Gender select; placeholders; nested ICU।
### Advanced
- Message reuse, tooling (xliffmerge), RTL impacts on messages।

## Copy-paste Example
```html
<!-- appointment-count.component.html -->
<p i18n="@@apptCount">{count, plural, one {# appointment today} other {# appointments today}}</p>
<p i18n="@@nurseGreet">{gender, select, male {He is ready} female {She is ready} other {They are ready}}</p>
```
```json
<!-- messages.bn.xlf excerpt -->
<trans-unit id="apptCount">
  <target>{count, plural, one {# টি অ্যাপয়েন্টমেন্ট আজ} other {# টি অ্যাপয়েন্টমেন্ট আজ}}</target>
</trans-unit>
<trans-unit id="nurseGreet">
  <target>{gender, select, male {তিনি প্রস্তুত} female {তিনি প্রস্তুত} other {তারা প্রস্তুত}}</target>
</trans-unit>
```

## Try it
- Beginner: plural message যোগ করে extraction করুন।
- Advanced: nested ICU (plural + select) তৈরি করে bn অনুবাদ করুন।

## Common mistakes
- ICU placeholder নাম না মিললে build fail।
- plural rule hardcode করা।

## Interview points
- ICU plural/select; translation workflow।

## Done when…
- অন্তত এক plural ও এক select message অনুবাদসহ।
- Build দুই লোকালে সফল।

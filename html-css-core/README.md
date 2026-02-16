# HTML & CSS Core — Startup Interview Track (Bengali, 2-week plan)

হাসপাতাল ম্যানেজমেন্ট ডোমেইন ব্যবহার করে HTML/CSS ফাউন্ডেশন থেকে ইন্টারভিউ-রেডি পর্যায়ে পৌঁছানোর জন্য সংক্ষিপ্ত, প্র্যাক্টিক্যাল ট্র্যাক।

## কার জন্য
- B.Tech fresher / জুনিয়র ইঞ্জিনিয়ার
- ভারতীয় স্টার্টআপ ইন্টারভিউ (frontend/Jr. web dev) ফোকাস
- Angular/TypeScript ট্র্যাকের সাথে সমান্তরালে নেওয়া যায়

## দ্রুত শুরু (tooling)
- Node LTS + `npm i -g live-server` (বা VS Code Live Server extension)।
- `cd html-css-core/demos/hospital-landing && live-server` → ডেমো পেজ দেখুন।
- Browser support: modern evergreen (Chrome/Edge/Firefox). CSS clamp(), Grid, flex gap দরকার।

## ২-সপ্তাহের স্টাডি প্ল্যান (সংক্ষিপ্ত)
- Day 1: HTML semantics, accessible structure, landmarks
- Day 2: Forms basics + labels + inputs for patient intake
- Day 3: CSS basics, box model, typography, colors, variables
- Day 4: Flexbox essentials (nav, cards); Try: bed cards row wrap
- Day 5: CSS Grid (layout shell, dashboard areas)
- Day 6: Responsive techniques (fluid type, clamp, media queries)
- Day 7: Component styling patterns (BEM/utility), tokens/theming
- Day 8: Forms advanced (validation states, focus/hover), ARIA for errors
- Day 9: Layout composites (sticky header/footer, sidebar)
- Day 10: Animation/motion basics (prefers-reduced-motion safe)
- Day 11: Assets & performance (images, fonts, critical CSS)
- Day 12: Tooling (PostCSS/SCSS optional), lint/format, stylelint hint
- Day 13: Debugging & testing (DevTools, a11y audits, Lighthouse)
- Day 14: Mini project build + mock interview Q&A

## Most-asked HTML/CSS interview questions (স্টার্টআপে)
1. Semantic tags কেন জরুরি? `<div>` vs `<main>/<section>`?
2. Flexbox vs Grid পার্থক্য ও কখন কোনটা?
3. Responsive units: `px` vs `rem` vs `%` vs `vw/vh` vs `clamp()`?
4. Form accessibility: label-for, aria-describedby, error messaging কিভাবে করবেন?
5. Specificity কিভাবে কাজ করে? কনফ্লিক্ট হলে কীভাবে ডিবাগ?
6. BEM/utility ক্লাস প্যাটার্নের সুবিধা কী?
7. Box model: padding/border/margin, `box-sizing: border-box` কেন প্র্যাকটিস?
8. Positioning: relative/absolute/fixed/sticky পার্থক্য ও ব্যবহার।
9. CSS performance: layout thrash এড়াতে কী করবেন? heavy shadow/filters এর প্রভাব?
10. prefers-reduced-motion কী? কিভাবে হ্যান্ডল করবেন?

## ফোল্ডার গাইড
- `topics/` — ছোট ছোট Markdown নোট (বাংলা ব্যাখ্যা + English কোড)।
- `demos/` — রানযোগ্য উদাহরণ; hospital data দিয়ে।
- `README.md` — এই ওভারভিউ + প্ল্যান + প্রশ্ন।

## এগিয়ে কী
1) `topics/01-html-semantics-aria.md` দিয়ে শুরু করুন।  
2) প্রতিটি “Try it” টাস্ক ছোট PR হিসেবে করুন।  
3) Day 14-এ “Mini project idea” অংশ ফলো করে ডেপ্লয়/ডেমো দিন।  

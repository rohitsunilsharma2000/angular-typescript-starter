# Advanced Topics (বাংলা, হাসপাতাল ম্যানেজমেন্ট উদাহরণ)

Design system, Storybook, বড় ফর্ম, i18n, real-time/offline, এবং monorepo productivity—সব HMS কেস (patient wizard, appointment live queue, billing UI) দিয়ে।

## কীভাবে ব্যবহার করবেন
1) `topics/` ক্রম ধরে পড়ুন; প্রতিটিতে কপি-পেস্টযোগ্য Angular/Storybook/snippet আছে।
2) `demos/advanced-lab` ও `demos/storybook-ci` দেখুন—tokens, stories, websocket widget, PWA banner, IndexedDB cache।
3) প্রতিটি টপিক শেষে “Done when…” মিলিয়ে নিন ও README-র “What to ship” চেক করুন।

## টপিক লিস্ট
* [01) Design tokens](topics/01-design-tokens.md)
* [02) Theming architecture](topics/02-theming-architecture.md)
* [03) Storybook setup](topics/03-storybook-setup.md)
* [04) Storybook testing (snapshot + interaction)](topics/04-storybook-testing.md)
* [05) Dynamic/typed forms](topics/05-dynamic-forms.md)
* [06) Wizard/stepper flows (draft/save)](topics/06-wizard-stepper-flows.md)
* [07) Complex validation UX](topics/07-complex-validation-ux.md)
* [08) i18n basics](topics/08-i18n-basics.md)
* [09) Locale + ICU messages](topics/09-locale-icu.md)
* [10) RTL support](topics/10-rtl-support.md)
* [11) WebSockets with RxJS](topics/11-websockets-rxjs.md)
* [12) PWA + Service Worker updates](topics/12-pwa-service-worker.md)
* [13) IndexedDB wrappers](topics/13-indexeddb-wrappers.md)
* [14) Monorepo (Nx/Turbo) basics](topics/14-monorepo-nx-turbo.md)
* [15) Generators & affected builds](topics/15-generators-affected.md)
* [16) Checkpoint: Definition of Done](topics/16-checkpoint-done.md)

## দ্রুত রিভিশন
- প্রতিটি ফাইল: কেন দরকার → Beginner/Intermediate/Advanced → Copy-paste Example → Try it → Common mistakes → Interview points → Done when।
- কোড: standalone components, route providers, Storybook stories with controls/play, RxJS WebSocket, PWA update banner, IndexedDB cache, Nx/Turbo configs।

## প্রাক-প্রয়োজন
- Angular 17+, TypeScript, Storybook basics, RxJS, কিছুটা Nx/Turbo ধারণা।

## Demo
- `demos/advanced-lab`: tokens, stories, wizard, websocket widget, PWA banner, IndexedDB cache snippets।
- `demos/storybook-ci`: Storybook build/CI নোট।

## What to ship
- Storybook 5+ components with controls/docs; CI build
- PWA toggle বা WebSocket widget (একটি প্রাইমারি ডেমো)
- i18n demo (2 locale + ICU plural + RTL toggle)
- Design tokens consumed in components; bundle budgets still green

## Checkpoint
- Tokens consumed, hardcoded color নেই
- Build budgets পাস
- Storybook build CI-তে পাস
- i18n+RTL demo স্থিতিশীল
- Real-time/offline demo reconnect/offline হ্যান্ডেল করে

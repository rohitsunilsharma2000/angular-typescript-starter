# Advanced Topics টিউটোরিয়াল (বাংলা, হাসপাতাল ম্যানেজমেন্ট উদাহরণ)

এই ফোল্ডার থেকে Angular UI Developer হিসেবে **Advanced UI Engineering** শিখবেন—Design System + Storybook, Forms at scale, i18n + RTL, Real-time + Offline (WebSockets/PWA/IndexedDB), Large-repo productivity (Nx/Turborepo)। উদাহরণগুলো হাসপাতাল ম্যানেজমেন্ট (HMS) কনটেক্সট থেকে নেওয়া।

## কীভাবে ব্যবহার করবেন

1. প্রথমে `00-setup/advanced-setup.md` পড়ে Storybook + i18n + PWA/WebSocket dev setup করুন।
2. তারপর `topics` ফোল্ডারের নোটগুলো **ক্রম অনুযায়ী** পড়ুন।
3. হাতে-কলম অনুশীলনের জন্য `demos/advanced-lab` দেখুন (Storybook + Wizard form + i18n page + WebSocket widget / PWA toggle)।

---

## টপিক লিস্ট (Sequence অনুসারে)

* [01) Design tokens fundamentals](topics/01-design-tokens.md)
  tokens (color/spacing/radius/typography/motion), CSS variables strategy, dark/high-contrast, token naming conventions, HMS theme উদাহরণ।

* [02) Theming architecture](topics/02-theming-architecture.md)
  multi-brand theming (clinic A/B), component theming rules (no hardcoded colors), token consumption patterns, a11y contrast guardrails।

* [03) Storybook setup + story-driven dev](topics/03-storybook-setup.md)
  Angular Storybook setup, stories as specs, controls/args, docs pages, component states (hover/focus/disabled/loading/error) কভার।

* [04) Storybook testing: snapshot + interaction](topics/04-storybook-testing.md)
  snapshot কখন useful, interaction tests (play function), a11y addon checks, CI Storybook build/publish pattern।

* [05) Forms at scale: typed + dynamic forms](topics/05-dynamic-forms.md)
  typed forms, schema-driven fields, field registry/renderers, conditional visibility, form-state modeling।

* [06) Wizard/Stepper flows + draft save](topics/06-wizard-stepper-flows.md)
  multi-step architecture (route-based vs in-component), save draft per step, resume later, navigation guards (unsaved changes)।

* [07) Complex validation UX](topics/07-complex-validation-ux.md)
  inline vs summary errors, async validators (server check), cross-field validation (date ranges), show-error policy (dirty/touched)।

* [08) i18n basics (Angular built-in)](topics/08-i18n-basics.md)
  mark/extract/build per locale, translation files strategy, where strings live (toasts/errors), fallback rules।

* [09) Locale data + ICU messages](topics/09-locale-icu.md)
  date/number/currency formatting, ICU plural/gender messages, parameterized errors, locale switching patterns।

* [10) RTL support](topics/10-rtl-support.md)
  layout flipping strategy, CSS logical properties, component-level RTL considerations, testing RTL via story/demo route।

* [11) Real-time: WebSockets with RxJS](topics/11-websockets-rxjs.md)
  connect/reconnect/backoff, message routing by topic, merging into UI state (waiting room queue), teardown safety।

* [12) Offline: Service Worker / PWA essentials](topics/12-pwa-service-worker.md)
  caching strategies, update banner flow, offline indicator, safe fallback pages, feature flagging PWA।

* [13) IndexedDB for offline drafts/cache](topics/13-indexeddb-wrappers.md)
  কখন IndexedDB দরকার (drafts, cached lists), wrapper concept (idb/dexie), versioning/migrations, sync strategy।

* [14) Large-repo productivity: Nx/Turborepo basics](topics/14-monorepo-nx-turbo.md)
  apps/libs layout, module boundaries, shared UI/data-access libs, path aliases rules।

* [15) Generators + affected builds/tests](topics/15-generators-affected.md)
  consistent scaffolding generators, affected pipelines, caching strategy, CI speedups without missing coverage।

* [16) Checkpoint: Ship list + Definition of Done](topics/16-checkpoint-done.md)
  Storybook CI build works, tokens consumed by components, i18n demo + RTL stable, PWA/WebSocket demo shipped, perf budgets still green—final checklist।

---

## What to ship (এই সিরিজ শেষ করলে যা বানাতে পারবেন)

* **Storybook**: 5+ components + controls + docs + CI build
* **Forms**: wizard/stepper flow (dynamic fields + complex validation UX)
* **i18n demo page**: 2 locales + ICU plural + RTL support
* **Real-time বা Offline demo**: WebSocket widget OR PWA toggle + offline indicator
* **Repo scale**: Nx/Turbo basics + path aliases + affected builds/tests (বা plan doc)

---

## দ্রুত রিভিশন

* প্রতিটি ফাইলে থাকবে:

  * “কেন দরকার”
  * “Beginner → Intermediate → Advanced”
  * “Copy-paste Example”
  * “Try it” tasks (beginner + advanced)
  * “Common mistakes” + “Interview points”
  * “Done when…” checklist

---

## প্রাক-প্রয়োজন

* Angular + TypeScript + RxJS intermediate
* Basics of routing + forms
* Git + CI basics

---

## Demo

* `demos/advanced-lab`

  * Storybook components + tokens
  * Patient registration wizard
  * i18n + RTL demo route
  * WebSocket waiting-room widget OR PWA toggle

---

চাইলে আমি এর জন্যও Codex prompt বানিয়ে দিচ্ছি যাতে `advanced-topics/` ফোল্ডার + সব `topics/*.md` auto-generate হয় (Beginner→Advanced + working copy-paste examples সহ)।

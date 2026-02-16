# 16) Checkpoint: Definition of Done

Advanced সেটআপ শিপ করার আগে এই চেকলিস্ট।

## Why this matters (real-world)
- Design system/i18n/real-time যুক্ত হওয়ার পরও build/perf বাজেট ও CI ঠিক আছে কিনা নিশ্চিত করে।

## Concepts
### Beginner
- Checklist culture।
### Intermediate
- Evidence সংগ্রহ (storybook build, bundle guard)।
### Advanced
- RTL/real-time stability, visual checks।

## Copy-paste Example
```md
# Advanced DoD (HMS)
- [ ] Design tokens consumed (no hardcoded colors)
- [ ] Storybook 5+ components with controls/docs; CI build green
- [ ] PWA toggle or WebSocket widget works; reconnect/offline handled
- [ ] i18n demo: 2 locales + ICU plural; RTL layout stable
- [ ] Bundle/perf budgets still green
- [ ] Preview/Storybook artifacts stored
- [ ] IndexedDB draft cache works; migration note
- [ ] Monorepo affected commands wired in CI
```

## Try it
- Beginner: তালিকা পূরণ করে লিঙ্ক (storybook build URL, websocket demo route) যোগ করুন।
- Advanced: CI শেষে DoD markdown auto-update করে PR comment করুন।

## Common mistakes
- Tokens ডিফাইন কিন্তু কম্পোনেন্টে ব্যবহার না করা।
- Storybook build assets মিসিং।
- WebSocket offline/reconnect কেস না দেখা।

## Interview points
- Tokens usage, Storybook CI, i18n+RTL stability, real-time resilience, budgets।

## Done when…
- সব চেকবক্স টিক/waiver সহ।
- Evidence (build reports, demo URLs) আছে।
- Budgets ও CI গেট পাস।

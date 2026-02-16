# 16) Checkpoint: Ship list + Definition of Done

রিলিজের আগে এই তালিকা পূরণ করুন—HMS critical path নিরাপদ কিনা নিশ্চিত করতে।

## Why this matters (real-world)
- CI green হলেও critical gap থাকলে প্রড ব্রেক হতে পারে।
- Stakeholder সাইন-অফ সহজ হয়।

## Concepts
### Beginner
- Checklist culture; CI status যাচাই।
### Intermediate
- Coverage/axe/flake রিপোর্ট পড়া।
### Advanced
- Waiver expiry; auto-report archiving।

## Copy-paste Example
```md
# Testing & Quality DoD (HMS)
- [ ] Critical path coverage ≥ 80% (auth + patients + appointments + billing)
- [ ] Unit tests pass (services/stores/components)
- [ ] Integration/feature tests pass
- [ ] E2E smoke (auth→main flow) pass
- [ ] A11y gate: axe serious/critical = 0 (waiver? expiry: ____)
- [ ] Lint + TS strict pass
- [ ] CI green (lint/test/coverage/e2e/a11y)
- [ ] Flaky tests triaged (no sleeps; stable locators)
- [ ] Visual regression (if enabled) no critical diff
- [ ] Reports stored (coverage, lighthouse if any, axe)
```

## Try it
- Beginner: checklist কপি করে বর্তমান অবস্থার পাশে টিক/ক্রস দিন।
- Advanced: CI job শেষে auto-append রিপোর্ট লিংক দিয়ে markdown আপডেট স্ক্রিপ্ট লিখুন।

## Common mistakes
- A11y waiver expiry না রাখা।
- Flaky টেস্ট rerun দিয়ে উপেক্ষা।

## Interview points
- Definition of Done শুধু tests-pass নয়; coverage + a11y + flake policy।

## Done when…
- উপরের সব চেকবক্স পূরণ/waiver সহ।
- রিপোর্ট সংরক্ষণ।
- CI green without retry hacks।

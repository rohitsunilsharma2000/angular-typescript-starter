# 13) Checkpoint: Definition of Done

রিলিজের আগে Build/Ops/Security চেকলিস্ট।

## Why this matters (real-world)
- CI সবুজ হলেও সিকিউরিটি/বাজেট চেক না করলে ঝুঁকি থাকে।

## Concepts
### Beginner
- Checklist culture।
### Intermediate
- CI আর্টিফ্যাক্ট (bundle report, scan result) সংরক্ষণ।
### Advanced
- Preview deploy evidence + waiver expiry (if any)।

## Copy-paste Example
```md
# Build-Ops-Security DoD (HMS)
- [ ] Preview deploy per PR (link: ______)
- [ ] CI workflow present (lint→test→build→preview)
- [ ] Bundle-size guard pass (budget JS ≤ 350KB gzip)
- [ ] Dependency scan (npm audit --audit-level=high, depcheck) pass
- [ ] Env/config doc updated; runtime config loader present
- [ ] Auth interceptor + refresh queue merged
- [ ] Error monitoring init (Sentry) + global error handler
- [ ] CSP report-only enabled; sanitization guidelines documented
- [ ] No secrets in repo; secrets policy acknowledged
```

## Try it
- Beginner: উপরের তালিকা পূরণ করুন ও লিঙ্ক/ফলাফল লিখুন।
- Advanced: CI শেষে স্বয়ংক্রিয়ভাবে DoD markdown আপডেট স্ক্রিপ্ট লিখে PR comment করুন।

## Common mistakes
- Preview deploy লিঙ্ক না দেওয়া।
- Bundle guard step skip করা।
- Secrets পলিসি ডক না করা।

## Interview points
- DoD এ preview + bundle guard + dependency scan + a11y/security নীতি উল্লেখ।

## Done when…
- সব চেকবক্স টিক/waiver সহ।
- রিপোর্ট সংরক্ষণ (bundle, audit, preview URL)।
- Secrets policy স্বাক্ষরিত।

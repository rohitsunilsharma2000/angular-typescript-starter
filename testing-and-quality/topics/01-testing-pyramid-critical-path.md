# 01) Testing pyramid + critical path plan

হাসপাতাল অ্যাপে login → dashboard → patient list → appointment create → billing — এগুলোই critical path। ৩-৫ লাইনে লক্ষ্য: কোন স্তরে কত টেস্ট লিখবেন ও কোন ফ্লো আগে কভার করবেন।

## Why this matters (real-world)
- সঠিক অগ্রাধিকার ছাড়া টেস্ট কভারেজ সংখ্যা বাড়ে কিন্তু ঝুঁকি কমে না।
- রিগ্রেশন দ্রুত ধরতে critical path চিহ্নিত করা জরুরি।
- ইন্টারভিউয়ে “Testing strategy?” প্রশ্নের ঝরঝরে উত্তর পাবেন।

## Concepts
### Beginner
- Testing pyramid: unit > component/integration > e2e (সংখ্যা কম)।
- Critical path সংজ্ঞা: auth + মূল জার্নি (patients/appointments/billing)।
### Intermediate
- Risk-based coverage: API পরিবর্তন/নতুন UI impact map করা।
- Flake vs gap পার্থক্য; smoke বনাম regression suite।
### Advanced
- Coverage targets per layer (unit 70%+, critical-path 80%+ behavior), contract tests, canary tests।

## Copy-paste Example
```md
# Critical path checklist (HMS)
- Login (auth)
- Patient list view + search
- Appointment create/update
- Billing invoice payment
- Logout
Tests:
- Unit: service + facade selectors
- Component: patient list loading/success/error
- Integration: appointments route with mocked API
- E2E: auth + appointment create
```

## Try it
- Beginner: নিজের প্রজেক্টে critical path 5টি ধাপ লিখুন।
- Advanced: প্রতিটি ধাপে টেস্ট স্তর ম্যাপ করুন (unit/component/e2e) এবং দায়িত্বশীল ব্যক্তি নোট করুন।

## Common mistakes
- Coverage % বাড়ানোর জন্য trivial unit tests লেখা।
- Critical path না জেনে random UI টেস্ট করা।

## Interview points
- Pyramid ব্যাখ্যা + critical path mapping উদাহরণ দিন।

## Done when…
- Critical path তালিকা লেখা ও শেয়ার।
- প্রতিটি ধাপের জন্য টেস্ট স্তর নির্ধারিত।
- Coverage লক্ষ্য লিখিত (≥80% critical path)।

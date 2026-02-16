# 12) Lighthouse + bundle budgets

Billing dashboardে chart যোগ করলে bundle ফুলে যায়; Lighthouse + bundle analyzer দিয়ে পরিমাপ করুন ও বাজেট সেট করুন।

## Why this matters (real-world)
- Budget ছাড়া ছোট ছোট রিগ্রেশন জমে বড় হয়।
- Lighthouse lab স্কোর দিয়ে স্টেকহোল্ডারকে বুঝানো সহজ।
- ইন্টারভিউ: “How to enforce bundle budgets?”

## Concepts
### Beginner
- Lighthouse repeatable run; mobile throttle set।
- Angular CLI budgets (angular.json) basics।
### Intermediate
- `ng build --stats-json` + `source-map-explorer`/`webpack-bundle-analyzer`।
- KB বাজেট JS/CSS/image আলাদা।
### Advanced
- CI gate: budget fail → pipeline fail; per-route bundle tracking; delta-based budgets।

## Copy-paste Example
```bash
# Generate stats
ng build --configuration=production --stats-json
npx source-map-explorer dist/*.js > bundle-report.txt
```
```json
// angular.json (excerpt)
"budgets": [
  { "type": "initial", "maximumWarning": "350kb", "maximumError": "400kb" },
  { "type": "anyComponentStyle", "maximumWarning": "80kb", "maximumError": "100kb" }
]
```
```bash
# Lighthouse CLI (mobile emulation)
npx lighthouse http://localhost:4200/billing --preset=desktop --output=json --output-path=./reports/billing-lh.json
```

## Try it
- Beginner: stats-json চালিয়ে top 3 বড় bundle chunk লিস্ট করুন।
- Advanced: angular.json বাজেট যোগ করুন; CI তে build ব্যর্থ হয় কিনা দেখুন।

## Common mistakes
- One-time রিপোর্ট রেখে পরে আপডেট না করা।
- Budgets শুধু warning রেখে error না দিয়ে অকার্যকর করা।

## Interview points
- Angular CLI budgets + bundle analyzer উল্লেখ করুন।
- Lighthouse repeatable run: same network/CPU throttle।

## Done when…
- stats-json রিপোর্ট আছে ও পড়া হয়েছে।
- angular.json বাজেট সেট (JS/CSS)।
- Lighthouse রিপোর্ট সংরক্ষণ ও লক্ষ্য উল্লেখ।

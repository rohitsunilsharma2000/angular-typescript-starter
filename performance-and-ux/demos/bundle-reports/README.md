# bundle-reports demo

Steps
1) Production build stats: `ng build --configuration=production --stats-json`.
2) Analyzer: `npx source-map-explorer dist/*.js > bundle-report.txt` বা `npx webpack-bundle-analyzer dist/stats.json` (যদি stats.json থাকে)।
3) Lighthouse: `npx lighthouse http://localhost:4200/appointments --preset=desktop --output=json --output-path=./reports/appointments-lh.json`.
4) বাজেট লিখুন: JS ≤ 250KB gzip, CSS ≤ 80KB, LCP ≤ 2.5s (lab)।
5) `reports/` ফোল্ডারে আউটপুট রাখুন এবং PR-এ লিংক দিন।

What to look for
- বড় চাঙ্ক কোন লাইব্রেরি থেকে আসছে (charts/pdf/editor)।
- Duplicate dependency (moment + date-fns?).
- Tree-shaking ব্যর্থ (big vendor chunk)।

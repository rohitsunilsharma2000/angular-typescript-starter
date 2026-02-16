# 06) Mocking Router/Guards/Navigation

লেম্যান-বাংলা: Router.navigate ও Guards মক করুন যাতে টেস্টে আসল ন্যাভ/URL না বদলায়।

## Hands-on
1) চালান:
   ```bash
   cd testing-and-quality/demos/mocking-router-demo
   npm install
   npm run demo
   npm run typecheck
   ```
2) আউটপুটে guard allow ও router log দেখুন।
3) canActivate=false করে rerun; navigate কল যোগ করুন।

## Done when…
- টেস্টে ন্যাভ/গার্ড মক করতে পারেন; side-effect নেই।

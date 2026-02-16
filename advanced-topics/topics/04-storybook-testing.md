# 04) Storybook testing (snapshot + interaction)

লেম্যান-বাংলা: Storybook story render + play ফাংশন দিয়ে UI ইন্টারঅ্যাকশন টেস্ট করুন। এখানে console mock।

## Hands-on
1) চালান:
   ```bash
   cd advanced-topics/demos/storybook-testing-demo
   npm install
   npm run demo
   npm run typecheck
   ```
2) আউটপুটে render HTML ও play assertion দেখুন।
3) label পরিবর্তন করে rerun; assertion ব্যর্থ হলে বোঝা যাবে।

## Next in real app
- `@storybook/testing-library` + `@storybook/jest` দিয়ে play/tests লিখুন।
- Snapshot এর বদলে interaction test প্রাধান্য দিন।

## Common mistakes
- story/test mismatch রাখা।
- async effect await না করে টেস্ট লেখা।

## Done when…
- Story render + play টেস্ট CI-তে পাস।

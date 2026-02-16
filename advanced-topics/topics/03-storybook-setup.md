# 03) Storybook setup

লেম্যান-বাংলা: কম্পোনেন্ট আলাদা করে দেখানো/টেস্ট করার টুল। এখানে console mock, আসল প্রজেক্টে `npx sb init` করুন।

## Hands-on
1) রান করুন:
   ```bash
   cd advanced-topics/demos/storybook-setup-demo
   npm install
   npm run demo
   npm run typecheck
   ```
2) আউটপুটে story meta এবং rendered button দেখুন।
3) args বদলে rerun করুন; ভাবুন এটাই আপনার `*.stories.ts`।

## Next in real app
- `npx sb init`
- Story ফাইল `Component.stories.ts` বানান; play function লিখুন।

## Common mistakes
- global decorators/config না দিয়ে প্রতিটি স্টোরি ডুপ্লিকেট সেটিংস।
- props না দিয়ে hardcoded UI।

## Done when…
- Storybook dev server রান করে; কম্পোনেন্ট args দিয়ে নিয়ন্ত্রণ সম্ভব।

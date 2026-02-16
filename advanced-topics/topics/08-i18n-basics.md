# 08) i18n basics

লেম্যান-বাংলা: message dictionary + locale সুইচ; plural/placeholder হ্যান্ডল করুন।

## Hands-on
1) চালান:
   ```bash
   cd advanced-topics/demos/i18n-basics-demo
   npm install
   npm run demo
   npm run typecheck
   ```
2) আউটপুটে en/bn hello এবং plural দেখুন।
3) নতুন key/locale যোগ করুন ও rerun করুন।

## Common mistakes
- hardcoded string।
- plural rules না মানা।

## Done when…
- locale বদলালে string বদলায়; plural ঠিক কাজ করে।

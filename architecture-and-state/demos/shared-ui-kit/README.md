# Shared UI Kit (tokens + docs)

কি আছে
- `theme-tokens.ts`: রং/spacing/radius/shadow token।
- `button.md`, `input.md`: কিভাবে ব্যবহার করবেন + a11y নোট।

ব্যবহার
1) Tokens import: `import { themeTokens } from 'architecture-and-state/demos/shared-ui-kit/theme-tokens';`
2) CSS vars সেট করুন অথবা inline styles এ tokens ব্যবহার করুন।
3) ডক ফাইল দেখে props/events ঠিক করুন।

কেন দরকার
- design consistency; দ্রুত নতুন component বানাতে reference।
- ইন্টারভিউতে design system উল্লেখ করার জন্য সংক্ষিপ্ত প্রমাণ।

# 01) Testing pyramid + critical path plan

লেম্যান-বাংলা: lint/types → unit → integration → E2E—সবচেয়ে জরুরি পথ আগে কভার করুন।

## Hands-on
1) চালান:
   ```bash
   cd testing-and-quality/demos/testing-pyramid-demo
   npm install
   npm run demo
   npm run typecheck
   ```
2) আউটপুটে লেয়ার + টার্গেট দেখুন; নিজের টিমের সংখ্যা বসান।
3) critical path (e.g., login → appointments → checkout) লিস্টে যোগ করুন।

## Done when…
- লেখা টার্গেট আছে; সবাই জানে কোন লেয়ারে কত কভারেজ/কেস দরকার।

# 02) Change Detection: Default vs OnPush

লেম্যান-বাংলা: Default সব জায়গায় রেন্ডার চালায়; OnPush শুধু input/observable ইমিট এ।

## Hands-on
1) চালান:
   ```bash
   cd performance-and-ux/demos/change-detection-demo
   npm install
   npm run demo
   npm run typecheck
   ```
2) আউটপুটে Default vs OnPush render সংখ্যা দেখুন।
3) inputChanged ফ্ল্যাগ বদলে rerun করুন।

## Common mistakes
- OnPush কম্পোনেন্টে mutable input পাস করা।
- Observable ইমিট না করলে UI আপডেট আশা করা।

## Done when…
- OnPush কোথায় ব্যবহার করতে হবে বুঝেন; render বাজেট কমে।

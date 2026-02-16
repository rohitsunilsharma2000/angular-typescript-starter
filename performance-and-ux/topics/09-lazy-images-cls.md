# 09) Lazy images + CLS control

লেম্যান-বাংলা: `loading="lazy"` দিন, কিন্তু width/height না দিলে CLS বাড়ে।

## Hands-on
1) চালান:
   ```bash
   cd performance-and-ux/demos/lazy-images-cls-demo
   npm install
   npm run demo
   npm run typecheck
   ```
2) আউটপুটে dimension থাকা/না থাকা img ট্যাগ দেখুন।
3) width/height সরিয়ে rerun করলে CLS ঝুঁকি বুঝুন।

## Done when…
- সব lazy ইমেজে dimension/placeholder সেট করেন।

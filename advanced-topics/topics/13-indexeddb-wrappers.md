# 13) IndexedDB wrappers

লেম্যান-বাংলা: IndexedDB সরাসরি না ছুঁয়ে ছোট wrapper লিখুন; async put/get করুন।

## Hands-on
1) চালান:
   ```bash
   cd advanced-topics/demos/indexeddb-wrappers-demo
   npm install
   npm run demo
   npm run typecheck
   ```
2) আউটপুটে patients স্টোর থেকে আইটেম পড়া দেখুন।
3) নতুন store/key যোগ করে rerun করুন।

## Common mistakes
- callback API ব্যবহার করে জটিল করা।
- version change/upgrade path না ভাবা।

## Done when…
- Promise ভিত্তিক put/get আছে; পরে idb/ngx-indexed-db দিয়ে swap করা সহজ।

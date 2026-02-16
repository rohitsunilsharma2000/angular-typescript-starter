# 11) Immutable array helpers — toSorted, toSpliced, findLast

**কি শিখবেন**
- ES2023 immutable array methods
- Avoid accidental mutation in UI/state
- `findLast` / `findLastIndex` for tail-first search

**Code**
```js
const vitals = [98, 101, 97, 105];

const sortedDesc = vitals.toSorted((a, b) => b - a); // new array
console.log('original', vitals, 'sorted', sortedDesc);

const withoutOutlier = vitals.toSpliced(1, 1); // remove vitals[1]
console.log('spliced copy', withoutOutlier);

const lastFever = vitals.findLast(v => v >= 100);
console.log('last fever', lastFever);
```

**Interview takeaways**
- `toSorted/toSpliced/toReversed` return copies; safer for shared state (Redux, signals).  
- Mutation vs immutability often comes up in UI performance discussions.  
- `findLast` searches from the end—handy for latest abnormal reading.  

**Try it**
- Use `toReversed` to show most recent vitals first without mutating original.  
- Build a `recordTemp` function that returns a new array with appended value.  
- Compare perf of `sort` vs `toSorted` in a micro-benchmark (optional).  

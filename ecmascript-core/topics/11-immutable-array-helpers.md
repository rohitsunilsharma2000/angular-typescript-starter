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

**আরো উদাহরণ (beginner → advanced)**
1) Beginner — toReversed copy  
```js
const ids = ['P1', 'P2', 'P3'];
const reversed = ids.toReversed();
console.log(ids, reversed);
```

2) Beginner — toSpliced insert without mutating  
```js
const queue = ['P1', 'P3'];
const withGapFilled = queue.toSpliced(1, 0, 'P2');
console.log(queue, withGapFilled);
```

3) Intermediate — chain immutable helpers  
```js
const temps = [98, 99, 101];
const result = temps
  .toSorted()
  .toSpliced(0, 0, 97)
  .toReversed();
console.log(result);
```

4) Intermediate — findLastIndex usage  
```js
const readings = [97, 101, 99, 103];
const idx = readings.findLastIndex(v => v >= 100);
console.log('last fever index', idx);
```

5) Advanced — compare mutation vs copy  
```js
const original = [1, 2, 3];
const mutated = original;
mutated.reverse(); // mutates original

const safeCopy = original.toReversed(); // now original already reversed, but demonstrates copy
console.log({ original, mutated, safeCopy });
```

**Interview takeaways**
- `toSorted/toSpliced/toReversed` return copies; safer for shared state (Redux, signals).  
- Mutation vs immutability often comes up in UI performance discussions.  
- `findLast` searches from the end—handy for latest abnormal reading.  

**Try it**
- Use `toReversed` to show most recent vitals first without mutating original.  
- Build a `recordTemp` function that returns a new array with appended value.  
- Compare perf of `sort` vs `toSorted` in a micro-benchmark (optional).  

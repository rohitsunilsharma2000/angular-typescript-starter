# 04) Arrays, iterables, iterators — rounds & labs

**কি শিখবেন**
- Array helpers vs manual iteration
- Iterable protocol & custom iterator
- for...of vs for...in

**Code**
```js
const rounds = ['ER', 'ICU', 'PED'];
rounds.push('WARD'); // mutate

for (const dept of rounds) console.log('visit', dept);

// Custom iterable for lab results
const labQueue = {
  items: ['CBC', 'CRP', 'D-Dimer'],
  [Symbol.iterator]() {
    let i = 0;
    return {
      next: () =>
        i < this.items.length
          ? { value: this.items[i++], done: false }
          : { done: true },
    };
  },
};
for (const test of labQueue) console.log('process', test);
```

**Interview takeaways**
- `for...of` uses the iterable protocol; `for...in` is for keys (avoid on arrays).  
- Iterables are lazy—useful for streams (labs).  
- Array helpers (`map`, `filter`, `reduce`) are expressive; prefer over manual loops when clarity wins.  

**আরো উদাহরণ (beginner → advanced)**
1) Beginner — map and filter  
```js
const vitals = [98, 101, 97, 105];
const doubled = vitals.map(v => v * 2);
const fevers = vitals.filter(v => v >= 100);
console.log(doubled, fevers);
```

2) Beginner — reduce to aggregate  
```js
const wards = ['ER', 'ICU', 'ICU', 'PED'];
const counts = wards.reduce((acc, w) => {
  acc[w] = (acc[w] ?? 0) + 1;
  return acc;
}, {});
console.log(counts);
```

3) Intermediate — generator for labs  
```js
function* labs() {
  yield 'CBC';
  yield 'CRP';
  yield 'D-Dimer';
}
for (const test of labs()) console.log('send', test);
```

4) Intermediate — iterator with Symbol.iterator on object  
```js
const queue = {
  items: ['P-1', 'P-2', 'P-3'],
  *[Symbol.iterator]() {
    for (const id of this.items) yield id;
  },
};
console.log([...queue]);
```

5) Advanced — custom iterator carrying index/value  
```js
const shift = {
  nurses: ['Asha', 'Liam'],
  [Symbol.iterator]() {
    let i = 0;
    return {
      next: () => i < this.nurses.length
        ? { value: { nurse: this.nurses[i], slot: i++ }, done: false }
        : { done: true },
    };
  },
};
for (const entry of shift) console.log(entry);
```

**Try it**
- Write a generator function `function* nurseShift(depts)` yielding `{dept, nurse}` pairs.  
- Convert `labQueue` to a generator for brevity.  
- Use `reduce` to count how many departments start with a vowel.  

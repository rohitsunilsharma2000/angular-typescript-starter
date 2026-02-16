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

**Try it**
- Write a generator function `function* nurseShift(depts)` yielding `{dept, nurse}` pairs.  
- Convert `labQueue` to a generator for brevity.  
- Use `reduce` to count how many departments start with a vowel.  

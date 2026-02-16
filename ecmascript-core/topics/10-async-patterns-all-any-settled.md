# 10) Async patterns — all vs allSettled vs any (alerts & ETAs)

**কি শিখবেন**
- Promise.all, Promise.allSettled, Promise.any differences
- Handling partial failure vs fastest success
- Simple cancellation idea (race with timeout)

**Code**
```js
const fetchMock = name =>
  new Promise((resolve, reject) =>
    setTimeout(() => (name === 'heli' ? reject('grounded') : resolve(name)), Math.random() * 50)
  );

// all: fail-fast
Promise.all([fetchMock('ambulance'), fetchMock('heli')])
  .then(res => console.log('all success', res))
  .catch(err => console.log('all failed', err));

// allSettled: gather all outcomes
Promise.allSettled([fetchMock('lab1'), fetchMock('lab2')]).then(results => {
  console.log(results.map(r => r.status));
});

// any: first fulfillment (ignores rejections until all reject)
Promise.any([fetchMock('ambulance'), fetchMock('heli')])
  .then(first => console.log('fastest transport', first))
  .catch(err => console.log('none available', err));

// cancellation idea: race with timeout
const timeout = ms => new Promise((_, reject) => setTimeout(() => reject('timeout'), ms));
Promise.race([fetchMock('ct-scan'), timeout(20)])
  .then(console.log)
  .catch(err => console.log('race result', err));
```

**Interview takeaways**
- `all` is fail-fast; use when every call must succeed (e.g., vitals + meds).  
- `allSettled` suits dashboards where partial data is acceptable.  
- `any` is for “first available” (ambulance/heli ETA).  
- `race` can approximate cancellation with a timeout Promise.  

**Try it**
- Build a triage alert that waits for the first critical lab using `any`.  
- Log which calls failed in `allSettled` and retry only those.  
- Implement a real abort with `AbortController` and fetch if your runtime supports it.  

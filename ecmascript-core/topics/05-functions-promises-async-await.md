# 05) Functions → Promises → async/await — bed allocation flow

**কি শিখবেন**
- Callback to Promise migration
- Promise states & chaining
- async/await with try/catch

**Code**
```js
// Promise-based fetch mock
function fetchBed(id) {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      if (id === 'ICU-0') reject(new Error('Bed offline'));
      else resolve({ id, status: 'clean' });
    }, 20);
  });
}

// Chaining
fetchBed('ICU-2')
  .then(bed => ({ ...bed, assignedTo: 'P-8' }))
  .then(bed => console.log('Assigned', bed))
  .catch(err => console.error('Assign failed', err.message));

// async/await
async function assignBed(patientId, bedId) {
  try {
    const bed = await fetchBed(bedId);
    return { ...bed, patientId };
  } catch (err) {
    return { error: err.message };
  }
}

assignBed('P-9', 'ICU-0').then(console.log);
```

**Interview takeaways**
- Promises are eager; executor runs immediately.  
- Always return/await Promises inside async functions to propagate errors.  
- `async` functions wrap return values into Promises automatically.  

**আরো উদাহরণ (beginner → advanced)**
1) Beginner — callback to Promise wrapper  
```js
function withCallback(cb) {
  setTimeout(() => cb('done'), 10);
}
const p = new Promise(res => withCallback(res));
p.then(console.log);
```

2) Beginner — promise chaining transform  
```js
fetchBed('ICU-2')
  .then(bed => ({ ...bed, cleaned: true }))
  .then(bed => console.log('Cleaned bed', bed));
```

3) Intermediate — async function auto-wrap  
```js
async function ping() {
  return 'pong';
}
ping().then(console.log);
```

4) Intermediate — bubble error to catch  
```js
fetchBed('bad-id')
  .then(console.log)
  .catch(err => console.error('Handled:', err.message));
```

5) Advanced — parallel awaits with Promise.all  
```js
async function warmBeds() {
  const [a, b] = await Promise.all([fetchBed('ICU-1'), fetchBed('ICU-3')]);
  console.log('Ready:', a.id, b.id);
}
warmBeds();
```

**Try it**
- Add a timeout Promise and race it with `fetchBed` to simulate SLA breach.  
- Convert the chaining example to async/await style.  
- Inject a deliberate throw inside `then` and observe how catch handles it.  

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

**Try it**
- Add a timeout Promise and race it with `fetchBed` to simulate SLA breach.  
- Convert the chaining example to async/await style.  
- Inject a deliberate throw inside `then` and observe how catch handles it.  

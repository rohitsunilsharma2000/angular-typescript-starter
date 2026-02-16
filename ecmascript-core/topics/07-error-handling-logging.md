# 07) Error handling & logging — safer APIs

**কি শিখবেন**
- try/catch/finally + optional catch binding
- Distinguish expected vs unexpected errors
- Lightweight structured logging

**Code**
```js
function fetchVitals(id) {
  if (!id) throw new Error('Missing patient id'); // expected (validation)
  return Promise.resolve({ hr: 92, bp: '120/80' });
}

async function safeVitals(id) {
  try {
    const data = await fetchVitals(id);
    console.log('INFO', { id, data });
    return data;
  } catch {
    console.error('WARN', { id, msg: 'Vitals fetch failed' });
    return null;
  } finally {
    console.log('TRACE', { id, at: Date.now() });
  }
}

safeVitals('P-1');
safeVitals(); // will hit catch
```

**Interview takeaways**
- Optional catch binding (`catch {}`) when error object unused.  
- Use `finally` for cleanup/metrics.  
- Log with consistent shape; avoid console spam in production.  

**Try it**
- Differentiate 4xx vs 5xx style errors and log severity accordingly.  
- Add a retry wrapper that stops after 3 attempts.  
- Swap console with a `log(level, payload)` function that prefixes timestamps.  

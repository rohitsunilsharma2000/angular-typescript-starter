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

**আরো উদাহরণ (beginner → advanced)**
1) Beginner — typed error check  
```js
try {
  JSON.parse('{ bad json }');
} catch (err) {
  if (err instanceof SyntaxError) console.log('Parse failed');
}
```

2) Beginner — promise catch shorthand  
```js
fetchVitals('x').catch(err => console.error('Vitals failed:', err.message));
```

3) Intermediate — custom error class  
```js
class BedError extends Error {
  constructor(code) { super(code); this.code = code; }
}
function claimBed(id) {
  if (id === 'ICU-0') throw new BedError('BED_OFFLINE');
  return id;
}
try { claimBed('ICU-0'); } catch (e) { console.log(e.code); }
```

4) Intermediate — finally cleanup  
```js
let timer;
try {
  timer = setTimeout(() => console.log('timeout'), 20);
  throw new Error('fail fast');
} catch (e) {
  console.log('caught', e.message);
} finally {
  clearTimeout(timer);
}
```

5) Advanced — structured logging helper  
```js
const log = (level, payload) =>
  console.log(JSON.stringify({ level, ts: Date.now(), ...payload }));

log('INFO', { msg: 'Server healthy' });
log('WARN', { msg: 'Bed offline', id: 'ICU-9' });
```

**Try it**
- Differentiate 4xx vs 5xx style errors and log severity accordingly.  
- Add a retry wrapper that stops after 3 attempts.  
- Swap console with a `log(level, payload)` function that prefixes timestamps.  

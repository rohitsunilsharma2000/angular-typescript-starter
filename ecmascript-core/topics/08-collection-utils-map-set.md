# 08) Map, Set, WeakMap — bed cache & session tokens

**কি শিখবেন**
- When to use Map/Set over plain objects
- WeakMap for memory-safe associations
- Common operations (has/get/delete)

**Code**
```js
// Bed occupancy cache
const beds = new Map([
  ['ICU-1', null],
  ['ICU-2', 'P-11'],
]);

function assignBed(id, patientId) {
  if (!beds.has(id)) throw new Error('Unknown bed');
  beds.set(id, patientId);
}
assignBed('ICU-1', 'P-9');
console.log('Occupied?', beds.get('ICU-1'));

// Set for unique alerts
const alerts = new Set(['oxygen-low']);
alerts.add('bp-high');
alerts.add('oxygen-low'); // ignored
console.log(alerts.size); // 2

// WeakMap for session → metadata
const sessions = new WeakMap();
function createSession(userObj) {
  sessions.set(userObj, { role: 'doctor', loggedInAt: Date.now() });
  return userObj;
}
const user = createSession({ id: 'U-7' });
console.log(sessions.get(user));
```

**Interview takeaways**
- Map keeps insertion order and any key type; Object keys coerce to string.  
- Set guarantees uniqueness with O(1) ops; great for deduping alerts.  
- WeakMap keys must be objects; allows garbage collection → good for ephemeral sessions.  

**Try it**
- Convert `beds` Map to an Array of `[id, patientId]` and back.  
- Use Set to dedupe a list of patient IDs then spread back into an array.  
- Explain why WeakMap can’t be iterated and why that’s fine for auth tokens.  

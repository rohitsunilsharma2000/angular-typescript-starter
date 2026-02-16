# 14) Mini project guide — ER Intake & Lab Tracker

**কি শিখবেন**
- Break a small app into modules
- Apply ES features from earlier topics
- Outline for interview demo

**Scope**
- Intake queue (Array + toSorted)
- Bed allocator (Map)
- Lab stream (async generator)
- Alerts (Promise.any for fastest transport ETA)
- UI stub (console or minimal DOM)

**Starter flow (pseudo)**
```js
import { allocateBed } from './beds.js';
import { streamLabs } from './labs.js';
import { render } from './ui.js';

const queue = ['P-1', 'P-2'];
const beds = new Map([['ICU-1', null], ['ICU-2', null]]);

queue.forEach(pid => allocateBed(beds, pid));

for await (const lab of streamLabs(queue)) {
  render(lab); // DOM or console
}
```

**আরো উদাহরণ (beginner → advanced)**
1) Beginner — simple bed allocator module  
```js
// beds.js
export function allocateBed(beds, patientId) {
  for (const [id, occupant] of beds) {
    if (!occupant) { beds.set(id, patientId); return id; }
  }
  return null;
}
```

2) Beginner — minimal renderer stub  
```js
// ui.js
export function render(event) {
  console.log('UI event:', event);
}
```

3) Intermediate — async generator lab stream  
```js
// labs.js
export async function* streamLabs(patients) {
  for (const id of patients) {
    await new Promise(r => setTimeout(r, 10));
    yield { patientId: id, test: 'CBC', status: 'ready' };
  }
}
```

4) Intermediate — alert fastest transport with Promise.any  
```js
// alerts.js
const eta = name => new Promise(res => setTimeout(() => res(name), Math.random() * 30));
export async function fastestTransport() {
  return Promise.any([eta('ambulance'), eta('heli'), eta('bike')]);
}
fastestTransport().then(mode => console.log('Use', mode));
```

5) Advanced — glue main script tying modules  
```js
import { allocateBed } from './beds.js';
import { streamLabs } from './labs.js';
import { render } from './ui.js';

const beds = new Map([['ICU-1', null]]);
const queue = ['P-1', 'P-2'];

queue.forEach(pid => console.log('assigned', allocateBed(beds, pid)));

(async () => {
  for await (const lab of streamLabs(queue)) render(lab);
})();
```

**Interview takeaways**
- Show architecture thinking: split by feature, keep functions pure where possible.  
- Use immutable helpers (`toSorted`) for UI lists, Map for allocations, async/await for flows.  
- Highlight one failure path and how you log/recover.  

**Try it**
- Add a timeout to lab stream; fallback message on timeout.  
- Persist queue to `localStorage` (browser) or JSON file (Node) safely.  
- Record a 2–3 minute walkthrough focusing on ES features you used.  

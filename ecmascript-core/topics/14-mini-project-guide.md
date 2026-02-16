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

**Interview takeaways**
- Show architecture thinking: split by feature, keep functions pure where possible.  
- Use immutable helpers (`toSorted`) for UI lists, Map for allocations, async/await for flows.  
- Highlight one failure path and how you log/recover.  

**Try it**
- Add a timeout to lab stream; fallback message on timeout.  
- Persist queue to `localStorage` (browser) or JSON file (Node) safely.  
- Record a 2–3 minute walkthrough focusing on ES features you used.  

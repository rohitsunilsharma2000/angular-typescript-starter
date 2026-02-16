# hospital-es-demo (Node 18+) — quick feature tour

Run a few modern ES features against hospital data.

## Run
```bash
node app.js
```

## What it shows
- Optional chaining + nullish: safe room labels.
- Promise.allSettled: parallel vitals fetch.
- toSorted/findLast: immutable list ops.
- Promise.withResolvers (needs Node 20+ or flag): external triage trigger.

Adjust `tsconfig.target` if you port to TS; add polyfills for older runtimes.  

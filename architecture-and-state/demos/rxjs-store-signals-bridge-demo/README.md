# RxJS service store + signals bridge demo

Shows how an RxJS BehaviorSubject-based service store can feed a signal-like getter (no Angular runtime needed).

## Commands
```bash
npm install
npm run demo       # runs ts-node src/main.ts
npm run typecheck  # tsc --noEmit
```

## Expected output (trimmed)
```
--- Initial ---
undefined

--- After adds (signal) ---
[ { id: '...', title: 'Write docs', done: false }, { id: '...', title: 'Ship demo', done: false } ]

--- After toggle (signal) ---
[ { id: '...', title: 'Write docs', done: true }, { id: '...', title: 'Ship demo', done: false } ]
```

## Break/fix ideas
- Comment out `fromObservable` and try reading the BehaviorSubject directly—notice you lose the signal-style getter ergonomics.
- Remove `distinctUntilChanged` in `store.selectAll()` and log inside `fromObservable` to see duplicate emissions.

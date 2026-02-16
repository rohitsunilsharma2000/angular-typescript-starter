# NgRx fundamentals demo (minimal, Angular-free)

Tiny RxJS-based mimic of NgRx patterns: actions, reducer, effect, store, selector (snapshot).

## Commands
```bash
npm install
npm run demo       # ts-node src/main.ts
npm run typecheck  # tsc --noEmit
```

## Expected output (short)
```
=== after load success ===
{ data: [ ...2 items ], loading: false, error: undefined }

=== after load failure ===
{ data: [ ...2 items ], loading: false, error: 'Server 500' }
```

## Break/fix ideas
- Toggle `setFailNext(true/false)` to switch success/failure.
- Extend state with `loading: true` during effect (set inside reducer) and log.
- Add a selector function (e.g., selectLoading) and use it in `main.ts`.

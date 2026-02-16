# ComponentStore feature slice demo

Minimal ComponentStore-like helper (no Angular deps) with selectors, updaters, and effects for a todo feature.

## Commands
```bash
npm install
npm run demo       # ts-node src/main.ts
npm run typecheck  # tsc --noEmit
```

## Expected output (trimmed)
```
=== after load ===
{ todos: [t1,t2], loading: 'idle', error: undefined }

=== after add success ===
{ todos: [... +1], loading: 'idle', error: undefined }

=== after add failure ===
{ todos: [... +1], loading: 'idle', error: 'Network 500' }
```

## Break/fix ideas
- Remove `catchError` to see unhandled rejection; add back to understand failure path.
- Switch `shouldFail` to false to watch clean flow.
- Add another selector in `todo.store.ts` (e.g., completed count) and log it in `main.ts`.

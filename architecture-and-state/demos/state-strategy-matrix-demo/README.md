# State Strategy Matrix Demo

Shows three state approaches without Angular deps: local component state, feature service store, and a global event bus.

## Commands
```bash
npm install
npm run demo       # runs ts-node src/main.ts
npm run typecheck  # tsc --noEmit
```

## Expected output (trimmed)
```
--- Local component state (isolated) ---
Local count: 2

--- Service store (feature-shared) ---
Listener saw count: 1
Listener saw count: 2

--- Global event bus (cross-feature) ---
Feature A heard: { id: 'u1', name: 'Ayman' }
Feature B heard: { id: 'u1', name: 'Ayman' }
```

## Break/fix idea
- Remove the unsubscribe in `src/main.ts` and re-run to see repeated listeners; add back to understand leak prevention.
- Change event name in `emit` to `user:login` and watch handlers stop firing—helps internalize contract coupling.

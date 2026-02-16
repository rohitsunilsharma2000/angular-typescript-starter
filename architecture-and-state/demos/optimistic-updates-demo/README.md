# Optimistic updates + rollback demo

Shows an optimistic toggle with rollback on failure using a simple store and fake API.

## Commands
```bash
npm install
npm run demo       # runs ts-node src/main.ts
npm run typecheck  # tsc --noEmit
```

## Expected output (short)
```
=== after load ===
{ loading: 'idle', error: undefined, todos: [ { id: 't1', ... }, { id: 't2', ... } ] }

=== after optimistic success ===
{ loading: 'idle', error: undefined, todos: [ { id: 't1', done: true }, { id: 't2', done: false } ] }

=== after optimistic fail+rollback ===
{ loading: 'idle', error: 'Network 500', todos: [ { id: 't1', done: true }, { id: 't2', done: false } ] }
```

## Break/fix ideas
- Set `simulateFail` to `false` on both toggles to see all-success flow.
- Remove the rollback set (the `prev` state apply) and notice wrong UI state after failure.
- Add a spinner when `loading === 'optimistic'` to visualize the pending update.

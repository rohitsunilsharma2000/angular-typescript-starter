# NgRx Entity + pagination demo (Angular-free)

Shows entity-style state with pagination/filter actions and an effect-driven load.

## Commands
```bash
npm install
npm run demo       # ts-node src/main.ts
npm run typecheck  # tsc --noEmit
```

## Expected output (trimmed)
```
=== page 1 ===
{ page: 1, pageSize: 5, total: 25, loading: false, ids: ['p1','p2',...], first: { id:'p1', ... } }

=== page 2 ===
{ page: 2, pageSize: 5, total: 25, ids: ['p6',...], first: { id:'p6', ... } }

=== filter name contains "5" ===
{ page: 1, pageSize: 5, total: 3, ids: ['p5','p15','p25'], first: { id:'p5', ... } }

=== failure (rollback keeps old data) ===
{ page: 1, pageSize: 5, total: 3, error: 'Server 500', ids: ['p5','p15','p25'] }
```

## Break/fix ideas
- Change pageSize to 3 and rerun to see pagination shape.
- Remove `setFailNext(true)` to see clean flow.
- Add `removeOne` call in reducer to simulate delete; log ids shrink.

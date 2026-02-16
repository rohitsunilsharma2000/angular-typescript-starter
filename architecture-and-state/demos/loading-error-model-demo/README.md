# Loading/Error model demo

Shows standardized loading/error state shape (loading | refreshing | saving) with a tiny BillingStore and simulated success/error flows.

## Commands
```bash
npm install
npm run demo       # runs ts-node src/main.ts
npm run typecheck  # tsc --noEmit
```

## Expected output (short)
```
=== initial ===
{ loading: 'idle', error: undefined, data: [], lastUpdated: undefined }

=== after load success ===
{ loading: 'idle', error: undefined, data: [ { id: 'inv-1', amount: 1200, patient: 'Ayman' }, ...], lastUpdated: 1690000000000 }

=== after load error ===
{ loading: 'idle', error: { message: 'Server 500: failed to load invoices', code: 'E500', correlationId: '...' }, data: [ ... ], lastUpdated: 1690000000000 }

=== after save ===
{ loading: 'idle', error: undefined, data: [ ... 3 items ... ], lastUpdated: 1690000000500 }
```

## Break/fix ideas
- Change `simulateError` to false in the second load call to see clean flow.
- Remove `lastUpdated` updates and notice observability loss.
- Add a `deleting` path to store and log how loading states differ.

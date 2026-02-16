# Data contracts + runtime validation demo

Zod-based runtime validation for an API response, plus a failing payload example.

## Commands
```bash
npm install
npm run demo       # runs ts-node src/main.ts
npm run typecheck  # tsc --noEmit
```

## Expected output (trimmed)
```
--- Valid payload ---
[ { id: 'p1', name: 'Ayman', age: 32, ward: 'A', allergies: [ 'penicillin' ] }, ... ]

--- Invalid payload (expect throw) ---
Validation error: Validation failed: 0.ward: Expected string, received number; 1.id: String must contain at least 1 character(s); 1.age: Number must be greater than or equal to 0
```

## Break/fix ideas
- Add `z.coerce.number()` for age and rerun to see lenient parsing.
- Remove `.nonnegative()` to observe failing tests disappear (less strict).
- Add a new required field (e.g., `bloodType`) and watch failures until API provides it.

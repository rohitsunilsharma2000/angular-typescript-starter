# Feature Boundaries Demo

Quick runnable sandbox for the "Feature boundaries + folder structure" topic.

## Commands
```bash
npm install      # install ts-node, typescript, @types/node (dev only)
npm run check:boundaries  # runs src/tools/check-boundary.ts
npm run typecheck         # runs tsc --noEmit to ensure path aliases resolve
npm run demo              # runs both checks
```

## What it shows
- Feature-first layout with patients + billing features
- Shared UI/data-access and core infra layers
- Boundary guard script that blocks cross-feature/shared→core UI imports

## Change it
- Add a bad import (e.g., core -> shared/ui) and rerun `npm run check:boundaries` to see the violation, then fix and rerun.

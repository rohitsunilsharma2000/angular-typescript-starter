# Standalone vs NgModule Demo

Shows how a standalone route (loadComponent) and a legacy NgModule route (loadChildren) can coexist.

## Commands
```bash
npm install
npm run demo       # prints outputs for home + legacy + missing route
npm run typecheck  # tsc --noEmit
```

## Expected output
```
[standalone] Home standalone works
[ngmodule] Legacy dashboard via NgModule
404: unknown route
```

## Break/fix idea
- Change `kind` of the legacy route to `standalone` in `src/app/app.routes.ts` and run `npm run demo`; notice it no longer resolves correctly. Switch back to `ngmodule` to fix.

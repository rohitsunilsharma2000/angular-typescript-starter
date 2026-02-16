# Smart vs Presentational Demo

Tiny runnable sandbox to feel the difference between smart (container) and presentational components.

## Commands
```bash
npm install
npm run demo       # runs ts-node src/main.ts
npm run typecheck  # tsc --noEmit for type safety
```

## What happens
- Smart container (CatalogPage) loads data via facade + API stub.
- Presentational component (ProductsListComponent) renders list and raises select event.
- Console shows data-down/events-up flow.

## Break/Fix exercise
- Add an API call inside `src/app/shared/ui/products-list.component.ts` and rerun `npm run typecheck`—you'll see why it's a bad idea (logic leak). Then revert.

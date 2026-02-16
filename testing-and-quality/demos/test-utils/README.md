# Test utils

Contains patterns to reuse in tests:
- HttpTestingController helpers (success + error)
- Router navigation helpers (provideRouter)
- render helper wrapping common providers (theme, intl)

Example render helper
```ts
import { render } from '@testing-library/angular';
import { provideRouter } from '@angular/router';
export async function renderWithRouter<T>(component: any, routes = []) {
  return render(component, { providers: [provideRouter(routes)] });
}
```

Use with snippets in `../snippets` to reduce boilerplate.

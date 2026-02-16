# 13) Tooling & testing — tsconfig target, polyfills, smoke tests

**কি শিখবেন**
- Picking `target`/`lib` in tsconfig for ES features
- Polyfill checklist for browsers
- Quick smoke tests with Node/Vitest

**Code**
```json
// tsconfig.json (minimal)
{
  "compilerOptions": {
    "target": "ES2020",
    "module": "ESNext",
    "lib": ["ES2020", "DOM"],
    "strict": true
  }
}
```

```js
// smoke.test.js (Vitest or Jest-style)
import { describe, it, expect } from 'vitest';
import { calcBill } from './billing-service.js';

describe('billing', () => {
  it('adds room + meds', () => {
    expect(calcBill({ roomRate: 5000, days: 2, meds: 800 })).toBe(10800);
  });
});
```

**আরো উদাহরণ (beginner → advanced)**
1) Beginner — Node ESM flag in package.json  
```json
{
  "type": "module",
  "scripts": { "test": "vitest run" }
}
```

2) Beginner — basic Vitest mock  
```js
import { vi, it, expect } from 'vitest';

const send = vi.fn().mockReturnValue('ok');
it('sends once', () => {
  send('msg');
  expect(send).toHaveBeenCalledTimes(1);
});
```

3) Intermediate — tsconfig path alias  
```json
{
  "compilerOptions": {
    "baseUrl": ".",
    "paths": {
      "@services/*": ["src/services/*"]
    }
  }
}
```

4) Intermediate — Promise.any polyfill import  
```js
import 'core-js/es/promise/any';
console.log(Promise.any ? 'polyfilled' : 'missing');
```

5) Advanced — smoke test for Promise.any  
```js
import { describe, it, expect } from 'vitest';

describe('Promise.any', () => {
  it('resolves fastest', async () => {
    const result = await Promise.any([
      new Promise(res => setTimeout(() => res('slow'), 20)),
      Promise.resolve('fast'),
    ]);
    expect(result).toBe('fast');
  });
});
```

**Interview takeaways**
- `target` controls emitted syntax; choose based on runtime (Node 18 → ES2021 safe).  
- `lib` must include DOM if you touch browser APIs.  
- Polyfill likely: optional chaining/nullish (old browsers), `Promise.any`, `toSorted` (very new).  
- Even one or two smoke tests show you respect correctness.  

**Try it**
- Change `target` to `ES5` and note what features break without transpile/polyfill.  
- Add a test for `Promise.any` using a simple fastest transport example.  
- List your project’s required polyfills explicitly in README.  

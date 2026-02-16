# 06) Modules (import/export) — splitting hospital services

**কি শিখবেন**
- ES module syntax: named vs default export
- File-per-feature organization
- Importing with path aliases (optional)

**Code**
```js
// billing-service.js
export function calcBill({ roomRate, days, meds = 0 }) {
  return roomRate * days + meds;
}

// pharmacy.js
const stock = new Map([['AMOX', 40]]);
export function dispense(code, count) {
  const available = stock.get(code) ?? 0;
  if (count > available) throw new Error('Not enough stock');
  stock.set(code, available - count);
  return stock.get(code);
}
export default stock;

// main.js
import stock, { dispense } from './pharmacy.js';
import { calcBill } from './billing-service.js';

dispense('AMOX', 5);
console.log('Left:', stock.get('AMOX'));
console.log('Bill:', calcBill({ roomRate: 5000, days: 2, meds: 800 }));
```

**Interview takeaways**
- One default export per module; named exports can be many.  
- Tree-shaking works best with named exports.  
- Node: ensure `"type": "module"` in `package.json` or use `.mjs`.  

**আরো উদাহরণ (beginner → advanced)**
1) Beginner — rename import  
```js
// main.js
import { calcBill as bill } from './billing-service.js';
console.log(bill({ roomRate: 3000, days: 1 }));
```

2) Beginner — namespace import
```js
// main.js
import * as pharmacy from './pharmacy.js';
pharmacy.dispense('AMOX', 2);
console.log(pharmacy.default.get('AMOX'));
```

3) Intermediate — barrel re-export
```js
// services/index.js
export * from './billing-service.js';
export * from './pharmacy.js';

// app.js
import { calcBill, dispense } from './services/index.js';
dispense('AMOX', 1);
console.log(calcBill({ roomRate: 2000, days: 3, meds: 400 }));
```

4) Intermediate — dynamic import on demand
```js
async function loadPharmacy() {
  const { dispense } = await import('./pharmacy.js');
  return dispense('AMOX', 1);
}
loadPharmacy().then(console.log);
```

5) Advanced — side‑effect import (polyfill)
```js
// polyfills/metrics.js
console.log('metrics polyfill loaded');

// main.js
import './polyfills/metrics.js';
console.log('App started');
```

**Try it**
- Split `bed` functions into `beds.js` and import into `main`.  
- Add `index.js` barrel that re-exports pharmacy + billing; import from barrel.  
- Explain the difference between CJS `require` and ESM `import`.  

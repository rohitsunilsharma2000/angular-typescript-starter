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

**Try it**
- Split `bed` functions into `beds.js` and import into `main`.  
- Add `index.js` barrel that re-exports pharmacy + billing; import from barrel.  
- Explain the difference between CJS `require` and ESM `import`.  

# 10) Pharmacy scenario: inventory + literal unions

**কী শিখবেন**
- string literal union দিয়ে ক্যাটাগরি ও dosage নিয়ন্ত্রণ
- `Record` ও `Partial` দিয়ে inventory মডেল
- narrowed function দিয়ে stock update

**হাসপাতাল উদাহরণ**
```ts
type DrugForm = 'tablet' | 'syrup' | 'injection';
type Strength = '250mg' | '500mg' | '1g';

type StockItem = {
  id: string;
  name: string;
  form: DrugForm;
  strength: Strength;
  quantity: number;
  reorderLevel?: number;
};

type Inventory = Record<string, StockItem>;

const pharmacy: Inventory = {
  AMOX: { id: 'AMOX', name: 'Amoxicillin', form: 'tablet', strength: '500mg', quantity: 40 },
};

function dispense(item: StockItem, count: number): StockItem {
  if (count > item.quantity) throw new Error('Not enough stock');
  return { ...item, quantity: item.quantity - count };
}

function restock(inv: Inventory, updates: Partial<StockItem> & { id: string }) {
  const existing = inv[updates.id];
  if (!existing) throw new Error('Unknown item');
  inv[updates.id] = { ...existing, ...updates };
}
```

**Interview takeaways**
- literal union দিয়ে ভুল বানান কমে (`'tablet'` vs `'table'` ধরবে)
- `Partial<T>` আপডেট পে-লোডের জন্য ভালো

**Try it**
- `type Temperature = 'cold' | 'room' | 'fridge'` যোগ করুন এবং স্টকে temperature ফিল্ড দিন; dispense করার সময় নিশ্চিত করুন fridge আইটেম ৪°C তে আছে (boolean flag ধরে নিতে পারেন)।

## ব্রাউজারে কনসোল টেস্ট (Pharmacy)

1) **Create inventory record**
```ts
type DrugForm = 'tablet' | 'syrup';
type Strength = '250mg' | '500mg';
type StockItem = { id: string; name: string; form: DrugForm; strength: Strength; quantity: number };
const inv: Record<string, StockItem> = {
  AMOX: { id: 'AMOX', name: 'Amoxicillin', form: 'tablet', strength: '500mg', quantity: 40 },
};
console.log(inv.AMOX);
```

2) **Dispense with guard**
```ts
function dispense(item: StockItem, count: number) {
  if (count > item.quantity) throw new Error('Not enough');
  item.quantity -= count;
}
dispense(inv.AMOX, 5);
console.log('After dispense:', inv.AMOX.quantity);
```

3) **Partial update (restock)**
```ts
function restock(store: Record<string, StockItem>, update: Partial<StockItem> & { id: string }) {
  store[update.id] = { ...store[update.id], ...update };
}
restock(inv, { id: 'AMOX', quantity: 60 });
console.log('After restock:', inv.AMOX.quantity);
```

4) **Literal union prevents typos**
```ts
// inv.AMOX.form = 'tablet';        // ok
// inv.AMOX.form = 'tabs';          // TS error (typo) if type-checked
console.log('Form stays controlled:', inv.AMOX.form);
```

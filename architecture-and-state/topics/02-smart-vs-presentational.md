# 02) Smart vs Presentational components

বেগিনারদের জন্য হাতে-কলমে: স্মার্ট কম্পোনেন্ট data এনে orchestrate করে; প্রেজেন্টেশনাল কম্পোনেন্ট শুধু UI রেন্ডার করে ও ইভেন্ট তোলে (data down, events up).

## Things to learn (layman + Bengali)
1) **কী পার্থক্য**: Smart = data/state + routing orchestration; Presentational = UI-only, `@Input` + `@Output` ধাঁচের props/callback।
2) **দায়িত্বের সীমানা**: UI কখনো API/Store জানবে না; ফেচ/রাউটিং সব Smart/Container সামলাবে।
3) **ফোল্ডার ম্যাপ**: `features/<domain>/pages` (smart), `shared/ui` (presentational), `features/<domain>/data-access|state` (API/store)।
4) **Data down, events up**: Smart data দেয়, Presentational ইভেন্ট তুলে। টাইপ্ড callback দিয়ে contract স্পষ্ট করুন।
5) **টেস্ট ফোকাস**: Presentational = render snapshot + ইভেন্ট; Smart = orchestration/side-effects।

## Hands-on (step-by-step, কমান্ডসহ)
1) **রেডি-টু-রান ডেমো ব্যবহার করুন**:
   ```bash
   cd architecture-and-state/demos/smart-vs-presentational-demo
   npm install
   npm run demo       # smart -> presentational flow + console output
   npm run typecheck  # path alias + typings ঠিক আছে কিনা
   ```
2) **নিজে বানাতে চাইলে (quick scaffold)**:
   ```bash
   mkdir -p src/app/shared/ui src/app/features/catalog/{data-access,pages}
   ```
   এরপর নিচের "Demos" ব্লক থেকে ফাইলগুলো কপি করুন।
3) **Boundary ভাঙা দেখুন**:
   - ভুল: `products-list.component.ts` এ API ফেচ যোগ করে `npm run typecheck` চালান—নিয়ম ভাঙা বোঝা সহজ হবে।
   - ঠিক: API কল শুধু `catalog.api.ts`/`catalog.facade.ts` এ রাখুন।
4) **ইভেন্ট ফ্লো বুঝুন**:
   - `npm run demo` শেষে লগে দেখবেন `[event up] selected product id: ...`—এটাই presentational → smart ইভেন্ট।

### Done when
- Presentational কোডে কোনো API/Store/Router নেই; শুধুই props + callbacks।
- Smart কম্পোনেন্ট data ফেচ করে, state ধরে, presentational-এ দেয়, ইভেন্টে action নেয়।
- কমান্ড চালালে লজিক ও আউটপুট পরিষ্কার বোঝা যায়।

## Demos (copy-paste)
`architecture-and-state/demos/smart-vs-presentational-demo/src/` থেকে মূল অংশগুলি:

```ts
// app/shared/ui/products-list.component.ts (Presentational)
export type ProductVM = { id: string; name: string; price: number };
export class ProductsListComponent {
  constructor(private props: { products: ProductVM[]; onSelect: (id: string) => void }) {}
  render(): string {
    if (!this.props.products.length) return 'No products yet';
    return this.props.products.map(p => `• ${p.name} ($${p.price.toFixed(2)})`).join('\n');
  }
  clickFirst(): void {
    const first = this.props.products[0];
    if (first) this.props.onSelect(first.id);
  }
}
```
```ts
// app/features/catalog/data-access/catalog.api.ts (API stub)
export type ProductDto = { id: string; name: string; price: number };
export class CatalogApi {
  async fetchAll(): Promise<ProductDto[]> {
    return [
      { id: 'p1', name: 'Stethoscope', price: 120 },
      { id: 'p2', name: 'Blood Pressure Cuff', price: 65 }
    ];
  }
}
```
```ts
// app/features/catalog/data-access/catalog.facade.ts (Smart data layer)
import { CatalogApi, ProductDto } from './catalog.api';
export type CatalogVM = { products: ProductDto[] };
export class CatalogFacade {
  private products: ProductDto[] = [];
  constructor(private api: CatalogApi) {}
  async load(): Promise<void> { this.products = await this.api.fetchAll(); }
  vm(): CatalogVM { return { products: this.products }; }
}
```
```ts
// app/features/catalog/pages/catalog.page.ts (Smart container/page)
import { CatalogFacade } from '../data-access/catalog.facade';
import { ProductsListComponent } from '../../../shared/ui/products-list.component';

export class CatalogPage {
  private list?: ProductsListComponent;
  constructor(private facade: CatalogFacade) {}

  async init(): Promise<void> {
    await this.facade.load();
    this.list = new ProductsListComponent({
      products: this.facade.vm().products,
      onSelect: this.handleSelect
    });
  }

  render(): string {
    return this.list ? this.list.render() : 'Loading...';
  }

  simulateUserClick(): void {
    this.list?.clickFirst();
  }

  private handleSelect = (id: string) => {
    console.log('[event up] selected product id:', id);
  };
}
```
```ts
// main.ts (runner for the demo)
import { CatalogApi } from './app/features/catalog/data-access/catalog.api';
import { CatalogFacade } from './app/features/catalog/data-access/catalog.facade';
import { CatalogPage } from './app/features/catalog/pages/catalog.page';

async function run() {
  const page = new CatalogPage(new CatalogFacade(new CatalogApi()));
  await page.init();
  console.log('--- Rendered UI (presentational) ---');
  console.log(page.render());
  console.log('--- Simulate click ---');
  page.simulateUserClick();
}

run();
```

## Ready-to-run demo (repo bundle)
- Path: `architecture-and-state/demos/smart-vs-presentational-demo`
- Commands:
  ```bash
  cd architecture-and-state/demos/smart-vs-presentational-demo
  npm install
  npm run demo       # smart -> presentational output + event log
  npm run typecheck  # নিশ্চিত করুন boundary রক্ষা হয়েছে
  ```
- Output আশা করুন:
  ```
  --- Rendered UI (presentational) ---
  • Stethoscope ($120.00)
  • Blood Pressure Cuff ($65.00)
  --- Simulate click ---
  [event up] selected product id: p1
  ```
 - কী টেস্ট করবেন:
   - `npm run demo`: data down/events up প্রবাহ ঠিক চলছে কিনা (console log এ event id দেখা উচিত)।
   - `npm run typecheck`: path alias/contract সঠিক ও presentational এ কোনো data-access leak নেই কিনা।
   - Boundary ব্রেক এক্সপেরিমেন্ট: `src/app/shared/ui/products-list.component.ts` এ ইচ্ছাকৃত API কল যোগ করে আবার `npm run typecheck` চালান—expect error; তারপর কোড সরিয়ে পুনরায় চালান।

## Common mistakes
- Presentational কম্পোনেন্টে API কল বা Router inject করা।
- Smart কম্পোনেন্ট সরাসরি DOM/UI ম্যানিপুলেট করে ফেলা (UI লেয়ারের কাজ)।
- Props/callback টাইপ না করা, ফলে contract অস্পষ্ট।

## Interview points
- "data down, events up" ব্যাখ্যা + উদাহরণ দিতে পারবেন।
- Smart vs Presentational separation কেন performance ও testability বাড়ায় তা ব্যাখ্যা করুন।
- Standalone world-এও একই boundary নিয়ম কীভাবে প্রয়োগ হয় জানেন।

## How to practice quickly
- `npm run demo` চালিয়ে আউটপুট দেখুন।
- Presentational এ API যোগ করে boundary ভেঙে দেখুন; তারপর সরিয়ে পুনরায় চালান।
- নতুন feature (e.g., Pharmacy) যোগ করে একই pattern অনুসরণ করুন।

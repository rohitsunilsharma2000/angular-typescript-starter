# perf-hot-paths demo

কি আছে
- OnPush + trackBy patient list
- Virtual scroll example
- Lazy route wiring
- Skeleton + toast snippet লিংক

Quick steps
1) আপনার Angular অ্যাপে `app/features/perf-demo/` ফোল্ডার তৈরি করুন।
2) `snippets/onpush-hot-list.component.ts`, `virtual-scroll.component.ts`, `lazy-route.routes.ts`, `skeleton-loader.component.ts`, `toast.service.ts` কপি করুন।
3) `app.routes.ts` এ lazy route যোগ করুন: `loadChildren: () => import('./features/perf-demo/lazy-route.routes').then(m => m.PERF_ROUTES)`।
4) `ng serve` চালিয়ে Network panel ও Angular DevTools দিয়ে re-render/চাঙ্ক যাচাই করুন।

Goals to verify
- Hot list OnPush + trackBy → DevTools রেন্ডার কাউন্ট কম।
- Lazy chunk লোড হয়।
- Skeleton layout-stable (CLS ~0)।
- Toast retry কাজ করে।

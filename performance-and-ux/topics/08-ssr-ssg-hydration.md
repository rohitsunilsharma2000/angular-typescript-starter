# 08) SSR/SSG + hydration

Hospital public portal (appointment booking) SEO ও প্রথম পেইন্ট দ্রুত করতে SSR/SSG কাজে লাগে। Hydration mismatch এড়ানো জরুরি।

## Why this matters (real-world)
- Faster first paint + SEO for public booking page.
- Hydration error হলে UI blank হতে পারে।
- ইন্টারভিউ: “Hydration mismatch কীভাবে ধরবেন?”

## Concepts
### Beginner
- SSR বনাম SSG; TransferState ধারণা।
### Intermediate
- Browser-only কোড guard (`isPlatformBrowser`); hydration-safe random/Date।
- Data fetch on server + reuse on client।
### Advanced
- Partial hydration hints; Streaming SSR; hydration error logging; blocking vs non-blocking scripts।

## Copy-paste Example
```ts
// server.ts (Angular Universal basic snippet)
import 'zone.js/node';
import { APP_BASE_HREF } from '@angular/common';
import { ngExpressEngine } from '@nguniversal/express-engine';
import express from 'express';
import { join } from 'path';
import { AppServerModule } from './src/main.server';
const app = express();
app.engine('html', ngExpressEngine({ bootstrap: AppServerModule }));
app.set('view engine', 'html');
app.set('views', join(process.cwd(), 'dist/browser'));
app.get('*', (req, res) => {
  res.render('index', { req, providers: [{ provide: APP_BASE_HREF, useValue: req.baseUrl }] });
});
app.listen(4000);
```
```ts
// app/features/public-booking/booking.component.ts (hydration-safe)
import { isPlatformBrowser } from '@angular/common';
import { ChangeDetectionStrategy, Component, Inject, PLATFORM_ID } from '@angular/core';
@Component({
  standalone: true,
  selector: 'hms-booking',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<p>Next token: {{ token }}</p>`
})
export class BookingComponent {
  token = 'SSR';
  constructor(@Inject(PLATFORM_ID) platformId: object) {
    if (isPlatformBrowser(platformId)) {
      this.token = crypto.randomUUID(); // only on client
    }
  }
}
```
```ts
// app/features/public-booking/booking.service.ts (TransferState)
import { Injectable, TransferState, makeStateKey } from '@angular/core';
import { HttpClient } from '@angular/common/http';
const BOOKINGS_KEY = makeStateKey<any[]>('bookings');
@Injectable({ providedIn: 'root' })
export class BookingService {
  constructor(private http: HttpClient, private state: TransferState) {}
  async list() {
    const cached = this.state.get(BOOKINGS_KEY, null);
    if (cached) return cached;
    const data = await this.http.get<any[]>('/api/bookings').toPromise();
    this.state.set(BOOKINGS_KEY, data);
    return data;
  }
}
```

## Try it
- Beginner: randomUUID server+client দুদিকে না চালিয়ে hydration mismatch এড়ান; console এ warning দেখুন।
- Advanced: Lighthouse SSR run করে প্রথম পেইন্ট সময় তুলনা করুন; hydration error log যোগ করুন।

## Common mistakes
- Browser-only API (window, localStorage) SSR-এ guard না করা।
- Server data fetch করে TransferState না করলে double fetch।

## Interview points
- Hydration mismatch উদাহরণ: random/Date/UI that differs server vs client।
- TransferState mention; isPlatformBrowser guard।

## Done when…
- SSR/SSG পাথ বাছাই ও নথিভুক্ত।
- Browser-only কোড guarded।
- Hydration error checkলিস্ট হাতে আছে।

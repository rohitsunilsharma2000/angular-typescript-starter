# 12) PWA + Service Worker updates

হাসপাতাল ফ্লোরে কখনো নেটওয়ার্ক দুর্বল; PWA ক্যাশ + আপডেট ব্যানার রাখুন।

## Why this matters (real-world)
- Offline/read-through ক্যাশে patient list দেখা যায়।
- নতুন রিলিজ এলে ইউজারকে আপডেট prompt।

## Concepts
### Beginner
- Angular PWA schematic (`ng add @angular/pwa`); service worker basics।
- Cache-first assets, network-first API (careful)।
### Intermediate
- Update check + banner; skipWaiting flow।
- Offline page; fallback strategy।
### Advanced
- Background sync ধারণা; push optional; SW versioning/logging।

## Copy-paste Example
```ts
// pwa-update-banner.component.ts
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { SwUpdate } from '@angular/service-worker';
@Component({
  standalone: true,
  selector: 'hms-update-banner',
  template: `
    <div *ngIf="hasUpdate" class="banner">
      New version available
      <button (click)="reload()">Reload</button>
    </div>
  `,
  styles: [`.banner{position:fixed;bottom:12px;left:12px;right:12px;padding:12px;background:#0ea5e9;color:white;}`],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PwaUpdateBannerComponent {
  private sw = inject(SwUpdate);
  hasUpdate = false;
  constructor() {
    this.sw.versionUpdates.subscribe(evt => {
      if (evt.type === 'VERSION_READY') this.hasUpdate = true;
    });
  }
  reload() { this.sw.activateUpdate().then(() => document.location.reload()); }
}
```
```bash
# Install
ng add @angular/pwa
```

## Try it
- Beginner: service worker enabled build সার্ভ করে banner ট্রিগার করুন।
- Advanced: offline fallback page ক্যাশ করুন এবং devtools offline মোডে পরীক্ষা।

## Common mistakes
- API cache blindly → stale data।
- SW update events সাবস্ক্রাইব না করা, ইউজার পুরনো ভার্সনে আটকে।

## Interview points
- PWA schematic, update flow, cache strategies, offline UX।

## Done when…
- PWA enabled; update banner কাজ করে।
- Offline ফallback পরিকল্পিত।

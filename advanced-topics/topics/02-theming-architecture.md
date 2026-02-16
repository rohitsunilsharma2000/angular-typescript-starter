# 02) Theming architecture

HMS বহুক্লিনিক—ব্র্যান্ড প্রতি আলাদা primary/typography দরকার। Runtime এ থিম সুইচ ও data-theme ক্লাস দিয়ে সাপোর্ট।

## Why this matters (real-world)
- নতুন ক্লিনিক অনবোর্ডিংয়ে দ্রুত থিম আপডেট।
- Dark/light বা brand swap ছাড়া কোড ডুপ্লিকেট হয় না।

## Concepts
### Beginner
- data-theme class + CSS vars override।
- Theme service to set attribute।
### Intermediate
- Multi-brand config JSON; persist choice (localStorage)।
- Motion/spacing token override per brand।
### Advanced
- Lazy-load brand assets; SSR render-safe; prefers-color-scheme sync।

## Copy-paste Example
```ts
// theme.service.ts
import { Injectable, signal } from '@angular/core';
@Injectable({ providedIn: 'root' })
export class ThemeService {
  current = signal<'light' | 'dark' | 'clinicA' | 'clinicB'>('light');
  setTheme(t: string) {
    this.current.set(t as any);
    document.documentElement.setAttribute('data-theme', t);
    localStorage.setItem('theme', t);
  }
  init() {
    const saved = localStorage.getItem('theme');
    this.setTheme(saved || 'light');
  }
}
```
```css
/* theme overrides */
[data-theme='clinicA'] { --color-primary: #0ea5e9; }
[data-theme='clinicB'] { --color-primary: #7c3aed; }
```
```ts
// app.component.ts
import { Component, OnInit, inject } from '@angular/core';
import { ThemeService } from './theme.service';
@Component({ selector: 'app-root', template: `<button (click)="toggle()">Toggle Theme</button><router-outlet/>` })
export class AppComponent implements OnInit {
  theme = inject(ThemeService);
  ngOnInit() { this.theme.init(); }
  toggle() {
    const next = this.theme.current() === 'light' ? 'clinicA' : 'light';
    this.theme.setTheme(next);
  }
}
```

## Try it
- Beginner: data-theme পরিবর্তন করে button রঙ বদল দেখুন।
- Advanced: clinicB tokens runtime fetch করে apply করুন (config.json থেকে)।

## Common mistakes
- CSS vars override না করে SCSS ভ্যারিয়েবল ব্যবহার → runtime switch অসম্ভব।
- Theme persist না করা।

## Interview points
- data-theme + CSS vars = runtime theming; multi-brand mapping; persistence।

## Done when…
- Theme service কাজ করে; data-theme পরিবর্তনে UI বদলে।
- Brand tokens override ডকুমেন্টেড।

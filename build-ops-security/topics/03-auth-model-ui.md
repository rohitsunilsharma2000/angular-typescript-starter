# 03) Auth model in UI (tokens, refresh)

HMS অ্যাপে login টোকেন সংরক্ষণ ও রিফ্রেশ ফ্লো বুঝতে হবে যাতে patients/appointments API নিরাপদে কল হয়।

## Why this matters (real-world)
- Expired token এ বারবার 401 হলে নার্সরা লগআউট হয়ে যায়।
- Refresh সুরক্ষিত না হলে টোকেন লিক হতে পারে।

## Concepts
### Beginner
- Access token + refresh token ভূমিকা; storage পছন্দ (httpOnly cookie preferred, fallback localStorage)।
### Intermediate
- Token expiry, clock skew buffer, refresh endpoint।
- Auth state service + logout flow।
### Advanced
- Single sign-on ধারণা; multi-tab sync; refresh token rotation।

## Copy-paste Example
```ts
// auth.model.ts
export interface AuthState { accessToken: string | null; refreshToken: string | null; expiresAt?: number; }
```
```ts
// auth.service.ts
import { Injectable, signal } from '@angular/core';
import { AuthState } from './auth.model';
@Injectable({ providedIn: 'root' })
export class AuthService {
  state = signal<AuthState>({ accessToken: null, refreshToken: null });
  setTokens(tokens: AuthState) { this.state.set(tokens); }
  clear() { this.state.set({ accessToken: null, refreshToken: null }); }
  isExpired(bufferSec = 60) {
    const exp = this.state().expiresAt ?? 0;
    return !exp || Date.now() + bufferSec * 1000 >= exp;
  }
}
```

## Try it
- Beginner: login পর state.set দিয়ে token রাখুন; logout এ clear।
- Advanced: multi-tab sync এর জন্য storage event listener যোগ করুন।

## Common mistakes
- refresh token localStorage-এ রাখা (চুরি ঝুঁকি); httpOnly cookie ব্যবহার নিরাপদ।
- expiry buffer না রাখা।

## Interview points
- access বনাম refresh ভূমিকা; storage trade-off; buffer for expiry।

## Done when…
- Auth state model রয়েছে।
- Expiry চেক ও logout flow ভাবা হয়েছে।

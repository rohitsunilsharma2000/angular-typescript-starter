# 04) Auth interceptor + refresh queue

সব API কলে টোকেন যোগ ও 401 এ single-flight refresh + queued retry দরকার, না হলে multiple refresh race/লগআউট।

## Why this matters (real-world)
- 401 ঝড়ে ইউজার লগআউট হবে না।
- Refresh একবারই হবে; অন্য কলে queued retry।

## Concepts
### Beginner
- HttpInterceptor basics; Authorization header attach।
### Intermediate
- 401 এ refresh কল; success হলে queued request retry; failure এ logout।
### Advanced
- Single-flight mutex; refresh token rotation; exponential backoff optional।

## Copy-paste Example
```ts
// auth.interceptor.ts
import { Injectable, inject } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, from, of } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { AuthService } from './auth.service';
import { AuthRefreshService } from './auth-refresh.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private auth = inject(AuthService);
  private refresh = inject(AuthRefreshService);

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.auth.state().accessToken;
    const authReq = token ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }) : req;
    return next.handle(authReq).pipe(
      catchError((err: HttpErrorResponse) => {
        if (err.status !== 401) return throwError(() => err);
        return from(this.refresh.refreshOnce()).pipe(
          switchMap(ok => ok
            ? next.handle(req.clone({ setHeaders: { Authorization: `Bearer ${this.auth.state().accessToken}` } }))
            : throwError(() => err)
          )
        );
      })
    );
  }
}
```
```ts
// auth-refresh.service.ts
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class AuthRefreshService {
  private http = inject(HttpClient);
  private auth = inject(AuthService);
  private inflight?: Promise<boolean>;

  refreshOnce(): Promise<boolean> {
    if (this.inflight) return this.inflight;
    const refreshToken = this.auth.state().refreshToken;
    if (!refreshToken) return Promise.resolve(false);
    this.inflight = this.http.post<{ accessToken: string; expiresIn: number }>('/api/auth/refresh', { refreshToken })
      .toPromise()
      .then(res => {
        this.auth.setTokens({ ...this.auth.state(), accessToken: res.accessToken, expiresAt: Date.now() + res.expiresIn * 1000 });
        return true;
      })
      .catch(() => { this.auth.clear(); return false; })
      .finally(() => { this.inflight = undefined; });
    return this.inflight;
  }
}
```
```ts
// app.config.ts (standalone bootstrap)
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { AuthInterceptor } from './auth.interceptor';
export const appConfig = {
  providers: [provideHttpClient(withInterceptors([() => new AuthInterceptor()]))]
};
```

## Try it
- Beginner: access token সেট করে API কল সফল হচ্ছে কিনা দেখুন।
- Advanced: একসাথে দুটো API 401 ফেরালে refreshOnlyOnce কাজ করে কিনা লগ করুন।

## Common mistakes
- প্রতি 401 এ নতুন refresh কল → race।
- Refresh success হলেও queued request একই পুরনো টোকেন পাঠায়।

## Interview points
- Single-flight refresh, queued retry, logout on fail।

## Done when…
- Interceptor attach + refresh queue ডেমো চলে।
- 401 race handled; failure এ logout।

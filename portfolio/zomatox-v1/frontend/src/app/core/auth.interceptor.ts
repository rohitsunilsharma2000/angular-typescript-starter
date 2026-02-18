import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, switchMap, throwError } from 'rxjs';
import { AuthStore } from './auth.store';
import { AuthService } from './auth.service';

const isAuthEndpoint = (url: string) =>
  url.includes('/api/auth/login') || url.includes('/api/auth/signup') || url.includes('/api/auth/refresh');

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const store = inject(AuthStore);
  const authService = inject(AuthService);

  let request = req;
  const token = store.accessToken();
  if (token) {
    request = req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
  }

  return next(request).pipe(
    catchError((err: HttpErrorResponse) => {
      if (err.status !== 401 || isAuthEndpoint(req.url)) {
        return throwError(() => err);
      }

      const refreshToken = store.refreshToken();
      if (!refreshToken) {
        store.clear();
        return throwError(() => err);
      }

      return authService.refresh(refreshToken).pipe(
        switchMap((r) => {
          store.setAuth(r.accessToken, r.refreshToken, r.user);
          const retried = req.clone({ setHeaders: { Authorization: `Bearer ${r.accessToken}` } });
          return next(retried);
        }),
        catchError((refreshErr) => {
          store.clear();
          return throwError(() => refreshErr);
        })
      );
    })
  );
};

import { Injectable, inject } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, from, throwError } from 'rxjs';
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

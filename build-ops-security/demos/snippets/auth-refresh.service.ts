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

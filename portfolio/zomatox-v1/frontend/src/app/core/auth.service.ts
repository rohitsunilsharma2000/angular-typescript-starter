import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { AuthStore, AuthUser } from './auth.store';

const API = 'http://localhost:8080/api';

type AuthResponse = {
  accessToken: string;
  refreshToken: string;
  user: AuthUser;
};

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private store = inject(AuthStore);

  login(email: string, password: string) {
    return this.http.post<AuthResponse>(`${API}/auth/login`, { email, password }).pipe(
      tap(r => this.store.setAuth(r.accessToken, r.refreshToken, r.user))
    );
  }

  signup(name: string, email: string, password: string) {
    return this.http.post<AuthResponse>(`${API}/auth/signup`, { name, email, password }).pipe(
      tap(r => this.store.setAuth(r.accessToken, r.refreshToken, r.user))
    );
  }

  refresh(refreshToken: string) {
    return this.http.post<AuthResponse>(`${API}/auth/refresh`, { refreshToken });
  }

  logout() {
    const rt = this.store.refreshToken();
    if (!rt) {
      this.store.clear();
      return;
    }
    this.http.post<void>(`${API}/auth/logout`, { refreshToken: rt }).subscribe({
      next: () => this.store.clear(),
      error: () => this.store.clear(),
    });
  }
}

import { Injectable, signal, computed } from '@angular/core';

export type AuthUser = {
  id: number;
  name: string;
  email: string;
  role: 'CUSTOMER' | 'OWNER' | 'DELIVERY_PARTNER' | 'ADMIN';
};

type AuthState = {
  accessToken: string | null;
  refreshToken: string | null;
  user: AuthUser | null;
};

const KEY = 'zomatox.auth';

@Injectable({ providedIn: 'root' })
export class AuthStore {
  private state = signal<AuthState>(this.load());

  accessToken = computed(() => this.state().accessToken);
  refreshToken = computed(() => this.state().refreshToken);
  user = computed(() => this.state().user);
  role = computed(() => this.state().user?.role ?? null);
  isLoggedIn = computed(() => !!this.state().accessToken && !!this.state().user);

  setAuth(accessToken: string, refreshToken: string, user: AuthUser) {
    const next = { accessToken, refreshToken, user };
    this.state.set(next);
    localStorage.setItem(KEY, JSON.stringify(next));
  }

  updateAccessToken(accessToken: string) {
    const cur = this.state();
    if (!cur.user || !cur.refreshToken) return;
    const next = { ...cur, accessToken };
    this.state.set(next);
    localStorage.setItem(KEY, JSON.stringify(next));
  }

  clear() {
    this.state.set({ accessToken: null, refreshToken: null, user: null });
    localStorage.removeItem(KEY);
  }

  private load(): AuthState {
    try {
      const raw = localStorage.getItem(KEY);
      if (!raw) return { accessToken: null, refreshToken: null, user: null };
      const parsed = JSON.parse(raw) as AuthState;
      return parsed;
    } catch {
      return { accessToken: null, refreshToken: null, user: null };
    }
  }
}

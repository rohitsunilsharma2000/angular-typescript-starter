import { Injectable } from '@angular/core';

export type Role = 'PRINCIPAL' | 'TEACHER' | 'STUDENT';

export interface Session {
  token: string;
  userId: string;
  role: Role;
  name: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private key = 'classroom_session';

  get session(): Session | null {
    const raw = localStorage.getItem(this.key);
    return raw ? JSON.parse(raw) : null;
  }

  set session(val: Session | null) {
    if (!val) {
      localStorage.removeItem(this.key);
    } else {
      localStorage.setItem(this.key, JSON.stringify(val));
    }
  }

  isLoggedIn(): boolean {
    return !!this.session?.token;
  }

  logout() {
    this.session = null;
  }
}

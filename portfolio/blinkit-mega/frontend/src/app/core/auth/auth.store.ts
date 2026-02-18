import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export type AppRole = 'CUSTOMER' | 'ADMIN' | 'RIDER' | 'WAREHOUSE_STAFF';

@Injectable({ providedIn: 'root' })
export class AuthStore {
  private readonly roleState = new BehaviorSubject<AppRole>((localStorage.getItem('blinkit_role') as AppRole) || 'CUSTOMER');
  private readonly tokenState = new BehaviorSubject<string>(localStorage.getItem('blinkit_access') || '');

  readonly role$ = this.roleState.asObservable();

  role(): AppRole {
    return this.roleState.value;
  }

  accessToken(): string {
    return this.tokenState.value;
  }

  setSession(token: string, role: AppRole): void {
    this.tokenState.next(token);
    this.roleState.next(role);
    localStorage.setItem('blinkit_access', token);
    localStorage.setItem('blinkit_role', role);
  }

  clearSession(): void {
    this.tokenState.next('');
    localStorage.removeItem('blinkit_access');
  }

  setRole(role: AppRole): void {
    this.roleState.next(role);
    localStorage.setItem('blinkit_role', role);
  }
}

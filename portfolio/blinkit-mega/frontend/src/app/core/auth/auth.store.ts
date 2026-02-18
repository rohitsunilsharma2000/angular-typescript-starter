import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export type AppRole = 'CUSTOMER' | 'ADMIN' | 'RIDER' | 'WAREHOUSE_STAFF';

@Injectable({ providedIn: 'root' })
export class AuthStore {
  private readonly roleState = new BehaviorSubject<AppRole>('CUSTOMER');
  private readonly tokenState = new BehaviorSubject<string>('');

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
  }
}

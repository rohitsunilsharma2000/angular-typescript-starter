import { Injectable } from '@angular/core';

export type DemoUserRole = 'CUSTOMER' | 'OWNER' | 'DELIVERY_PARTNER';
export type DemoUser = { id: number; label: string; role: DemoUserRole };

@Injectable({ providedIn: 'root' })
export class UserContextService {
  users: DemoUser[] = [
    { id: 1, label: 'Customer (id=1)', role: 'CUSTOMER' },
    { id: 2, label: 'Owner (id=2)', role: 'OWNER' },
    { id: 3, label: 'Delivery (id=3)', role: 'DELIVERY_PARTNER' },
  ];

  private keyId = 'zomatox.userId';
  private keyRole = 'zomatox.userRole';

  get userId(): number {
    const v = localStorage.getItem(this.keyId);
    return v ? Number(v) : 1;
  }

  set userId(id: number) {
    localStorage.setItem(this.keyId, String(id));
  }

  get role(): DemoUserRole {
    const v = localStorage.getItem(this.keyRole) as DemoUserRole | null;
    return v ?? 'CUSTOMER';
  }

  set role(r: DemoUserRole) {
    localStorage.setItem(this.keyRole, r);
  }

  setUser(u: DemoUser) {
    this.userId = u.id;
    this.role = u.role;
  }
}
